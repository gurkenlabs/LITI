package de.gurkenlabs.liti.entities;

import java.awt.Graphics2D;

import de.gurkenlabs.liti.GameManager;
import de.gurkenlabs.liti.abilities.Bash;
import de.gurkenlabs.liti.abilities.Dash;
import de.gurkenlabs.liti.abilities.Proficiency;
import de.gurkenlabs.liti.abilities.SurvivalSkill;
import de.gurkenlabs.liti.abilities.Trait;
import de.gurkenlabs.liti.entities.controllers.PlayerAnimationController;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.GameLoop;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.attributes.RangeAttribute;
import de.gurkenlabs.litiengine.entities.*;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.graphics.IRenderable;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.physics.CollisionEvent;
import de.gurkenlabs.litiengine.tweening.TweenFunction;
import de.gurkenlabs.litiengine.tweening.TweenType;

public abstract class Player extends Creature implements IUpdateable, IRenderable {

  public enum PlayerState {
    LOCKED, NORMAL
  }

  private static final int INTERACT_COOLDOWN = 1000;
  private static final int STAMINA_DEPLETION_DELAY = 3000;
  private static final int BLOCK_COOLDOWN = 500;

  private final PlayerConfiguration configuration;
  private final PlayerCombatStatistics combatStatistics;
  private final PlayerProgress playerProgress;

  private final RangeAttribute<Double> stamina;
  private int index;
  private PlayerState playerState;
  private Dash dash;
  private Bash bash;

  private boolean isBlocking;
  private boolean isFalling;

  private boolean isDashing;
  private long lastBlock;
  private long staminaDepleted;
  private long lastDeath;
  private long lastInteract;
  private long resurrection;

  private Chicken currentChicken;
  private Egg channelledEgg;

  protected Player(PlayerConfiguration config) {
    this.stamina = new RangeAttribute<>(Proficiency.get(config.getPlayerClass(), Trait.STAMINA), 0.0, Proficiency.get(config.getPlayerClass(), Trait.STAMINA));
    this.playerState = PlayerState.NORMAL;
    this.configuration = config;
    this.combatStatistics = new PlayerCombatStatistics(this);
    this.playerProgress = new PlayerProgress(this);

    this.dash = new Dash(this);
    this.bash = new Bash(this);
    this.setTurnOnMove(false);
    this.setScaling(false);
    this.updateAnimationController();

    this.movement().onMovementCheck(e -> !this.isBlocking());
    this.onCollision(this::handleCliffs);
    this.onDeath(this::handleDeath);
    this.onResurrect(this::handleResurrect);
  }

  @Override
  public boolean canCollideWith(final ICollisionEntity otherEntity) {
    return !((otherEntity instanceof Chicken));
  }

  @Override
  public void update() {
    if (this.isStaminaDepleted()) {
      this.setBlocking(false);
      return;
    }

    if (this.isBlocking()) {
      this.drainStaminaWhileBlocking();
    }

    if (this.getStamina().get() == this.getStamina().getMin()) {
      this.staminaDepleted = Game.time().now();
    }

    if (!this.isBlocking()) {
      this.recoverStamina();
    }
  }

  public boolean isStaminaDepleted() {
    return this.staminaDepleted != 0 && Game.time().since(this.staminaDepleted) < STAMINA_DEPLETION_DELAY;
  }

  public int getIndex() {
    return index;
  }

  public PlayerState getState() {
    return playerState;
  }

  public Dash getDash() {
    return dash;
  }

  public boolean isFalling() {
    return isFalling;
  }

  public void setFalling(boolean falling) {
    isFalling = falling;
  }

  public long getLastDeath() {
    return lastDeath;
  }

  public long getResurrection() {
    return resurrection;
  }

  public void setResurrection(long resurrection) {
    this.resurrection = resurrection;
  }

  public Chicken getCurrentChicken() {
    return currentChicken;
  }

  public void setCurrentChicken(Chicken currentChicken) {
    this.currentChicken = currentChicken;
  }

  public Egg getCurrentEgg() {
    return channelledEgg;
  }

  public void setChannelledEgg(Egg channelledEgg) {
    this.channelledEgg = channelledEgg;
  }

  @Override
  public void render(Graphics2D g) {
    this.bash.render(g);

    if (this.currentChicken != null) {
      this.currentChicken.setRenderType(RenderType.NORMAL);
      Game.graphics().renderEntity(g, this.currentChicken);
      this.currentChicken.setRenderType(RenderType.NONE);
    }
  }

  public PlayerConfiguration getConfiguration() { return configuration; }

  public PlayerCombatStatistics getCombatStatistics() {
    return combatStatistics;
  }

  public PlayerProgress getProgress() { return playerProgress; }

  public PlayerClass getPlayerClass() {
    return this.getConfiguration().getPlayerClass();
  }

  public RangeAttribute<Double> getStamina() {
    return stamina;
  }

  public boolean isBlocking() {
    return isBlocking;
  }

  public void setBlocking(boolean blocking) {
    if (blocking == this.isBlocking) {
      return;
    }

    if (blocking && (this.getStamina().get() < 0.20 * this.getStamina().getMax() || this.isStaminaDepleted())) {
      return;
    }

    if (!this.isBlocking && (this.lastBlock != 0 && Game.time().since(this.lastBlock) < BLOCK_COOLDOWN)) {
      return;
    }

    if (this.currentChicken != null) {
      this.currentChicken.drop();
    }

    this.isBlocking = blocking;
    if (this.isBlocking) {
      this.movement().setVelocity(0);
      this.getStamina().setBaseValue(this.getStamina().get() - this.getStamina().getMax() * 0.2);
    } else {
      this.lastBlock = Game.time().now();
    }
  }

  public void interact() {
    if (Game.time().since(this.lastInteract) < INTERACT_COOLDOWN) {
      return;
    }

    if (this.channelledEgg != null && this.getState() == PlayerState.LOCKED) {
      this.channelledEgg.release();
      this.lastInteract = Game.time().now();
      return;
    }

    if (this.currentChicken != null) {
      Prop base = Game.world().environment().getProp("player-" + (this.getConfiguration().getIndex() + 1));
      if (base != null && base.getCollisionBoxCenter().distance(this.getCollisionBoxCenter()) < 20) {
        this.currentChicken.capture();
      } else {
        this.currentChicken.drop();
      }
      this.lastInteract = Game.time().now();
      return;
    }

    for (Chicken chicken : Game.world().environment().getEntities(Chicken.class)) {
      if (chicken.getBoundingBox().intersects(this.getBoundingBox()) && this.doesFace(chicken)) {
        if (!chicken.isPickedUpOrBeingPickedUp()) {
          chicken.startPickup(this);
          this.lastInteract = Game.time().now();
          return;
        }
      }
    }

    for (Egg egg : Game.world().environment().getEntities(Egg.class)) {
      if (egg.getBoundingBox().intersects(this.getBoundingBox()) && this.doesFace(egg)) {
        if (!egg.isBeingChannelled()) {
          egg.channel(this);
          this.lastInteract = Game.time().now();
          return;
        }
      }
    }
  }

  public boolean isDashing() {
    return isDashing;
  }

  public void setDashing(boolean dashing) {
    // DONT CALL THIS MANUALLY
    isDashing = dashing;
  }

  public void setIndex(int index) {
    this.index = index;
    this.setTeam(this.index);
  }

  public void setState(PlayerState playerState) {
    this.playerState = playerState;

    if (this.playerState == PlayerState.LOCKED) {
      this.movement().setVelocity(0);
    }
  }

  @Action(name = "DASH")
  public void useDash() {
    if (this.currentChicken != null) {
      this.currentChicken.drop();
    }
    this.dash.cast();
  }

  @Action(name = "BASH")
  public void useBash() {
    if (this.currentChicken != null) {
      this.currentChicken.drop();
    }

    this.bash.cast();
  }

  @Action(name = "SURVIVALSKILL")
  public void useSurvivalSkill() {
    if (this.getSurvivalSkill() != null) {
      if (this.currentChicken != null) {
        this.currentChicken.drop();
      }

      this.getSurvivalSkill().cast();
    }
  }

  @Override
  public String toString() {
    return "Player " + (this.getConfiguration().getIndex() + 1) + " (#" + this.getMapId() + ")";
  }

  public abstract SurvivalSkill getSurvivalSkill();

  @Override
  public void updateAnimationController() {
    if (this.getConfiguration() == null || this.getConfiguration().getSkin() == null) {
      return;
    }

    super.updateAnimationController();
  }

  @Override
  public void loaded(Environment environment) {
    final EntityInfo info = this.getClass().getAnnotation(EntityInfo.class);
    this.setSize(info.width(), info.height());
    super.loaded(environment);
  }

  @Override
  protected IEntityAnimationController<?> createAnimationController() {
    return new PlayerAnimationController(this);
  }

  private void recoverStamina() {
    if (this.stamina.get() < this.stamina.getMax()) {
      double recovery = Math.min(Game.loop().getDeltaTime(), GameLoop.TICK_DELTATIME_LAG) * 0.02F * Proficiency.get(this.getPlayerClass(), Trait.RECOVERY) * Game.loop().getTimeScale();
      if (this.stamina.get() + recovery > this.stamina.getMax()) {
        this.stamina.setToMax();
      } else {
        this.stamina.setBaseValue(this.stamina.get() + recovery);
      }
    }
  }

  private void drainStaminaWhileBlocking() {
    if (this.stamina.get() > this.stamina.getMin()) {
      double drain = Math.min(Game.loop().getDeltaTime(), GameLoop.TICK_DELTATIME_LAG) * 0.02F * (1.5 - Proficiency.get(this.getPlayerClass(), Trait.RECOVERY)) * Game.loop().getTimeScale();
      if (this.stamina.get() - drain <= this.stamina.getMin()) {
        this.stamina.setToMin();
      } else {
        this.stamina.setBaseValue(this.stamina.get() - drain);
      }
    }
  }

  private void handleCliffs(CollisionEvent e) {
    if (this.isFalling) {
      return;
    }

    for (ICollisionEntity entity : e.getInvolvedEntities()) {
      for (String tag : entity.getTags()) {
        if (tag != null && tag.equals("bounds")) {
          this.setState(PlayerState.LOCKED);
          System.out.println("bye bye!");
          this.setFalling(true);
          this.setScaling(true);
          Game.tweens().begin(this, TweenType.SIZE_BOTH, 2000).targetRelative(-10, -10);
          if (entity.getCollisionBoxCenter().getY() > this.getCollisionBoxCenter().getY()) {
            Game.tweens().begin(this, TweenType.POSITION_Y, 1500).targetRelative(+20);
          } else {
            Game.tweens().begin(this, TweenType.POSITION_Y, 1500).targetRelative(-10).ease(TweenFunction.BACK_OUT);
          }
          Game.loop().perform(1500, () -> {
            this.die();
            Game.tweens().remove(this, TweenType.POSITION_Y);
            System.out.println("you fell off a cliff...");
          });

          break;
        }
      }
    }
  }

  private void handleDeath(ICombatEntity e) {
    Game.world().environment().remove(this);
    GameManager.playerDied(this);
    this.lastDeath = Game.time().now();
    this.setFalling(false);
    this.setScaling(false);
  }

  private void handleResurrect(ICombatEntity entity) {
    this.stamina.setToMax();
    this.staminaDepleted = 0;
  }

  public boolean doesFace(Entity entity) {
    boolean facesTrigger = false;
    switch (this.getFacingDirection()) {
      case DOWN:
        if (entity.getCenter().getY() > this.getCollisionBox().getMinY()) {
          facesTrigger = true;
        }
        break;
      case UP:
        if (entity.getCenter().getY() < this.getCollisionBox().getMaxY()) {
          facesTrigger = true;
        }
        break;
      case LEFT:
        if (entity.getCenter().getX() < this.getCollisionBox().getMaxX()) {
          facesTrigger = true;
        }
        break;
      case RIGHT:
        if (entity.getCenter().getX() > this.getCollisionBox().getMinX()) {
          facesTrigger = true;
        }
        break;
      default:
        break;
    }

    return facesTrigger;
  }
}
