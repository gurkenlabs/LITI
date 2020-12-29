package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.abilities.Trait;
import de.gurkenlabs.litiengine.Align;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.ImageScaleMode;
import de.gurkenlabs.litiengine.resources.Resources;

public class TraitComponent extends GuiComponent {
  private Trait trait;
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
    this.name.setText(Resources.strings().get(this.getTrait().toString()));
  }

  public Trait getTrait() {
    return trait;
  }

  public void setProficiencyLevel(int proficiencyLevel) {
    this.proficiency = proficiencyLevel;
    this.value.setImage(Resources.spritesheets().get("proficiency-levels").getSprite(proficiencyLevel));
  }

  @Override
  protected void initializeComponents() {
    super.initializeComponents();
    this.name = new ImageComponent(this.getX() + this.getWidth() * 1 / 6d, this.getY(), this.getWidth() * 3 / 6d, this.getHeight());
    this.value = new ImageComponent(this.getX() + this.getWidth() * 4 / 6d, this.getY(), this.getWidth() * 1 / 6d, this.getHeight());
    this.value.setImageScaleMode(ImageScaleMode.FIT);
    this.name.setTextAlign(Align.LEFT);
    this.getComponents().add(this.name);
    this.getComponents().add(this.value);
  }
}
