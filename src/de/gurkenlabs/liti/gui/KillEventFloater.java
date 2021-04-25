package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.constants.LitiFonts;
import de.gurkenlabs.liti.constants.Timings;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.events.KillEvent;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.tweening.TweenFunction;
import de.gurkenlabs.litiengine.tweening.TweenType;

public class KillEventFloater extends GameEventFloater {
  private ImageComponent p1;
  private ImageComponent icon;
  private ImageComponent p2;

  protected KillEventFloater(double x, double y, double width, double height, KillEvent event) {
    super(x, y, width, height, event);
  }

  @Override
  public void display() {
    super.display();
    Player killer = ((KillEvent) getEvent()).getKiller();
    Player killed = ((KillEvent) getEvent()).getKilled();
    p1.setEnabled(false);
    icon.setEnabled(false);
    p2.setEnabled(false);

    p1.getCurrentAppearance().setForeColor(killer.getColor());
    p1.setText(String.format("P%d", killer.getConfiguration().getIndex() + 1));
    p1.setFont(LitiFonts.ROUND.deriveFont((float) (getHeight() * 4 / 5f)));
    p1.setTextShadow(true);

    icon.getCurrentAppearance().setForeColor(LitiColors.TEXT_DARK);
    icon.setText(getEvent().getIcon().getText());
    icon.setFont(getEvent().getIcon().getFont().deriveFont((float) (getHeight() * 4 / 5f)));

    p2.getCurrentAppearance().setForeColor(killed.getColor());
    p2.setText(String.format("P%d", killed.getConfiguration().getIndex() + 1));
    p2.setFont(LitiFonts.ROUND.deriveFont((float) (getHeight() * 4 / 5f)));
    p2.setTextShadow(true);

  }

  @Override
  protected void beginTween() {
    Game.tweens().begin(this, TweenType.POSITION_Y, Timings.DELAY_UI_LOGEVENT).targetRelative((float) (-getHeight() * 6))
        .ease(TweenFunction.CIRCLE_IN);
    Game.tweens().begin(p1, TweenType.OPACITY, Timings.DELAY_UI_LOGEVENT).target(0, 0, 0, 0, 0);
    Game.tweens().begin(p2, TweenType.OPACITY, Timings.DELAY_UI_LOGEVENT).target(0, 0, 0, 0, 0);
    Game.tweens().begin(icon, TweenType.OPACITY, Timings.DELAY_UI_LOGEVENT).target(0, 0, 0, 0, 0);
  }

  @Override
  protected void initializeComponents() {
    super.initializeComponents();
    p1 = new ImageComponent(getX(), getY(), getWidth() * 2 / 5d, getHeight());
    icon = new ImageComponent(getX() + getWidth() * 2 / 5d, getY(), getWidth() * 1 / 5d, getHeight());
    p2 = new ImageComponent(getX() + getWidth() * 3 / 5d, getY(), getWidth() * 2 / 5d, getHeight());
    getComponents().add(p1);
    getComponents().add(icon);
    getComponents().add(p2);
  }
}
