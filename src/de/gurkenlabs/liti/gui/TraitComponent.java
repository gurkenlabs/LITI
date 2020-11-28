package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.abilities.Trait;
import de.gurkenlabs.litiengine.gui.ImageComponent;

public class TraitComponent extends ImageComponent {
  private Trait trait;

  protected TraitComponent(double x, double y, double width, double height, Trait trait) {
    super(x, y, width, height);
    this.trait = trait;
    this.setImage(Resources.images.get(""));
  }

  public Trait getTrait() {
    return trait;
  }
}
