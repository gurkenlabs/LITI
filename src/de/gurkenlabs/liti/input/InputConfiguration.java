package de.gurkenlabs.liti.input;

import de.gurkenlabs.litiengine.configuration.ConfigurationGroup;
import de.gurkenlabs.litiengine.configuration.ConfigurationGroupInfo;

@ConfigurationGroupInfo(prefix = "lepus_input_")
public class InputConfiguration extends ConfigurationGroup {
  private String keyboard_up = "KEYBOARD_W_PRESSED";
  private String keyboard_down = "KEYBOARD_S_PRESSED";
  private String keyboard_left = "KEYBOARD_A_PRESSED";
  private String keyboard_right = "KEYBOARD_D_PRESSED";
  private String keyboard_interact = "KEYBOARD_E_PRESSED";
  private String keyboard_cancel = "KEYBOARD_ESCAPE_RELEASED";
  private String keyboard_dash = "KEYBOARD_SHIFT_RELEASED";

  private String gamepad_up = "GAMEPAD_AXIS-Y_-";
  private String gamepad_down = "GAMEPAD_AXIS-Y_+";
  private String gamepad_left = "GAMEPAD_AXIS-X_-";
  private String gamepad_right = "GAMEPAD_AXIS-X_+";
  private String gamepad_interact = "GAMEPAD_BUTTON-0_PRESSED";
  private String gamepad_cancel = "GAMEPAD_BUTTON-1_PRESSED";
  private String gamepad_dash = "GAMEPAD_BUTTON-0_PRESSED";

  private String ui_keyboard_menu = "KEYBOARD_ESCAPE_RELEASED";
  private String ui_gamepad_menu = "GAMEPAD_BUTTON-7_PRESSED";

  public InputConfiguration() {
  }

  public String getkeyboard_up() {
    return this.keyboard_up;
  }

  public void setkeyboard_up(String keyboard_up) {
    this.keyboard_up = keyboard_up;
  }

  public String getkeyboard_down() {
    return this.keyboard_down;
  }

  public void setkeyboard_down(String keyboard_down) { this.keyboard_down = keyboard_down; }

  public String getkeyboard_left() {
    return this.keyboard_left;
  }

  public void setkeyboard_left(String keyboard_left) {
    this.keyboard_left = keyboard_left;
  }

  public String getkeyboard_right() {
    return this.keyboard_right;
  }

  public void setkeyboard_right(String keyboard_right) {
    this.keyboard_right = keyboard_right;
  }

  public String getkeyboard_interact() {
    return this.keyboard_interact;
  }

  public void setkeyboard_interact(String keyboard_interact) {
    this.keyboard_interact = keyboard_interact;
  }

  public String getkeyboard_cancel() {
    return this.keyboard_cancel;
  }

  public void setkeyboard_cancel(String keyboard_cancel) {
    this.keyboard_cancel = keyboard_cancel;
  }

  public String getkeyboard_dash() {
    return this.keyboard_dash;
  }

  public void setkeyboard_dash(String keyboard_dash) {
    this.keyboard_dash = keyboard_dash;
  }

  public String getgamepad_up() {
    return this.gamepad_up;
  }

  public void setgamepad_up(String gamepad_up) {
    this.gamepad_up = gamepad_up;
  }

  public String getgamepad_down() {
    return this.gamepad_down;
  }

  public void setgamepad_down(String gamepad_down) {
    this.gamepad_down = gamepad_down;
  }

  public String getgamepad_left() {
    return this.gamepad_left;
  }

  public void setgamepad_left(String gamepad_left) {
    this.gamepad_left = gamepad_left;
  }

  public String getgamepad_right() {
    return this.gamepad_right;
  }

  public void setgamepad_right(String gamepad_right) {
    this.gamepad_right = gamepad_right;
  }

  public String getgamepad_interact() {
    return this.gamepad_interact;
  }

  public void setgamepad_interact(String gamepad_interact) {
    this.gamepad_interact = gamepad_interact;
  }

  public String getgamepad_cancel() {
    return this.gamepad_cancel;
  }

  public void setgamepad_cancel(String gamepad_cancel) {
    this.gamepad_cancel = gamepad_cancel;
  }

  public String getgamepad_dash() {
    return this.gamepad_dash;
  }

  public void setgamepad_dash(String gamepad_dash) {
    this.gamepad_dash = gamepad_dash;
  }

  public String getUi_gamepad_menu() {
    return this.ui_gamepad_menu;
  }

  public void setUi_gamepad_menu(String ui_gamepad_menu) {
    this.ui_gamepad_menu = ui_gamepad_menu;
  }

  public String getUi_keyboard_menu() {
    return ui_keyboard_menu;
  }

  public void setUi_keyboard_menu(String ui_keyboard_menu) {
    this.ui_keyboard_menu = ui_keyboard_menu;
  }

}