package de.gurkenlabs.liti.entities;

import de.gurkenlabs.liti.abilities.SurvivalSkill;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.entities.MovementInfo;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.resources.Resources;

@EntityInfo(width = 8, height = 23)
@CollisionInfo(collision = true, collisionBoxWidth = 4, collisionBoxHeight = 6, valign = Valign.MIDDLE_DOWN)
@CombatInfo(hitpoints = 70)
@MovementInfo(velocity = 90, acceleration = 150, deceleration = 100)
public class Huntress extends Player {
  protected Huntress(PlayerConfiguration config) {
    super(config);
  }

  @Override
  public SurvivalSkill getSurvivalSkill() {
    return null;
  }
}
