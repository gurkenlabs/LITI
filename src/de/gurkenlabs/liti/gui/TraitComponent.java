package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.abilities.Trait;
import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.constants.LitiFonts;
import de.gurkenlabs.litiengine.Align;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.ImageScaleMode;
import de.gurkenlabs.litiengine.resources.Resources;

public class TraitComponent extends GuiComponent {
  private Trait trait;
  private ImageComponent name;
  private ImageComponent value;

  protected TraitComponent(double x, double y, double width, double height, Trait trait) {
    super(x, y, width, height);
    this.trait = trait;
    this.name.setText(Resources.strings().get(this.getTrait().toString()));

  }

  public Trait getTrait() {
    return trait;
  }

  public void setProficiencyLevel(int proficiencyLevel) {
    this.value.setImage(Resources.spritesheets().get("proficiency-levels").getSprite(proficiencyLevel));
  }

  @Override
  protected void initializeComponents() {
    super.initializeComponents();
    this.name = new ImageComponent(this.getX() + this.getWidth() * 1 / 6d, this.getY(), this.getWidth() * 3 / 6d, this.getHeight());
    this.name.setFont(LitiFonts.ETCHED.deriveFont((float) (this.name.getHeight() * 3 / 5d)));
    this.name.setTextAlign(Align.CENTER);
    this.name.setTextValign(Valign.MIDDLE);
    this.name.setTextAntialiasing(true);
    this.name.setSpriteSheet(Resources.spritesheets().get("frame-proficiency"));
    double padding = this.getHeight() * 1 / 10;
    this.value = new ImageComponent(this.getX() + this.getWidth() * 4 / 6d + padding, this.getY() + padding / 2d, this.getWidth() * 1 / 6d - padding,
        this.getHeight() - padding);
    this.value.setSpriteSheet(Resources.spritesheets().get("frame-proficiencylevel"));
    this.getComponents().add(this.name);
    this.getComponents().add(this.value);
  }
}
