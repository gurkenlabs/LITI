package de.gurkenlabs.liti.entities;

import de.gurkenlabs.litiengine.resources.Resources;

public enum PlayerClass {
  WARRIOR,
  SHAMAN,
  HUNTRESS,
  GATHERER;

  public String getSurvivalSkill() {
    return Resources.strings().get(String.format("%s-survivalSkill", this.name().toLowerCase()));
  }

}
