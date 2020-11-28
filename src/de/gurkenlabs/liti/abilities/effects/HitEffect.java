package de.gurkenlabs.liti.abilities.effects;

import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.entities.ICombatEntity;

public class HitEffect extends Effect {
  public HitEffect(Ability ability) {
    super(ability, EffectTarget.ENEMY);
  }

  @Override
  public void apply(final ICombatEntity affectedEntity) {
    if (affectedEntity.isIndestructible()) {
      return;
    }

    super.apply(affectedEntity);
    final int damage = this.getAbility().getAttributes().value().get();
    affectedEntity.hit(damage, this.getAbility());
  }
}
