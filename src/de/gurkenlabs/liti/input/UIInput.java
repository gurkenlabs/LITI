package de.gurkenlabs.liti.input;

import de.gurkenlabs.liti.constants.Timings;
import de.gurkenlabs.liti.gui.LitiScreen;
import com.litiengine.Direction;
import com.litiengine.Game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UIInput {
  private static final Map<Integer, Long> lastPlayerSpecificInputs = new ConcurrentHashMap<>();

  //TODO: dispatching delay
  private UIInput() {

  }

  public static void cancel(int player) {
    if (!checkInputDelay(player)) {
      return;
    }

    getLitiScreen().dispatchCancel(player);
    lastPlayerSpecificInputs.put(player, Game.time().now());
  }

  public static void confirm(int player) {
    if (!checkInputDelay(player)) {
      return;
    }

    getLitiScreen().dispatchConfirm(player);
    lastPlayerSpecificInputs.put(player, Game.time().now());
  }

  public static void menu(int player) {
    if (!checkInputDelay(player)) {
      return;
    }

    getLitiScreen().dispatchMenu(player);
    lastPlayerSpecificInputs.put(player, Game.time().now());
  }

  public static void direction(int player, Direction direction) {
    if (!checkInputDelay(player)) {
      return;
    }
    getLitiScreen().dispatchDirection(player, direction);
    lastPlayerSpecificInputs.put(player, Game.time().now());
  }

  public static void info(int player) {
    if (!checkInputDelay(player)) {
      return;
    }

    getLitiScreen().dispatchInfo(player);
    lastPlayerSpecificInputs.put(player, Game.time().now());
  }


  private static LitiScreen getLitiScreen() {
    if (Game.screens().current() instanceof LitiScreen) {
      return (LitiScreen) Game.screens().current();
    }

    throw new IllegalArgumentException("Screens need to inherit from LitiScreen!");
  }

  private static boolean checkInputDelay(int player) {
    if (!lastPlayerSpecificInputs.containsKey(player)) {
      return true;
    }

    return Game.time().since(lastPlayerSpecificInputs.get(player)) > Timings.DELAY_UI_INPUT;
  }
}
