package de.gurkenlabs.liti.entities;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.gurkenlabs.liti.abilities.Proficiency;
import de.gurkenlabs.liti.abilities.Trait;
import com.litiengine.Game;
import com.litiengine.GameLoop;
import com.litiengine.IUpdateable;
import com.litiengine.Valign;
import com.litiengine.entities.AnimationInfo;
import com.litiengine.entities.CollisionInfo;
import com.litiengine.entities.EntityInfo;
import com.litiengine.entities.ICombatEntity;
import com.litiengine.entities.MovementInfo;
import com.litiengine.graphics.RenderType;
import com.litiengine.graphics.animation.Animation;
import com.litiengine.graphics.animation.AnimationListener;
import com.litiengine.graphics.animation.EntityAnimationController;
import com.litiengine.graphics.animation.IEntityAnimationController;
import com.litiengine.graphics.emitters.Emitter;
import com.litiengine.resources.Resources;
import com.litiengine.util.geom.GeometricUtilities;

@EntityInfo(width = 21, height = 41)
@AnimationInfo(spritePrefix = "projectile-stone")
@CollisionInfo(collision = false, valign = Valign.TOP, collisionBoxWidth = 16, collisionBoxHeight = 16)
@MovementInfo(velocity = 150)
public class StoneProjectile extends Projectile implements IUpdateable {
  private List<ICombatEntity> hitEntities = new CopyOnWriteArrayList<>();
  private final Player executor;

  private final Point2D origin;
  private final double velocity;

  public StoneProjectile(Player executor, double angle, Point2D origin) {
    super(angle, origin);
    this.executor = executor;
    this.origin = origin;
    this.setRenderType(RenderType.OVERLAY);
    this.velocity = 1000 / Game.loop().getTickRate() * 0.001f * this.getVelocity().get();

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

        final int impactSize = 60;
        Point2D impactCenter = new Point2D.Double(StoneProjectile.this.getCollisionBoxCenter().getX(),
            StoneProjectile.this.getCollisionBoxCenter().getY());
        Emitter stoneParticles = new Emitter(impactCenter.getX() - 20, impactCenter.getY() - 20, "stone-particle");
        Emitter crackEmitter = new Emitter(impactCenter.getX() - 20, impactCenter.getY() - 20, "crack");
        crackEmitter.setRenderType(RenderType.GROUND);
        Game.world().environment().add(stoneParticles);
        Game.world().environment().add(crackEmitter);
        Game.loop().perform(300, () -> {
          for (ICombatEntity entity : Game.world().environment().findCombatEntities(
              new Ellipse2D.Double(impactCenter.getX() - impactSize / 2.0, impactCenter.getY() - impactSize / 2.0, impactSize, impactSize),
              e -> (e instanceof Player) && !e.equals(StoneProjectile.this.executor) && !StoneProjectile.this.hitEntities.contains(e))) {
            entity.hit((int) (Proficiency.get(PlayerClass.WARRIOR, Trait.DAMAGE) * 2));
            StoneProjectile.this.hitEntities.add(entity);
          }
        });
      }
    });
  }

  @Override
  public void update() {
    final double vel = this.velocity * Game.loop().getTimeScale();
    this.setLocation(GeometricUtilities.project(this.getLocation(), this.getAngle(), vel));
    for (ICombatEntity entity : Game.world().environment().findCombatEntities(this.getCollisionBox(),
        e -> (e instanceof Player) && !e.equals(this.executor) && !this.hitEntities.contains(e))) {
      entity.hit((int) (Proficiency.get(PlayerClass.WARRIOR, Trait.DAMAGE) * 2));
      this.hitEntities.add(entity);
    }
  }
}
