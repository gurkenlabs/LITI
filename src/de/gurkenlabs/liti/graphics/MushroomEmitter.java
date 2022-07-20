package de.gurkenlabs.liti.graphics;

import de.gurkenlabs.liti.constants.Timings;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.EmitterInfo;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.emitters.Emitter;
import de.gurkenlabs.litiengine.graphics.emitters.particles.Particle;
import de.gurkenlabs.litiengine.physics.Collision;
import de.gurkenlabs.litiengine.resources.Resources;

@EmitterInfo(duration = 200, spawnRate = 200, spawnAmount = 10)
public class MushroomEmitter extends Emitter {

  private Player player;

  public MushroomEmitter(Player player) {
    super(player.getCenter(), "mushrooms");
    this.player = player;
    this.setRenderType(RenderType.OVERLAY);
    this.setX(this.getX() - this.getWidth() / 2d);
    this.setY(this.getY() - this.getHeight() / 2d);
    this.data().setCollision(Collision.STATIC);
    this.data().setEmitterDuration(Timings.DURATION_MUSHROOMFRENZY);
  }

  public Player getPlayer() {
    return player;
  }

  @Override protected Particle createNewParticle() {
    Spritesheet sprite = Resources.spritesheets().get(this.data().getSpritesheet());
    if (sprite == null || sprite.getTotalNumberOfSprites() <= 0) {
      return null;
    }
    CollidingSpriteParticle sp = new CollidingSpriteParticle(sprite, this.getPlayer(),
        (int) Math.round(this.getPlayer().traits().damage().get() / 6d));
    sp.setAnimateSprite(this.data().isAnimatingSprite());
    sp.setLoopSprite(this.data().isLoopingSprite());
    sp.init(this.data());
    sp.setVelocityX(Game.random().nextSign());
    sp.setVelocityY(Game.random().nextSign());
    return sp;
  }
}
