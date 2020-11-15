package de.gurkenlabs.liti.gui;

import de.gurkenlabs.litiengine.Game;

import java.awt.*;

public class IngameScreen extends LitiScreen {
  public IngameScreen() {
    super("INGAME");
  }

  @Override
  public void render(final Graphics2D g) {
    if (Game.world().environment() != null) {
      Game.world().environment().render(g);
    }

    super.render(g);
  }
}
