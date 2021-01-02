package de.gurkenlabs.liti.input;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.input.Gamepad;
import de.gurkenlabs.litiengine.input.Gamepad.Axis;
import de.gurkenlabs.litiengine.input.Gamepad.Buttons;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.util.ReflectionUtilities;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class InputBinding {
  public enum InputType {
    NONE,
    KEYBOARD,
    GAMEPAD,
    MOUSE
  }

  public enum InputEventType {
    NONE,
    PRESSED,
    RELEASED,
    CHANGED;

    public static InputEventType fromString(String eventType) {
      try {
        return Enum.valueOf(InputEventType.class, eventType);
      } catch (IllegalArgumentException ex) {
        System.out.println("Invalid binding configuration! Unknown keyevent type: '" + eventType + "!");
        return InputEventType.NONE;
      }
    }
  }

  public enum GamepadInputCondition {
    NONE(""),
    POSITIVE("+"),
    NEGATIVE("-"),
    DEADZONE("CHANGED");

    private final String symbol;

    private GamepadInputCondition(String symbol) {
      this.symbol = symbol;
    }

    public String getSymbol() {
      return this.symbol;
    }

    public static GamepadInputCondition fromSymbol(String symbol) {
      for (final GamepadInputCondition dir : GamepadInputCondition.values()) {
        if (dir.getSymbol().equals(symbol)) {
          return dir;
        }
      }

      return NONE;
    }
  }

  public enum InputEvent {
    CANCEL, CONFIRM, INFO, MENU, STRIKE, DASH, BLOCK, INTERACT, SURVIVALSKILL, AIM, UP, DOWN, LEFT, RIGHT
  }

  public static final InputBinding INVALID = new InputBinding(InputType.NONE, InputEventType.NONE, null);

  private final InputType inputType;
  private final InputEventType eventType;
  private GamepadInputCondition inputCondition;
  private final String action;
  private final int keyCode;

  public InputBinding(InputType inputType, InputEventType eventType, String action) {
    this.inputType = inputType;
    this.eventType = eventType;
    this.action = action;
    this.keyCode = -1;
  }

  public InputBinding(InputType inputType, InputEventType eventType, String action, int keyCode) {
    this.inputType = inputType;
    this.eventType = eventType;
    this.keyCode = keyCode;
    this.action = action;
  }

  public static InputBinding getBinding(String bindingString) {
    InputType type = getInputType(bindingString);
    if (type == InputType.NONE) {
      return InputBinding.INVALID;
    }

    String[] rawBinding = getBindingArray(bindingString);
    if (rawBinding == null) {
      return InputBinding.INVALID;
    }

    InputBinding binding = InputBinding.INVALID;
    switch (type) {
    case MOUSE:
      binding = InputBinding.getMouseBinding(rawBinding);
      break;
    case KEYBOARD:
      binding = InputBinding.getKeyboardBinding(rawBinding);
      break;
    case GAMEPAD:
      binding = InputBinding.getGamepadBinding(rawBinding);
      break;
    default:
      break;
    }

    return binding;
  }

  public static InputBinding bind(Consumer<Float> action, String bindingString, Gamepad gamepad) {
    InputType type = getInputType(bindingString);
    if (type == InputType.NONE) {
      return InputBinding.INVALID;
    }

    final InputBinding binding = getBinding(bindingString);
    switch (type) {
    case MOUSE:
      bindToMouse(action, binding);
      break;
    case KEYBOARD:
      bindToKeyboard(action, binding);
      break;
    case GAMEPAD:
      bindToGamepad(action, binding, gamepad);
      break;
    default:
      break;
    }

    return binding;
  }

  public static void bind(Consumer<Float> action, InputBinding binding, Gamepad gamepad) {
    switch (binding.getInputType()) {
    case MOUSE:
      bindToMouse(action, binding);
      break;
    case KEYBOARD:
      bindToKeyboard(action, binding);
      break;
    case GAMEPAD:
      bindToGamepad(action, binding, gamepad);
      break;
    default:
      break;
    }
  }

  public static InputBinding bind(IEntity entity, String action, String bindingString, Gamepad gamepad) {
    InputType type = getInputType(bindingString);
    if (type == InputType.NONE) {
      return InputBinding.INVALID;
    }

    String[] rawBinding = getBindingArray(bindingString);
    if (rawBinding == null) {
      return InputBinding.INVALID;
    }

    InputBinding binding = InputBinding.INVALID;
    switch (type) {
    case MOUSE:
      binding = InputBinding.getMouseBinding(rawBinding);
      bindToMouse(entity, binding, action);
      break;
    case KEYBOARD:
      binding = InputBinding.getKeyboardBinding(rawBinding);
      bindToKeyboard(entity, binding, action);
      break;
    case GAMEPAD:
      binding = InputBinding.getGamepadBinding(rawBinding);
      bindToGamepad(entity, binding, action, gamepad);
      break;
    default:
      break;
    }

    return binding;
  }

  protected static InputBinding getKeyboardBinding(String[] split) {

    String keyId = "VK_" + split[1];

    int keyCode = ReflectionUtilities.getStaticValue(KeyEvent.class, keyId);

    InputEventType type = InputEventType.fromString(split[2]);
    if (type == InputEventType.NONE) {
      System.out.println("Invalid binding configuration! Unknown key event'" + split[2] + "'");
      return InputBinding.INVALID;
    }

    return new InputBinding(InputType.KEYBOARD, type, keyId, keyCode);
  }

  protected static InputBinding getMouseBinding(String[] split) {
    if (split[1].equals("POSITION")) {
      return new InputBinding(InputType.MOUSE, InputEventType.CHANGED, "POSITION");
    }

    String button = "BUTTON" + split[1];

    // find MouseEvent button
    int buttonCode = ReflectionUtilities.getStaticValue(MouseEvent.class, button);

    InputEventType type = InputEventType.fromString(split[2]);
    if (type == InputEventType.NONE) {
      System.out.println("Invalid binding configuration! Unknown key event'" + split[2] + "'");
      return InputBinding.INVALID;
    }

    return new InputBinding(InputType.MOUSE, type, button, buttonCode);
  }

  protected static InputBinding getGamepadBinding(String[] split) {
    String rawButton = split[1];
    String[] buttonSplit = rawButton.split("-");

    GamepadInputCondition condition = GamepadInputCondition.NONE;
    String identifier;
    InputEventType type;
    if (buttonSplit[0].equals("BUTTON")) {
      String buttonId = rawButton.replace('-', '_');
      identifier = ReflectionUtilities.getStaticValue(Buttons.class, buttonId);
      type = InputEventType.fromString(split[2]);
      if (type == InputEventType.NONE) {
        System.out.println("Invalid binding configuration! Unknown key event'" + split[2] + "'");
        return InputBinding.INVALID;
      }
    } else if (buttonSplit[0].equals("AXIS")) {
      identifier = ReflectionUtilities.getStaticValue(Axis.class, buttonSplit[1]);
      type = split.length > 3 ? InputEventType.fromString(split[3]) : InputEventType.CHANGED;
      condition = GamepadInputCondition.fromSymbol(split[2]);
    } else {
      System.out.println("Invalid binding configuration! Unknown gamepad button type '" + buttonSplit[0] + "' used!");
      return InputBinding.INVALID;
    }

    if (identifier == null) {
      return InputBinding.INVALID;
    }

    InputBinding binding = new InputBinding(InputType.GAMEPAD, type, identifier);
    binding.setInputCondition(condition);

    return binding;
  }

  public InputType getInputType() {
    return this.inputType;
  }

  public InputEventType getEventType() {
    return this.eventType;
  }

  public String getAction() {
    return this.action;
  }

  public int getKeyCode() {
    return this.keyCode;
  }

  public GamepadInputCondition getInputCondition() {
    return inputCondition;
  }

  public void setInputCondition(GamepadInputCondition inputCondition) {
    this.inputCondition = inputCondition;
  }

  private static void bindToKeyboard(Consumer<Float> action, InputBinding binding) {
    switch (binding.getEventType()) {
    case PRESSED:
      Input.keyboard().onKeyPressed(binding.getKeyCode(), e -> action.accept(1.0f));
      return;
    case RELEASED:
      Input.keyboard().onKeyTyped(binding.getKeyCode(), e -> action.accept(-1.0f));
      return;
    default:
      return;
    }
  }

  private static void bindToKeyboard(IEntity entity, InputBinding binding, String action) {
    bindToKeyboard((e) -> entity.perform(action), binding);
  }

  private static void bindToMouse(Consumer<Float> action, InputBinding binding) {
    switch (binding.getEventType()) {
    case PRESSED:
      if (binding.getKeyCode() == MouseEvent.BUTTON1) {
        Input.mouse().onPressing(() -> {
          if (Input.mouse().isLeftButtonPressed()) {
            action.accept(1.0f);
          }
        });
      } else if (binding.getKeyCode() == MouseEvent.BUTTON2) {
        Input.mouse().onPressing(() -> {
          if (Input.mouse().isRightButtonPressed()) {
            action.accept(1.0f);
          }
        });
      }
      break;
    case RELEASED:
      if (binding.getKeyCode() == MouseEvent.BUTTON1) {
        Input.mouse().onReleased(e -> {
          if (Input.mouse().isLeftButtonPressed()) {
            action.accept(-1.0f);
          }
        });
      } else if (binding.getKeyCode() == MouseEvent.BUTTON2) {
        Input.mouse().onReleased(e -> {
          if (Input.mouse().isRightButton(e)) {
            action.accept(-1.0f);
          }
        });
      }
      break;
    case CHANGED:
      if (binding.getAction().equals("POSITION")) {
        Input.mouse().onMoved(e -> action.accept(0.0f));
        Input.mouse().onDragged(e -> action.accept(0.0f));
      }
      break;
    default:
      break;
    }
  }

  private static void bindToMouse(IEntity entity, InputBinding binding, String action) {
    bindToMouse((e) -> entity.perform(action), binding);
  }

  private static void bindToGamepad(Consumer<Float> action, InputBinding binding, Gamepad gamepad) {
    if (!Game.config().input().isGamepadSupport() || gamepad == null) {
      return;
    }

    switch (binding.getEventType()) {
    case PRESSED:
      gamepad.onPressed(binding.getAction(), e -> {
        if (checkGamepadInputCondition(binding, e.getValue())) {
          action.accept(e.getValue());
        }
      });
      return;
    case RELEASED:
      gamepad.onReleased(binding.getAction(), e -> {
        if (checkGamepadInputCondition(binding, e.getValue())) {
          action.accept(e.getValue());
        }
      });
      return;
    case CHANGED:
      gamepad.onPoll(binding.getAction(), e -> {
        if (checkGamepadInputCondition(binding, e.getValue())) {
          action.accept(e.getValue());
        }
      });
    default:
      break;
    }
  }

  private static void bindToGamepad(IEntity entity, InputBinding binding, String action, Gamepad gamepad) {
    bindToGamepad((e) -> entity.perform(action), binding, gamepad);
  }

  private static boolean checkGamepadInputCondition(InputBinding binding, float value) {
    if (binding.getEventType() == InputEventType.RELEASED && value < Game.config().input().getGamepadStickDeadzone()) {
      return true;
    }

    if (Math.abs(value) < Game.config().input().getGamepadStickDeadzone()) {
      return false;
    }

    switch (binding.getInputCondition()) {
    case NEGATIVE:
      return value < 0;
    case POSITIVE:
      return value > 0;
    case NONE:
    default:
      return true;
    }
  }

  private static InputType getInputType(String bindingString) {
    String[] split = getBindingArray(bindingString);
    if (split == null) {
      return InputType.NONE;
    }

    try {
      return Enum.valueOf(InputType.class, split[0]);
    } catch (
        IllegalArgumentException ex) {
      System.out.println("Invalid binding configuration! Unknown input type: '" + split[0] + "'");
    }

    return InputType.NONE;
  }

  private static String[] getBindingArray(String bindingString) {
    if (bindingString == null || bindingString.isEmpty()) {
      System.out.println("Invalid binding configuration.");
      return null;
    }

    // split the configuration value e.g. : GAMEPAD_RIGHT_TRIGGER_-, KEYBOARD_D or MOUSE_1_DOWN
    // more generally speaking, its {InputType}_{Input}_{Value}
    String[] split = bindingString.split("_");
    if (split.length < 2) {
      System.out.println("Invalid binding configuration '" + bindingString + "'");
      return null;
    }

    return split;
  }
}
