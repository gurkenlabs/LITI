package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.constants.Timings;
import de.gurkenlabs.liti.events.GameEvent;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.ImageComponentList;

import java.awt.Color;
import java.awt.Graphics2D;

public class GameLog extends ImageComponentList {
  private static final int rows = 5;
  private static final Color[] rowColors = new Color[] { LitiColors.GAMELOG_ROW1, LitiColors.GAMELOG_ROW2, LitiColors.GAMELOG_ROW3,
      LitiColors.GAMELOG_ROW4, LitiColors.GAMELOG_ROW5 };
  private long lastEvent;

  public GameLog(double x, double y, double width, double height) {
    super(x, y, width, height, rows, 1, null, null);
  }

  @Override public void render(Graphics2D g) {
    super.render(g);
    if (Game.time().since(lastEvent) >= Timings.DELAY_UI_LOGEVENT && isVisible()) {
      setVisible(false);
    }
  }

  @Override protected void initializeComponents() {
    super.initializeComponents();

  }

  @Override public void prepare() {
    super.prepare();
    for (int i = 0; i < getCellComponents().size(); i++) {
      ImageComponent comp = getCellComponents().get(i);
      comp.getAppearanceDisabled().setTransparentBackground(false);
      comp.getAppearanceDisabled().setBackgroundColor1(rowColors[i]);
      comp.getAppearanceDisabled().setForeColor(LitiColors.TEXT_BRIGHT);
      comp.setEnabled(false);
      comp.setVisible(false);
    }
  }

  @Override public void suspend() {
    super.suspend();
  }

  public void logEvent(GameEvent evt) {
    for (int i = 0; i < getCellComponents().size() - 1; i++) {
      ImageComponent comp = getCellComponents().get(i);
      comp.setText(getCellComponents().get(i + 1).getText());
      if (!comp.getText().isEmpty() && !comp.isVisible()) {
        comp.setVisible(true);
      }
    }
    ImageComponent latest = getCellComponents().get(getCellComponents().size() - 1);
    latest.setVisible(true);
    latest.setText(evt.getMessage());
    lastEvent = Game.time().now();
  }
}
