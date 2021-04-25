package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.constants.LitiFonts;
import de.gurkenlabs.liti.constants.Timings;
import de.gurkenlabs.liti.events.GameEvent;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.ImageComponentList;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class GameLog extends ImageComponentList {
  private static final int rows = 5;
  private static final Color[] rowColors = new Color[] { LitiColors.GAMELOG_ROW1, LitiColors.GAMELOG_ROW2, LitiColors.GAMELOG_ROW3,
      LitiColors.GAMELOG_ROW4, LitiColors.GAMELOG_ROW5 };
  private List<GameEvent> pastEvents;

  public GameLog(double x, double y, double width, double height) {
    super(x, y, width, height, rows, 1, null, null);
    this.pastEvents = new ArrayList<>();
  }

  public List<GameEvent> getPastEvents() {
    return pastEvents;
  }

  @Override
  public void render(Graphics2D g) {
    super.render(g);
    if (getPastEvents().size() == 0) {
      return;
    }
    if (Game.time().since(getPastEvents().get(getPastEvents().size() - 1).getTick()) >= Timings.DELAY_UI_LOGEVENT && isVisible()) {
      setVisible(false);
    }
    for (int i = getCellComponents().size() - 1; i >= 0; i--) {
      ImageComponent comp = getCellComponents().get(i);
      int eventIndex = pastEvents.size() - (getCellComponents().size() - i);
      if (eventIndex < 0 || !comp.isVisible()) {
        return;
      }
      GameEvent e = pastEvents.get(eventIndex);
      g.setFont(e.getIcon().getFont().deriveFont((float) (comp.getHeight() * 2 / 3f)));
      g.setColor(LitiColors.TEXT_BRIGHT);
      TextRenderer.render(g, e.getIcon().getText(),
          comp.getX() + e.getIconAlign().getLocation(comp.getWidth(), g.getFontMetrics().stringWidth(e.getIcon().getText())),
          comp.getY() + comp.getHeight() - TextRenderer.getHeight(g, e.getIcon().getText()) / 2d);
      e.getIcon().render(g, e.getColor(), (float) (comp.getHeight() * 2 / 3f),
          comp.getX() + e.getIconAlign().getValue(comp.getWidth()) - g.getFontMetrics().stringWidth(e.getIcon().getText()) / 2d, comp.getTextY(),
          false);
    }
  }

  @Override
  protected void initializeComponents() {
    super.initializeComponents();

  }

  @Override
  public void prepare() {
    super.prepare();
    for (int i = 0; i < getCellComponents().size(); i++) {
      ImageComponent comp = getCellComponents().get(i);
      comp.setFont(LitiFonts.ETCHED.deriveFont((float) (comp.getHeight() * 2 / 3f)));
      comp.setTextAntialiasing(true);
      comp.getAppearanceDisabled().setTransparentBackground(false);
      comp.getAppearanceDisabled().setBackgroundColor1(rowColors[i]);
      comp.getAppearanceDisabled().setForeColor(LitiColors.TEXT_BRIGHT);
      comp.setEnabled(false);
      comp.setVisible(false);
    }
  }

  @Override
  public void suspend() {
    super.suspend();
  }

  public void logEvent(GameEvent evt) {
    pastEvents.add(evt);
    setVisible(true);
    for (int i = getCellComponents().size() - 1; i >= 0; i--) {
      ImageComponent comp = getCellComponents().get(i);
      int eventIndex = pastEvents.size() - (getCellComponents().size() - i);

      if (eventIndex < 0) {
        comp.setVisible(false);
        continue;
      } else if (!comp.getText().isEmpty() && !comp.isVisible()) {
        comp.setVisible(true);
      }
      GameEvent e = pastEvents.get(eventIndex);
      comp.setText(e.getMessage());
      comp.setTextAlign(e.getTextAlign());
      comp.getAppearanceDisabled().setForeColor(e.getColor());
    }
  }
}
