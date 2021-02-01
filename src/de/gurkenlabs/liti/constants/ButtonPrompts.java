package de.gurkenlabs.liti.constants;

import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.liti.input.InputBinding;
import com.litiengine.resources.Resources;

import java.awt.image.BufferedImage;

public final class ButtonPrompts {

  private ButtonPrompts() {

  }

  public static BufferedImage get(int playerIndex, InputBinding.InputEvent inputEvent) {
    if (Players.getConfigurations().size() <= playerIndex) {
      return null;
    }
    InputBinding.InputType device = Players.getConfiguration(playerIndex).getInputDevice();
    if (device != InputBinding.InputType.GAMEPAD && device != InputBinding.InputType.KEYBOARD) {
      return null;
    }
    return Resources.images().get(String.format("%s_%s.png", device.toString().toLowerCase(), inputEvent.toString().toLowerCase()));
  }
}
