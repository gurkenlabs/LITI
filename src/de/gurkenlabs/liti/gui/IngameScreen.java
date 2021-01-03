package de.gurkenlabs.liti.gui;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;

import java.awt.*;

public class IngameScreen extends LitiScreen {
  private Hud hud;

  public IngameScreen() {
    super("INGAME");

  }

  @Override
  public void prepare() {
    super.prepare();
    Game.world().loadEnvironment("plateau2");
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

  @Override public boolean canPressDirection(int player, Direction direction) {
    return true;
  }

  @Override public boolean canPressMenu(int player) {
    return true;
  }

  @Override public boolean canPressInfo(int player) {
    return true;
  }

  @Override public boolean canPressConfirm(int player) {
    return true;
  }

  @Override public boolean canPressCancel(int player) {
    return true;
  }
}
