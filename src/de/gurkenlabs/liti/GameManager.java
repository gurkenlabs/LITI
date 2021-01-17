package de.gurkenlabs.liti;

import de.gurkenlabs.liti.constants.Skins;
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
import java.util.function.Supplier;

public final class GameManager {
  public enum GameState {
    NONE,
    PREGAME,
    PAUSED,
    INGAME,
    FINISHED,
  }

  public static int DURATION_DEFAULT_DEATH = 5000;
  public static int DURATION_PREGAME = 5000;
  public static int OBJECTIVE_COOLDOWN = 20000;

  private static final List<MapArea> baseAreas = new CopyOnWriteArrayList<>();
  private static final List<Spawnpoint> spawnPoints = new CopyOnWriteArrayList<>();
  private static MapArea chickenArea;
  private static Spawnpoint chickenSpawn;
  private static Spawnpoint eggSpawn;

  private static Player winner;

  private static GameState state = GameState.NONE;

  private static Objective currentObjective;
  private static Objective nextObjective;

  private static long nextObjectiveRequested;

  private GameManager() {
  }

  public static GameState getGameState(){
    return state;
  }

  public static void setGameState(GameState gameState){
    state = gameState;
  }

  public static void update() {
    for (Player player : Players.getAll()) {
      if (player.isDead() && Game.time().since(player.getLastDeath()) > player.getResurrection()) {
        player.resurrect();
        spawn(player);
      }
    }

    spawnNextObjective();
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

      winner = null;
      baseAreas.clear();
      baseAreas.addAll(e.getByTag(MapArea.class, "base"));

      spawnPoints.clear();
      spawnPoints.addAll(e.getSpawnpoints("playerspawn"));

      chickenArea = e.getArea("area-chicken");
      chickenSpawn = e.getSpawnpoint("spawn-chicken");
      eggSpawn = e.getSpawnpoint("spawn-egg");

      currentObjective = Objective.CHICKEN;
      currentObjective.start();

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
      Game.loop().perform(DURATION_PREGAME, () -> {
        state = GameState.INGAME;
        for(Player player : Players.getAll()) {
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
    long resurrection = DURATION_DEFAULT_DEATH;
    player.setResurrection(resurrection);
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

  public static MapArea getChickenArea(){
    return chickenArea;
  }

  public static Player getWinner(){
    return winner;
  }

  public static void stage1Reached(Player player) {
    System.out.println(player + " unlocked survival skill");
  }

  public static void stage2Reached(Player player) {
    System.out.println(player + " traits buffed");
  }

  public static void stage3Reached(Player player) {
    System.out.println(player + " end game");
    winner = player;
    setGameState(GameState.FINISHED);

    Game.world().camera().pan(player.getCenter(), (int)Game.time().toTicks(2000));
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

    for(Player p : Players.getAll()){
      p.setState(Player.PlayerState.LOCKED);
    }
  }

  private static void nextObjective(){
    nextObjectiveRequested = Game.time().now();

    if(currentObjective == Objective.CHICKEN){
      nextObjective = Objective.EGG;
    } else {
      nextObjective = Objective.CHICKEN;
    }

    currentObjective = null;
  }

  private static void spawnNextObjective() {
    if(nextObjectiveRequested == 0 || nextObjective == null || getGameState() != GameState.INGAME){
      return;
    }

    if(Game.time().since(nextObjectiveRequested) < OBJECTIVE_COOLDOWN){
      return;
    }

    currentObjective = nextObjective;
    nextObjective = null;
    currentObjective.start();
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

  public static class Objective {
    private static final Objective CHICKEN = new Objective(Objective::spawnChicken);
    private static final Objective EGG = new Objective(Objective::spawnEgg);

    private final Supplier<IObjective> init;

    private IObjective spawnedEntity;
    Objective(Supplier<IObjective> init){
      this.init = init;
    }

    public void start(){
      this.spawnedEntity = this.init.get();
      if(this.spawnedEntity != null){
        this.spawnedEntity.onFinished(() -> GameManager.nextObjective());
      }
    }

    private static IObjective spawnChicken(){
      if(chickenSpawn == null){
        System.out.println("NO SPAWN FOR CHICKEN FOUND!");
        return null;
      }

      Chicken chicken = new Chicken();
      chickenSpawn.spawn(chicken);
      return chicken;
    }

    private static IObjective spawnEgg() {
      if(eggSpawn == null){
        System.out.println("NO SPAWN FOR EGG FOUND!");
        return null;
      }

      Egg egg = new Egg("egg");
      eggSpawn.spawn(egg);
      return egg;
    }
  }
}
