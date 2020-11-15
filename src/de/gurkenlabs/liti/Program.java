package de.gurkenlabs.liti;

import de.gurkenlabs.liti.gui.IngameScreen;
import de.gurkenlabs.liti.gui.LobbyScreen;
import de.gurkenlabs.liti.gui.MenuScreen;
import de.gurkenlabs.liti.gui.ScoreScreen;
import de.gurkenlabs.liti.input.InputConfiguration;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import de.gurkenlabs.litiengine.resources.Resources;

import static de.gurkenlabs.litiengine.Game.config;

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
    InputConfiguration inputConfig = new InputConfiguration();
    config().add(inputConfig);

    // init the game infrastructure
    Game.init(args);

    Resources.load("game.litidata");

    System.out.println(System.getProperty("java.library.path"));
    GameManager.init(inputConfig);

    Game.screens().add(new IngameScreen());
    Game.screens().add(new MenuScreen());
    Game.screens().add(new LobbyScreen());
    Game.screens().add(new ScoreScreen());

    Game.start();
    Game.world().loadEnvironment("dustpit");

  }
}
