package de.gurkenlabs.liti.entities;

import java.awt.Graphics2D;

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
import de.gurkenlabs.litiengine.entities.Action;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICollisionEntity;
import de.gurkenlabs.litiengine.graphics.IRenderable;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.tweening.Tween;
import de.gurkenlabs.litiengine.tweening.TweenFunction;
import de.gurkenlabs.litiengine.tweening.TweenType;
import de.gurkenlabs.litiengine.util.Imaging;

public abstract class Player extends Creature implements IUpdateable, IRenderable {

  public boolean isFalling() {
    return isFalling;
  }

  public void setFalling(boolean falling) {
    isFalling = falling;
  }

  public enum PlayerState {
    LOCKED,
    NORMAL
  }

  private static final int STAMINA_DEPLETION_DELAY = 3000;
  private static final int BLOCK_COOLDOWN = 500;

  private PlayerConfiguration configuration;

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

  protected Player(PlayerConfiguration config) {
    this.stamina = new RangeAttribute<>(Proficiency.get(config.getPlayerClass(), Trait.STAMINA), 0.0, Proficiency.get(config.getPlayerClass(), Trait.STAMINA));
    this.playerState = PlayerState.NORMAL;
    this.configuration = config;
    this.dash = new Dash(this);
    this.bash = new Bash(this);
    this.movement().onMovementCheck(e -> !this.isBlocking());
    this.setTurnOnMove(false);
    this.updateAnimationController();
    this.onCollision(e -> {
      for (ICollisionEntity entity : e.getInvolvedEntities()) {
        for (String tag : entity.getTags()) {
          if (tag != null && tag.equals("bounds")) {
            this.setState(PlayerState.LOCKED);
            System.out.println("bye bye!");
            this.setScaling(true);
            //this.setSize(this.animations().getCurrentImage().getWidth(), this.animations().getCurrentImage().getHeight());
            this.setFalling(true);
            Tween tween = Game.tweens().begin(this, TweenType.SIZE_BOTH, 3000);
            tween.ease(TweenFunction.QUAD_OUT);
            Game.loop().perform(2000, () -> {
              this.die();
              tween.stop();
              // TODO: reset size after tweening
              this.setScaling(false);
              this.setFalling(false);
              Game.world().environment().remove(this);
              System.out.println("you fell off a cliff...");
            });

            break;
          }
        }
      }
    });
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

  @Override
  public void render(Graphics2D g) {
    this.bash.render(g);
  }

  public PlayerConfiguration getConfiguration() {
    return configuration;
  }

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

    this.isBlocking = blocking;
    if (this.isBlocking) {
      this.movement().setVelocity(0);
      this.getStamina().setBaseValue(this.getStamina().get() - this.getStamina().getMax() * 0.2);
    } else {
      this.lastBlock = Game.time().now();
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

  public void setConfiguration(PlayerConfiguration configuration) {
    this.configuration = configuration;
  }

  @Action(name = "DASH")
  public void useDash() {
    this.dash.cast();
  }

  @Action(name = "BASH")
  public void useBash() {
    this.bash.cast();
  }

  @Action(name = "ULTIMATE")
  public void useUltimate() {
    if (this.getUltimate() != null) {
      this.getUltimate().cast();
    }
  }

  @Override
  public String toString() {
    return "Player " + (this.getConfiguration().getIndex() + 1) + " (#" + this.getMapId() + ")";
  }

  public abstract SurvivalSkill getUltimate();

  @Override
  public void updateAnimationController() {
    if (this.getConfiguration() == null || this.getConfiguration().getSkin() == null) {
      return;
    }

    super.updateAnimationController();

    this.animations().getAll().forEach(an -> {
      Spritesheet replaced = new Spritesheet(Imaging.replaceColors(an.getSpritesheet().getImage(), this.getConfiguration().getSkin().getColorMappings()), an.getSpritesheet().getName(), an.getSpritesheet().getSpriteWidth(), an.getSpritesheet().getSpriteHeight());
      this.animations().add(new Animation(replaced, an.isLooping(), an.getKeyFrameDurations()));
      this.animations().remove(an);
    });

    this.setController(PlayerAnimationController.class, new PlayerAnimationController(this));
    if (Game.world().environment() != null && Game.world().environment().isLoaded()) {
      this.animations().attach();
    }
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
}
