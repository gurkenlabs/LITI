package de.gurkenlabs.liti;

import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.gui.IngameScreen;
import de.gurkenlabs.liti.gui.LoadingScreen;
import de.gurkenlabs.liti.gui.LobbyScreen;
import de.gurkenlabs.liti.gui.MenuScreen;
import de.gurkenlabs.liti.gui.ScoreScreen;
import de.gurkenlabs.liti.gui.SplashScreen;
import de.gurkenlabs.liti.input.InputManager;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.GuiProperties;
import de.gurkenlabs.litiengine.resources.Resources;

import java.util.Arrays;

public class Program {
  /**
   * The main entry point for the Game.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    parseArguments(args);

    // Locate libvlc dlls for video playback.

    // NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "libs");
    // NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcCoreLibraryName(),
    // "libs");

    // set meta information about the game
    Game.info().setName("LITI");
    Game.info().setSubTitle("Stoneage Brawl");
    Game.info().setVersion("v1.0.0");

    GuiProperties.getDefaultAppearance().setForeColor(LitiColors.TEXT_DARK);
    GuiProperties.setDefaultTextShadowColor(LitiColors.TEXT_OUTLINE);
    GuiProperties.setDefaultTextShadowRadius(2f);

    // init the game infrastructure
    Game.init(args);
    InputManager.init();

    Resources.load("game.litidata");
    Game.window().setIcon(Resources.images().get("fire-logox64.png"));

    Game.screens().add(new SplashScreen());
    Game.screens().add(new MenuScreen());
    if (GameManager.DBG_SKIP_TO_INGAME) {
      GameManager.init();
    } else {
      Game.screens().add(new LobbyScreen());
      Game.screens().add(new LoadingScreen());
    }

    Game.screens().add(IngameScreen.instance());
    Game.screens().add(new ScoreScreen());

    Game.start();
  }

  private static void parseArguments(String[] args) {
    if (Game.isDebug()) {
      if (Arrays.stream(args).anyMatch(x -> x.equals("--skiptoingame"))) {
        GameManager.DBG_SKIP_TO_INGAME = true;
      }
    }
  }
}
