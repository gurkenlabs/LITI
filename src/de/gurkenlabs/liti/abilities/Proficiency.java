package de.gurkenlabs.liti.abilities;

import java.util.EnumMap;
import java.util.Map;

import de.gurkenlabs.liti.entities.PlayerClass;

public class Proficiency {
  private static final Map<PlayerClass, Map<Trait, Double>> playerProficiencies = new EnumMap<>(PlayerClass.class);
  private static final Map<Trait, Double> baseProficiencies = new EnumMap<>(Trait.class);

  {
    baseProficiencies.put(Trait.DAMAGE, 1.0);
    baseProficiencies.put(Trait.MOBILITY, 1.0);
    baseProficiencies.put(Trait.HEALTH, 1.0);
    baseProficiencies.put(Trait.RANGE, 1.0);
    baseProficiencies.put(Trait.RECOVERY, 1.0);

    playerProficiencies.put(PlayerClass.GATHERER, new EnumMap<>(Trait.class));
    playerProficiencies.put(PlayerClass.WARRIOR, new EnumMap<>(Trait.class));
    playerProficiencies.put(PlayerClass.HUNTRESS, new EnumMap<>(Trait.class));
    playerProficiencies.put(PlayerClass.SHAMAN, new EnumMap<>(Trait.class));

    setProficiency(PlayerClass.GATHERER, Trait.DAMAGE, 1.0);
    setProficiency(PlayerClass.GATHERER, Trait.MOBILITY, 1.2);
    setProficiency(PlayerClass.GATHERER, Trait.HEALTH, 1.5);
    setProficiency(PlayerClass.GATHERER, Trait.RANGE, 1.0);
    setProficiency(PlayerClass.GATHERER, Trait.RECOVERY, 1.25);

    setProficiency(PlayerClass.WARRIOR, Trait.DAMAGE, 1.0);
    setProficiency(PlayerClass.WARRIOR, Trait.MOBILITY, 1.2);
    setProficiency(PlayerClass.WARRIOR, Trait.HEALTH, 1.5);
    setProficiency(PlayerClass.WARRIOR, Trait.RANGE, 1.0);
    setProficiency(PlayerClass.WARRIOR, Trait.RECOVERY, 1.25);
  }

  public static double getBaseValue(Trait trait) {
    return baseProficiencies.get(trait);
  }

  public static double getMultiplier(PlayerClass playerClass, Trait trait) {
    return playerProficiencies.get(playerClass).get(trait);
  }

  public static double get(PlayerClass playerClass, Trait trait) {
    return getMultiplier(playerClass, trait) * getBaseValue(trait);
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

  private static void setProficiency(PlayerClass playerClass, Trait trait, double value) {
    playerProficiencies.get(playerClass).put(trait, value);
  }
}
