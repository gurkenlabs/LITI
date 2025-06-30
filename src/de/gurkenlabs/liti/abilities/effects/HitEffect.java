package de.gurkenlabs.liti.abilities.effects;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.effects.AbilityEffect;
import de.gurkenlabs.litiengine.abilities.targeting.TargetingStrategy;
import de.gurkenlabs.litiengine.attributes.Modification;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import de.gurkenlabs.litiengine.graphics.OverlayPixelsImageEffect;
import java.awt.Color;

public class HitEffect extends AbilityEffect {
  public HitEffect(Ability ability) {
    super(TargetingStrategy.enemies(), ability);
  }

  @Override public void apply(final ICombatEntity affectedEntity) {
    if (affectedEntity.isIndestructible()) {
      return;
    }
    final int damage = getAbility().getAttributes().value().getValue();
    super.apply(affectedEntity);

    if (affectedEntity instanceof Player player && player.isBlocking()) {
      player.traits().stamina().modify(Modification.SUBTRACT, damage);
      if (player.traits().stamina().getValue() > 0) {
        player.getCombatStatistics().addDamageBlocked(damage);
        return;
      }
    }

    affectedEntity.hit(damage, getAbility());

    if (!affectedEntity.isDead()) {
      affectedEntity.animations().add(new OverlayPixelsImageEffect(120, new Color(255, 255, 255, 200)));
      Game.loop().perform(130, () -> affectedEntity.animations().add(new OverlayPixelsImageEffect(120, new Color(167, 16, 16, 170))));
    }
  }
}
