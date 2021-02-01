package de.gurkenlabs.liti.input;

import de.gurkenlabs.liti.entities.Player;
import com.litiengine.physics.MovementController;

public class PlayerInputController extends MovementController<Player> {
  public PlayerInputController(Player player) {
    super(player);
  }
}
