package de.gurkenlabs.liti.entities;

import de.gurkenlabs.liti.abilities.ForceOfNature;
import com.litiengine.Valign;
import com.litiengine.entities.CollisionInfo;
import com.litiengine.entities.CombatInfo;
import com.litiengine.entities.EntityInfo;
import com.litiengine.entities.MovementInfo;
import com.litiengine.graphics.animation.Animation;
import com.litiengine.graphics.animation.AnimationController;
import com.litiengine.resources.Resources;
import de.gurkenlabs.liti.constants.Animations;

@EntityInfo(width = 18, height = 30)
@CollisionInfo(collision = true, collisionBoxWidth = 4, collisionBoxHeight = 5, valign = Valign.MIDDLE_DOWN)
@CombatInfo(hitpoints = 100)
@MovementInfo(velocity = 60, acceleration = 1000, deceleration = 500)
public class Warrior extends Player {

  public Warrior(PlayerConfiguration config) {
    super(config);
    this.animations().add(new Animation(Resources.spritesheets().get(Animations.WARRIOR_FORCEOFNATURE_RIGHT), false));
    this.animations()
        .add(AnimationController.flipAnimation(this.animations().get(Animations.WARRIOR_FORCEOFNATURE_RIGHT), Animations.WARRIOR_FORCEOFNATURE_LEFT));

    this.setSurvivalSkill(new ForceOfNature(this));
  }
}
