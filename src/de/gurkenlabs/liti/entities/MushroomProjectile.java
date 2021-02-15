package de.gurkenlabs.liti.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.AnimationListener;
import de.gurkenlabs.litiengine.graphics.animation.EntityAnimationController;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.graphics.emitters.Emitter;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.util.geom.GeometricUtilities;
import de.gurkenlabs.liti.abilities.Proficiency;
import de.gurkenlabs.liti.abilities.Trait;
import de.gurkenlabs.liti.constants.Animations;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MushroomProjectile extends Projectile implements IUpdateable {
  private final List<ICombatEntity> hitEntities = new CopyOnWriteArrayList<>();
  private final Player executor;

  private final double velocity;

  protected MushroomProjectile(Player executor, double angle, Point2D origin) {
    super(angle, origin);
    this.executor = executor;
    this.setRenderType(RenderType.OVERLAY);
    this.velocity = (1000d / Game.loop().getTickRate()) * 0.001f * this.getVelocity().get();
    this.setController(IEntityAnimationController.class,
        new EntityAnimationController<>(this, Resources.spritesheets().get(Animations.PROJECTILE_MUSHROOM)));
    this.animations().addListener(new AnimationListener() {
      @Override
      public void played(Animation animation) {
        if (animation.getSpritesheet().getName().equals(Animations.PROJECTILE_MUSHROOM)) {
          animation.setLooping(false);
        }
      }

      @Override
      public void finished(Animation animation) {
        MushroomProjectile.this.die();
        Game.world().environment().remove(MushroomProjectile.this);

        final int impactSize = 60;
        Point2D impactCenter = MushroomProjectile.this.getCollisionBoxCenter();
        Emitter cloudEmitter = new Emitter(impactCenter.getX() - impactSize / 2d, impactCenter.getY() - impactSize / 2d, "poisonCloud");
        cloudEmitter.setRenderType(RenderType.OVERLAY);
        Game.world().environment().add(cloudEmitter);
      }
    });
  }

  @Override
  public void update() {
    final double vel = this.velocity * Game.loop().getTimeScale();
    this.setLocation(GeometricUtilities.project(this.getLocation(), this.getAngle(), vel));
    for (ICombatEntity entity : Game.world().environment().findCombatEntities(this.getCollisionBox(),
        e -> (e instanceof Player) && !e.equals(this.executor) && !this.hitEntities.contains(e))) {
      entity.hit((int) (Proficiency.get(MushroomProjectile.this.executor.getPlayerClass(), Trait.DAMAGE) * 2));
      this.hitEntities.add(entity);
    }
  }
}
