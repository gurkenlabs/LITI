package de.gurkenlabs.liti.abilities;

import java.util.EnumMap;
import java.util.Map;

import de.gurkenlabs.liti.entities.PlayerClass;

public final class Proficiency {
  private static final Map<PlayerClass, Map<Trait, Double>> classProficiencies = new EnumMap<>(PlayerClass.class);

  private static final Map<PlayerClass, Map<Trait, Double>> buffedClassProficiencies = new EnumMap<>(PlayerClass.class);

  static {
    classProficiencies.put(PlayerClass.GATHERER, new EnumMap<>(Trait.class));
    classProficiencies.put(PlayerClass.WARRIOR, new EnumMap<>(Trait.class));
    classProficiencies.put(PlayerClass.HUNTRESS, new EnumMap<>(Trait.class));
    classProficiencies.put(PlayerClass.SHAMAN, new EnumMap<>(Trait.class));

    buffedClassProficiencies.put(PlayerClass.GATHERER, new EnumMap<>(Trait.class));
    buffedClassProficiencies.put(PlayerClass.WARRIOR, new EnumMap<>(Trait.class));
    buffedClassProficiencies.put(PlayerClass.HUNTRESS, new EnumMap<>(Trait.class));
    buffedClassProficiencies.put(PlayerClass.SHAMAN, new EnumMap<>(Trait.class));

    set(PlayerClass.WARRIOR, Trait.DAMAGE, 20);
    set(PlayerClass.WARRIOR, Trait.STAMINA, 100);
    set(PlayerClass.WARRIOR, Trait.HEALTH, 100);
    set(PlayerClass.WARRIOR, Trait.RANGE, 40);
    set(PlayerClass.WARRIOR, Trait.RECOVERY, .75);
    set(PlayerClass.WARRIOR, Trait.MOBILITY, 60);

    set(PlayerClass.SHAMAN, Trait.DAMAGE, 20);
    set(PlayerClass.SHAMAN, Trait.STAMINA, 60);
    set(PlayerClass.SHAMAN, Trait.HEALTH, 70);
    set(PlayerClass.SHAMAN, Trait.RANGE, 50);
    set(PlayerClass.SHAMAN, Trait.RECOVERY, .90);
    set(PlayerClass.SHAMAN, Trait.MOBILITY, 60);

    set(PlayerClass.HUNTRESS, Trait.DAMAGE, 13);
    set(PlayerClass.HUNTRESS, Trait.STAMINA, 100);
    set(PlayerClass.HUNTRESS, Trait.HEALTH, 70);
    set(PlayerClass.HUNTRESS, Trait.RANGE, 40);
    set(PlayerClass.HUNTRESS, Trait.RECOVERY, 1.25);
    set(PlayerClass.HUNTRESS, Trait.MOBILITY, 90);

    set(PlayerClass.GATHERER, Trait.DAMAGE, 15);
    set(PlayerClass.GATHERER, Trait.STAMINA, 80);
    set(PlayerClass.GATHERER, Trait.HEALTH, 120);
    set(PlayerClass.GATHERER, Trait.RANGE, 30);
    set(PlayerClass.GATHERER, Trait.RECOVERY, .90);
    set(PlayerClass.GATHERER, Trait.MOBILITY, 80);

    setBuffed(PlayerClass.GATHERER, Trait.HEALTH, 150);
  }

  public static Map<Trait, Double> getTraits(PlayerClass playerClass){
    return classProficiencies.get(playerClass);
  }

  public static Map<Trait, Double> getBuffedTraits(PlayerClass playerClass){
    return buffedClassProficiencies.get(playerClass);
  }

  public static void update(PlayerClass playerClass, Trait trait, double value){
    classProficiencies.get(playerClass).put(trait, value);
  }

  public static double get(PlayerClass playerClass, Trait trait) {
    return classProficiencies.get(playerClass).get(trait);
  }

  public static int getLevel(PlayerClass playerClass, Trait trait) {
    double value = get(playerClass, trait);
    int level = 0;
    switch (trait) {
    case HEALTH:
      if (value < 100) {
        return 0;
      } else if (value >= 100 && value < 120) {
        return 1;
      } else {
        return 2;
      }
    case DAMAGE:
      if (value < 15) {
        return 0;
      } else if (value >= 15 && value < 20) {
        return 1;
      } else {
        return 2;
      }
    case STAMINA:
      if (value < 80) {
        return 0;
      } else if (value >= 80 && value < 100) {
        return 1;
      } else {
        return 2;
      }
    case RECOVERY:
      if (value < 0.9) {
        return 0;
      } else if (value >= 0.9 && value < 1.25) {
        return 1;
      } else {
        return 2;
      }
    case RANGE:
      if (value < 40) {
        return 0;
      } else if (value >= 40 && value < 50) {
        return 1;
      } else {
        return 2;
      }
    case MOBILITY:
      if (value < 80) {
        return 0;
      } else if (value >= 80 && value < 90) {
        return 1;
      } else {
        return 2;
      }
    default:
      return 0;
    }
  }

  private static void set(PlayerClass playerClass, Trait trait, double value) {
    classProficiencies.get(playerClass).put(trait, value);
  }

  private static void setBuffed(PlayerClass playerClass, Trait trait, double value) {
    buffedClassProficiencies.get(playerClass).put(trait, value);
  }
}
