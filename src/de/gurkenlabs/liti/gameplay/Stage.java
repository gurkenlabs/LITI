package de.gurkenlabs.liti.gameplay;

import de.gurkenlabs.liti.GameManager;
import de.gurkenlabs.liti.entities.Player;

import java.util.function.Consumer;

public class Stage {
  public static Stage STAGE0 = new Stage(0, 0, player -> {
  }, 3);
  public static Stage STAGE1 = new Stage(1, 1000, Stage::stage1Reached, 5);
  public static Stage STAGE2 = new Stage(2, 2000, Stage::stage2Reached, 8);
  public static Stage STAGE3 = new Stage(3, 2000, Stage::stage3Reached, 8);

  private final int index;
  private final int requiredEP;
  private final Consumer<Player> stageReached;
  private final int respawn;

  private Stage(int index, int requiredEP, Consumer<Player> stageReached, int respawn) {
    this.index = index;
    this.requiredEP = requiredEP;
    this.stageReached = stageReached;
    this.respawn = respawn;
  }

  public static void stage1Reached(Player player) {
    System.out.println(player + " unlocked survival skill");
  }

  public static void stage2Reached(Player player) {
    System.out.println(player + " traits buffed");
  }

  public static void stage3Reached(Player player) {
    GameManager.endGame(player);
  }

  public int getIndex() {
    return this.index;
  }

  public int getRequiredEP() {
    return this.requiredEP;
  }

  public Stage next() {
    switch (this.index) {
      case 0:
        return STAGE1;
      case 1:
        return STAGE2;
      case 2:
        return STAGE3;
      default:
        return null;
    }
  }

  public Consumer<Player> uponStageReached() {
    return stageReached;
  }
}
