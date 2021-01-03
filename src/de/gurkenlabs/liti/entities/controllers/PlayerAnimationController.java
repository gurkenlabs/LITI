package de.gurkenlabs.liti.entities.controllers;

import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.graphics.CreatureShadowImageEffect;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.CreatureAnimationController;
import de.gurkenlabs.litiengine.util.Imaging;

public class PlayerAnimationController extends CreatureAnimationController<Player> {

  public PlayerAnimationController(Player entity) {
    super(entity, true);
    this.add(new CreatureShadowImageEffect(entity, LitiColors.SHADOW_COLOR).setOffsetY(1));
    this.getAll().forEach(an -> {
      Spritesheet replaced = new Spritesheet(Imaging.replaceColors(an.getSpritesheet().getImage(), this.getEntity().getConfiguration().getSkin().getColorMappings()), an.getSpritesheet().getName(), an.getSpritesheet().getSpriteWidth(), an.getSpritesheet().getSpriteHeight());
      this.add(new Animation(replaced, an.isLooping(), an.getKeyFrameDurations()));
    });
  }


}
