package de.gurkenlabs.liti.abilities;

import com.litiengine.Direction;
import com.litiengine.Game;
import com.litiengine.abilities.AbilityExecution;
import com.litiengine.graphics.animation.Animation;
import com.litiengine.graphics.animation.KeyFrame;
import com.litiengine.util.geom.GeometricUtilities;
import de.gurkenlabs.liti.constants.Animations;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.entities.StoneProjectile;

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
  protected MushroomFrenzy(Player executor) {
    super(executor, 50);
    this.getPlayer().animations().get(Animations.GATHERER_MUSHROOMFRENZY_DOWN).onKeyFrameChanged((l, c) -> {
      this.spawnMushroomProjectiles(c);
    });

    this.getPlayer().animations().get(Animations.GATHERER_MUSHROOMFRENZY_LEFT).onKeyFrameChanged((l, c) -> {
      this.spawnMushroomProjectiles(c);
    });
  }

  private void spawnMushroomProjectiles(KeyFrame frame) {
    if (!spawnFrames.contains(frame.getSpriteIndex())) {
      return;
    }
    this.getPlayer().setAngle(this.getPlayer().getAngle() + 45);
    // TODO: anticipation: show the directions / radius on the ground
    Point2D spawn = GeometricUtilities.getPointOnCircle(this.getPlayer().getCenter(), 8, this.getPlayer().getAngle());
    double y = this.getPlayer().getFacingDirection() == Direction.UP ? spawn.getY() - 41 : spawn.getY() - 20;
    Game.world().environment().add(new StoneProjectile(this.getPlayer(), this.getPlayer().getAngle(), new Point2D.Double(spawn.getX() - 10, y)));

  }

  @Override
  public AbilityExecution cast() {
    if (!this.canCast()) {
      return super.cast();
    }

    this.getPlayer().setState(Player.PlayerState.LOCKED);

    switch (this.getPlayer().getFacingDirection()) {

    case UP:
      this.getPlayer().animations().play(Animations.GATHERER_MUSHROOMFRENZY_UP);
      break;
    case DOWN:
      this.getPlayer().animations().play(Animations.GATHERER_MUSHROOMFRENZY_DOWN);
      break;
    case LEFT:
      this.getPlayer().animations().play(Animations.GATHERER_MUSHROOMFRENZY_LEFT);
      break;
    case RIGHT:
      this.getPlayer().animations().play(Animations.GATHERER_MUSHROOMFRENZY_RIGHT);
      break;

    default:
      break;
    }

    return super.cast();
  }
}
