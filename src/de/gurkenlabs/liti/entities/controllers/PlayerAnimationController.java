package de.gurkenlabs.liti.entities.controllers;

import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.graphics.CreatureShadowImageEffect;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.CreatureAnimationController;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.util.Imaging;

public class PlayerAnimationController extends CreatureAnimationController<Player> {

  public PlayerAnimationController(Player entity) {
    super(entity, true);
    this.add(new CreatureShadowImageEffect(entity, LitiColors.SHADOW_COLOR).setOffsetY(1));

    this.addPlayerAnimations();

    this.getAll().forEach(an -> {
      Spritesheet replaced = new Spritesheet(Imaging.replaceColors(an.getSpritesheet().getImage(), this.getEntity().getConfiguration().getSkin().getColorMappings()), an.getSpritesheet().getName(), an.getSpritesheet().getSpriteWidth(), an.getSpritesheet().getSpriteHeight());
      boolean def = this.get(an.getName()).equals(this.getDefault());

      Animation newAnimation = new Animation(replaced, an.isLooping(), an.getKeyFrameDurations());
      this.add(newAnimation);

      if (def) {
        this.setDefault(newAnimation);
      }
    });
  }
  @Override
  public void update() {
    if(this.getEntity().getCurrentEgg() != null){
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
  }

}
