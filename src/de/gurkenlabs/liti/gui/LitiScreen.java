package de.gurkenlabs.liti.gui;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.gui.screens.Screen;

public abstract class LitiScreen extends Screen {
  protected LitiScreen(String screenName) {
    super(screenName);
  }

  public void dispatchCancel(int player) {

  }

  public void dispatchConfirm(int player) {

  }

  public void dispatchDirection(int player, Direction direction) {

  }

  public void dispatchInfo(int player) {

  }
}
