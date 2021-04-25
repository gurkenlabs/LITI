package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.events.GameEvent;
import de.gurkenlabs.liti.events.KillEvent;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.gui.GuiComponent;

import java.util.ArrayList;
import java.util.List;

public class GameLog extends GuiComponent implements IUpdateable {
  private List<GameEvent> pastEvents;
  private List<GameEventFloater> activeFloaters;

  public GameLog(double x, double y, double width, double height) {
    super(x, y, width, height);
    this.pastEvents = new ArrayList<>();
    this.activeFloaters = new ArrayList<>();
  }

  public List<GameEvent> getPastEvents() {
    return pastEvents;
  }

  public List<GameEventFloater> getActiveFloaters() {
    return activeFloaters;
  }

  public void logEvent(GameEvent evt) {
    pastEvents.add(evt);
    GameEventFloater floater = null;
    double x = getX();
    double y = getNewFloaterY();
    double width = getWidth();
    double height = getFloaterHeight();
    switch (evt.getType()) {
    case CHICKENDELIVERED:
      break;
    case EGGDESTROYED:
      break;
    case DARWINAWARD:
      break;
    case KILL:
      floater = new KillEventFloater(x, y, width, height, (KillEvent) evt);
    case SPAWN:
      break;
    case EVOLUTIONREACHED:
      break;
    case GAMEWON:
      break;
    }
    activeFloaters.add(floater);
  }

  private double getNewFloaterY() {
    return getY() + getHeight() - getHeight() * 1 / 5d;
  }

  private double getFloaterHeight() {
    return getHeight() * 1 / 5d;
  }

  @Override
  public void update() {
    for (int i = 0; i < getActiveFloaters().size(); i++) {
      GameEventFloater floater = getActiveFloaters().get(i);
      if (!getComponents().contains(floater) && (i == 0 || getActiveFloaters().get(i - 1).getY() <= getY() / getHeight())) {
        floater.display();
        getComponents().add(floater);
        floater.beginTween();
      } else if (floater.ttlReached()) {
        getComponents().remove(floater);
        getActiveFloaters().remove(floater);
        floater.suspend();
      }
    }
  }

  @Override
  public void prepare() {
    super.prepare();
    Game.loop().attach(this);
  }

  @Override
  public void suspend() {
    super.suspend();
    Game.loop().detach(this);
  }
}
