package de.gurkenlabs.liti;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.entities.PlayerConfiguration;
import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.liti.gui.DynamicZoomCamera;
import de.gurkenlabs.liti.input.InputConfiguration;
import de.gurkenlabs.liti.input.InputManager;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.environment.Environment;

public final class GameManager {
  private GameManager() {
  }

  public static void init(InputConfiguration inputConfig) {
    Game.world().onLoaded(e -> {
      if (!e.getMap().getName().equals("plateau")) {
        return;
      }

      Game.graphics().setBaseRenderScale(4f);
      Game.world().setCamera(new DynamicZoomCamera());

      System.out.println(Players.joinedPlayers() + " players joined...");
      for (PlayerConfiguration config : Players.getConfigurations()) {
        Player player = Players.join(config);
        spawn(player, e);
      }
    });

    InputManager.init(inputConfig);
  }

  private static void spawn(Player player, Environment e) {
    Spawnpoint spawn = e.getSpawnpoint("player-" + (player.getConfiguration().getIndex() + 1));
    if (spawn == null) {
      System.out.println("No spawnpoint found for player " + player);
      return;
    }
    System.out.println("Spawning player " + player);
    spawn.spawn(player);
  }
}
