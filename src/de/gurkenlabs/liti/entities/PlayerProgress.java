package de.gurkenlabs.liti.entities;

import de.gurkenlabs.liti.GameManager;
import de.gurkenlabs.liti.graphics.FloatingCombatTextEmitter;
import de.gurkenlabs.litiengine.Game;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.function.Consumer;

public class PlayerProgress {
  private final Player player;

  private Stage currentStage = Stage.STAGE0;
  private int evolutionPoints = 0;

  public PlayerProgress(Player player) {
    this.player = player;
  }

  public int getEvolutionPoints() {
    return this.evolutionPoints;
  }

  public Stage getCurrentStage() {
    return this.currentStage;
  }

  public void grantEP(int points) {
    if (this.getCurrentStage().next() == null) {
      // don't collect any XP if max stage is reached
      return;
    }

    if (this.getCurrentStage().next() != null && this.getEvolutionPoints() + points > this.getCurrentStage().next().requiredEP) {
      int remaining = this.getEvolutionPoints() + points - this.getCurrentStage().next().requiredEP;
      this.transitionToNextStage();
      this.grantEP(remaining);
      return;
    }

    this.evolutionPoints += points;
    Game.world().environment().add(new FloatingCombatTextEmitter(points + "EP", new Point2D.Double(this.player.getCenter().getX() - 17, this.player.getY()), Color.YELLOW));
  }

  public double getRelativeEP() {
    return this.getCurrentStage().next() == null ? 0 : this.evolutionPoints / this.getCurrentStage().next().requiredEP;
  }

  private void transitionToNextStage() {
    this.currentStage = this.currentStage.next();
    this.evolutionPoints = 0;
    this.currentStage.stageReached.accept(this.player);
    System.out.println(this.player + " reached stage " + this.currentStage.index);
  }

  public static class Stage {
    public static Stage STAGE0 = new Stage(0, 0, player -> {}, 3);
    public static Stage STAGE1 = new Stage(1, 1000, GameManager::unlockSurvivalSkill, 5);
    public static Stage STAGE2 = new Stage(2, 2000, GameManager::buffTraits, 8);
    public static Stage STAGE3 = new Stage(3, 2000, GameManager::endGame, 8);

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
  }
}
