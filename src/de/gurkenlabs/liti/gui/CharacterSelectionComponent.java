package de.gurkenlabs.liti.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.gurkenlabs.liti.entities.Costume;
import de.gurkenlabs.liti.entities.PlayerClass;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.resources.Resources;

public class CharacterSelectionComponent extends GuiComponent {
  private ImageComponent characterPortrait;
  private ImageComponent className;
  private CharacterInfoComponent info;
  private boolean playerAssigned;
  private boolean ready;
  private PlayerClass currentClass;
  private int playerIndex;
  private Costume currentCostume;

  protected CharacterSelectionComponent(double x, double y, double width, double height, int playerIndex) {
    super(x, y, width, height);
    this.playerIndex = playerIndex;
  }

  @Override
  public void render(Graphics2D g) {
    super.render(g);
    if (this.getPortrait() != null) {
      ImageRenderer.render(g, this.getPortrait(), this.characterPortrait.getBoundingBox().getCenterX() - this.getPortrait().getWidth() / 2d,
          this.characterPortrait.getBoundingBox().getCenterY() - this.getPortrait().getHeight() / 2d);
    }
  }

  @Override
  public void prepare() {
    super.prepare();
    this.info.setVisible(false);
    this.currentClass = Game.random().choose(PlayerClass.values());
  }

  @Override
  protected void initializeComponents() {
    super.initializeComponents();
    double portraitHeight = this.getHeight() * 4 / 5d;
    double textHeight = this.getHeight() * 1 / 5d;

    this.characterPortrait = new ImageComponent(this.getX(), this.getY(), this.getWidth(), portraitHeight);
    this.characterPortrait.setSpriteSheet(Resources.spritesheets().get("woodframe_single"));
    this.className = new ImageComponent(this.getX(), this.getY() + this.characterPortrait.getHeight(), this.getWidth(), textHeight);

    this.info = new CharacterInfoComponent(this.getX(), this.className.getY() + textHeight, this.getWidth(), textHeight * 4);
    this.getComponents().add(characterPortrait);
    this.getComponents().add(className);
    this.getComponents().add(info);
  }

  public PlayerClass getCurrentPlayerClass() {
    return currentClass;
  }

  public Costume getCostume() {
    return currentCostume;
  }

  public int getPlayerIndex() {
    return playerIndex;
  }

  public boolean hasPlayerAssigned() {
    return playerAssigned;
  }

  public boolean isReady() {
    return ready;
  }

  public void nextCostume() {
    setCostume(Costume.values()[(currentCostume.ordinal() + 1) % Costume.values().length]);
  }

  public void previousCostume() {
    setCostume(Costume.values()[Math.floorMod(currentCostume.ordinal() - 1, Costume.values().length)]);
  }

  public void nextClass() {
    setClass(PlayerClass.values()[(currentClass.ordinal() + 1) % PlayerClass.values().length]);
  }

  public void previousClass() {
    setClass(PlayerClass.values()[Math.floorMod(currentClass.ordinal() - 1, PlayerClass.values().length)]);
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

  public void info() {
    this.info.setVisible(!this.info.isVisible());
  }

  private BufferedImage getPortrait() {
    return null;
  }

  private void setClass(PlayerClass newClass) {
    this.currentClass = newClass;
    this.className.setText(this.getCurrentPlayerClass().name());
  }

  private void setCostume(Costume newCostume) {
    this.currentCostume = newCostume;
  }
}
