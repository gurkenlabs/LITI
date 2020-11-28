package de.gurkenlabs.liti.abilities;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.entities.StoneProjectile;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.AbilityExecution;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.util.geom.GeometricUtilities;

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
  }

  @Override
  public AbilityExecution cast() {
    if (!this.canCast()) {
      return super.cast();
    }

    // TODO: anticipation - see servus bonus charge
    Point2D spawn = GeometricUtilities.getPointOnCircle(this.getPlayer().getCenter(), 15, this.getPlayer().getAngle());
    Game.world().environment().add(new StoneProjectile(this.getPlayer(), this.getPlayer().getAngle(), new Point2D.Double(spawn.getX() - 10, spawn.getY() - 20)));
    return super.cast();
  }
}
