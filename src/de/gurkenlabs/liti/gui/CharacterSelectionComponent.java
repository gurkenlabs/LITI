package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.abilities.Trait;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.resources.Resources;

public class CharacterSelectionComponent extends GuiComponent {
  private ImageComponent characterPortrait;
  private ImageComponent className;
  private boolean playerAssigned;
  private boolean ready;
  private TraitComponent[] traits;

  protected CharacterSelectionComponent(double x, double y, double width, double height) {
    super(x, y, width, height);
  }

  @Override
  protected void initializeComponents() {
    super.initializeComponents();
    double portraitHeight = this.getHeight() / 2d;
    double textHeight = this.getHeight() / 8d;

    this.characterPortrait = new ImageComponent(this.getX(), this.getY(), this.getWidth(), portraitHeight);
    this.characterPortrait.setSpriteSheet(Resources.spritesheets().get("woodframe_single"));

    this.className = new ImageComponent(this.getX(), this.getY() + this.characterPortrait.getHeight(), this.getWidth(), textHeight);

    this.traits = new TraitComponent[Trait.values().length];
    for (int i = 0; i < Trait.values().length; i++) {
      traits[i] = new TraitComponent(this.getX(), this.className.getY() + textHeight + i * textHeight, this.getWidth(), textHeight,
          Trait.values()[i]);
      this.getComponents().add(traits[i]);
    }
    this.getComponents().add(characterPortrait);
    this.getComponents().add(className);
  }

  public boolean hasPlayerAssigned() {
    return playerAssigned;
  }

  public boolean isReady() {
    return ready;
  }

  public void nextCostume() {

  }

  public void previousCostume() {

  }

  public void nextClass() {

  }

  public void previousClass() {

  }

  public void assignPlayer() {
    playerAssigned = true;
  }

  public void removePlayer() {
    playerAssigned = false;
  }

  public void ready() {

  }

  public void unready() {

  }
}
