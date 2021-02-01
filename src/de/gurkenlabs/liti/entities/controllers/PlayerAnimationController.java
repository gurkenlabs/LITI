package de.gurkenlabs.liti.entities.controllers;

import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.entities.Player;
import com.litiengine.graphics.CreatureShadowImageEffect;
import com.litiengine.graphics.Spritesheet;
import com.litiengine.graphics.animation.Animation;
import com.litiengine.graphics.animation.CreatureAnimationController;
import com.litiengine.resources.Resources;
import com.litiengine.util.Imaging;

import java.awt.image.BufferedImage;

public class PlayerAnimationController extends CreatureAnimationController<Player> {

  private final String dieAnimationName;

  public PlayerAnimationController(Player entity) {
    super(entity, true);
    this.dieAnimationName = this.getEntity().getConfiguration().getPlayerClass().toString().toLowerCase() + "-die";
    ;
    this.add(new CreatureShadowImageEffect(entity, LitiColors.SHADOW_COLOR).setOffsetY(1));

    this.addPlayerAnimations();

    this.getAll().forEach(an -> {
      BufferedImage replacedImg = Imaging.replaceColors(an.getSpritesheet().getImage(), this.getEntity().getConfiguration().getSkin().getSkinColorMappings());
      replacedImg = Imaging.replaceColors(replacedImg, LitiColors.getPlayerColorMappings(this.getEntity().getConfiguration().getIndex()));
      Spritesheet replaced = new Spritesheet(replacedImg, an.getSpritesheet().getName(), an.getSpritesheet().getSpriteWidth(), an.getSpritesheet().getSpriteHeight());
      boolean def = this.get(an.getName()).equals(this.getDefault());

      Animation newAnimation = new Animation(replaced, an.isLooping(), an.getKeyFrameDurations());
      this.add(newAnimation);

      if (def) {
        this.setDefault(newAnimation);
      }
    });
  }

  public String getDieAnimationName() {
    return this.dieAnimationName;
  }

  @Override
  public void update() {
    if (this.getEntity().getCurrentEgg() != null) {
      this.play(this.getEntity().findBashAnimation().getName());
    }

    super.update();
  }

  private void addPlayerAnimations() {
    final String hitLeftName = this.getEntity().getConfiguration().getPlayerClass().toString().toLowerCase() + "-hit-left";
    final String hitRightName = this.getEntity().getConfiguration().getPlayerClass().toString().toLowerCase() + "-hit-right";

    Spritesheet hitLeft = Resources.spritesheets().get(hitLeftName);
    if (hitLeft != null) {
      Animation hitLeftAnimation = new Animation(hitLeft, false);
      this.add(hitLeftAnimation);
      this.add(flipAnimation(hitLeftAnimation, hitRightName));
    }

    Spritesheet die = Resources.spritesheets().get(this.dieAnimationName);
    if (die != null) {
      Animation dieAnimation = new Animation(die, false);
      this.add(dieAnimation);
    }
  }

}
