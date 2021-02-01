package de.gurkenlabs.liti.entities;

import de.gurkenlabs.liti.abilities.SurvivalSkill;
import com.litiengine.Valign;
import com.litiengine.entities.CollisionInfo;
import com.litiengine.entities.CombatInfo;
import com.litiengine.entities.EntityInfo;
import com.litiengine.entities.MovementInfo;
import com.litiengine.graphics.animation.Animation;
import com.litiengine.resources.Resources;

@EntityInfo(width = 8, height = 23)
@CollisionInfo(collision = true, collisionBoxWidth = 4, collisionBoxHeight = 6, valign = Valign.MIDDLE_DOWN)
@CombatInfo(hitpoints = 70)
@MovementInfo(velocity = 90, acceleration = 150, deceleration = 100)
public class Huntress extends Player {
  protected Huntress(PlayerConfiguration config) {
    super(config);
  }
}
