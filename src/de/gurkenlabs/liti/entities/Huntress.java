package de.gurkenlabs.liti.entities;

import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.MovementInfo;

@CollisionInfo(collision = true, collisionBoxWidth = 4, collisionBoxHeight = 6, valign = Valign.MIDDLE_DOWN)
@CombatInfo(hitpoints = 70)
@MovementInfo(velocity = 90, acceleration = 150, deceleration = 100)
public class Huntress extends Player {
  protected Huntress(PlayerConfiguration config) {
    super(config);
  }
}
