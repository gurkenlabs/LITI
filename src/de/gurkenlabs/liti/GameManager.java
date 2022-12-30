package de.gurkenlabs.liti;

import de.gurkenlabs.liti.constants.Skins;
import de.gurkenlabs.liti.constants.Timings;
import de.gurkenlabs.liti.entities.Chicken;
import de.gurkenlabs.liti.entities.Egg;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.entities.PlayerClass;
import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.liti.gameplay.Objective;
import de.gurkenlabs.liti.gui.DynamicZoomCamera;
import de.gurkenlabs.liti.gui.IngameScreen;
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

  public static boolean DBG_SKIP_TO_INGAME = false;

  private static final List<MapArea> baseAreas = new CopyOnWriteArrayList<>();
  private static final List<Spawnpoint> spawnPoints = new CopyOnWriteArrayList<>();


  private static Player winner;

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
      if (!player.isLoaded() && player.isDead() && player.getLastDeath() != 0 && Game.time().since(player.getLastDeath()) > player
          .getResurrection()) {
        player.resurrect();
        spawn(player);
        player.setState(Player.PlayerState.NORMAL);
      }
    }

    Objective.spawnNextObjective();
  }

  public static void init() {

    CreatureMapObjectLoader.registerCustomCreatureType(Chicken.class);
    PropMapObjectLoader.registerCustomPropType(Egg.class);

    Game.loop().attach(GameManager::update);
    Game.world().onLoaded(e -> {
      if (!e.getMap().getName().equals("plateau2")) {
        return;
      }

      winner = null;
      baseAreas.clear();
      baseAreas.addAll(e.getByTag(MapArea.class, "base"));

      spawnPoints.clear();
      spawnPoints.addAll(e.getSpawnpoints("playerspawn"));

      Objective.plateauMapLoaded(e);

      Game.graphics().setBaseRenderScale(4f);
      Game.world().setCamera(new DynamicZoomCamera());
      Game.world().camera().setZoom(DynamicZoomCamera.maxZoom * 2, 0);

      Game.world().camera().setZoom(DynamicZoomCamera.minZoom, Timings.COUNTDOWN_PREGAME);

      if (DBG_SKIP_TO_INGAME) {
        Players.getConfigurations().forEach(c -> {
          c.setPlayerClass(Game.random().next(PlayerClass.class));
          c.setSkin(Skins.getRandom());
          Players.join(c);
        });
      }

      Players.getAll().forEach(GameManager::spawn);

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

      Input.keyboard().onKeyTyped(KeyEvent.VK_F8, e -> Players.getAll().get(0).getProgress().grantEP(Game.random().nextInt(100, 200)));
      Input.keyboard().onKeyTyped(KeyEvent.VK_F9, e -> Players.getAll().get(0).die());
    }
  }

  public static void playerDied(Player player) {
    player.setResurrection(Timings.COUNTDOWN_RESPAWN);
  }

  public static Spawnpoint getSpawn(Player player) {
    Optional<Spawnpoint> spawn = spawnPoints.stream().filter(
        x -> x.getName() != null && x.getName().equals("player-" + (player.getConfiguration().getIndex() + 1))).findFirst();

    return spawn.orElse(null);
  }

  public static MapArea getBase(Player player) {
    Optional<MapArea> base = baseAreas.stream().filter(
        x -> x.getName() != null && x.getName().equals("player-" + (player.getConfiguration().getIndex() + 1))).findFirst();

    return base.orElse(null);
  }

  public static Player getWinner() {
    return winner;
  }

  public static void endGame(Player winner) {
    System.out.println(winner + " end game");
    GameManager.winner = winner;
    GameManager.setGameState(GameState.FINISHED);

    Game.world().camera().pan(winner.getCenter(), (int) Game.time().toTicks(2000));
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
