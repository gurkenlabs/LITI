package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.constants.LitiFonts;
import de.gurkenlabs.liti.constants.LitiSounds;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.tweening.TweenFunction;
import de.gurkenlabs.litiengine.tweening.TweenType;
import de.gurkenlabs.litiengine.util.TimeUtilities;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class CountdownComponent extends GuiComponent {
  private final int duration;
  private long lastStart;
  private boolean active;
  private final boolean playSounds;
  private boolean secondPassed;
  private final Collection<CountdownListener> countdownListeners = ConcurrentHashMap.newKeySet();

  protected CountdownComponent(double x, double y, double width, double height, int duration, boolean playSounds) {
    super(x, y, width, height);
    this.duration = duration;
    this.playSounds = playSounds;
  }

  @Override public void prepare() {
    super.prepare();
    this.setVisible(false);
  }

  @Override protected void initializeComponents() {
    super.initializeComponents();
    this.setFont(LitiFonts.ROUND.deriveFont((float) (getHeight() * 2 / 3d)));
    getAppearanceDisabled().setForeColor(Color.WHITE);

  }

  @Override public void render(Graphics2D g) {
    if (!isActive()) {
      return;
    }
    if (hasFinished()) {
      this.finish();
    }
    g.setColor(new Color(0, 0, 0, 100));
    g.fill(new Rectangle(0, 0, (int) Game.window().getResolution().getWidth(), (int) Game.window().getResolution().getHeight()));
    super.render(g);
    this.setText(getRemainingTimeString());
    this.passSecond();
  }

  public void start() {
    this.lastStart = Game.time().now();
    this.active = true;
    this.setVisible(true);
    this.setEnabled(false);

    for (final CountdownListener listener : this.countdownListeners) {
      listener.started();
    }
  }

  public void stop() {
    this.active = false;
    this.setVisible(false);

    for (final CountdownListener listener : this.countdownListeners) {
      listener.stopped();
    }
  }

  public void finish() {
    this.stop();
    for (final CountdownListener listener : this.countdownListeners) {
      listener.finished();
    }
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
    return getRemainingTime() == 0;
  }

  private long getRemainingTime() {
    return Math.clamp(getDuration() - Game.time().since(getLastStart()), 0, getDuration());
  }

  private String getRemainingTimeString() {
    return TimeUtilities.toTimerFormat(getRemainingTime(), TimeUtilities.TimerFormat.S_0);
  }

  private void passSecond() {
    if (getRemainingTimeString().charAt(2) != '0' || this.secondPassed) {
      return;
    }

    if (playSounds) {
      Game.audio().playSound(getRemainingTimeString().charAt(0) != '0' ? LitiSounds.COUNTDOWN_RUNNING : LitiSounds.COUNTDOWN_FINISHED);
    }
    Game.tweens().reset(this, TweenType.FONTSIZE);
    Game.tweens().begin(this, TweenType.FONTSIZE, 500).target(getFont().getSize2D() * 2 / 3f).ease(TweenFunction.EXPO_OUT);

    for (final CountdownListener listener : this.countdownListeners) {
      listener.secondPassed();
    }

    this.secondPassed = true;
    Game.loop().perform(999, () -> this.secondPassed = false);
  }

  public void addListener(final CountdownListener listener) {
    this.countdownListeners.add(listener);
  }
}
