package de.gurkenlabs.liti.gui;

import java.awt.geom.Rectangle2D;
import java.util.Collection;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.FreeFlightCamera;
import de.gurkenlabs.litiengine.util.MathUtilities;

public class DynamicZoomCamera extends FreeFlightCamera {

  private static float minZoom = .3f;
  private static float maxZoom = 1.4f;

  public DynamicZoomCamera() {
    this.setFocus(Game.world().environment().getCenter());
  }

  @Override
  public void update() {
    this.determineFocusAndZoom();
  }

  private void determineFocusAndZoom() {
    Collection<Player> players = Players.getAll().values();
    double minX = 0;
    double maxX = 0;
    double minY = 0;
    double maxY = 0;
    for (Player p : players) {
      double playerX = p.getCenter().getX();
      double playerY = p.getCenter().getY();
      if (minX == 0 || playerX < minX) {
        minX = playerX;
      }
      if (maxX == 0 || playerX > maxX) {
        maxX = playerX;
      }
      if (minY == 0 || playerY < minY) {
        minY = playerY;
      }
      if (maxY == 0 || playerY > maxY) {
        maxY = playerY;
      }
    }
    Rectangle2D bounds = new Rectangle2D.Double(minX - 20, minY - 20, maxX - minX + 40, maxY - minY + 40);
    this.setFocus(bounds.getCenterX(), bounds.getCenterY());
    double rel = Math.min(Game.world().environment().getMap().getSizeInPixels().getWidth() / bounds.getWidth() / 2, Game.world().environment().getMap().getSizeInPixels().getHeight() / bounds.getHeight() / 2);
    float targetZoom = MathUtilities.clamp((float) rel, minZoom, maxZoom);
    this.setZoom(targetZoom, 0);
  }
}
