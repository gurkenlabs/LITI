package de.gurkenlabs.liti.input;

public class InputConfiguration {
  public static final String KEYBOARD_UP = "KEYBOARD_W_PRESSED";
  public static final String KEYBOARD_DOWN = "KEYBOARD_S_PRESSED";
  public static final String KEYBOARD_LEFT = "KEYBOARD_A_PRESSED";
  public static final String KEYBOARD_RIGHT = "KEYBOARD_D_PRESSED";
  public static final String KEYBOARD_INTERACT = "KEYBOARD_E_PRESSED";
  public static final String KEYBOARD_BLOCK_START = "MOUSE_2_PRESSED";
  public static final String KEYBOARD_BLOCK_STOP = "MOUSE_2_RELEASED";
  public static final String KEYBOARD_CANCEL = "KEYBOARD_ESCAPE_RELEASED";
  public static final String KEYBOARD_DASH = "KEYBOARD_SPACE_PRESSED";
  public static final String KEYBOARD_SURVIVALSKILL = "KEYBOARD_SHIFT_PRESSED";
  public static final String KEYBOARD_BASH = "MOUSE_1_PRESSED";
  public static final String KEYBOARD_AIM = "MOUSE_POSITION";
  public static final String UI_KEYBOARD_MENU = "KEYBOARD_ESCAPE_RELEASED";
  public static final String UI_KEYBOARD_CONFIRM = "KEYBOARD_SPACE_RELEASED";
  public static final String UI_KEYBOARD_INFO = "KEYBOARD_CONTROL_PRESSED";

  public static final class Xbox
  {
    public static final String GAMEPAD_UP = "GAMEPAD_AXIS-Y_-_PRESSED";
    public static final String GAMEPAD_DOWN = "GAMEPAD_AXIS-Y_+_PRESSED";
    public static final String GAMEPAD_LEFT = "GAMEPAD_AXIS-X_-_PRESSED";
    public static final String GAMEPAD_RIGHT = "GAMEPAD_AXIS-X_+_PRESSED";
    public static final String GAMEPAD_POV = "GAMEPAD_AXIS-POV_+_PRESSED";
    public static final String GAMEPAD_BLOCK_START = "GAMEPAD_BUTTON-0_PRESSED";
    public static final String GAMEPAD_BLOCK_STOP = "GAMEPAD_BUTTON-0_RELEASED";
    public static final String GAMEPAD_INTERACT = "GAMEPAD_BUTTON-1_PRESSED";
    public static final String GAMEPAD_CANCEL = "GAMEPAD_BUTTON-2_PRESSED";
    public static final String GAMEPAD_DASH = "GAMEPAD_AXIS-Z_+_PRESSED";
    public static final String GAMEPAD_SURVIVALSKILL = "GAMEPAD_BUTTON-1_PRESSED";
    public static final String GAMEPAD_BASH = "GAMEPAD_AXIS-Z_-_PRESSED";
    public static final String GAMEPAD_AIMX = "GAMEPAD_AXIS-RX_CHANGED";
    public static final String GAMEPAD_AIMY = "GAMEPAD_AXIS-RY_CHANGED";
    public static final String UI_GAMEPAD_MENU = "GAMEPAD_BUTTON-7_PRESSED";
    public static final String UI_GAMEPAD_CONFIRM = "GAMEPAD_BUTTON-1_PRESSED";
    public static final String UI_GAMEPAD_INFO = "GAMEPAD_BUTTON-2_PRESSED";
  }

  public static final class DualShock
  {
    public static final String GAMEPAD_UP = "GAMEPAD_AXIS-Y_-_PRESSED";
    public static final String GAMEPAD_DOWN = "GAMEPAD_AXIS-Y_+_PRESSED";
    public static final String GAMEPAD_LEFT = "GAMEPAD_AXIS-X_-_PRESSED";
    public static final String GAMEPAD_RIGHT = "GAMEPAD_AXIS-X_+_PRESSED";
    public static final String GAMEPAD_POV = "GAMEPAD_AXIS-POV_+_PRESSED";
    public static final String GAMEPAD_BLOCK_START = "GAMEPAD_BUTTON-1_PRESSED";
    public static final String GAMEPAD_BLOCK_STOP = "GAMEPAD_BUTTON-1_RELEASED";
    public static final String GAMEPAD_INTERACT = "GAMEPAD_BUTTON-2_PRESSED";
    public static final String GAMEPAD_CANCEL = "GAMEPAD_BUTTON-0_PRESSED";
    public static final String GAMEPAD_DASH = "GAMEPAD_AXIS-RX_+_PRESSED";
    public static final String GAMEPAD_SURVIVALSKILL = "GAMEPAD_BUTTON-2_PRESSED";
    public static final String GAMEPAD_BASH = "GAMEPAD_AXIS-RY_+_PRESSED";
    public static final String GAMEPAD_AIMX = "GAMEPAD_AXIS-Z_CHANGED";
    public static final String GAMEPAD_AIMY = "GAMEPAD_AXIS-RZ_CHANGED";
    public static final String UI_GAMEPAD_MENU = "GAMEPAD_BUTTON-9_PRESSED";
    public static final String UI_GAMEPAD_CONFIRM = "GAMEPAD_BUTTON-1_PRESSED";
    public static final String UI_GAMEPAD_INFO = "GAMEPAD_BUTTON-0_PRESSED";
  }

  private InputConfiguration() {
  }

}