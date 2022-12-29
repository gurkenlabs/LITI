package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.constants.LitiFonts;
import de.gurkenlabs.liti.constants.Timings;
import de.gurkenlabs.liti.events.GameEvent;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.tweening.TweenFunction;
import de.gurkenlabs.litiengine.tweening.TweenType;

import java.awt.Font;

public abstract class GameEventFloater extends GuiComponent {
  private GameEvent event;
  private long popupTime;

  protected GameEventFloater(double x, double y, double width, double height, GameEvent event) {
    super(x, y, width, height);
    this.event = event;
  }

  public void display() {
    popupTime = Game.time().now();
    setVisible(true);
    setEnabled(false);
    setFont(LitiFonts.ETCHED.deriveFont((float) (getHeight() * 2 / 3f)).deriveFont(Font.BOLD));
  }

  public GameEvent getEvent() {
    return event;
  }

  public boolean ttlReached() {
    return Game.time().since(popupTime) > Timings.DELAY_UI_LOGEVENT;
  }

  protected void beginTween() {
    Game.tweens().begin(this, TweenType.LOCATION_Y, Timings.DELAY_UI_LOGEVENT).targetRelative((float) (-getHeight() * 6))
        .ease(TweenFunction.CIRCLE_IN);
    Game.tweens().begin(this, TweenType.OPACITY, Timings.DELAY_UI_LOGEVENT).target(0, 0, 0, 0, 0);
  }
}
