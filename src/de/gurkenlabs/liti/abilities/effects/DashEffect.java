package de.gurkenlabs.liti.abilities.effects;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.effects.AbilityEffect;
import de.gurkenlabs.litiengine.abilities.effects.EffectApplication;
import de.gurkenlabs.litiengine.abilities.targeting.TargetingStrategy;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import de.gurkenlabs.litiengine.entities.IMobileEntity;

public class DashEffect extends AbilityEffect {
  private double angle;


  public DashEffect(final Ability ability) {
    super(TargetingStrategy.executingEntity(), ability);
  }

  @Override
  public void update() {
    final long deltaTime = Game.loop().getDeltaTime();
    final double maxPixelsPerTick = this.getAbility().getAttributes().value().getValue() / 1000.0 * Math.min(deltaTime, 50);

    Game.physics().move((IMobileEntity) getExecutingEntity(), this.angle, maxPixelsPerTick);
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
    Player player = (Player) getExecutingEntity();
    player.setState(Player.PlayerState.NORMAL);
    player.setDashing(false);
    super.cease(appliance);
  }
}
