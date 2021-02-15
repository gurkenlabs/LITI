package de.gurkenlabs.liti.gui;

import de.gurkenlabs.litiengine.Direction;

import java.awt.*;

public class MenuScreen extends LitiScreen {
  public MenuScreen() {
    super("MENU");
  }

  @Override
  public void render(Graphics2D g) {
    super.render(g);
  }

  @Override
  protected void initializeComponents() {
    super.initializeComponents();
  }

  @Override public boolean canPressDirection(int player, Direction direction) {
    return false;
  }

  @Override public boolean canPressMenu(int player) {
    return false;
  }

  @Override public boolean canPressInfo(int player) {
    return false;
  }

  @Override public boolean canPressConfirm(int player) {
    return false;
  }

  @Override public boolean canPressCancel(int player) {
    return false;
  }
}
