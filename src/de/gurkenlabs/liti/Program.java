package de.gurkenlabs.liti;

import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.gui.IngameScreen;
import de.gurkenlabs.liti.gui.LoadingScreen;
import de.gurkenlabs.liti.gui.LobbyScreen;
import de.gurkenlabs.liti.gui.ScoreScreen;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.GuiProperties;
import de.gurkenlabs.litiengine.resources.Resources;

public class Program {
  /**
   * The main entry point for the Game.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {

    // Locate libvlc dlls for video playback.

    // NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "libs");
    // NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcCoreLibraryName(),
    // "libs");

    // set meta information about the game
    Game.info().setName("LITI");
    Game.info().setSubTitle("Stoneage Brawl");
    Game.info().setVersion("v1.0.0");

    GuiProperties.getDefaultAppearance().setForeColor(LitiColors.TEXT_DARK);

    // init the game infrastructure
    Game.init(args);

    Resources.load("game.litidata");

    GameManager.init();
    // Game.screens().add(new SplashScreen());
    // Game.screens().add(new MenuScreen());

    // Game.screens().add(new LobbyScreen());
    //Game.screens().add(new LoadingScreen());
    Game.screens().add(IngameScreen.instance());
    Game.screens().add(new ScoreScreen());

    Game.start();
  }
}
