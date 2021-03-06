package de.gurkenlabs.liti.entities;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import de.gurkenlabs.liti.GameManager;
import de.gurkenlabs.liti.abilities.Bash;
import de.gurkenlabs.liti.abilities.Dash;
import de.gurkenlabs.liti.abilities.SurvivalSkill;
import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.entities.controllers.PlayerAnimationController;
import de.gurkenlabs.liti.gameplay.PlayerProgress;
import de.gurkenlabs.liti.graphics.WalkDustSpawner;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.GameLoop;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.*;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.graphics.IRenderable;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.physics.CollisionEvent;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.tweening.TweenFunction;
import de.gurkenlabs.litiengine.tweening.TweenType;
import de.gurkenlabs.litiengine.util.Imaging;

public abstract class Player extends Creature implements IUpdateable, IRenderable {

  public enum PlayerState {
    LOCKED, NORMAL
  }

  private static final int INTERACT_COOLDOWN = 1000;
  private static final int STAMINA_DEPLETION_DELAY = 3000;
  private static final int BLOCK_COOLDOWN = 500;
  private final int HEALTH_RECOVER_INTERVAL;

  private final PlayerConfiguration configuration;
  private final PlayerCombatStatistics combatStatistics;
  private final PlayerProgress playerProgress;
  private final PlayerTraits playerTraits;

  private final Image playerCircle;

  private PlayerState playerState;
  private final Dash dash;
  private final Bash bash;
  private SurvivalSkill survivalSkill;

  private boolean isBlocking;
  private boolean isFalling;

  private boolean isDashing;
  private long lastBlock;
  private long staminaDepleted;
  private long lastDeath;
  private long lastInteract;
  private long resurrection;
  private long lastHealthRecovery;

  private Chicken currentChicken;
  private Egg channelledEgg;

  protected Player(PlayerConfiguration config) {

    this.playerState = PlayerState.NORMAL;
    this.configuration = config;
    this.playerTraits = new PlayerTraits(this);
    this.playerTraits.init();

    this.HEALTH_RECOVER_INTERVAL = (int) (1.0 / this.getHitPoints().getMax() * 10000);
    this.setTeam(this.configuration.getIndex());

    this.playerCircle = Imaging.flashVisiblePixels(Resources.spritesheets().get("player-circle").getImage(),
        LitiColors.getPlayerColorMappings(this.getConfiguration().getIndex()).get(LitiColors.defaultMainOutfitColor));
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
    this.onMoved(new WalkDustSpawner());

    this.addEntityRenderListener(new EntityRenderListener() {
      @Override
      public void rendered(EntityRenderEvent event) {
      }

      @Override
      public void rendering(EntityRenderEvent event) {
        if (Player.this.isDead()) {
          return;
        }

        final Point2D location = Game.world().camera().getViewportLocation(Player.this.getCollisionBoxCenter());
        final AffineTransform t = new AffineTransform();

        t.translate(location.getX() - 8, location.getY() - (Player.this.getPlayerClass() == PlayerClass.WARRIOR ? 3.5 : 2.5));
        t.scale(.35, .35);

        t.rotate(Math.toRadians(360 - Player.this.getAngle()), 19.5, 17.5);

        RenderingHints hints = event.getGraphics().getRenderingHints();

        event.getGraphics().drawImage(Player.this.playerCircle, t, null);
        event.getGraphics().setRenderingHints(hints);
      }
    });

    this.initBash();
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
      this.getProgress().getInterval().didSomething();
    }

    if (this.currentChicken != null) {
      // while a chicken is carried around, a player is considered to be in combat for extra EP gains per interval
      this.getProgress().getInterval().inCombat();
    }

    if (this.traits().stamina().get() == this.traits().stamina().getMin()) {
      this.staminaDepleted = Game.time().now();
    }

    if (!this.isBlocking()) {
      this.recoverStamina();
    }

    if (this.isInBase()) {
      this.recoverHealth();
    }
  }

  public boolean isStaminaDepleted() {
    return this.staminaDepleted != 0 && Game.time().since(this.staminaDepleted) < STAMINA_DEPLETION_DELAY;
  }

  public PlayerState getState() {
    return playerState;
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
    // this.bash.render(g);

    if (this.currentChicken != null) {
      this.currentChicken.setRenderType(RenderType.NORMAL);
      Game.graphics().renderEntity(g, this.currentChicken);
      this.currentChicken.setRenderType(RenderType.NONE);
    }
  }

  public PlayerConfiguration getConfiguration() {
    return configuration;
  }

  public Color getColor() {
    return LitiColors.getPlayerColorMappings(getConfiguration().getIndex()).get(LitiColors.defaultMainOutfitColor);
  }

  public PlayerCombatStatistics getCombatStatistics() {
    return combatStatistics;
  }

  public PlayerProgress getProgress() {
    return playerProgress;
  }

  public PlayerTraits traits() {
    return this.playerTraits;
  }

  public PlayerClass getPlayerClass() {
    return this.getConfiguration().getPlayerClass();
  }

  public boolean isBlocking() {
    return isBlocking;
  }

  public void setBlocking(boolean blocking) {
    if (blocking == this.isBlocking) {
      return;
    }

    if (blocking && (this.traits().stamina().get() < 0.20 * this.traits().stamina().getMax() || this.isStaminaDepleted())) {
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
      this.traits().stamina().setBaseValue(this.traits().stamina().get() - this.traits().stamina().getMax() * 0.2);
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
          this.getProgress().getInterval().didSomething();
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

  private double preDashVelocity;

  public void setDashing(boolean dashing) {
    // DONT CALL THIS MANUALLY
    isDashing = dashing;

    if (isDashing) {
      this.preDashVelocity = this.movement().getVelocity();
    } else {
      this.movement().setVelocity(this.preDashVelocity);
    }
  }

  public void setState(PlayerState playerState) {
    this.playerState = playerState;

    if (this.playerState == PlayerState.LOCKED) {
      this.movement().setVelocity(0);
    }
  }

  @Action(name = "DASH")
  public void useDash() {
    if (!this.dash.canCast()) {
      return;
    }

    if (this.currentChicken != null) {
      this.currentChicken.drop();
    }

    this.dash.cast();
    this.getProgress().getInterval().didSomething();
  }

  @Action(name = "BASH")
  public void useBash() {
    if (!this.bash.canCast()) {
      return;
    }

    if (this.currentChicken != null) {
      this.currentChicken.drop();
    }

    final Animation hitAnimation = this.findBashAnimation();
    if (hitAnimation != null) {
      this.animations().play(hitAnimation.getName());
    }

    this.bash.cast();
    this.getProgress().getInterval().didSomething();
  }

  public Animation findBashAnimation() {
    // hit animations are narrowed down to left and right, I think we can save some time here without loosing too much immersion could add up/down later
    final String hitAnimationName =
        this.getPlayerClass().toString().toLowerCase() + "-hit-" + (this.getAngle() > Direction.UP.toAngle() % 360 ? "left" : "right");
    return this.animations().get(hitAnimationName);
  }

  public Animation findDieAnimation() {
    return this.animations().get(((PlayerAnimationController) this.animations()).getDieAnimationName());
  }

  @Action(name = "SURVIVALSKILL")
  public void useSurvivalSkill() {
    // a player must reach stage 1 in order to use the survival skill
    if (this.getProgress().getCurrentStage().getIndex() < 1) {
      return;
    }

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

  public SurvivalSkill getSurvivalSkill() {
    return this.survivalSkill;
  }

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

  protected Bash getBash() {
    return this.bash;
  }

  protected Dash getDash() {
    return dash;
  }

  protected void setSurvivalSkill(SurvivalSkill survivalSkill) {
    this.survivalSkill = survivalSkill;
  }

  @Override
  protected IEntityAnimationController<?> createAnimationController() {
    return new PlayerAnimationController(this);
  }

  private void initBash() {
    // if there is any bash animation, delay the bash by the duration of the first frame
    Animation hitAnimation = findBashAnimation();
    if (hitAnimation != null) {
      // first effect is the HitEffect, no need for pretty code here ^^
      this.bash.getEffects().get(0).setDelay(hitAnimation.getKeyFrameDurations()[0]);
    }
  }

  private void recoverStamina() {
    if (this.traits().stamina().get() < this.traits().stamina().getMax()) {
      double recovery =
          Math.min(Game.loop().getDeltaTime(), GameLoop.TICK_DELTATIME_LAG) * 0.02F * this.traits().recovery().get() * Game.loop().getTimeScale();
      if (this.traits().stamina().get() + recovery > this.traits().stamina().getMax()) {
        this.traits().stamina().setToMax();
      } else {
        this.traits().stamina().setBaseValue(this.traits().stamina().get() + recovery);
      }
    }
  }

  private void recoverHealth() {
    if (this.isDead() || !this.isLoaded() || Game.time().since(this.lastHealthRecovery) < HEALTH_RECOVER_INTERVAL) {
      return;
    }

    this.lastHealthRecovery = Game.time().now();
    if (this.getHitPoints().get() < this.getHitPoints().getMax()) {
      int recovery = 1;
      if (this.getHitPoints().get() + recovery > this.getHitPoints().getMax()) {
        this.getHitPoints().setToMax();
      } else {
        this.getHitPoints().setBaseValue(this.getHitPoints().get() + recovery);
      }
    }
  }

  private void drainStaminaWhileBlocking() {
    if (this.traits().stamina().get() > this.traits().stamina().getMin()) {
      double drain = Math.min(Game.loop().getDeltaTime(), GameLoop.TICK_DELTATIME_LAG) * 0.02F * (1.5 - this.traits().recovery().get()) * Game.loop()
          .getTimeScale();
      if (this.traits().stamina().get() - drain <= this.traits().stamina().getMin()) {
        this.traits().stamina().setToMin();
      } else {
        this.traits().stamina().setBaseValue(this.traits().stamina().get() - drain);
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
    if (this.currentChicken != null) {
      this.currentChicken.drop();
    }

    if (this.channelledEgg != null) {
      this.channelledEgg.release();
    }

    int removeDelay = 600;
    final Animation dieAnimation = this.findDieAnimation();
    if (dieAnimation != null) {
      removeDelay = dieAnimation.getTotalDuration();
      this.animations().play(dieAnimation.getName());
    }

    Game.loop().perform(removeDelay, () -> {
      Game.world().environment().remove(this);
      GameManager.playerDied(this);
      this.lastDeath = Game.time().now();
      this.setFalling(false);
      this.setScaling(false);
    });
  }

  private void handleResurrect(ICombatEntity entity) {
    this.traits().stamina().setToMax();
    this.staminaDepleted = 0;
  }

  private boolean doesFace(Entity entity) {
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

  private boolean isInBase() {
    MapArea base = GameManager.getBase(this);
    return base != null && base.getBoundingBox().intersects(this.getBoundingBox());
  }
}
