package de.gurkenlabs.liti.gameplay;

import de.gurkenlabs.liti.GameManager;
import de.gurkenlabs.liti.GameState;
import de.gurkenlabs.liti.constants.Timings;
import de.gurkenlabs.liti.entities.Chicken;
import de.gurkenlabs.liti.entities.Egg;
import de.gurkenlabs.liti.entities.IObjective;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.MapArea;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.environment.Environment;

import java.util.function.Supplier;

public class Objective {
  /**
   * <b>Capture the chicken</b>
   * <p>Chicken spawns and walks around.
   * Channel a chicken for 1s to capture it and bring it to the base and gain EP.
   * The player cannot dash, block, or hit anything.
   * Performing these actions will drop the chicken.
   * Fixed velocity while the chicken is carried around (not class-specific).</p>
   */
  private static final Objective CHICKEN = new Objective(Objective::spawnChicken);

  /**
   * <b>Smash dinosaur egg</b>
   * <p>Channel the egg for 4 seconds to destroy it and gain EP</p>
   */
  private static final Objective EGG = new Objective(Objective::spawnEgg);

  private static Objective currentObjective;
  private static Objective nextObjective;
  private static MapArea chickenArea;
  private static Spawnpoint chickenSpawn;
  private static Spawnpoint eggSpawn;

  private static long nextObjectiveRequested;

  private final Supplier<IObjective> init;

  private IObjective spawnedEntity;

  Objective(Supplier<IObjective> init) {
    this.init = init;
  }

  public static void plateauMapLoaded(Environment environment) {
    chickenArea = environment.getArea("area-chicken");
    chickenSpawn = environment.getSpawnpoint("spawn-chicken");
    eggSpawn = environment.getSpawnpoint("spawn-egg");

    currentObjective = Objective.CHICKEN;
    currentObjective.start();
  }

  public static MapArea getChickenArea() {
    return chickenArea;
  }

  public static void spawnNextObjective() {
    if (nextObjectiveRequested == 0 || nextObjective == null || GameManager.getGameState() != GameState.INGAME) {
      return;
    }

    if (Game.time().since(nextObjectiveRequested) < Timings.COOLDOWN_OBJECTIVE) {
      return;
    }

    currentObjective = nextObjective;
    nextObjective = null;
    currentObjective.start();
  }

  public void start() {
    this.spawnedEntity = this.init.get();
    if (this.spawnedEntity != null) {
      this.spawnedEntity.onFinished(Objective::nextObjective);
    }
  }

  private static void nextObjective() {
    nextObjectiveRequested = Game.time().now();

    if (currentObjective == Objective.CHICKEN) {
      nextObjective = Objective.EGG;
    } else {
      nextObjective = Objective.CHICKEN;
    }

    currentObjective = null;
  }

  private static IObjective spawnChicken() {
    if (chickenSpawn == null) {
      System.out.println("NO SPAWN FOR CHICKEN FOUND!");
      return null;
    }

    Chicken chicken = new Chicken();
    chickenSpawn.spawn(chicken);
    return chicken;
  }

  private static IObjective spawnEgg() {
    if (eggSpawn == null) {
      System.out.println("NO SPAWN FOR EGG FOUND!");
      return null;
    }

    Egg egg = new Egg("egg");
    eggSpawn.spawn(egg);
    return egg;
  }
}