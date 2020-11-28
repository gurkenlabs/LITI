package de.gurkenlabs.liti.abilities;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.AbilityExecution;
import de.gurkenlabs.litiengine.attributes.AttributeModifier;
import de.gurkenlabs.litiengine.attributes.Modification;

public class SurvivalSkill extends Ability {
  private final Player player;
  private final int requiredStamina;
  private final AttributeModifier<Double> modifier;

  /**
   * Initializes a new instance of the {@code Ability} class.
   *
   * @param executor The executing entity
   */
  protected SurvivalSkill(Player executor, int requiredStamina) {
    super(executor);
    this.player = executor;
    this.requiredStamina = requiredStamina;
    this.modifier = new AttributeModifier<>(Modification.SUBTRACT, this.getRequiredStamina());
  }

  @Override
  public boolean canCast() {
    return !this.player.isBlocking() && this.player.getStamina().get() >= this.getRequiredStamina() && super.canCast();
  }

  @Override
  public AbilityExecution cast() {
    AbilityExecution execution = super.cast();
    if (execution == null) {
      return null;
    }

    this.player.getStamina().modifyBaseValue(this.modifier);
    return execution;
  }

  public int getRequiredStamina() {
    return requiredStamina;
  }
}
