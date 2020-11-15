package de.gurkenlabs.liti.gui;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;

//TODO: dispatching delay
public final class UI {
  private UI() {
  }

  public static void cancel(int player) {
    getLitiScreen().dispatchCancel(player);
  }

  public static void confirm(int player) {
    getLitiScreen().dispatchConfirm(player);
  }

  public static void direction(int player, Direction direction) {
    getLitiScreen().dispatchDirection(player, direction);
  }

  private static LitiScreen getLitiScreen() {
    if (Game.screens().current() instanceof LitiScreen) {
      return (LitiScreen) Game.screens().current();
    }

    throw new IllegalArgumentException("Sceens need to inherit from LitiScreen!");
  }
}
