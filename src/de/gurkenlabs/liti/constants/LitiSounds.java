package de.gurkenlabs.liti.constants;

import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.sound.Sound;

import java.awt.*;

public final class LitiSounds {
  public static final Sound UI_DIRECTION_UP = Resources.sounds().get("UI_direction_up");
  public static final Sound UI_DIRECTION_DOWN = Resources.sounds().get("UI_direction_down");
  public static final Sound UI_DIRECTION_LEFT = Resources.sounds().get("UI_direction_left");
  public static final Sound UI_DIRECTION_RIGHT = Resources.sounds().get("UI_direction_right");

  public static final Sound UI_CANCEL = Resources.sounds().get("UI_cancel");
  public static final Sound UI_CONFIRM = Resources.sounds().get("UI_confirm");
  public static final Sound UI_INFO = Resources.sounds().get("UI_info");
  public static final Sound UI_MENU = Resources.sounds().get("UI_menu");

  private LitiSounds() {

  }
}
