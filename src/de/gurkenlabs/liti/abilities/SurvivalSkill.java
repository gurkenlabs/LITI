package de.gurkenlabs.liti.abilities;

import de.gurkenlabs.liti.entities.Player;
import com.litiengine.abilities.Ability;
import com.litiengine.abilities.AbilityExecution;
import com.litiengine.attributes.AttributeModifier;
import com.litiengine.attributes.Modification;

public class SurvivalSkill extends Ability {
  private final Player player;
  private final int requiredStamina;
  private final AttributeModifier<Double> modifier;

  /**
   * Initializes a new instance of the {@code Ability} class.
   *
   * @param executor
   *         The executing entity
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

    this.player.getProgress().getInterval().didSomething();
    this.player.getStamina().modifyBaseValue(this.modifier);
    return execution;
  }

  public Player getPlayer() {
    return this.player;
  }

  public int getRequiredStamina() {
    return requiredStamina;
  }
}
