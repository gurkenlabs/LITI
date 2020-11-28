package de.gurkenlabs.liti.gui;

import de.gurkenlabs.litiengine.Game;

import java.awt.*;

public class IngameScreen extends LitiScreen {
  private Hud hud;

  public IngameScreen() {
    super("INGAME");

  }

  @Override
  protected void initializeComponents() {
    super.initializeComponents();
    this.hud = new Hud();
    this.getComponents().add(this.hud);
  }

  @Override
  public void render(final Graphics2D g) {
    if (Game.world().environment() != null) {
      Game.world().environment().render(g);
    }

    super.render(g);
  }
}
