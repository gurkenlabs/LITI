package de.gurkenlabs.liti.entities;

import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.MovementInfo;

@CollisionInfo(collision = true, collisionBoxWidth = 4, collisionBoxHeight = 5, valign = Valign.MIDDLE_DOWN)
@CombatInfo(hitpoints = 70)
@MovementInfo(velocity = 60, acceleration = 150, deceleration = 100)
public class Shaman extends Player {
  protected Shaman(PlayerConfiguration config) {
    super(config);
  }
}
