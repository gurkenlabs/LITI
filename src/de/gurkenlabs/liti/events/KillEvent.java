package de.gurkenlabs.liti.events;

import de.gurkenlabs.liti.abilities.Bash;
import de.gurkenlabs.liti.abilities.ForceOfNature;
import de.gurkenlabs.liti.abilities.MushroomFrenzy;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.abilities.Ability;

public class KillEvent extends GameEvent {
  private final Player killer;
  private final Ability ability;
  private final Player killed;

  public KillEvent(Player killer, Ability ability, Player killed) {
    super();
    this.killer = killer;
    this.ability = ability;
    this.killed = killed;
  }

  public Player getKiller() {
    return killer;
  }

  public Player getKilled() {
    return killed;
  }

  @Override public String getMessage() {
    int killerIndex = killer.getConfiguration().getIndex();
    int killedIndex = killed.getConfiguration().getIndex();
    String killerClass = killer.getPlayerClass().name();
    String killedClass = killed.getPlayerClass().name();
    return String.format(getMessageFormat(), killerIndex, killerClass, getKillAbilityIcon(), killedIndex, killedClass);
  }

  private String getKillAbilityIcon() {
    if (ability instanceof ForceOfNature) {
      return "\ue801";
    } else if (ability instanceof MushroomFrenzy) {
      return "\ue804";
    } else if (ability instanceof Bash) {
      switch (killer.getPlayerClass()) {
      case WARRIOR:
        return "\ue800";
      case SHAMAN:
        return "\ue806";
      case HUNTRESS:
        return "\ue805";
      case GATHERER:
        return "\ue809";
      }
    }
    return "has killed";
  }
}
