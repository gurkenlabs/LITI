package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.abilities.Trait;
import de.gurkenlabs.litiengine.gui.GuiComponent;

public class CharacterInfoComponent extends GuiComponent {
  private TraitComponent[] traits;

  protected CharacterInfoComponent(double x, double y, double width, double height) {
    super(x, y, width, height);
  }

  @Override
  protected void initializeComponents() {
    super.initializeComponents();
    this.traits = new TraitComponent[Trait.values().length];
    double cellHeight = this.getHeight() / (Trait.values().length + 1);
    for (int i = 0; i < Trait.values().length; i++) {
      traits[i] = new TraitComponent(this.getX(), this.getY() + i * cellHeight, this.getWidth(), cellHeight,
          Trait.values()[i]);
      this.getComponents().add(traits[i]);
    }
  }
}
