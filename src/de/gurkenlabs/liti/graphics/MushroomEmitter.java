package de.gurkenlabs.liti.graphics;

import com.litiengine.entities.IEntity;
import com.litiengine.graphics.RenderType;
import com.litiengine.graphics.emitters.EntityEmitter;
import com.litiengine.physics.Collision;
import de.gurkenlabs.liti.constants.Timings;

public class MushroomEmitter extends EntityEmitter {
  public MushroomEmitter(IEntity player) {
    super(player, "mushrooms", true);
    this.setRenderType(RenderType.OVERLAY);
    this.data().setCollisionType(Collision.STATIC);
    this.data().setEmitterDuration(Timings.DURATION_MUSHROOMFRENZY);
  }
}
