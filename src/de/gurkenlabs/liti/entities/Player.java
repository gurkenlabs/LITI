package de.gurkenlabs.liti.entities;

import de.gurkenlabs.liti.abilities.Dash;
import de.gurkenlabs.litiengine.entities.Action;
import de.gurkenlabs.litiengine.entities.Creature;

public abstract class Player extends Creature {

  public enum PlayerState {
    LOCKED,
    NORMAL
  }

  private PlayerConfiguration configuration;

  private int index;
  private PlayerState playerState;
  private Dash dash;

  protected Player(PlayerConfiguration config) {
    this.playerState = PlayerState.NORMAL;
    this.configuration = config;
    this.dash = new Dash(this);
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


  public void setIndex(int index) {
    this.index = index;
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

  @Override
  public String toString() {
    return "Player " + (this.getConfiguration().getIndex() + 1) + " (#" + this.getMapId() + ")";
  }
}
