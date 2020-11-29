package de.gurkenlabs.liti.abilities;

import java.util.EnumMap;
import java.util.Map;

import de.gurkenlabs.liti.entities.PlayerClass;

public class Proficiency {
  private static final Map<PlayerClass, Map<Trait, Double>> classProficiencies = new EnumMap<>(PlayerClass.class);

  {
    classProficiencies.put(PlayerClass.GATHERER, new EnumMap<>(Trait.class));
    classProficiencies.put(PlayerClass.WARRIOR, new EnumMap<>(Trait.class));
    classProficiencies.put(PlayerClass.HUNTRESS, new EnumMap<>(Trait.class));
    classProficiencies.put(PlayerClass.SHAMAN, new EnumMap<>(Trait.class));

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
  }

  public static double get(PlayerClass playerClass, Trait trait) {
    return classProficiencies.get(playerClass).get(trait);
  }

  private static void set(PlayerClass playerClass, Trait trait, double value) {
    classProficiencies.get(playerClass).put(trait, value);
  }
}
