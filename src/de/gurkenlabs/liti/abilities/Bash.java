package de.gurkenlabs.liti.abilities;

import de.gurkenlabs.liti.abilities.effects.HitEffect;
import de.gurkenlabs.liti.entities.Player;
import com.litiengine.abilities.Ability;
import com.litiengine.abilities.AbilityInfo;
import com.litiengine.entities.EntityPivotType;

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
    this.getAttributes().value().setBaseValue((int) Proficiency.get(executor.getPlayerClass(), Trait.DAMAGE));
    this.getAttributes().impact().setBaseValue((int) Proficiency.get(executor.getPlayerClass(), Trait.RANGE));
    this.addEffect(new HitEffect(this));
  }

  @Override
  public boolean canCast() {
    return !this.player.isBlocking() && !this.player.isDashing() && super.canCast();
  }
}
