package de.gurkenlabs.liti.abilities;

import de.gurkenlabs.liti.abilities.effects.DashEffect;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.AbilityInfo;

@AbilityInfo(name = "Dash", cooldown = 1000, value = 150, duration = 300)
public class Dash extends Ability {
  public Dash(Player executor) {
    super(executor);
    this.getEffects().add(new DashEffect(this));
  }
}
