package de.gurkenlabs.liti.input;

import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.function.Consumer;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.liti.gui.Hud;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.input.Gamepad;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.util.geom.GeometricUtilities;

public final class InputManager {
  private static InputConfiguration config;

  private InputManager() {
  }

  public static void update() {
  }

  public static void bindUiInput(int index, Gamepad gamepad) {
    if (gamepad != null) {
      InputBinding.bind((value) -> Hud.cancel(index), config.getgamepad_cancel(), gamepad);
      InputBinding.bind((value) -> Hud.confirm(index), config.getUi_gamepad_confirm(), gamepad);
      InputBinding.bind((value) -> Hud.menu(index), config.getUi_gamepad_menu(), gamepad);
      InputBinding.bind((value) -> Hud.info(index), config.getUi_gamepad_info(), gamepad);
      InputBinding.bind((value) -> Hud.direction(index, Direction.UP), config.getgamepad_up(), gamepad);
      InputBinding.bind((value) -> Hud.direction(index, Direction.DOWN), config.getgamepad_down(), gamepad);
      InputBinding.bind((value) -> Hud.direction(index, Direction.LEFT), config.getgamepad_left(), gamepad);
      InputBinding.bind((value) -> Hud.direction(index, Direction.RIGHT), config.getgamepad_right(), gamepad);
      return;
    }

    InputBinding.bind((value) -> Hud.cancel(index), config.getkeyboard_cancel(), null);
    InputBinding.bind((value) -> Hud.confirm(index), config.getUi_keyboard_confirm(), null);
    InputBinding.bind((value) -> Hud.menu(index), config.getUi_keyboard_menu(), null);
    InputBinding.bind((value) -> Hud.info(index), config.getUi_keyboard_info(), null);
    InputBinding.bind((value) -> Hud.direction(index, Direction.UP), config.getkeyboard_up(), null);
    InputBinding.bind((value) -> Hud.direction(index, Direction.DOWN), config.getkeyboard_down(), null);
    InputBinding.bind((value) -> Hud.direction(index, Direction.LEFT), config.getkeyboard_left(), null);
    InputBinding.bind((value) -> Hud.direction(index, Direction.RIGHT), config.getkeyboard_right(), null);
  }

  public static void bindPlayerInput(Player player, Gamepad gamepad) {
    // GAMEPAD CONTROLS
    if (player.getConfiguration().getInputDevice() == InputBinding.InputType.GAMEPAD) {
      // WALK UP
      InputBinding.bind((value) -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.movement().setDy(value);
        }
      }, config.getgamepad_up(), gamepad);

      // WALK DOWN
      InputBinding.bind((value) -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.movement().setDy(value);
        }
      }, config.getgamepad_down(), gamepad);

      // WALK LEFT
      InputBinding.bind((value) -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.movement().setDx(value);
        }
      }, config.getgamepad_left(), gamepad);

      // WALK RIGHT
      InputBinding.bind((value) -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.movement().setDx(value);
        }
      }, config.getgamepad_right(), gamepad);

      // DASH
      InputBinding.bind((value) -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.perform("DASH");
        }
      }, config.getgamepad_dash(), gamepad);

      // BASH
      InputBinding.bind((value) -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.perform("BASH");
        }
      }, config.getgamepad_bash(), gamepad);

      // ULTIMATE
      InputBinding.bind((value) -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.perform("ULTIMATE");
        }
      }, config.getgamepad_ultimate(), gamepad);

      // BLOCK START
      InputBinding.bind((value) -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.setBlocking(true);
        }
      }, config.getgamepad_block_start(), gamepad);

      // BLOCK STOP
      InputBinding.bind((value) -> {
        if (player.getState() != Player.PlayerState.LOCKED) {
          player.setBlocking(false);
        }
      }, config.getgamepad_block_stop(), gamepad);

      // AIM
      InputBinding bindingAimX = InputBinding.getBinding(config.getgamepad_aimx());
      InputBinding bindingAimY = InputBinding.getBinding(config.getgamepad_aimy());

      Consumer<Float> aim = (value) -> {
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
    InputBinding.bind((value) -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        player.movement().setDy(-1);
      }
    }, config.getkeyboard_up(), null);

    // WALK DOWN
    InputBinding.bind((value) -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        player.movement().setDy(1);
      }
    }, config.getkeyboard_down(), null);

    // WALK LEFT
    InputBinding.bind((value) -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        player.movement().setDx(-1);
      }
    }, config.getkeyboard_left(), null);

    // WALK RIGHT
    InputBinding.bind((value) -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        player.movement().setDx(1);
      }
    }, config.getkeyboard_right(), null);

    // DASH
    InputBinding.bind((value) -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        player.perform("DASH");
      }
    }, config.getkeyboard_dash(), null);

    // BASH
    InputBinding.bind((value) -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        player.perform("BASH");
      }
    }, config.getkeyboard_bash(), null);

    // ULTIMATE
    InputBinding.bind((value) -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        player.perform("ULTIMATE");
      }
    }, config.getkeyboard_ultimate(), null);

    // BLOCK START
    InputBinding.bind((value) -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        player.setBlocking(true);
      }
    }, config.getkeyboard_block_start(), null);

    // BLOCK STOP
    InputBinding.bind((value) -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        player.setBlocking(false);
      }
    }, config.getkeyboard_block_stop(), null);

    // AIM
    InputBinding.bind((value) -> {
      if (player.getState() != Player.PlayerState.LOCKED) {
        final double rotation = GeometricUtilities.calcRotationAngleInDegrees(player.getCenter(), Input.mouse().getMapLocation());
        player.setAngle((float) rotation);
      }
    }, config.getkeyboard_aim(), null);
  }

  public static void init(InputConfiguration conf) {
    config = conf;
    setupDefaultUIInput();

    Game.loop().attach(InputManager::update);

    if (Game.config().input().isGamepadSupport()) {
      // TODO: also display error message if input device gets lost during the
      // game:
      // "missing gamepad for player X"
      Input.gamepads().onRemoved(l -> {
        System.out.println(String.format("Gamepad %d (%s) removed.", l.getId(), l.getName()));
      });
      Input.gamepads().onAdded(l -> {
        System.out.println(String.format("Gamepad %d (%s) added.", l.getId(), l.getName()));
      });
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
