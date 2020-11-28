package de.gurkenlabs.liti.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.entities.*;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.AnimationListener;
import de.gurkenlabs.litiengine.graphics.animation.EntityAnimationController;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.util.geom.GeometricUtilities;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@EntityInfo(width = 21, height = 41)
@AnimationInfo(spritePrefix = "projectile-stone")
@CollisionInfo(collision = false, valign = Valign.TOP, collisionBoxWidth = 16, collisionBoxHeight = 16)
@MovementInfo(velocity = 150)
public class StoneProjectile extends Projectile implements IUpdateable {
  private List<ICombatEntity> hitEntities = new CopyOnWriteArrayList<>();
  private final Player executor;

  public StoneProjectile(Player executor, double angle, Point2D origin) {
    super(angle, origin);
    this.executor = executor;
    this.setRenderType(RenderType.OVERLAY);

    this.setController(IEntityAnimationController.class, new EntityAnimationController<>(this, Resources.spritesheets().get("projectile-stone")));
    this.animations().addListener(new AnimationListener() {
      @Override
      public void played(Animation animation) {
        if (animation.getSpritesheet().getName().equals("projectile-stone")) {
          animation.setLooping(false);
          animation.onKeyFrameChanged((previous, current) -> {
            if (current.getSpriteIndex() == 0) {
              StoneProjectile.this.setCollisionBoxValign(Valign.TOP);
              return;
            }
            if (current.getSpriteIndex() == 3) {
              StoneProjectile.this.setCollisionBoxValign(Valign.MIDDLE_TOP);
              return;
            }

            if (current.getSpriteIndex() == 4) {
              StoneProjectile.this.setCollisionBoxValign(Valign.MIDDLE);
            }
            if (current.getSpriteIndex() == 5) {
              StoneProjectile.this.setCollisionBoxValign(Valign.MIDDLE_DOWN);
            }
          });
        }
      }

      @Override
      public void finished(Animation animation) {
        StoneProjectile.this.die();
        Game.world().environment().remove(StoneProjectile.this);
        Game.world().camera().shake(1, 0, 700);
        // TODO: DO SOME DPS
        // TODO: SPAWN EMITTER
      }
    });
  }

  @Override
  public void update() {
    final double velocity = this.getTickVelocity();
    this.setLocation(GeometricUtilities.project(this.getLocation(), this.getAngle(), velocity));
    for (ICombatEntity entity : Game.world().environment().findCombatEntities(this.getCollisionBox(), e -> (e instanceof Player) && !e.equals(this.executor) && !this.hitEntities.contains(e))) {
      entity.hit(PlayerClass.WARRIOR.getDamage() * 3);
      this.hitEntities.add(entity);
    }
  }
}
