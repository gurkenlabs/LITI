package de.gurkenlabs.liti.abilities.effects;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.attributes.Modification;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import de.gurkenlabs.litiengine.entities.Prop;
import de.gurkenlabs.litiengine.graphics.OverlayPixelsImageEffect;

import java.awt.*;

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
        player.traits().stamina().modifyBaseValue(Modification.SUBTRACT, damage);

        if (player.traits().stamina().get() > 0) {
          player.getCombatStatistics().addDamageBlocked(damage);
          return;
        }
      }
    }

    affectedEntity.hit(damage, this.getAbility());

    if (!affectedEntity.isDead()) {
      affectedEntity.animations().add(new OverlayPixelsImageEffect(120, new Color(255, 255, 255, 200)));
      Game.loop().perform(130, () -> affectedEntity.animations().add(new OverlayPixelsImageEffect(120, new Color(167, 16, 16, 170))));
    }
  }

  @Override
  protected boolean canAttackEntity(ICombatEntity entity) {
    // cannot destroy props in LITI
    return !(entity instanceof Prop) && super.canAttackEntity(entity);
  }
}
