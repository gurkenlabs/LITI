package de.gurkenlabs.liti.events;

import de.gurkenlabs.liti.entities.Player;

public class KillEvent extends GameEvent {
  private final Player killer;
  private final Player killed;

  public KillEvent(Player killer, Player killed) {
    super();
    this.killer = killer;
    this.killed = killed;
  }

  public Player getKiller() {
    return killer;
  }

  public Player getKilled() {
    return killed;
  }

  @Override public String getMessage() {
    int killerIndex = killer.getConfiguration().getIndex();
    int killedIndex = killed.getConfiguration().getIndex();
    String killerClass = killer.getPlayerClass().name();
    String killedClass = killed.getPlayerClass().name();
    return String.format(getMessageFormat(), killerIndex, killerClass, killedIndex, killedClass);
  }
}
