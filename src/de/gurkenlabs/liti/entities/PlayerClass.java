package de.gurkenlabs.liti.entities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        return 13;
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
  
  public int getRange() {
    switch (this) {
      case GATHERER:
        return 30;
      case SHAMAN:
        return 40;
      case WARRIOR:
        return 30;
      case HUNTRESS:
        return 50;
      case INVALID:
      default:
        return 30;
    }
  }
  
  public int getDashValue() {
    switch (this) {
      case GATHERER:
        return 100;
      case SHAMAN:
        return 200;
      case WARRIOR:
        return 120;
      case HUNTRESS:
        return 200;
      case INVALID:
      default:
        return 150;
    }
  }
  
  public double getStaminaRecoveryFactor() {
    switch (this) {
      case HUNTRESS:
        return 1.25;
      case WARRIOR:
        return .75;
      case INVALID:
      default:
        return 1.0;
    }
  }
  
  public double getStaminaDrainFactor() {
    switch (this) {
      case WARRIOR:
        return .25;
      case INVALID:
      default:
        return 1.0;
    }
  }
}
