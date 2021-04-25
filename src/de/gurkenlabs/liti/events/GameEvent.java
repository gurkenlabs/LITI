package de.gurkenlabs.liti.events;

import de.gurkenlabs.liti.gui.IngameScreen;
import de.gurkenlabs.litiengine.Align;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.FontIcon;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.Color;

public abstract class GameEvent {
  private long tick;
  private String messageFormat;

  private GameEventType type;

  protected GameEvent(GameEventType type) {
    this.tick = Game.time().now();
    this.messageFormat = Resources.strings().get(getClass().getSimpleName().toLowerCase());
    this.type = type;
  }

  public GameEventType getType() {
    return type;
  }

  public long getTick() {
    return tick;
  }

  public String getMessageFormat() {
    return messageFormat;
  }

  public Align getIconAlign() {
    return Align.LEFT;
  }

  public Align getTextAlign() {
    return Align.CENTER;
  }

  public abstract FontIcon getIcon();

  public abstract String getMessage();
}
