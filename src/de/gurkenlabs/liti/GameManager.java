package de.gurkenlabs.liti;

import de.gurkenlabs.liti.entities.Warrior;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.environment.CreatureMapObjectLoader;
import de.gurkenlabs.litiengine.input.KeyboardEntityController;
import de.gurkenlabs.litiengine.physics.IMovementController;

public final class GameManager {
  private GameManager() {
  }

  public static void init() {
    CreatureMapObjectLoader.registerCustomCreatureType(Warrior.class);

    Game.world().onLoaded(e -> {
      Warrior warrior = Game.world().environment().get(Warrior.class, "warrior-test");
      if (warrior != null) {
        IMovementController controller = new KeyboardEntityController<>(warrior);
        warrior.setController(IMovementController.class, controller);
      }
    });
  }
}
