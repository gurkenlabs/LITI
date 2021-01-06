package de.gurkenlabs.liti.graphics;

import java.awt.geom.Point2D;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.EntityMovedEvent;
import de.gurkenlabs.litiengine.entities.IMobileEntity.EntityMovedListener;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.emitters.Emitter;
import de.gurkenlabs.litiengine.resources.Resources;

public class WalkDustSpawner implements EntityMovedListener {
  private long lastWalkDust;

  @Override
  public void moved(EntityMovedEvent event) {
    final Player player = (Player)event.getEntity();
    if (event.getEntity().movement().getVelocity() <= .5 && !player.isDashing()) {
      return;
    }

    final int STEP_DELAY = player.isDashing() ? 100 : 360;
    if (event.getDeltaX() == 0 && event.getDeltaY() == 0 || Game.world().environment() == null) {
      return;
    }
    Spritesheet walkDustSprite = Resources.spritesheets().get("walk-dust");
    if (Game.time().since(this.lastWalkDust) < STEP_DELAY) {
      return;
    }

    this.lastWalkDust = Game.loop().getTicks();

    Point2D walkLocation = new Point2D.Double(event.getEntity().getCollisionBoxCenter().getX() - walkDustSprite.getSpriteWidth() / 2.0, event.getEntity().getCollisionBoxCenter().getY());
    Emitter walkDust = new Emitter(walkLocation, "walk-dust");
    walkDust.setSize(walkDustSprite.getSpriteWidth(), walkDustSprite.getSpriteHeight());
    Game.world().environment().add(walkDust);
  }

}
