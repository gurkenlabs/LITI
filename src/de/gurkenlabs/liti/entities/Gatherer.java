package de.gurkenlabs.liti.entities;

import de.gurkenlabs.liti.abilities.ForceOfNature;
import de.gurkenlabs.liti.abilities.MushroomFrenzy;
import de.gurkenlabs.liti.abilities.SurvivalSkill;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.entities.MovementInfo;

@EntityInfo(width = 12, height = 22)
@CollisionInfo(collision = true, collisionBoxWidth = 6, collisionBoxHeight = 5, valign = Valign.MIDDLE_DOWN)
@CombatInfo(hitpoints = 120)
@MovementInfo(velocity = 70, acceleration = 500, deceleration = 250)
public class Gatherer extends Player {
  public Gatherer(PlayerConfiguration config) {
    super(config);
    this.setSurvivalSkill(new MushroomFrenzy(this));
  }
}
