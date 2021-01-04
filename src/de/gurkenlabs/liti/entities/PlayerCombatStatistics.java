package de.gurkenlabs.liti.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.EntityHitEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class PlayerCombatStatistics {
  private static final int RECENT_DAMAGE_PERIOD = 700;
  private static final int RECENT_KILL_PERIOD = 10000;

  private final List<EntityHitEvent> entityHits;
  private final List<Long> killTimes;

  private final Player player;
  private int damageDealt;
  private int damageReceived;
  private int damageBlocked;
  private int deaths;
  private int kills;

  public PlayerCombatStatistics(Player player) {
    this.player = player;
    this.entityHits = new CopyOnWriteArrayList<>();
    this.killTimes = new CopyOnWriteArrayList<>();

    player.onHit(this::handlePlayerHit);
    player.onDeath(e -> this.deaths++);
  }

  public void reset() {
    this.entityHits.clear();
    this.killTimes.clear();
    this.damageDealt = 0;
    this.damageReceived = 0;
    this.damageBlocked = 0;
    this.deaths = 0;
    this.kills = 0;
  }

  public int getRecentDamageReceived() {
    int damage = 0;
    for (int i = this.entityHits.size() - 1; i > 0; i--) {
      final EntityHitEvent event = entityHits.get(i);
      if (Game.time().since(event.getTime()) > RECENT_DAMAGE_PERIOD) {
        return damage;
      }

      damage += event.getDamage();
    }

    return damage;
  }

  public int getRecentKills(){
    int kills = 0;
    for (int i = this.killTimes.size() - 1; i > 0; i--) {
      if (Game.time().since(this.killTimes.get(i)) > RECENT_KILL_PERIOD) {
        return kills;
      }

      kills++;
    }

    return kills;
  }

  public int getDamageDealt() {
    return damageDealt;
  }

  public int getDamageReceived() {
    return damageReceived;
  }

  public int getDamageBlocked() {
    return damageBlocked;
  }

  public int getKills() {
    return kills;
  }

  public int getDeaths() {
    return deaths;
  }

  public void addDamageDealt(int damage) {
    this.damageDealt += damage;
  }

  public void addDamageBlocked(int damage){
    this.damageBlocked += damage;
  }

  private void trackKill() {
    this.kills++;
    this.killTimes.add(Game.time().now());

    System.out.println(this.player + ": kills: " + this.getKills() + ", deaths: " + this.getDeaths() + ", damage: " + this.getDamageDealt() + ", received: " + this.getDamageReceived() + ", blocked: " + this.getDamageBlocked());
  }

  private void handlePlayerHit(EntityHitEvent entityHitEvent) {
    entityHits.add(entityHitEvent);
    this.damageReceived += entityHitEvent.getDamage();

    if (entityHitEvent.getExecutor() instanceof Player) {
      Player executor = (Player) entityHitEvent.getExecutor();
      executor.getCombatStatistics().addDamageDealt(entityHitEvent.getDamage());

      if (entityHitEvent.wasKilled()) {
        executor.getCombatStatistics().trackKill();
      }
    }
  }
}
