package de.gurkenlabs.liti;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import de.gurkenlabs.litiengine.resources.Resources;

public class Program {
  /**
   * The main entry point for the Game.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    // set meta information about the game
    Game.info().setName("LITI");
    Game.info().setSubTitle("Stoneage Brawl");
    Game.info().setVersion("v1.0.0");

    // init the game infrastructure
    Game.init(args);

    Resources.load("game.litidata");

    GameManager.init();

    Game.screens().add(new GameScreen());

    Game.start();
    Game.world().loadEnvironment("dustpit");
  }
}
