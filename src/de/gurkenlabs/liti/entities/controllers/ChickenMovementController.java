package de.gurkenlabs.liti.entities.controllers;

import de.gurkenlabs.liti.entities.Chicken;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.physics.MovementController;

public class ChickenMovementController extends MovementController<Chicken> {
  private static final int ANGLE_CHANGE_MIN_DELAY = 2000;
  private static final int ANGLE_CHANGE_RANDOM_DELAY = 3000;

  private long lastAngleChange;
  private long nextAngleChange;

  private int angle;

  private boolean walkingAround;
  private boolean idlePause;

  public ChickenMovementController(Chicken chicken) {
    super(chicken);

    this.calculateNextAngleChange();
  }

  @Override
  public void update() {
    super.update();

    this.walkingAround = false;

    if(this.getEntity().isPickedUp()){
      this.getEntity().setLocation(this.getEntity().getCarryingPlayer().getX()-1, this.getEntity().getCarryingPlayer().getY() + 10);
    }

    if (!this.canMove() || (this.idlePause && Game.time().since(this.lastAngleChange) < this.nextAngleChange)) {
      return;
    }

    this.walkAroundLikeMotherfucker();
  }


  protected void walkAroundLikeMotherfucker() {
    // WALK AROUND LIKE MOTHERFUCKERS
    float pixelsPerTick = this.getEntity().getTickVelocity();
    final long currentTick = Game.loop().getTicks();
    final long timeSinceLastAngleChange = Game.time().since(this.lastAngleChange);
    if (this.angle == 0 || timeSinceLastAngleChange > this.nextAngleChange) {
      if (Game.random().probe(0.5)) {
        this.idlePause = true;
        this.lastAngleChange = currentTick;
        return;
      }

      this.idlePause = false;

      this.angle = Game.random().nextInt(360);
      this.lastAngleChange = currentTick;
      this.calculateNextAngleChange();
    }

    this.getEntity().setAngle(this.angle);
    Game.physics().move(this.getEntity(), this.getEntity().getAngle(), pixelsPerTick);
  }

  private void calculateNextAngleChange() {
    this.nextAngleChange = Game.random().nextInt(ANGLE_CHANGE_RANDOM_DELAY) + ANGLE_CHANGE_MIN_DELAY;
  }

  private boolean canMove() {
    return !this.getEntity().isPickedUpOrBeingPickedUp();
  }

}