package de.gurkenlabs.liti.input;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.physics.MovementController;

public class PlayerInputController extends MovementController<Player> {
  public PlayerInputController(Player player) {
    super(player);
  }
}
