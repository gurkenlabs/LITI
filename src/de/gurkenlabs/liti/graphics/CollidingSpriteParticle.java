package de.gurkenlabs.liti.graphics;

import com.litiengine.Game;
import com.litiengine.graphics.Spritesheet;
import com.litiengine.graphics.emitters.particles.SpriteParticle;
import de.gurkenlabs.liti.constants.Timings;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.entities.Players;

import java.awt.geom.Point2D;

public class CollidingSpriteParticle extends SpriteParticle {
  private final Player player;
  private final int damagePerTick;
  private long lastHit;

  public CollidingSpriteParticle(Spritesheet spritesheet, Player p, int damagePerTick) {
    super(spritesheet);
    this.player = p;
    this.damagePerTick = damagePerTick;
  }

  @Override
  public void update(Point2D emitterOrigin, float updateRatio) {
    super.update(emitterOrigin, updateRatio);
    Players.getAll().forEach(p -> {
      if (p == this.player || !p.getBoundingBox().intersects(this.getBoundingBox(emitterOrigin))
          || Game.time().since(lastHit) < Timings.DELAY_HIT_MUSHROOMPROJECTILE) {
        return;
      }
      p.hit(damagePerTick);
      lastHit = Game.time().now();
    });
  }
}
