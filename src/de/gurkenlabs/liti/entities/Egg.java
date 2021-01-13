package de.gurkenlabs.liti.entities;

import de.gurkenlabs.litiengine.Align;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.entities.AnimationInfo;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.entities.Prop;

@AnimationInfo(spritePrefix = "prop-egg")
@EntityInfo(width = 31, height = 29)
@CollisionInfo(collision = true, collisionBoxWidth = 18, collisionBoxHeight = 14, align = Align.CENTER, valign = Valign.MIDDLE)
public class Egg extends Prop implements IUpdateable {
  private static final int KILL_DURATION = 5000;

  private Player channelling;
  private long channelStart;

  public Egg(String spritesheetName) {
    super(spritesheetName);

    System.out.println("egg");
  }

  public boolean isBeingChannelled(){
    return this.channelling != null;
  }

  public void channel(Player player) {
    if (this.channelling != null) {
      return;
    }

    player.setChannelledEgg(this);
    player.setState(Player.PlayerState.LOCKED);
    this.channelling = player;
    this.channelStart = Game.time().now();
    System.out.println(player + ": started to destroy egg " + this.getMapId());
    // TODO: UI for channelling duration
  }

  public void release() {
    if (this.channelling != null) {
      this.channelling.setChannelledEgg(null);
      this.channelling.setState(Player.PlayerState.NORMAL);
    }

    this.channelling = null;
    this.channelStart = 0;
  }

  @Override
  public void update() {
    if (this.channelling != null && this.channelStart != 0 && Game.time().since(this.channelStart) > KILL_DURATION) {
      this.destroy();
    }
  }

  private void destroy() {
    System.out.println(this.channelling + ": destroyed egg " + this.getMapId());
    this.release();
    Game.world().environment().remove(this);

    // TODO: animation
    // TODO: grant EP
  }
}
