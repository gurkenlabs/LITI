package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.gui.screens.Screen;

public abstract class LitiScreen extends Screen {
  protected LitiScreen(String screenName) {
    super(screenName);
  }

  public void dispatchCancel(int player) {
    System.out.println("player " + player + ": cancel");
  }

  public void dispatchConfirm(int player) {
    System.out.println("player " + player + ": confirm");
  }

  public void dispatchDirection(int player, Direction direction) {
    System.out.println("player " + player + ": direction " + direction);
  }
}
