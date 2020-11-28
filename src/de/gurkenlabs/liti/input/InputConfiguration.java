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
  private String keyboard_block_start = "KEYBOARD_SHIFT_PRESSED";
  private String keyboard_block_stop = "KEYBOARD_SHIFT_RELEASED";
  private String keyboard_cancel = "KEYBOARD_ESCAPE_RELEASED";
  private String keyboard_dash = "KEYBOARD_SPACE_RELEASED";
  private String keyboard_bash = "MOUSE_1_PRESSED";
  private String keyboard_aim = "MOUSE_POSITION";

  private String gamepad_up = "GAMEPAD_AXIS-Y_-_PRESSED";
  private String gamepad_down = "GAMEPAD_AXIS-Y_+_PRESSED";
  private String gamepad_left = "GAMEPAD_AXIS-X_-_PRESSED";
  private String gamepad_right = "GAMEPAD_AXIS-X_+_PRESSED";
  private String gamepad_block_start = "GAMEPAD_BUTTON-0_PRESSED";
  private String gamepad_block_stop = "GAMEPAD_BUTTON-0_RELEASED";
  private String gamepad_interact = "GAMEPAD_BUTTON-0_PRESSED";
  private String gamepad_cancel = "GAMEPAD_BUTTON-1_PRESSED";
  private String gamepad_dash = "GAMEPAD_AXIS-Z_+_PRESSED";
  private String gamepad_bash = "GAMEPAD_AXIS-Z_-_PRESSED";
  private String gamepad_aimx = "GAMEPAD_AXIS-RX_CHANGED";
  private String gamepad_aimy = "GAMEPAD_AXIS-RY_CHANGED";
  
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

  public String getkeyboard_block_start() { return this.keyboard_block_start; }

  public void setkeyboard_block_start(String keyboard_block_start) {
    this.keyboard_block_start = keyboard_block_start;
  }

  public String getkeyboard_block_stop() { return this.keyboard_block_stop; }

  public void setkeyboard_block_stop(String keyboard_block_stop) {
    this.keyboard_block_stop = keyboard_block_stop;
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

  public String getkeyboard_bash() {
    return this.keyboard_bash;
  }

  public void setkeyboard_bash(String keyboard_bash) {
    this.keyboard_bash = keyboard_bash;
  }
  
  public String getkeyboard_aim() {
    return this.keyboard_aim;
  }
  
  public void setkeyboard_aim(String keyboard_aim) {
    this.keyboard_aim = keyboard_aim;
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

  public String getgamepad_block_start() { return this.gamepad_block_start; }

  public void setgamepad_block_start(String gamepad_block_start) {
    this.gamepad_block_start = gamepad_block_start;
  }

  public String getgamepad_block_stop() { return this.gamepad_block_stop; }

  public void setgamepad_block_stop(String gamepad_block_stop) {
    this.gamepad_block_stop = gamepad_block_stop;
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

  public String getgamepad_bash() {
    return this.gamepad_bash;
  }

  public void setgamepad_bash(String gamepad_bash) {
    this.gamepad_bash = gamepad_bash;
  }
  
  public String getgamepad_aimx() { return this.gamepad_aimx; }
  
  public void setgamepad_aimx(String gamepad_aimx) {
    this.gamepad_aimx = gamepad_aimx;
  }
  
  public String getgamepad_aimy() {
    return this.gamepad_aimy;
  }
  
  public void setgamepad_aimy(String gamepad_aimy) {
    this.gamepad_aimy = gamepad_aimy;
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