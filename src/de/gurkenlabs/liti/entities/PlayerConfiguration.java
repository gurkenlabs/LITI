package de.gurkenlabs.liti.entities;

import de.gurkenlabs.liti.input.InputBinding;
import de.gurkenlabs.litiengine.input.Gamepad;

public class PlayerConfiguration {
  private int index;
  private InputBinding.InputType inputDevice = InputBinding.InputType.NONE;
  private Gamepad gamepad;

  private PlayerClass type;

  // TODO: color etc.

  PlayerConfiguration(int index, InputBinding.InputType inputDevice, Gamepad gamepad) {
    this.index = index;
    this.inputDevice = inputDevice;
    this.gamepad = gamepad;
  }

  public InputBinding.InputType getInputDevice() {
    return inputDevice;
  }

  public Gamepad getGamepad() {
    return gamepad;
  }

  public PlayerClass getType() {
    return type;
  }

  public int getIndex() {
    return index;
  }

  public void setInputDevice(InputBinding.InputType inputDevice) {
    this.inputDevice = inputDevice;
  }

  public void setGamepad(Gamepad gamepad) {
    this.gamepad = gamepad;
  }

  public void setPlayerClass(PlayerClass type) {
    this.type = type;
  }
}
