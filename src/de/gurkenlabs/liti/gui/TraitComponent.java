package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.abilities.Trait;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.ImageScaleMode;
import de.gurkenlabs.litiengine.resources.Resources;

public class TraitComponent extends GuiComponent {
  private Trait trait;
  private ImageComponent icon;
  private ImageComponent name;
  private ImageComponent value;
  private int proficiency;

  protected TraitComponent(double x, double y, double width, double height, Trait trait) {
    super(x, y, width, height);
    this.trait = trait;
  }

  @Override
  public void prepare() {
    super.prepare();
    this.icon.setImage(Resources.images().get(this.getTrait() + ".png"));
    this.name.setText(Resources.strings().get(this.getTrait().toString()));
    this.value.setImage(Resources.spritesheets().get("proficiencies").getSprite(proficiency));
  }

  public Trait getTrait() {
    return trait;
  }

  public void setProficiency(int proficiency) {
    this.proficiency = proficiency;
  }

  @Override
  protected void initializeComponents() {
    super.initializeComponents();
    this.icon = new ImageComponent(this.getX(), this.getY(), this.getWidth() * 1 / 6d, this.getHeight());
    this.name = new ImageComponent(this.getX() + this.getWidth() * 1 / 6d, this.getY(), this.getWidth() * 4 / 6d, this.getHeight());
    this.value = new ImageComponent(this.getX() + this.getWidth() * 5 / 6d, this.getY(), this.getWidth() * 1 / 6d, this.getHeight());
    this.icon.setImageScaleMode(ImageScaleMode.FIT);
    this.value.setImageScaleMode(ImageScaleMode.FIT);
    this.getComponents().add(this.icon);
    this.getComponents().add(this.name);
    this.getComponents().add(this.value);
  }
}
