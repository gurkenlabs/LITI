package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.GameManager;
import de.gurkenlabs.liti.GameState;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.graphics.Camera;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

public class DynamicZoomCamera extends Camera {

  public static float minZoom = .71f;
  public static float maxZoom = 1.4f;
  private static final short padding = 20;

  public DynamicZoomCamera() {
    this.setClampToMap(true);
    this.setFocus(Game.world().environment().getCenter());
  }

  @Override
  public void update() {
    super.update();

    if (GameManager.getGameState() == GameState.FINISHED) {
      return;
    }

    this.determineFocusAndZoom();
  }

  private void determineFocusAndZoom() {
    if (GameManager.getGameState() == GameState.PREGAME) {
      return;
    }

    Collection<Player> players = Players.getAll();
    double minX = 0;
    double maxX = 0;
    double minY = 0;
    double maxY = 0;
    for (Player p : players) {
      Spawnpoint spawn = GameManager.getSpawn(p);
      final boolean isNotVisible = p.isDead() && !p.isLoaded();

      double playerMinX = isNotVisible ? spawn.getX() : p.getX();
      double playerMinY = isNotVisible ? spawn.getY() : p.getY() - 6; //6 accounts for the healthbar
      double playerMaxX = isNotVisible ? spawn.getBoundingBox().getMaxX() : p.getBoundingBox().getMaxX();
      double playerMaxY = isNotVisible ? spawn.getBoundingBox().getMaxY() : p.getBoundingBox().getMaxY();
      if (minX == 0 || playerMinX < minX) {
        minX = playerMinX;
      }
      if (maxX == 0 || playerMaxX > maxX) {
        maxX = playerMaxX;
      }
      if (minY == 0 || playerMinY < minY) {
        minY = playerMinY;
      }
      if (maxY == 0 || playerMaxY > maxY) {
        maxY = playerMaxY;
      }
    }
    Rectangle2D bounds = new Rectangle2D.Double(minX - padding, minY - padding, maxX - minX + 2 * padding, maxY - minY + 2 * padding);
    this.setFocus(bounds.getCenterX(), bounds.getCenterY());
    double rel = Math.min(Game.world().environment().getMap().getSizeInPixels().getWidth() / bounds.getWidth() / 2,
        Game.world().environment().getMap().getSizeInPixels().getHeight() / bounds.getHeight() / 2);

    float targetZoom = Math.clamp((float) rel, minZoom, maxZoom);
    this.setZoom(targetZoom, 100);
  }
}
