package de.gurkenlabs.liti.abilities;

import de.gurkenlabs.liti.abilities.effects.HitEffect;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.abilities.AbilityInfo;
import de.gurkenlabs.litiengine.entities.EntityPivotType;

@AbilityInfo(name = "Bash", cooldown = 700, range = 0, impact = 15, value = 20, duration = 300, multiTarget = true, origin = EntityPivotType.DIMENSION_CENTER)
public class Bash extends SurvivalSkill {
  /**
   * Initializes a new instance of the {@code Ability} class.
   *
   * @param executor The executing entity
   */
  public Bash(Player executor) {
    super(executor, 20);

    this.getAttributes().value().setBaseValue(executor.getPlayerClass().getDamage());
    this.getAttributes().impact().setBaseValue(executor.getPlayerClass().getRange());
    this.addEffect(new HitEffect(this));
  }
}
