package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.GameManager;
import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.constants.Timings;
import de.gurkenlabs.liti.entities.PlayerConfiguration;
import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.graphics.emitters.Emitter;
import de.gurkenlabs.litiengine.gui.screens.Screen;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class LoadingScreen extends LitiScreen {
  private static final BufferedImage BACKGROUND = Resources.images().get("lobby-background.png");

  public LoadingScreen() {
    super("LOADING");
  }

  @Override
  public void render(Graphics2D g) {
    //    ImageRenderer.render(g, BACKGROUND, 0, 0);
    g.setColor(LitiColors.LOADING_BACKGROUND);
    g.fill(new Rectangle(0, 0, (int) Game.window().getResolution().getWidth(), (int) Game.window().getResolution().getHeight()));
    Game.world().environment().render(g);
    super.render(g);

  }

  @Override
  protected void initializeComponents() {
    super.initializeComponents();
  }

  @Override
  public void prepare() {
    super.prepare();
    Game.window().getRenderComponent().fadeIn(1000);

    GameManager.init();

    for (PlayerConfiguration config : Players.getConfigurations()) {
      if (config.getPlayerClass() == null) {
        continue;
      }
      Players.join(config);
    }

    Game.loop().perform(Timings.LOADING_DURATION - 500, () -> {
      Game.window().getRenderComponent().fadeOut(500);
    });

    Game.loop().perform(Timings.LOADING_DURATION, () -> {
      Game.screens().display("INGAME");
      GameManager.init();
      Game.window().getRenderComponent().fadeIn(500);

    });
    Game.world().loadEnvironment("loadingScreen");
    Game.world().camera().setFocus(Game.world().environment().getCenter());

  }

  @Override
  public void suspend() {
    super.suspend();

  }
}
