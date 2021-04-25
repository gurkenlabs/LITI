package de.gurkenlabs.liti.events;

import de.gurkenlabs.liti.abilities.Bash;
import de.gurkenlabs.liti.abilities.ForceOfNature;
import de.gurkenlabs.liti.abilities.MushroomFrenzy;
import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.constants.LitiIcons;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.Align;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.gui.FontIcon;

import java.awt.Color;

public class KillEvent extends GameEvent {
  private final Player killer;
  private final Ability ability;
  private final Player killed;

  public KillEvent(Player killer, Ability ability, Player killed) {
    super(GameEventType.KILL);
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

  @Override
  public String getMessage() {
    int killerIndex = killer.getConfiguration().getIndex() + 1;
    int killedIndex = killed.getConfiguration().getIndex() + 1;
    String killerClass = killer.getPlayerClass().name().substring(0, 1) + killer.getPlayerClass().name().substring(1).toLowerCase();
    String killedClass = killed.getPlayerClass().name().substring(0, 1) + killed.getPlayerClass().name().substring(1).toLowerCase();
    return String.format(getMessageFormat(), killerIndex, killedIndex);
  }

  @Override
  public FontIcon getIcon() {
    if (ability instanceof ForceOfNature) {
      return LitiIcons.ABILITY_FORCEOFNATURE;
    } else if (ability instanceof MushroomFrenzy) {
      return LitiIcons.ABILITY_MUSHROOMFRENZY;
    } else if (ability instanceof Bash) {
      switch (killer.getPlayerClass()) {
      case WARRIOR:
        return LitiIcons.ABILITY_BASH_WARRIOR;
      case SHAMAN:
        return LitiIcons.ABILITY_BASH_SHAMAN;
      case HUNTRESS:
        return LitiIcons.ABILITY_BASH_HUNTRESS;
      case GATHERER:
        return LitiIcons.ABILITY_BASH_GATHERER;
      }
    }
    return null;
  }

  @Override
  public Align getTextAlign() {
    return Align.CENTER;
  }

  @Override
  public Align getIconAlign() {
    return Align.CENTER;
  }
}
