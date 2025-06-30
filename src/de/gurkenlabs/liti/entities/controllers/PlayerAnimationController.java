package de.gurkenlabs.liti.entities.controllers;

import de.gurkenlabs.liti.constants.Animations;
import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.AnimationController;
import de.gurkenlabs.litiengine.graphics.animation.CreatureAnimationController;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.util.Imaging;
import java.awt.image.BufferedImage;

public class PlayerAnimationController extends CreatureAnimationController<Player> {

  private final String dieAnimationName;

  public PlayerAnimationController(Player entity) {
    super(entity, true);
    this.dieAnimationName = getEntity().getConfiguration().getPlayerClass().toString().toLowerCase() + "-die";
    ;
    //    this.add(new CreatureShadowImageEffect(entity, LitiColors.SHADOW_COLOR).setOffsetY(1));

    this.addPlayerAnimations();

    getAll().forEach(an -> {
      BufferedImage replacedImg =
          Imaging.replaceColors(an.getSpritesheet().getImage(), getEntity().getConfiguration().getSkin().getSkinColorMappings());
      replacedImg = Imaging.replaceColors(replacedImg, LitiColors.getPlayerColorMappings(getEntity().getConfiguration().getIndex()));

      Spritesheet replaced = new Spritesheet(replacedImg, appendIndexAndSkin(an.getSpritesheet().getName()), an.getSpritesheet().getSpriteWidth(),
          an.getSpritesheet().getSpriteHeight());
      boolean def = get(an.getName()).equals(getDefault());

      Animation newAnimation = new Animation(an.getName(), replaced, an.isLooping(), an.getKeyFrameDurations());
      this.add(newAnimation);

      if (def) {
        this.setDefault(newAnimation);
      }
    });
  }

  public String getDieAnimationName() {
    return this.dieAnimationName;
  }

  @Override public void update() {
    if (getEntity().getCurrentEgg() != null && getEntity().findBashAnimation() != null) {
      this.play(getEntity().findBashAnimation().getName());
    }

    super.update();
  }

  private String appendIndexAndSkin(String spriteName) {
    return String.format("%s__%d__%s", spriteName, getEntity().getConfiguration().getIndex(), getEntity().getConfiguration().getSkin());
  }

  private void addPlayerAnimations() {
    final String hitLeftName = getEntity().getConfiguration().getPlayerClass().toString().toLowerCase() + "-hit-left";
    final String hitRightName = getEntity().getConfiguration().getPlayerClass().toString().toLowerCase() + "-hit-right";

    Spritesheet hitLeft = Resources.spritesheets().get(hitLeftName);
    if (hitLeft != null) {
      Animation hitLeftAnimation = new Animation(hitLeft, false);
      this.add(hitLeftAnimation);
      this.add(flippedAnimation(hitLeftAnimation, hitRightName, false));
    }

    Spritesheet die = Resources.spritesheets().get(this.dieAnimationName);
    if (die != null) {
      Animation dieAnimation = new Animation(die, false);
      this.add(dieAnimation);
    }

    switch (getEntity().getPlayerClass()) {
      case WARRIOR:
        this.add(new Animation(Resources.spritesheets().get(Animations.WARRIOR_FORCEOFNATURE_RIGHT), false));
        this.add(AnimationController.flippedAnimation(get(Animations.WARRIOR_FORCEOFNATURE_RIGHT), Animations.WARRIOR_FORCEOFNATURE_LEFT, false));
        break;
      case SHAMAN:
        break;
      case HUNTRESS:
        break;
      case GATHERER:
        break;
    }
  }

}
