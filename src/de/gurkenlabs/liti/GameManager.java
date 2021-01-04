package de.gurkenlabs.liti;

import de.gurkenlabs.liti.constants.Skins;
import de.gurkenlabs.liti.entities.*;
import de.gurkenlabs.liti.gui.DynamicZoomCamera;
import de.gurkenlabs.liti.input.InputManager;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.environment.CreatureMapObjectLoader;
import de.gurkenlabs.litiengine.environment.PropMapObjectLoader;
import de.gurkenlabs.litiengine.input.Input;

import java.awt.event.KeyEvent;

public final class GameManager {

  private static int DEFAULT_DEATH_DURATION = 5000;

  private GameManager() {
  }

  public static void update() {
    for (Player player : Players.getAll()) {
      if (player.isDead() && Game.time().since(player.getLastDeath()) > player.getResurrection()) {
        player.resurrect();
        spawn(player);
      }
    }
  }

  public static void init() {
    CreatureMapObjectLoader.registerCustomCreatureType(Chicken.class);
    PropMapObjectLoader.registerCustomPropType(Egg.class);

    Game.loop().attach(GameManager::update);
    InputManager.init();
    Game.world().onLoaded(e -> {
      if (!e.getMap().getName().equals("plateau2")) {
        return;
      }

      Game.graphics().setBaseRenderScale(4f);
      Game.world().setCamera(new DynamicZoomCamera());

      System.out.println(Players.joinedPlayers() + " players joined...");
      for (PlayerConfiguration config : Players.getConfigurations()) {
        // config.setPlayerClass(Game.random().next(PlayerClass.class));
        // config.setSkin(Skins.getRandom());
        Player player = Players.join(config);
        spawn(player);
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

  public static void playerDied(Player player) {
    long resurrection = DEFAULT_DEATH_DURATION;
    player.setResurrection(resurrection);
  }

  public static Spawnpoint getSpawn(Player player) {
    Spawnpoint spawn = Game.world().environment().getSpawnpoint("player-" + (player.getConfiguration().getIndex() + 1));
    if (spawn == null) {
      return null;
    }

    return spawn;
  }

  private static void spawn(Player player) {
    Spawnpoint spawn = getSpawn(player);
    if (spawn == null) {
      System.out.println("No spawnpoint found for player " + player);
      return;
    }

    System.out.println("Spawning player " + player);
    spawn.spawn(player);
    player.setState(Player.PlayerState.NORMAL);
  }
}
