package de.gurkenlabs.liti;

import com.sun.jna.NativeLibrary;
import de.gurkenlabs.liti.gui.IngameScreen;
import de.gurkenlabs.liti.gui.LobbyScreen;
import de.gurkenlabs.liti.gui.MenuScreen;
import de.gurkenlabs.liti.gui.ScoreScreen;
import de.gurkenlabs.liti.input.InputConfiguration;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.resources.Resources;
import uk.co.caprica.vlcj.binding.RuntimeUtil;

public class Program {
  /**
   * The main entry point for the Game.
   *
   * @param args
   *          The command line arguments.
   */
  public static void main(String[] args) {
    NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "dist/libs");
    NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcCoreLibraryName(), "dist/libs");
    // set meta information about the game
    Game.info().setName("LITI");
    Game.info().setSubTitle("Stoneage Brawl");
    Game.info().setVersion("v1.0.0");

    // init the game infrastructure
    Game.init(args);

    Resources.load("game.litidata");

    GameManager.init(new InputConfiguration());
    
    //Game.screens().add(new SplashScreen());
    Game.screens().add(new IngameScreen());
    Game.screens().add(new MenuScreen());
    Game.screens().add(new LobbyScreen());
    Game.screens().add(new ScoreScreen());

    Game.start();
    Game.world().loadEnvironment("plateau");

  }
}
