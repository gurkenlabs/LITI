package de.gurkenlabs.liti.abilities;

import de.gurkenlabs.liti.abilities.effects.DashEffect;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.abilities.AbilityInfo;

@AbilityInfo(name = "Dash", cooldown = 1000, value = 150, duration = 300)
public class Dash extends SurvivalSkill {
  public Dash(Player executor) {
    super(executor, 40);

    this.getAttributes().value().setBaseValue((int) (executor.traits().mobility().get() * 2.2));
    this.getEffects().add(new DashEffect(this));
  }
}
