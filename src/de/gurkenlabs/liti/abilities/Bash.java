package de.gurkenlabs.liti.abilities;

import de.gurkenlabs.liti.abilities.effects.HitEffect;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.AbilityInfo;
import de.gurkenlabs.litiengine.entities.EntityPivotType;

@AbilityInfo(name = "Bash", cooldown = 700, range = 0, impact = 15, impactAngle = 90, value = 20, duration = 300, multiTarget = true, origin = EntityPivotType.DIMENSION_CENTER)
public class Bash extends Ability {
  private final Player player;

  /**
   * Initializes a new instance of the {@code Ability} class.
   *
   * @param executor
   *          The executing entity
   */
  public Bash(Player executor) {
    super(executor);
    this.player = executor;
    this.getAttributes().value().setBaseValue(Proficiency.getDamage(executor.getPlayerClass()));
    this.getAttributes().impact().setBaseValue(Proficiency.getRange(executor.getPlayerClass()));
    this.addEffect(new HitEffect(this));
  }

  @Override
  public boolean canCast() {
    return !this.player.isBlocking() && !this.player.isDashing() && super.canCast();
  }
}
