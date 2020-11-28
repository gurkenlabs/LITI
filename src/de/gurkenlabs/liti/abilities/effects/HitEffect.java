package de.gurkenlabs.liti.abilities.effects;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.attributes.Modification;
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
    final int damage = this.getAbility().getAttributes().value().get();
    super.apply(affectedEntity);

    if (affectedEntity instanceof Player) {
      Player player = (Player) affectedEntity;
      if (player.isBlocking()) {
        player.getStamina().modify(Modification.SUBTRACT, Math.sqrt(damage));

        if (player.getStamina().get() > 0) {
          return;
        }
      }
    }

    affectedEntity.hit(damage, this.getAbility());
  }
}
