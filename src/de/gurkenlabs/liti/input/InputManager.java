package de.gurkenlabs.liti.input;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.entities.PlayerClass;
import de.gurkenlabs.liti.entities.PlayerConfiguration;
import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.liti.gui.Hud;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.input.Gamepad;
import de.gurkenlabs.litiengine.input.Input;

import java.awt.event.KeyEvent;

public final class InputManager {
  private static InputConfiguration config;

  // TODO: also display error message if input device gets lost during the game: "missing gamepad for player X"
  private static boolean defaultInputSet = false;

  private InputManager() {
  }

  public static boolean isDefaultInputSet() {
    return defaultInputSet;
  }

  public static void update() {
    // setup player configuration 1 if no configuration is bound yet
    if (!defaultInputSet) {
      assignDefaultUiInput();

      // TODO: set this in the lobby screen
      Players.getConfiguration(0).setPlayerClass(PlayerClass.WARRIOR);

      // TODO: Hack to join multiple players without lobby screen
      if (Input.gamepads().getAll().size() > 0) {
        PlayerConfiguration configPlayer2 = Players.addConfiguration(1, InputBinding.InputType.GAMEPAD, Input.gamepads().get(0));
        configPlayer2.setPlayerClass(PlayerClass.GATHERER);
        bindUiInput(1, Input.gamepads().get(0));
      }

      if (Input.gamepads().getAll().size() > 1) {
        PlayerConfiguration configPlayer3 = Players.addConfiguration(2, InputBinding.InputType.GAMEPAD, Input.gamepads().get(1));
        configPlayer3.setPlayerClass(PlayerClass.SHAMAN);
        bindUiInput(2, Input.gamepads().get(1));
      }

      defaultInputSet = true;
    }
  }

  public static void bindUiInput(int index, Gamepad gamepad) {
    if (gamepad != null) {
      InputBinding.bind((value) -> Hud.cancel(index), config.getgamepad_cancel(), gamepad);
      InputBinding.bind((value) -> Hud.confirm(index), config.getgamepad_interact(), gamepad);
      InputBinding.bind((value) -> Hud.confirm(index), config.getUi_gamepad_menu(), gamepad);
      InputBinding.bind((value) -> Hud.direction(index, Direction.UP), config.getgamepad_up(), gamepad);
      InputBinding.bind((value) -> Hud.direction(index, Direction.DOWN), config.getgamepad_down(), gamepad);
      InputBinding.bind((value) -> Hud.direction(index, Direction.LEFT), config.getgamepad_left(), gamepad);
      InputBinding.bind((value) -> Hud.direction(index, Direction.RIGHT), config.getgamepad_right(), gamepad);
      return;
    }

    InputBinding.bind((value) -> Hud.cancel(index), config.getkeyboard_cancel(), null);
    InputBinding.bind((value) -> Hud.confirm(index), config.getkeyboard_interact(), null);
    InputBinding.bind((value) -> Hud.confirm(index), config.getUi_keyboard_menu(), null);
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
  }

  public static void init(InputConfiguration conf) {
    config = conf;

    Game.loop().attach(InputManager::update);

    if (Game.config().input().isGamepadSupport()) {
      Input.gamepads().onRemoved(gamepad -> {
        // TODO
      });
    }

    if (Game.isDebug()) {
      Input.keyboard().onKeyReleased(KeyEvent.VK_F11, e -> Game.config().debug().setDebugEnabled(!Game.config().debug().isDebugEnabled()));
    }
  }

  private static void assignDefaultUiInput() {
    // TODO: hardcoded keyboard input for testing. Target: prefer controller over keyboard if present
    if (Game.config().input().isGamepadSupport()) {
      for (Gamepad gamepad : Input.gamepads().getAll()) {
        // bindUiInput(0, gamepad);
        // Players.addConfiguration(0, InputBinding.InputType.GAMEPAD, gamepad);
        // return;
      }
    }

    bindUiInput(0, null);
    Players.addConfiguration(0, InputBinding.InputType.KEYBOARD, null);
  }
}
