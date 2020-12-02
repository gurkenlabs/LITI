package de.gurkenlabs.liti.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.gurkenlabs.liti.entities.Costume;
import de.gurkenlabs.liti.entities.PlayerClass;
import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.AnimationController;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.util.Imaging;

public class CharacterSelectionComponent extends GuiComponent {
  private ImageComponent characterPortrait;
  private ImageComponent className;
  private CharacterInfoComponent info;
  private boolean playerAssigned;
  private boolean ready;
  private PlayerClass currentClass;
  private int playerIndex;
  private Costume currentCostume;
  private AnimationController inactivePlayerPrompt;

  protected CharacterSelectionComponent(double x, double y, double width, double height, int playerIndex) {
    super(x, y, width, height);
    this.playerIndex = playerIndex;
    this.inactivePlayerPrompt = new AnimationController(Resources.spritesheets().get("confirm_prompt"), true);

  }

  @Override
  public void render(Graphics2D g) {
    super.render(g);
    if (this.getPortrait() != null) {
      ImageRenderer.render(g, this.getPortrait(), this.characterPortrait.getBoundingBox().getCenterX() - this.getPortrait().getWidth() / 2d,
          this.characterPortrait.getBoundingBox().getCenterY() - this.getPortrait().getHeight() / 2d);
    } else {
      if (this.inactivePlayerPrompt == null || this.inactivePlayerPrompt.getCurrentImage() == null) {
        return;
      }
      BufferedImage prompt = Imaging.scale(this.inactivePlayerPrompt.getCurrentImage(), (int) (this.characterPortrait.getBoundingBox().getWidth() * 2 / 3d), (int) (this.characterPortrait.getBoundingBox().getHeight() * 2 / 3d), true);
      ImageRenderer.render(g, prompt, this.characterPortrait.getBoundingBox().getCenterX() - prompt.getWidth() / 2d,
          this.characterPortrait.getBoundingBox().getCenterY() - prompt.getHeight() / 2d);
    }
  }

  @Override
  public void prepare() {
    super.prepare();
    this.info.setVisible(false);
    this.currentClass = Game.random().choose(PlayerClass.values());
    Game.loop().attach(inactivePlayerPrompt);
  }

  @Override
  public void suspend() {
    super.suspend();
    this.currentClass = null;
    Game.loop().detach(inactivePlayerPrompt);
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
    this.updateClassName();
  }

  public void removePlayer() {
    playerAssigned = false;
    this.className.setText("");
    this.info.setVisible(false);
  }

  public void ready() {

  }

  public void unready() {

  }

  public void info() {
    this.info.setVisible(!this.info.isVisible());
  }

  private BufferedImage getPortrait() {
    if (this.getCurrentPlayerClass() == null) {
      return null;
    }
    return null;
  }

  private void setClass(PlayerClass newClass) {
    this.currentClass = newClass;
    this.updateClassName();
    Players.getConfiguration(this.getPlayerIndex()).setPlayerClass(this.getCurrentPlayerClass());
  }

  private void setCostume(Costume newCostume) {
    this.currentCostume = newCostume;
  }

  private void updateClassName() {
    this.className.setText(this.getCurrentPlayerClass().name());
  }
}
