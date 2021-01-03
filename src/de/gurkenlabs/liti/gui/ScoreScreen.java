package de.gurkenlabs.liti.gui;

import de.gurkenlabs.litiengine.Direction;

public class ScoreScreen extends LitiScreen {
  public ScoreScreen() {
    super("SCORE");
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
