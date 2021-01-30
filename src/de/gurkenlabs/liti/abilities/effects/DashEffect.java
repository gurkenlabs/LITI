package de.gurkenlabs.liti.abilities.effects;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectApplication;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.entities.EntityMovedEvent;
import de.gurkenlabs.litiengine.entities.ICombatEntity;

import java.awt.geom.Point2D;

public class DashEffect extends Effect {
  private double angle;
  
  public DashEffect(final Ability ability) {
    super(ability, EffectTarget.EXECUTINGENTITY);
  }
  
  @Override
  public void update() {
    final long deltaTime = Game.loop().getDeltaTime();
    final double maxPixelsPerTick = this.getAbility().getAttributes().value().get() / 1000.0 * Math.min(deltaTime, 50);

    Game.physics().move(this.getAbility().getExecutor(), this.angle, maxPixelsPerTick);
    super.update();
  }
  
  @Override
  protected void apply(final ICombatEntity entity) {
    
    Player player = (Player) entity;
    player.setDashing(true);
    if (player.movement().getVelocity() > 0) {
      this.angle = player.movement().getMoveAngle();
    } else {
      this.angle = entity.getAngle();
    }
    
    player.setState(Player.PlayerState.LOCKED);
    super.apply(entity);
  }
  
  @Override
  protected void cease(EffectApplication appliance) {
    Player player = (Player) this.getAbility().getExecutor();
    player.setState(Player.PlayerState.NORMAL);
    player.setDashing(false);
    super.cease(appliance);
  }
}
