package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.constants.LitiSounds;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.screens.Screen;

public abstract class LitiScreen extends Screen {
  protected LitiScreen(String screenName) {
    super(screenName);
  }

  public void dispatchCancel(int player) {
    if ((!canPressCancel(player))) {
      return;
    }
    Game.audio().playSound(LitiSounds.UI_CANCEL);
  }

  public void dispatchConfirm(int player) {
    if ((!canPressConfirm(player))) {
      return;
    }
    Game.audio().playSound(LitiSounds.UI_CONFIRM);
  }

  public void dispatchDirection(int player, Direction direction) {
    if ((!canPressDirection(player, direction))) {
      return;
    }
    switch (direction) {
      case DOWN:
        Game.audio().playSound(LitiSounds.UI_DIRECTION_DOWN);
        break;
      case LEFT:
        Game.audio().playSound(LitiSounds.UI_DIRECTION_LEFT);
        break;
      case RIGHT:
        Game.audio().playSound(LitiSounds.UI_DIRECTION_RIGHT);
        break;
      case UP:
        Game.audio().playSound(LitiSounds.UI_DIRECTION_UP);
        break;
      default:
        break;
    }
  }

  public void dispatchInfo(int player) {
    if ((!canPressInfo(player))) {
      return;
    }
    Game.audio().playSound(LitiSounds.UI_INFO);
  }

  public void dispatchMenu(int player) {
    if ((!canPressMenu(player))) {
      return;
    }
    Game.audio().playSound(LitiSounds.UI_MENU);
  }

  public abstract boolean canPressDirection(int player, Direction direction);

  public abstract boolean canPressMenu(int player);

  public abstract boolean canPressInfo(int player);

  public abstract boolean canPressConfirm(int player);

  public abstract boolean canPressCancel(int player);
}
