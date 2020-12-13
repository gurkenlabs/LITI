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
import de.gurkenlabs.litiengine.input.Input;

import java.awt.event.KeyEvent;

public final class GameManager {
  private GameManager() {
  }

  public static void init(InputConfiguration inputConfig) {
    InputManager.init(inputConfig);
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

    if (Game.isDebug()) {
      Input.keyboard().onKeyPressed(KeyEvent.VK_F12, e -> {
        for (Player player : Game.world().environment().getEntities(Player.class)) {
          if (player.isDead()) {
            player.resurrect();
          }
        }
      });
    }
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
