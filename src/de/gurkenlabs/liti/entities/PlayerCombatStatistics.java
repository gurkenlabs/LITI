package de.gurkenlabs.liti.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.EntityHitEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class PlayerCombatStatistics {
  private static final int RECENT_DAMAGE_PERIOD = 700;

  private final List<EntityHitEvent> entityHits;

  public PlayerCombatStatistics(Player player) {
    this.entityHits = new CopyOnWriteArrayList<>();

    player.onHit(this::handlePlayerHit);
  }

  public int getRecentDamage() {
    int damage = 0;
    for (int i = entityHits.size() - 1; i > 0; i--) {
      final EntityHitEvent event = entityHits.get(i);
      if (Game.time().since(event.getTime()) > RECENT_DAMAGE_PERIOD) {
        return damage;
      }

      damage += event.getDamage();
    }

    return damage;
  }

  private void handlePlayerHit(EntityHitEvent entityHitEvent) {
    entityHits.add(entityHitEvent);
  }
}
