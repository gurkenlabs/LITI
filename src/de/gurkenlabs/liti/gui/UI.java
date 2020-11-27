package de.gurkenlabs.liti.gui;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//TODO: dispatching delay
public final class UI {
  private static final int UI_INPUT_DELAY = 100;
  private static Map<Integer, Long> lastInputs = new ConcurrentHashMap<>();

  private UI() {
  }

  public static void cancel(int player) {
    if (!checkInputDelay(player)) {
      return;
    }

    getLitiScreen().dispatchCancel(player);
    lastInputs.put(player, Game.time().now());
  }

  public static void confirm(int player) {
    if (!checkInputDelay(player)) {
      return;
    }

    getLitiScreen().dispatchConfirm(player);
    lastInputs.put(player, Game.time().now());
  }

  public static void direction(int player, Direction direction) {
    if (!checkInputDelay(player)) {
      return;
    }

    getLitiScreen().dispatchDirection(player, direction);
    lastInputs.put(player, Game.time().now());
  }

  private static LitiScreen getLitiScreen() {
    if (Game.screens().current() instanceof LitiScreen) {
      return (LitiScreen) Game.screens().current();
    }

    throw new IllegalArgumentException("Sceens need to inherit from LitiScreen!");
  }

  private static boolean checkInputDelay(int player) {
    if (!lastInputs.containsKey(player)) {
      return true;
    }

    return Game.time().since(lastInputs.get(player)) > UI_INPUT_DELAY;
  }
}
