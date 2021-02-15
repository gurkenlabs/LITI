package de.gurkenlabs.liti.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;

import de.gurkenlabs.liti.constants.LitiFonts;
import de.gurkenlabs.litiengine.entities.EmitterInfo;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.graphics.emitters.Emitter;
import de.gurkenlabs.litiengine.graphics.emitters.particles.Particle;
import de.gurkenlabs.litiengine.graphics.emitters.particles.ParticleType;
import de.gurkenlabs.litiengine.graphics.emitters.particles.TextParticle;
import de.gurkenlabs.litiengine.graphics.emitters.xml.ParticleParameter;

@EmitterInfo(maxParticles = 1, spawnAmount = 1, duration = 1200, particleType = ParticleType.TEXT)
@EntityInfo(renderType = RenderType.OVERLAY)
public class FloatingCombatTextEmitter extends Emitter {
  public final Font font;
  public float dy = -1.0f;
  public float dyInc = -0.025f;

  public FloatingCombatTextEmitter(final String text, final Point2D origin, final Color color) {
    this(text, origin, color, LitiFonts.NUMBERS.deriveFont(5f));
  }

  public FloatingCombatTextEmitter(final String text, final Point2D origin, final Color color, final Font font) {
    super(origin);
    this.data().setText(text);
    this.data().setColor(color);
    this.data().setVelocityY(new ParticleParameter(dy));
    this.data().setAccelerationY(new ParticleParameter(dyInc));
    this.setRenderType(RenderType.OVERLAY);
    this.font = font;
  }

  @Override
  protected Particle createNewParticle() {
    Particle particle = super.createNewParticle();
    particle.setAntiAliasing(true);
    ((TextParticle) particle).setFont(this.font);
    return particle;
  }
}
