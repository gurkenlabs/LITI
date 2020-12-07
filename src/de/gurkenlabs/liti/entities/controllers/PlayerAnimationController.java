package de.gurkenlabs.liti.entities.controllers;

import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.graphics.CreatureShadowImageEffect;
import de.gurkenlabs.litiengine.graphics.animation.CreatureAnimationController;

public class PlayerAnimationController extends CreatureAnimationController<Player> {

  public PlayerAnimationController(Player entity) {
    super(entity, true);
    this.add(new CreatureShadowImageEffect(entity, LitiColors.SHADOW_COLOR).setOffsetY(1));
  }


}
