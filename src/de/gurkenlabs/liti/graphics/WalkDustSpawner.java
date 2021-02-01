package de.gurkenlabs.liti.graphics;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.entities.Player;
import com.litiengine.Game;
import com.litiengine.entities.EntityMovedEvent;
import com.litiengine.entities.IMobileEntity.EntityMovedListener;
import com.litiengine.graphics.Spritesheet;
import com.litiengine.graphics.emitters.Emitter;
import com.litiengine.resources.Resources;

public class WalkDustSpawner implements EntityMovedListener {
  private long lastWalkDust;

  @Override
  public void moved(EntityMovedEvent event) {
    final Player player = (Player) event.getEntity();
    if (event.getEntity().movement().getVelocity() <= .5 && !player.isDashing()) {
      return;
    }

    final int STEP_DELAY = player.isDashing() ? 100 : 360;
    if (event.getDeltaX() == 0 && event.getDeltaY() == 0 || Game.world().environment() == null) {
      return;
    }

    String walkSprite = "walk-dust";

    BufferedImage map = Resources.spritesheets().get("plateau").getImage();
    if (map != null) {
      int x = (int) Math.min(map.getWidth() - 1, event.getEntity().getCollisionBoxCenter().getX());
      int y = (int) Math.min(map.getHeight() - 1, event.getEntity().getCollisionBoxCenter().getY());
      Color groundColor = new Color(map.getRGB(x, y));
      if (groundColor != null && (groundColor.equals(LitiColors.MAP_GRASS) || groundColor.equals(LitiColors.MAP_DARKGRASS))) {
        walkSprite = "walk-grass";
      }
    }

    Spritesheet walkDustSprite = Resources.spritesheets().get(walkSprite);
    if (Game.time().since(this.lastWalkDust) < STEP_DELAY) {
      return;
    }

    this.lastWalkDust = Game.loop().getTicks();

    Point2D walkLocation = new Point2D.Double(event.getEntity().getCollisionBoxCenter().getX() - walkDustSprite.getSpriteWidth() / 2.0, event.getEntity().getCollisionBoxCenter().getY());
    Emitter walkDust = new Emitter(walkLocation, walkSprite);
    walkDust.setSize(walkDustSprite.getSpriteWidth(), walkDustSprite.getSpriteHeight());
    Game.world().environment().add(walkDust);
  }

}
