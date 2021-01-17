package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.constants.Timings;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.gui.screens.Screen;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class LoadingScreen extends Screen {
  private static final BufferedImage BACKGROUND = Resources.images().get("lobby-background.png");

  public LoadingScreen() {
    super("LOADING");
  }

  @Override public void render(Graphics2D g) {
    //    ImageRenderer.render(g, BACKGROUND, 0, 0);
    g.setColor(LitiColors.LOADING_BACKGROUND);
    g.fill(new Rectangle(0, 0, (int) Game.window().getResolution().getWidth(), (int) Game.window().getResolution().getHeight()));
    super.render(g);
  }

  @Override protected void initializeComponents() {
    super.initializeComponents();
  }

  @Override public void prepare() {
    super.prepare();
    Game.window().getRenderComponent().fadeIn(1000);

    Game.loop().perform(Timings.LOADING_DURATION, () -> {
      Game.screens().display("INGAME");
      Game.window().getRenderComponent().fadeOut(1000);
    });
  }

  @Override public void suspend() {
    super.suspend();

  }
}
