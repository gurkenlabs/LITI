package de.gurkenlabs.liti.entities;

import de.gurkenlabs.liti.input.InputBinding;
import de.gurkenlabs.liti.input.InputManager;
import de.gurkenlabs.litiengine.input.Gamepad;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Players {
  public static final int MAX_PLAYERS = 4;

  private static Map<PlayerClass, Player> allPlayers = new ConcurrentHashMap<>();

  private static List<PlayerConfiguration> configurations = new CopyOnWriteArrayList<>();

  private Players() {
  }

  /**
   * Creates actual entities and bind the actions to the entity
   *
   * @param config
   *          The player configuration containing details about the chosen input
   *          device, class and the player index.
   */
  public static Player join(PlayerConfiguration config) {
    if (config.getPlayerClass() == null) {
      throw new IllegalArgumentException("unspecified player class for player " + (config.getIndex() + 1));
    }

    Player player = null;
    switch (config.getPlayerClass()) {
    case WARRIOR:
      player = new Warrior(config);
      break;
    case SHAMAN:
      player = new Shaman(config);
      break;
    case HUNTRESS:
      player = new Huntress(config);
      break;
    case GATHERER:
      player = new Gatherer(config);
      break;
    default:
      break;
    }

    if (player == null) {
      throw new IllegalArgumentException("unspecified player class.");
    }

    player.setIndex(config.getIndex());
    InputManager.bindPlayerInput(player, config.getGamepad());
    getAll().put(config.getPlayerClass(), player);
    return player;
  }

  public static Map<PlayerClass, Player> getAll() {
    return allPlayers;
  }

  public static int joinedPlayers() {
    return getConfigurations().size();
  }

  public static List<PlayerConfiguration> getConfigurations() {
    return configurations;
  }

  public static PlayerConfiguration getConfiguration(int index) {
    return configurations.get(index);
  }

  public static PlayerConfiguration addConfiguration(int index, InputBinding.InputType device, Gamepad gamepad) {
    if (configurations.size() >= MAX_PLAYERS) {
      throw new UnsupportedOperationException("ONLY " + MAX_PLAYERS + " SUPPORTED");
    }

    PlayerConfiguration config = new PlayerConfiguration(index, device, gamepad);
    getConfigurations().add(config);
    return config;
  }
}
