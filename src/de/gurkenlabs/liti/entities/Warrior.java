package de.gurkenlabs.liti.entities;

import de.gurkenlabs.liti.abilities.ForceOfNature;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.MovementInfo;

@CollisionInfo(collision = true, collisionBoxWidth = 4, collisionBoxHeight = 5, valign = Valign.MIDDLE_DOWN)
@CombatInfo(hitpoints = 100)
@MovementInfo(velocity = 60, acceleration = 1000, deceleration = 500)
public class Warrior extends Player {

  public Warrior(PlayerConfiguration config) {
    super(config);
    this.setSurvivalSkill(new ForceOfNature(this));
  }
}
