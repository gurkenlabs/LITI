package de.gurkenlabs.liti.entities;

import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.entities.*;

@EntityInfo(width = 12, height = 21)
@CollisionInfo(collision = true, collisionBoxWidth = 4, collisionBoxHeight = 5, valign = Valign.MIDDLE_DOWN)
@CombatInfo(hitpoints = 100)
@MovementInfo(velocity = 80, acceleration = 1000, deceleration = 500)
public class Warrior extends Player {
  protected Warrior(PlayerConfiguration config) {
    super(config);
  }
}
