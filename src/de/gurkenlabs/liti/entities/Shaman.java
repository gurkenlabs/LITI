package de.gurkenlabs.liti.entities;

import de.gurkenlabs.liti.abilities.SurvivalSkill;
import com.litiengine.Valign;
import com.litiengine.entities.CollisionInfo;
import com.litiengine.entities.CombatInfo;
import com.litiengine.entities.EntityInfo;
import com.litiengine.entities.MovementInfo;

@EntityInfo(width = 12, height = 22)
@CollisionInfo(collision = true, collisionBoxWidth = 4, collisionBoxHeight = 5, valign = Valign.MIDDLE_DOWN)
@CombatInfo(hitpoints = 70)
@MovementInfo(velocity = 60, acceleration = 150, deceleration = 100)
public class Shaman extends Player {
  protected Shaman(PlayerConfiguration config) {
    super(config);
  }
}
