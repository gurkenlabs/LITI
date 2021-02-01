package de.gurkenlabs.liti.entities;

import de.gurkenlabs.liti.abilities.SurvivalSkill;
import com.litiengine.Valign;
import com.litiengine.entities.CollisionInfo;
import com.litiengine.entities.CombatInfo;
import com.litiengine.entities.EntityInfo;
import com.litiengine.entities.MovementInfo;

@EntityInfo(width = 12, height = 22)
@CollisionInfo(collision = true, collisionBoxWidth = 6, collisionBoxHeight = 5, valign = Valign.MIDDLE_DOWN)
@CombatInfo(hitpoints = 120)
@MovementInfo(velocity = 80, acceleration = 500, deceleration = 250)
public class Gatherer extends Player {
  public Gatherer(PlayerConfiguration config) {
    super(config);
  }
}
