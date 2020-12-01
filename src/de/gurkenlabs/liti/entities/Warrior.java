package de.gurkenlabs.liti.entities;

import de.gurkenlabs.liti.abilities.ForceOfNature;
import de.gurkenlabs.liti.abilities.SurvivalSkill;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.entities.MovementInfo;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.AnimationController;
import de.gurkenlabs.litiengine.resources.Resources;

@EntityInfo(width = 12, height = 21)
@CollisionInfo(collision = true, collisionBoxWidth = 4, collisionBoxHeight = 5, valign = Valign.MIDDLE_DOWN)
@CombatInfo(hitpoints = 100)
@MovementInfo(velocity = 60, acceleration = 1000, deceleration = 500)
public class Warrior extends Player {
  public static final String ANIM_THROW_ROCK = "warrior-throw-rock";
  public static final String ANIM_THROW_ROCK_LEFT = "warrior-throw-rock-left";

  private final ForceOfNature ultimate;

  public Warrior(PlayerConfiguration config) {
    super(config);
    Animation throwRock = new Animation(Resources.spritesheets().get(ANIM_THROW_ROCK), false);
    this.animations().add(throwRock);
    this.animations().add(AnimationController.flipAnimation(throwRock, ANIM_THROW_ROCK_LEFT));

    this.ultimate = new ForceOfNature(this);
  }

  @Override
  public SurvivalSkill getUltimate() {
    return this.ultimate;
  }
}
