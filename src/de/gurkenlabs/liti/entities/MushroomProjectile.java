package de.gurkenlabs.liti.entities;

import com.litiengine.Game;
import com.litiengine.IUpdateable;
import com.litiengine.entities.ICombatEntity;
import com.litiengine.graphics.RenderType;
import com.litiengine.graphics.animation.Animation;
import com.litiengine.graphics.animation.AnimationListener;
import com.litiengine.graphics.animation.EntityAnimationController;
import com.litiengine.graphics.animation.IEntityAnimationController;
import com.litiengine.graphics.emitters.Emitter;
import com.litiengine.resources.Resources;
import com.litiengine.util.geom.GeometricUtilities;
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
