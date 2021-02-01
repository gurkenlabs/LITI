package de.gurkenlabs.liti.entities;

import de.gurkenlabs.liti.abilities.ForceOfNature;
import de.gurkenlabs.liti.abilities.SurvivalSkill;
import com.litiengine.Valign;
import com.litiengine.entities.CollisionInfo;
import com.litiengine.entities.CombatInfo;
import com.litiengine.entities.EntityInfo;
import com.litiengine.entities.MovementInfo;
import com.litiengine.graphics.animation.Animation;
import com.litiengine.graphics.animation.AnimationController;
import com.litiengine.resources.Resources;

@EntityInfo(width = 18, height = 30)
@CollisionInfo(collision = true, collisionBoxWidth = 4, collisionBoxHeight = 5, valign = Valign.MIDDLE_DOWN)
@CombatInfo(hitpoints = 100)
@MovementInfo(velocity = 60, acceleration = 1000, deceleration = 500)
public class Warrior extends Player {
  public static final String ANIM_THROW_ROCK = "warrior-throw-rock";
  public static final String ANIM_THROW_ROCK_LEFT = "warrior-throw-rock-left";

  public Warrior(PlayerConfiguration config) {
    super(config);
    Animation throwRock = new Animation(Resources.spritesheets().get(ANIM_THROW_ROCK), false);
    this.animations().add(throwRock);
    this.animations().add(AnimationController.flipAnimation(throwRock, ANIM_THROW_ROCK_LEFT));

    this.setSurvivalSkill(new ForceOfNature(this));
  }
}
