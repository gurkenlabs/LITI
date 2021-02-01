package de.gurkenlabs.liti.abilities;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.entities.StoneProjectile;
import de.gurkenlabs.liti.entities.Warrior;
import com.litiengine.Direction;
import com.litiengine.Game;
import com.litiengine.abilities.AbilityExecution;
import com.litiengine.graphics.animation.Animation;
import com.litiengine.graphics.animation.KeyFrame;
import com.litiengine.util.geom.GeometricUtilities;

import java.awt.geom.Point2D;

public class ForceOfNature extends SurvivalSkill {

  /**
   * Initializes a new instance of the {@code Ability} class.
   *
   * @param executor
   *         The executing entity
   */
  public ForceOfNature(Player executor) {
    super(executor, 60);

    Animation throwRock = this.getPlayer().animations().get(Warrior.ANIM_THROW_ROCK);
    throwRock.onKeyFrameChanged((l, c) -> {
      this.spawnStoneProjectile(c);
    });

    Animation throwRockLeft = this.getPlayer().animations().get(Warrior.ANIM_THROW_ROCK_LEFT);
    throwRockLeft.onKeyFrameChanged((l, c) -> {
      this.spawnStoneProjectile(c);
    });
  }

  private void spawnStoneProjectile(KeyFrame frame) {
    if (frame.getSpriteIndex() == 4) {
      this.getPlayer().setState(Player.PlayerState.NORMAL);
      // TODO: anticipation - see servus bonus charge
      Point2D spawn = GeometricUtilities.getPointOnCircle(this.getPlayer().getCenter(), 25, this.getPlayer().getAngle() - 90);
      double y = this.getPlayer().getFacingDirection() == Direction.UP ? spawn.getY() - 41 : spawn.getY() - 20;
      Game.world().environment().add(new StoneProjectile(this.getPlayer(), this.getPlayer().getAngle(), new Point2D.Double(spawn.getX() - 10, y)));
    }
  }

  @Override
  public AbilityExecution cast() {
    if (!this.canCast()) {
      return super.cast();
    }

    this.getPlayer().setState(Player.PlayerState.LOCKED);

    double angle = this.getPlayer().movement().getVelocity() > 0 ? this.getPlayer().movement().getMoveAngle() : this.getPlayer().getAngle();
    if (angle > 180) {
      this.getPlayer().animations().play(Warrior.ANIM_THROW_ROCK_LEFT);
    } else {
      this.getPlayer().animations().play(Warrior.ANIM_THROW_ROCK);
    }

    return super.cast();
  }
}
