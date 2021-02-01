package de.gurkenlabs.liti.input;

import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.function.Consumer;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.liti.gui.Hud;
import com.litiengine.Direction;
import com.litiengine.Game;
import com.litiengine.input.Gamepad;
import com.litiengine.input.Input;
import com.litiengine.util.geom.GeometricUtilities;

public final class InputManager {

  private InputManager() {
  }

  public static void update() {
  }

  public static void bindUiInput(int index, Gamepad gamepad) {
    if (gamepad != null) {
      InputBinding.bind(value -> UIInput.cancel(index), InputConfiguration.GAMEPAD_CANCEL, gamepad);
      InputBinding.bind(value -> UIInput.confirm(index), InputConfiguration.UI_GAMEPAD_CONFIRM, gamepad);
      InputBinding.bind(value -> UIInput.menu(index), InputConfiguration.UI_GAMEPAD_MENU, gamepad);
      InputBinding.bind(value -> UIInput.info(index), InputConfiguration.UI_GAMEPAD_INFO, gamepad);
      InputBinding.bind(value -> UIInput.direction(index, Direction.UP), InputConfiguration.GAMEPAD_UP, gamepad);
      InputBinding.bind(value -> UIInput.direction(index, Direction.DOWN), InputConfiguration.GAMEPAD_DOWN, gamepad);
      InputBinding.bind(value -> UIInput.direction(index, Direction.LEFT), InputConfiguration.GAMEPAD_LEFT, gamepad);
      InputBinding.bind(value -> UIInput.direction(index, Direction.RIGHT), InputConfiguration.GAMEPAD_RIGHT, gamepad);
      InputBinding.bind(value -> {
        if (value.floatValue() == Gamepad.DPad.UP) {
          UIInput.direction(index, Direction.UP);
        }
      }, InputConfiguration.GAMEPAD_POV, gamepad);
      InputBinding.bind(value -> {
        if (value.floatValue() == Gamepad.DPad.DOWN) {
          UIInput.direction(index, Direction.DOWN);
        }
      }, InputConfiguration.GAMEPAD_POV, gamepad);
      InputBinding.bind(value -> {
        if (value.floatValue() == Gamepad.DPad.LEFT) {
          UIInput.direction(index, Direction.LEFT);
        }
      }, InputConfiguration.GAMEPAD_POV, gamepad);
      InputBinding.bind(value -> {
        if (value.floatValue() == Gamepad.DPad.RIGHT) {
          UIInput.direction(index, Direction.RIGHT);
        }
      }, InputConfiguration.GAMEPAD_POV, gamepad);
      return;
    }

    InputBinding.bind(value -> UIInput.cancel(index), InputConfiguration.KEYBOARD_CANCEL, null);
    InputBinding.bind(value -> UIInput.confirm(index), InputConfiguration.UI_KEYBOARD_CONFIRM, null);
    InputBinding.bind(value -> UIInput.menu(index), InputConfiguration.UI_KEYBOARD_MENU, null);
    InputBinding.bind(value -> UIInput.info(index), InputConfiguration.UI_KEYBOARD_INFO, null);
    InputBinding.bind(value -> UIInput.direction(index, Direction.UP), InputConfiguration.KEYBOARD_UP, null);
    InputBinding.bind(value -> UIInput.direction(index, Direction.DOWN), InputConfiguration.KEYBOARD_DOWN, null);
    InputBinding.bind(value -> UIInput.direction(index, Direction.LEFT), InputConfiguration.KEYBOARD_LEFT, null);
    InputBinding.bind(value -> UIInput.direction(index, Direction.RIGHT), InputConfiguration.KEYBOARD_RIGHT, null);
  }

  public static void bindPlayerInput(Player player, Gamepad gamepad) {
    // GAMEPAD CONTROLS
    if (player.getConfiguration().getInputDevice() == InputBinding.InputType.GAMEPAD) {
      // WALK UP
      InputBinding.bind(value -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.movement().setDy(value);
        }
      }, InputConfiguration.GAMEPAD_UP, gamepad);

      // WALK DOWN
      InputBinding.bind(value -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.movement().setDy(value);
        }
      }, InputConfiguration.GAMEPAD_DOWN, gamepad);

      // WALK LEFT
      InputBinding.bind(value -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.movement().setDx(value);
        }
      }, InputConfiguration.GAMEPAD_LEFT, gamepad);

      // WALK RIGHT
      InputBinding.bind(value -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.movement().setDx(value);
        }
      }, InputConfiguration.GAMEPAD_RIGHT, gamepad);

      // INTERACT
      InputBinding.bind(value -> {
        player.interact();
      }, InputConfiguration.GAMEPAD_INTERACT, gamepad);

      // DASH
      InputBinding.bind(value -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.perform("DASH");
        }
      }, InputConfiguration.GAMEPAD_DASH, gamepad);

      // BASH
      InputBinding.bind(value -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.perform("BASH");
        }
      }, InputConfiguration.GAMEPAD_BASH, gamepad);

      // ULTIMATE
      InputBinding.bind(value -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.perform("SURVIVALSKILL");
        }
      }, InputConfiguration.GAMEPAD_SURVIVALSKILL, gamepad);

      // BLOCK START
      InputBinding.bind(value -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.setBlocking(true);
        }
      }, InputConfiguration.GAMEPAD_BLOCK_START, gamepad);

      // BLOCK STOP
      InputBinding.bind(value -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.setBlocking(false);
        }
      }, InputConfiguration.GAMEPAD_BLOCK_STOP, gamepad);

      // AIM
      InputBinding bindingAimX = InputBinding.getBinding(InputConfiguration.GAMEPAD_AIMX);
      InputBinding bindingAimY = InputBinding.getBinding(InputConfiguration.GAMEPAD_AIMY);

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
            final Point2D target = new Point2D.Double(player.getCenter().getX() + targetX, player.getCenter().getY() + targetY);
            final double angle = GeometricUtilities.calcRotationAngleInDegrees(player.getCenter(), target);
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
        final double rotation = GeometricUtilities.calcRotationAngleInDegrees(player.getCenter(), Input.mouse().getMapLocation());
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
      Input.keyboard().onKeyReleased(KeyEvent.VK_F11, e -> Game.config().debug().setDebugEnabled(!Game.config().debug().isDebugEnabled()));
    }
  }

  private static void setupDefaultUIInput() {
    int inputIndex = 0;
    Players.addConfiguration(inputIndex, InputBinding.InputType.KEYBOARD, null);
    for (Gamepad pad : Input.gamepads().getAll()) {
      inputIndex++;
      Players.addConfiguration(inputIndex, InputBinding.InputType.GAMEPAD, pad);
    }
  }
}
