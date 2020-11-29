package de.gurkenlabs.liti.abilities;

import java.util.EnumMap;
import java.util.Map;

import de.gurkenlabs.liti.entities.PlayerClass;

public class Proficiency {
  private Map<PlayerClass, Map<Trait, Integer>> proficiencies;

  private Proficiency() {
    proficiencies = new EnumMap<>(PlayerClass.class);
    proficiencies.put(PlayerClass.GATHERER, new EnumMap(Trait.class) {

    });
  }

  public int getProficiencies(PlayerClass playerClass, Trait trait) {
    return proficiencies.get(playerClass).get(trait);
  }

  public static double getStamina(PlayerClass playerClass) {
    switch (playerClass) {
    case GATHERER:
      return 100;
    case SHAMAN:
      return 60;
    case WARRIOR:
      return 60;
    case HUNTRESS:
      return 100;
    default:
      return 100;
    }
  }

  public static int getDamage(PlayerClass playerClass) {
    switch (playerClass) {
    case GATHERER:
      return 13;
    case SHAMAN:
      return 20;
    case WARRIOR:
      return 20;
    case HUNTRESS:
      return 15;
    default:
      return 15;
    }
  }

  public static int getRange(PlayerClass playerClass) {
    switch (playerClass) {
    case GATHERER:
      return 30;
    case SHAMAN:
      return 40;
    case WARRIOR:
      return 30;
    case HUNTRESS:
      return 50;
    default:
      return 30;
    }
  }

  public static int getDashValue(PlayerClass playerClass) {
    switch (playerClass) {
    case GATHERER:
      return 100;
    case SHAMAN:
      return 200;
    case WARRIOR:
      return 120;
    case HUNTRESS:
      return 200;
    default:
      return 150;
    }
  }

  public static double getStaminaRecoveryFactor(PlayerClass playerClass) {
    switch (playerClass) {
    case HUNTRESS:
      return 1.25;
    case WARRIOR:
      return .75;
    default:
      return 1.0;
    }
  }

  public static double getStaminaDrainFactor(PlayerClass playerClass) {
    switch (playerClass) {
    case WARRIOR:
      return .25;
    default:
      return 1.0;
    }
  }
}
