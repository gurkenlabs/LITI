package de.gurkenlabs.liti.entities;

import de.gurkenlabs.liti.abilities.Bash;
import de.gurkenlabs.liti.abilities.Dash;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.GameLoop;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.attributes.RangeAttribute;
import de.gurkenlabs.litiengine.entities.Action;
import de.gurkenlabs.litiengine.entities.Creature;
import org.w3c.dom.ranges.Range;

public abstract class Player extends Creature implements IUpdateable {

  public enum PlayerState {
    LOCKED,
    NORMAL
  }

  private PlayerConfiguration configuration;

  private final RangeAttribute<Double> stamina;
  private int index;
  private PlayerState playerState;
  private Dash dash;
  private Bash bash;

  protected Player(PlayerConfiguration config) {
    this.stamina = new RangeAttribute<>(config.getPlayerClass().getStamina(), 0.0, 100.0);
    this.playerState = PlayerState.NORMAL;
    this.configuration = config;
    this.dash = new Dash(this);
    this.bash = new Bash(this);
  }

  @Override
  public void update() {
    this.recoverStamina();
  }

  public int getIndex() {
    return index;
  }

  public PlayerState getState() {
    return playerState;
  }

  public Dash getDash() {
    return dash;
  }

  public PlayerConfiguration getConfiguration() {
    return configuration;
  }

  public PlayerClass getPlayerClass() {
    return this.getConfiguration().getPlayerClass();
  }

  public RangeAttribute<Double> getStamina() {
    return stamina;
  }

  public void setIndex(int index) {
    this.index = index;
    this.setTeam(this.index);
  }

  public void setState(PlayerState playerState) {
    this.playerState = playerState;
  }

  public void setConfiguration(PlayerConfiguration configuration) {
    this.configuration = configuration;
  }

  @Action(name = "DASH")
  public void useDash() {
    this.dash.cast();
  }

  @Action(name = "BASH")
  public void useBash() {
    this.bash.cast();
  }

  @Override
  public String toString() {
    return "Player " + (this.getConfiguration().getIndex() + 1) + " (#" + this.getMapId() + ")";
  }

  private void recoverStamina() {
    if (this.stamina.get() < this.stamina.getMax()) {
      double recovery = Math.min(Game.loop().getDeltaTime(), GameLoop.TICK_DELTATIME_LAG) * 0.02F * Game.loop().getTimeScale();
      if (this.stamina.get() + recovery > this.stamina.getMax()) {
        this.stamina.setToMax();
      } else {
        this.stamina.setBaseValue(this.stamina.get() + recovery);
      }
    }
  }
}
