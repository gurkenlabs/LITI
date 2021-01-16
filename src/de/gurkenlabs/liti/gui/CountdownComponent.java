package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.constants.LitiFonts;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.tweening.TweenFunction;
import de.gurkenlabs.litiengine.tweening.TweenType;
import de.gurkenlabs.litiengine.util.MathUtilities;
import de.gurkenlabs.litiengine.util.TimeUtilities;

import java.awt.Color;
import java.awt.Graphics2D;

public class CountdownComponent extends GuiComponent {
  private final int duration;
  private long lastStart;
  private boolean active;

  protected CountdownComponent(double x, double y, double width, double height, int duration) {
    super(x, y, width, height);
    this.duration = duration;
  }

  @Override public void prepare() {
    super.prepare();
    this.setVisible(false);
  }

  @Override protected void initializeComponents() {
    super.initializeComponents();
    this.setTextAntialiasing(true);
    this.setFont(LitiFonts.ROUND.deriveFont((float) (this.getHeight() * 2 / 3d)));
    this.getAppearance().setForeColor(Color.WHITE);

  }

  @Override public void render(Graphics2D g) {
    super.render(g);
    if (this.getRemainingTime() == 0 && this.isActive()) {
      return;
    }
    this.setText(this.getRemainingTimeString());
    if (this.getRemainingTimeString().charAt(2) == '0') {
      this.pulsate();
    }
  }

  public void start() {
    this.lastStart = Game.time().now();
    this.active = true;
    this.setVisible(true);
  }

  public void stop() {
    this.active = false;
    this.setVisible(false);
  }

  public boolean isActive() {
    return active;
  }

  public int getDuration() {
    return duration;
  }

  public long getLastStart() {
    return lastStart;
  }

  public boolean hasFinished() {
    return this.getRemainingTime() == 0;
  }

  private long getRemainingTime() {
    return MathUtilities.clamp(this.getDuration() - Game.time().since(this.getLastStart()), 0, this.getDuration());
  }

  private String getRemainingTimeString() {
    return String.format(TimeUtilities.toTimerFormat(this.getRemainingTime(), TimeUtilities.TimerFormat.S_0));
  }

  private void pulsate() {
    Game.tweens().begin(this, TweenType.FONTSIZE, 1000).target(this.getFont().getSize2D() * 2 / 3f).ease(TweenFunction.EXPO_OUT);
  }
}
