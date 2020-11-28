package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.abilities.Trait;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.ImageComponent;
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
    this.setText(trait.toString());
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
    this.icon = new ImageComponent(this.getX(), this.getY(), this.getWidth() * 1 / 6d, this.getHeight(),
        Resources.images().get(getTrait().toString() + ".png"));
    this.name = new ImageComponent(this.getX(), this.getY(), this.getWidth() * 1 / 6d, this.getHeight(),
        getTrait().toString());
    this.value = new ImageComponent(this.getX(), this.getY(), this.getWidth() * 1 / 6d, this.getHeight(),
        Resources.spritesheets().get("proficiencies").getSprite(proficiency));
  }
}
