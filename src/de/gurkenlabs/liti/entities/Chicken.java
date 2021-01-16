package de.gurkenlabs.liti.entities;

import de.gurkenlabs.liti.abilities.Proficiency;
import de.gurkenlabs.liti.abilities.Trait;
import de.gurkenlabs.liti.entities.controllers.ChickenMovementController;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.*;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.physics.IMovementController;

@EntityInfo(width = 14, height = 11)
@AnimationInfo(spritePrefix = "rooster")
@MovementInfo(velocity = 10)
public class Chicken extends Creature implements IUpdateable {
  private static final int PICKUP_DURATION = 1000;
  private Player carryingPlayer;
  private Player pickupStarted;
  private long pickupStart;

  public Chicken() {
    this.setIndestructible(true);
  }

  @Override
  public boolean canCollideWith(final ICollisionEntity otherEntity) {
    return !((otherEntity instanceof Player));
  }

  @Override
  public void update() {
    if (this.pickupStarted != null && this.pickupStart != 0 && Game.time().since(this.pickupStart) > PICKUP_DURATION) {
      this.pickup(this.pickupStarted);
    }
  }

  public Player getCarryingPlayer() {
    return this.carryingPlayer;
  }

  public boolean startPickup(Player player) {
    if (this.pickupStarted != null) {
      return false;
    }

    this.pickupStarted = player;
    player.setState(Player.PlayerState.LOCKED);
    player.setCurrentChicken(this);
    this.pickupStart = Game.time().now();

    // TODO: if player receives damage cancel the pickup
    // TODO: UI for pickup delay
    return true;
  }

  public void stopPickup() {
    if (this.pickupStarted != null) {
      this.pickupStarted.setState(Player.PlayerState.NORMAL);
    }
    this.pickupStarted = null;
    this.pickupStart = 0;
  }

  public void drop() {
    if (this.carryingPlayer != null) {
      this.carryingPlayer.setVelocity((float) Proficiency.get(this.carryingPlayer.getPlayerClass(), Trait.MOBILITY));
      this.carryingPlayer.setCurrentChicken(null);
    }

    this.carryingPlayer = null;
    this.setRenderType(RenderType.NORMAL);
    this.setCollision(true);
  }

  public void capture() {
    System.out.println(this.carryingPlayer + ": captured chicken " + this.getMapId());
    this.carryingPlayer.getProgress().grantEP(PlayerProgress.EP_OBJECTIVE);
    this.drop();
    Game.world().environment().remove(this);
  }

  public boolean isPickedUpOrBeingPickedUp() {
    return this.isPickedUp() || this.pickupStarted != null;
  }

  public boolean isPickedUp() {
    return this.carryingPlayer != null;
  }

  @Override
  protected IMovementController createMovementController() {
    return new ChickenMovementController(this);
  }

  private void pickup(Player player) {
    this.stopPickup();
    this.carryingPlayer = player;
    this.carryingPlayer.setVelocity(40);
    this.setCollision(false);
    this.setRenderType(RenderType.NONE);
    System.out.println(player + ": picked up chicken " + this.getMapId());
  }
}
