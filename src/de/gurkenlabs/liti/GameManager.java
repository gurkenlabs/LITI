package de.gurkenlabs.liti;

import de.gurkenlabs.liti.constants.Timings;
import de.gurkenlabs.liti.entities.*;
import de.gurkenlabs.liti.gui.DynamicZoomCamera;
import de.gurkenlabs.liti.gui.IngameScreen;
import de.gurkenlabs.liti.input.InputManager;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.MapArea;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.environment.CreatureMapObjectLoader;
import de.gurkenlabs.litiengine.environment.PropMapObjectLoader;
import de.gurkenlabs.litiengine.input.Input;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public final class GameManager {
  public enum GameState {
    NONE,
    PREGAME,
    PAUSED,
    INGAME,
    FINISHED,
  }

  private static final List<MapArea> baseAreas = new CopyOnWriteArrayList<>();
  private static final List<Spawnpoint> spawnPoints = new CopyOnWriteArrayList<>();
  private static MapArea chickenArea;

  private static GameState state = GameState.NONE;

  private GameManager() {
  }

  public static GameState getGameState() {
    return state;
  }

  public static void setGameState(GameState gameState) {
    state = gameState;
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

      baseAreas.clear();
      baseAreas.addAll(e.getByTag(MapArea.class, "base"));

      spawnPoints.clear();
      spawnPoints.addAll(e.getSpawnPoints("playerspawn"));

      chickenArea = e.getArea("area-chicken");
      Game.graphics().setBaseRenderScale(4f);
      Game.world().setCamera(new DynamicZoomCamera());
      Game.world().camera().setZoom(DynamicZoomCamera.maxZoom * 2, 0);
      Game.world().camera().setZoom(DynamicZoomCamera.minZoom, 5000);

      System.out.println(Players.joinedPlayers() + " players joined...");
      for (PlayerConfiguration config : Players.getConfigurations()) {
        //         config.setPlayerClass(Game.random().next(PlayerClass.class));
        //         config.setSkin(Skins.getRandom());
        Player player = Players.join(config);
        spawn(player);
      }

      state = GameState.PREGAME;
      IngameScreen.instance().getHud().getPregameCountdown().start();
      Game.loop().perform(Timings.COUNTDOWN_PREGAME, () -> {
        state = GameState.INGAME;
        for (Player player : Players.getAll()) {
          player.setState(Player.PlayerState.NORMAL);
        }
      });
    });

    if (Game.isDebug()) {
      Input.keyboard().onKeyPressed(KeyEvent.VK_F12, e -> {
        for (Player player : Game.world().environment().getEntities(Player.class)) {
          if (player.isDead()) {
            player.resurrect();
          }
        }
      });

      Input.keyboard().onKeyTyped(KeyEvent.VK_F8, e -> {
        Players.getAll().get(0).getProgress().grantEP(Game.random().nextInt(100, 200));
      });
    }
  }

  public static void playerDied(Player player) {
    player.setResurrection(Timings.COUNTDOWN_RESPAWN);
  }

  public static Spawnpoint getSpawn(Player player) {
    Optional<Spawnpoint> spawn = spawnPoints.stream().filter(
        x -> x.getName() != null && x.getName().equals("player-" + (player.getConfiguration().getIndex() + 1))).findFirst();
    if (!spawn.isPresent()) {
      return null;
    }

    return spawn.get();
  }

  public static MapArea getBase(Player player) {
    Optional<MapArea> base = baseAreas.stream().filter(
        x -> x.getName() != null && x.getName().equals("player-" + (player.getConfiguration().getIndex() + 1))).findFirst();
    if (!base.isPresent()) {
      return null;
    }

    return base.get();
  }

  public static MapArea getChickenArea() {
    return chickenArea;
  }

  public static void stage1Reached(Player player) {
    System.out.println(player + " unlocked survival skill");
  }

  public static void stage2Reached(Player player) {
    System.out.println(player + " traits buffed");
  }

  public static void stage3Reached(Player player) {
    System.out.println(player + " end game");
    setGameState(GameState.FINISHED);

    Game.world().camera().pan(player.getCenter(), (int) Game.time().toTicks(2000));
    Game.loop().perform(2000, () -> {
      Game.world().camera().setZoom(DynamicZoomCamera.maxZoom, 1000);

      Game.loop().perform(2000, () -> {
        Game.window().getRenderComponent().fadeOut(3000);
        Game.loop().perform(3000, () -> {
          Game.screens().display("SCORE");
          Game.window().getRenderComponent().fadeIn(2000);
        });
      });
    });

    for (Player p : Players.getAll()) {
      p.setState(Player.PlayerState.LOCKED);
    }
  }

  private static void spawn(Player player) {
    Spawnpoint spawn = getSpawn(player);
    if (spawn == null) {
      System.out.println("No spawnpoint found for player " + player);
      return;
    }

    System.out.println("Spawning player " + player);
    spawn.spawn(player);
    player.setState(Player.PlayerState.LOCKED);
  }
}
