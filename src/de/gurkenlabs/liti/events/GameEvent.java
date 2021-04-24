package de.gurkenlabs.liti.events;

import de.gurkenlabs.liti.gui.IngameScreen;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.resources.Resources;

public abstract class GameEvent {
  private long tick;
  private String messageFormat;

  protected GameEvent() {
    this.tick = Game.time().now();
    this.messageFormat = Resources.strings().get(getClass().getSimpleName().toLowerCase());
  }

  public long getTick() {
    return tick;
  }

  public String getMessageFormat() {
    return messageFormat;
  }

  public abstract String getMessage();
}
