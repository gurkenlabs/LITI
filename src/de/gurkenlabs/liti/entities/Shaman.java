package de.gurkenlabs.liti.entities;

import de.gurkenlabs.liti.abilities.SurvivalSkill;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.entities.MovementInfo;

@EntityInfo(width = 12, height = 22)
@CollisionInfo(collision = true, collisionBoxWidth = 4, collisionBoxHeight = 5, valign = Valign.MIDDLE_DOWN)
@CombatInfo(hitpoints = 70)
@MovementInfo(velocity = 60, acceleration = 150, deceleration = 100)
public class Shaman extends Player {
  protected Shaman(PlayerConfiguration config) {
    super(config);
  }

  @Override
  public SurvivalSkill getUltimate() {
    return null;
  }
}
