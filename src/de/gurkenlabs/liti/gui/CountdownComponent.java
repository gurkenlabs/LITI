package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.constants.LitiFonts;
import de.gurkenlabs.liti.constants.LitiSounds;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.EntityRenderListener;
import de.gurkenlabs.litiengine.entities.EntityRenderedListener;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.tweening.TweenFunction;
import de.gurkenlabs.litiengine.tweening.TweenType;
import de.gurkenlabs.litiengine.util.MathUtilities;
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
  private boolean playSounds;
  private boolean pulsating;
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
    this.setTextAntialiasing(true);
    this.setFont(LitiFonts.ROUND.deriveFont((float) (this.getHeight() * 2 / 3d)));
    this.getAppearance().setForeColor(Color.WHITE);

  }

  @Override public void render(Graphics2D g) {
    if (!this.isActive()) {
      return;
    }
    if (this.hasFinished()) {
      this.stop();
    }
    g.setColor(new Color(0, 0, 0, 100));
    g.fill(new Rectangle(0, 0, (int) Game.window().getResolution().getWidth(), (int) Game.window().getResolution().getHeight()));
    super.render(g);
    this.setText(this.getRemainingTimeString());
    this.pulsate();
  }

  public void start() {
    this.lastStart = Game.time().now();
    this.active = true;
    this.setVisible(true);

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
    if (this.getRemainingTimeString().charAt(2) != '0' || this.pulsating) {
      return;
    }
    pulsating = true;

    if (playSounds) {
      Game.audio().playSound(this.getRemainingTimeString().charAt(0) != '0' ? LitiSounds.COUNTDOWN_RUNNING : LitiSounds.COUNTDOWN_FINISHED);
    }
    Game.tweens().begin(this, TweenType.FONTSIZE, 500).target(this.getFont().getSize2D() * 2 / 3f).ease(TweenFunction.EXPO_OUT);
    Game.loop().perform(999, () -> {
      pulsating = false;
      Game.tweens().reset(this, TweenType.FONTSIZE);
    });
  }

  public void addListener(final CountdownListener listener) {
    this.countdownListeners.add(listener);
  }
}
