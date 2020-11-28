package de.gurkenlabs.liti.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.physics.Collision;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Projectile extends Creature {
  private final List<Rectangle2D> intersecting;
  private final Point2D origin;

  protected Projectile(double angle, Point2D origin) {
    this.intersecting = new CopyOnWriteArrayList<>();
    this.setAngle((float) angle);
    this.origin = origin;
    this.setLocation(this.origin);

    // add all static collision boxes that are intersected at the time this projectile spawns
    // because hitting this collision box will not destroy the projectile
    for (final Rectangle2D rect : Game.physics().getCollisionBoxes(Collision.STATIC)) {
      if (this.getCollisionBox().intersects(rect)) {
        this.intersecting.add(rect);
      }
    }
  }

  public Point2D getOrigin() {
    return this.origin;
  }

  protected List<Rectangle2D> getIntersectingCollisionBoxes() {
    return this.intersecting;
  }
}
