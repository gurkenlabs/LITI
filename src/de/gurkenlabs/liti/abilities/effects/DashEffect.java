package de.gurkenlabs.liti.abilities.effects;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectApplication;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.entities.ICombatEntity;

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
    this.angle = entity.getAngle();

    Player character = (Player) entity;
    character.setState(Player.PlayerState.LOCKED);
    super.apply(entity);
  }

  @Override
  protected void cease(EffectApplication appliance) {
    Player character = (Player) this.getAbility().getExecutor();
    character.setState(Player.PlayerState.NORMAL);
    super.cease(appliance);
    super.cease(appliance);
  }
}
