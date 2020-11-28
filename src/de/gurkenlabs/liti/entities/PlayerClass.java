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

  public int getDamage() {
    switch (this) {
      case GATHERER:
        return 15;
      case SHAMAN:
        return 20;
      case WARRIOR:
        return 20;
      case HUNTRESS:
        return 15;
      case INVALID:
      default:
        return 15;
    }
  }
}
