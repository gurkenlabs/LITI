package de.gurkenlabs.liti.abilities;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.AbilityExecution;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.KeyFrame;
import de.gurkenlabs.litiengine.graphics.emitters.Emitter;
import de.gurkenlabs.litiengine.util.geom.GeometricUtilities;
import de.gurkenlabs.liti.constants.Animations;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.entities.StoneProjectile;
import de.gurkenlabs.liti.graphics.MushroomEmitter;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MushroomFrenzy extends SurvivalSkill {
  private static final List<Integer> spawnFrames = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9);

  /**
   * Initializes a new instance of the {@code Ability} class.
   *
   * @param executor The executing entity
   */
  public MushroomFrenzy(Player executor) {
    super(executor, 50);
    //    this.getPlayer().animations().get(Animations.GATHERER_MUSHROOMFRENZY_DOWN).onKeyFrameChanged((l, c) -> {
    //      this.spawnMushroomProjectiles(c);
    //    });
    //
    //    this.getPlayer().animations().get(Animations.GATHERER_MUSHROOMFRENZY_LEFT).onKeyFrameChanged((l, c) -> {
    //      this.spawnMushroomProjectiles(c);
    //    });
  }

  private void spawnMushroomProjectiles(KeyFrame frame) {
    if (frame.getSpriteIndex() != 4) {
      return;
    }

  }

  @Override
  public AbilityExecution cast() {
    if (!this.canCast()) {
      return super.cast();
    }
    MushroomEmitter mushrooms = new MushroomEmitter(this.getPlayer());
    Game.world().environment().add(mushrooms);
    //    this.getPlayer().setState(Player.PlayerState.LOCKED);
    //
    //    switch (this.getPlayer().getFacingDirection()) {
    //
    //    case UP:
    //      this.getPlayer().animations().play(Animations.GATHERER_MUSHROOMFRENZY_UP);
    //      break;
    //    case DOWN:
    //      this.getPlayer().animations().play(Animations.GATHERER_MUSHROOMFRENZY_DOWN);
    //      break;
    //    case LEFT:
    //      this.getPlayer().animations().play(Animations.GATHERER_MUSHROOMFRENZY_LEFT);
    //      break;
    //    case RIGHT:
    //      this.getPlayer().animations().play(Animations.GATHERER_MUSHROOMFRENZY_RIGHT);
    //      break;
    //
    //    default:
    //      break;
    //    }

    return super.cast();
  }
}
