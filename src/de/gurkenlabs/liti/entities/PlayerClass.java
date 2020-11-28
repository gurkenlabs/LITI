package de.gurkenlabs.liti.entities;

public enum PlayerClass {
  INVALID,
  WARRIOR,
  SHAMAN,
  HUNTRESS,
  GATHERER;

  public double getStamina() {
    switch (this) {
      case GATHERER:
        return 100;
      case SHAMAN:
        return 60;
      case WARRIOR:
        return 60;
      case HUNTRESS:
        return 100;
      case INVALID:
      default:
        return 100;
    }
  }
}
