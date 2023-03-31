package de.gurkenlabs.liti.input;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.input.Gamepad;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.util.geom.GeometricUtilities;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.function.Consumer;

public final class InputManager {

  private InputManager() {
  }

  public static void update() {
  }

  public static void bindUiInput(int index, Gamepad gamepad) {
    if (gamepad != null) {
      InputBinding.bind(value -> UIInput.cancel(index),
        gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
          ? InputConfiguration.DualShock.GAMEPAD_CANCEL : InputConfiguration.Xbox.GAMEPAD_CANCEL,
        gamepad);
      InputBinding.bind(value -> UIInput.confirm(index),
        gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
          ? InputConfiguration.DualShock.UI_GAMEPAD_CONFIRM
          : InputConfiguration.Xbox.UI_GAMEPAD_CONFIRM, gamepad);
      InputBinding.bind(value -> UIInput.menu(index),
        gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
          ? InputConfiguration.DualShock.UI_GAMEPAD_MENU : InputConfiguration.Xbox.UI_GAMEPAD_MENU,
        gamepad);
      InputBinding.bind(value -> UIInput.info(index),
        gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
          ? InputConfiguration.DualShock.UI_GAMEPAD_INFO : InputConfiguration.Xbox.UI_GAMEPAD_INFO,
        gamepad);
      InputBinding.bind(value -> UIInput.direction(index, Direction.UP),
        gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
          ? InputConfiguration.DualShock.GAMEPAD_UP : InputConfiguration.Xbox.GAMEPAD_UP, gamepad);
      InputBinding.bind(value -> UIInput.direction(index, Direction.DOWN),
        gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
          ? InputConfiguration.DualShock.GAMEPAD_DOWN : InputConfiguration.Xbox.GAMEPAD_DOWN,
        gamepad);
      InputBinding.bind(value -> UIInput.direction(index, Direction.LEFT),
        gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
          ? InputConfiguration.DualShock.GAMEPAD_LEFT : InputConfiguration.Xbox.GAMEPAD_LEFT,
        gamepad);
      InputBinding.bind(value -> UIInput.direction(index, Direction.RIGHT),
        gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
          ? InputConfiguration.DualShock.GAMEPAD_RIGHT : InputConfiguration.Xbox.GAMEPAD_RIGHT,
        gamepad);
      String pov = gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
        ? InputConfiguration.DualShock.GAMEPAD_POV : InputConfiguration.Xbox.GAMEPAD_POV;

      InputBinding.bind(value -> {
        if (value.floatValue() == Gamepad.DPad.UP) {
          UIInput.direction(index, Direction.UP);
        }
      }, pov, gamepad);
      InputBinding.bind(value -> {
        if (value.floatValue() == Gamepad.DPad.DOWN) {
          UIInput.direction(index, Direction.DOWN);
        }
      }, pov, gamepad);
      InputBinding.bind(value -> {
        if (value.floatValue() == Gamepad.DPad.LEFT) {
          UIInput.direction(index, Direction.LEFT);
        }
      }, pov, gamepad);
      InputBinding.bind(value -> {
        if (value.floatValue() == Gamepad.DPad.RIGHT) {
          UIInput.direction(index, Direction.RIGHT);
        }
      }, pov, gamepad);
      return;
    }

    InputBinding.bind(value -> UIInput.cancel(index), InputConfiguration.KEYBOARD_CANCEL, null);
    InputBinding.bind(value -> UIInput.confirm(index), InputConfiguration.UI_KEYBOARD_CONFIRM,
      null);
    InputBinding.bind(value -> UIInput.menu(index), InputConfiguration.UI_KEYBOARD_MENU, null);
    InputBinding.bind(value -> UIInput.info(index), InputConfiguration.UI_KEYBOARD_INFO, null);
    InputBinding.bind(value -> UIInput.direction(index, Direction.UP),
      InputConfiguration.KEYBOARD_UP, null);
    InputBinding.bind(value -> UIInput.direction(index, Direction.DOWN),
      InputConfiguration.KEYBOARD_DOWN, null);
    InputBinding.bind(value -> UIInput.direction(index, Direction.LEFT),
      InputConfiguration.KEYBOARD_LEFT, null);
    InputBinding.bind(value -> UIInput.direction(index, Direction.RIGHT),
      InputConfiguration.KEYBOARD_RIGHT, null);
  }

  public static void bindPlayerInput(Player player, Gamepad gamepad) {
    // GAMEPAD CONTROLS
    if (player.getConfiguration().getInputDevice() == InputBinding.InputType.GAMEPAD) {
      // WALK UP
      InputBinding.bind(value -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.movement().setDy(value);
        }
      }, gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
        ? InputConfiguration.DualShock.GAMEPAD_UP : InputConfiguration.Xbox.GAMEPAD_UP, gamepad);

      // WALK DOWN
      InputBinding.bind(value -> {
          if (player.getState() != Player.PlayerState.LOCKED) {
            player.movement().setDy(value);
          }
        }, gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
          ? InputConfiguration.DualShock.GAMEPAD_DOWN : InputConfiguration.Xbox.GAMEPAD_DOWN,
        gamepad);

      // WALK LEFT
      InputBinding.bind(value -> {
          if (player.getState() != Player.PlayerState.LOCKED) {
            player.movement().setDx(value);
          }
        }, gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
          ? InputConfiguration.DualShock.GAMEPAD_LEFT : InputConfiguration.Xbox.GAMEPAD_LEFT,
        gamepad);

      // WALK RIGHT
      InputBinding.bind(value -> {
          if (player.getState() != Player.PlayerState.LOCKED) {
            player.movement().setDx(value);
          }
        }, gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
          ? InputConfiguration.DualShock.GAMEPAD_RIGHT : InputConfiguration.Xbox.GAMEPAD_RIGHT,
        gamepad);

      // INTERACT
      InputBinding.bind(value -> {
          player.interact();
        }, gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
          ? InputConfiguration.DualShock.GAMEPAD_INTERACT : InputConfiguration.Xbox.GAMEPAD_INTERACT,
        gamepad);

      // DASH
      InputBinding.bind(value -> {
          if (player.getState() != Player.PlayerState.LOCKED) {
            player.perform("DASH");
          }
        }, gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
          ? InputConfiguration.DualShock.GAMEPAD_DASH : InputConfiguration.Xbox.GAMEPAD_DASH,
        gamepad);

      // BASH
      InputBinding.bind(value -> {
          if (player.getState() != Player.PlayerState.LOCKED) {
            player.perform("BASH");
          }
        }, gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
          ? InputConfiguration.DualShock.GAMEPAD_BASH : InputConfiguration.Xbox.GAMEPAD_BASH,
        gamepad);

      // ULTIMATE
      InputBinding.bind(value -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.perform("SURVIVALSKILL");
        }
      }, gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
        ? InputConfiguration.DualShock.GAMEPAD_SURVIVALSKILL
        : InputConfiguration.Xbox.GAMEPAD_SURVIVALSKILL, gamepad);

      // BLOCK START
      InputBinding.bind(value -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.setBlocking(true);
        }
      }, gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
        ? InputConfiguration.DualShock.GAMEPAD_BLOCK_START
        : InputConfiguration.Xbox.GAMEPAD_BLOCK_START, gamepad);

      // BLOCK STOP
      InputBinding.bind(value -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.setBlocking(false);
        }
      }, gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
        ? InputConfiguration.DualShock.GAMEPAD_BLOCK_STOP
        : InputConfiguration.Xbox.GAMEPAD_BLOCK_STOP, gamepad);

      // AIM
      InputBinding bindingAimX = InputBinding.getBinding(
        gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
          ? InputConfiguration.DualShock.GAMEPAD_AIMX : InputConfiguration.Xbox.GAMEPAD_AIMX);
      InputBinding bindingAimY = InputBinding.getBinding(
        gamepad.getType().equals(Gamepad.DualShock4.GAMEPAD_TYPE)
          ? InputConfiguration.DualShock.GAMEPAD_AIMY : InputConfiguration.Xbox.GAMEPAD_AIMY);

      Consumer<Float> aim = value -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          final float x = gamepad.getPollData(bindingAimX.getAction());
          final float y = gamepad.getPollData(bindingAimY.getAction());
          float targetX = 0;
          float targetY = 0;
          if (Math.abs(x) > Game.config().input().getGamepadStickDeadzone()) {
            targetX = x;
          }

          if (Math.abs(y) > Game.config().input().getGamepadStickDeadzone()) {
            targetY = y;
          }

          if (targetX != 0 || targetY != 0) {
            final Point2D target = new Point2D.Double(player.getCenter().getX() + targetX,
              player.getCenter().getY() + targetY);
            final double angle = GeometricUtilities.calcRotationAngleInDegrees(player.getCenter(),
              target);
            player.setAngle((float) angle);
          }
        }
      };

      InputBinding.bind(aim, bindingAimX, gamepad);
      InputBinding.bind(aim, bindingAimY, gamepad);

      return;
    }

    // KEYBOARD CONTROLS
    // WALK UP
    InputBinding.bind(value -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        player.movement().setDy(-1);
      }
    }, InputConfiguration.KEYBOARD_UP, null);

    // WALK DOWN
    InputBinding.bind(value -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        player.movement().setDy(1);
      }
    }, InputConfiguration.KEYBOARD_DOWN, null);

    // WALK LEFT
    InputBinding.bind(value -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        player.movement().setDx(-1);
      }
    }, InputConfiguration.KEYBOARD_LEFT, null);

    // WALK RIGHT
    InputBinding.bind(value -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        player.movement().setDx(1);
      }
    }, InputConfiguration.KEYBOARD_RIGHT, null);

    // INTERACT
    InputBinding.bind(value -> {
      player.interact();
    }, InputConfiguration.KEYBOARD_INTERACT, gamepad);

    // DASH
    InputBinding.bind(value -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        player.perform("DASH");
      }
    }, InputConfiguration.KEYBOARD_DASH, null);

    // BASH
    InputBinding.bind(value -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        player.perform("BASH");
      }
    }, InputConfiguration.KEYBOARD_BASH, null);

    // ULTIMATE
    InputBinding.bind(value -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        player.perform("SURVIVALSKILL");
      }
    }, InputConfiguration.KEYBOARD_SURVIVALSKILL, null);

    // BLOCK START
    InputBinding.bind(value -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        player.setBlocking(true);
      }
    }, InputConfiguration.KEYBOARD_BLOCK_START, null);

    // BLOCK STOP
    InputBinding.bind(value -> {
      if (player.isBlocking()) {
        player.setBlocking(false);
      }
    }, InputConfiguration.KEYBOARD_BLOCK_STOP, null);

    // AIM
    InputBinding.bind(value -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        final double rotation = GeometricUtilities.calcRotationAngleInDegrees(player.getCenter(),
          Input.mouse().getMapLocation());
        player.setAngle((float) rotation);
      }
    }, InputConfiguration.KEYBOARD_AIM, null);
  }

  public static void init() {
    setupDefaultUIInput();
    Game.loop().attach(InputManager::update);

    if (Game.config().input().isGamepadSupport()) {
      // TODO: also display error message if input device gets lost during the game: "missing gamepad for player X"
      Input.gamepads().onRemoved(l ->
        System.out.println(String.format("Gamepad %d (%s) removed.", l.getId(), l.getName()))
      );
      Input.gamepads().onAdded(l ->
        System.out.println(String.format("Gamepad %d (%s) added.", l.getId(), l.getName()))
      );
    }

    if (Game.isDebug()) {
      Input.keyboard().onKeyReleased(KeyEvent.VK_F11,
        e -> Game.config().debug().setDebugEnabled(!Game.config().debug().isDebugEnabled()));
    }
  }

  private static void setupDefaultUIInput() {
    int inputIndex = 0;
    Players.addConfiguration(inputIndex, InputBinding.InputType.KEYBOARD, null);
    if (Input.gamepads() == null) {
      return;
    }
    for (Gamepad pad : Input.gamepads().getAll()) {
      inputIndex++;
      Players.addConfiguration(inputIndex, InputBinding.InputType.GAMEPAD, pad);
    }
  }
}
