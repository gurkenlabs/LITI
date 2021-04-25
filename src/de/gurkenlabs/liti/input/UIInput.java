package de.gurkenlabs.liti.input;

import de.gurkenlabs.liti.constants.Timings;
import de.gurkenlabs.liti.gui.LitiScreen;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class UIInput {
  private static final List<UIInputEvent> lastPlayerSpecificInputs = new CopyOnWriteArrayList<>();

  private UIInput() {

  }

  public static void cancel(int player) {
    if (!checkInputDelay(player, "cancel")) {
      return;
    }

    getLitiScreen().dispatchCancel(player);
    trackEvent(player, "cancel");
  }

  private static void trackEvent(int player, String action) {
    lastPlayerSpecificInputs.removeIf(e -> e.player == player && e.action.equals(action));
    lastPlayerSpecificInputs.add(new UIInputEvent(player, action));
  }

  public static void confirm(int player) {
    if (!checkInputDelay(player, "confirm")) {
      return;
    }

    getLitiScreen().dispatchConfirm(player);
    trackEvent(player, "confirm");
  }

  public static void menu(int player) {
    if (!checkInputDelay(player, "menu")) {
      return;
    }

    getLitiScreen().dispatchMenu(player);
    trackEvent(player, "menu");
  }

  public static void direction(int player, Direction direction) {
    if (!checkInputDelay(player, "direction_" + direction)) {
      return;
    }
    getLitiScreen().dispatchDirection(player, direction);
    trackEvent(player, "direction_" + direction);
  }

  public static void info(int player) {
    if (!checkInputDelay(player, "info")) {
      return;
    }

    getLitiScreen().dispatchInfo(player);
    trackEvent(player, "info");
  }


  private static LitiScreen getLitiScreen() {
    if (Game.screens().current() instanceof LitiScreen) {
      return (LitiScreen) Game.screens().current();
    }

    throw new IllegalArgumentException("Screens need to inherit from LitiScreen!");
  }

  private static boolean checkInputDelay(int player, String action) {
    for(UIInputEvent event : lastPlayerSpecificInputs){
      if(event.player == player && event.action.equals(action)){
        if(Game.time().since(event.tick) > Timings.DELAY_UI_INPUT){
          return true;
        } else {
          return false;
        }
      }
    }

    return true;
   }

  private static class UIInputEvent{
    private int player;
    private long tick;
    private String action;

    UIInputEvent(int player, String action){
      this.player = player;
      this.action = action;
      this.tick = Game.time().now();
    }
  }
}
