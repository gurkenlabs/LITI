package de.gurkenlabs.liti.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Optional;

import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.constants.Skins;
import de.gurkenlabs.liti.entities.PlayerClass;
import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.liti.entities.Skin;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.graphics.animation.AnimationController;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.input.Input;
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
  private Skin currentSkin;
  private AnimationController inactivePlayerPrompt;

  protected CharacterSelectionComponent(double x, double y, double width, double height, int playerIndex) {
    super(x, y, width, height);
    this.playerIndex = playerIndex;
    this.inactivePlayerPrompt = new AnimationController(Resources.spritesheets().get("confirm_prompt"), true);
    this.updateClassName();
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
      BufferedImage prompt = Imaging
          .scale(this.inactivePlayerPrompt.getCurrentImage(), (int) (this.characterPortrait.getBoundingBox().getWidth() * 2 / 3d),
              (int) (this.characterPortrait.getBoundingBox().getHeight() * 2 / 3d), true);
      ImageRenderer.render(g, prompt, this.characterPortrait.getBoundingBox().getCenterX() - prompt.getWidth() / 2d,
          this.characterPortrait.getBoundingBox().getCenterY() - prompt.getHeight() / 2d);
    }
  }

  @Override
  public void prepare() {
    super.prepare();
    this.info.setVisible(false);
    this.setClass(Game.random().choose(PlayerClass.values()));
    this.setSkin(Game.random().choose(Skins.getAll()));
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

    this.className = new ImageComponent(this.getX(), this.getY(), this.getWidth(), textHeight);
    this.characterPortrait = new ImageComponent(this.getX(), this.getY() + this.className.getHeight(), this.getWidth(), portraitHeight);
    this.characterPortrait.setSpriteSheet(Resources.spritesheets().get("frame-portrait"));
    this.info = new CharacterInfoComponent(this.getX(), this.characterPortrait.getY() + this.characterPortrait.getHeight(), this.getWidth(),
        textHeight * 4);
    this.getComponents().add(characterPortrait);
    this.getComponents().add(className);
    this.getComponents().add(info);
  }

  public PlayerClass getCurrentPlayerClass() {
    return currentClass;
  }

  public Skin getCurrentSkin() {
    return currentSkin;
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

  public void nextSkin() {
    setSkin(Skins.getAll().get((Skins.getAll().indexOf(this.getCurrentSkin()) + 1) % Skins.getAll().size()));
  }

  public void previousSkin() {
    setSkin(Skins.getAll().get(Math.floorMod(Skins.getAll().indexOf(this.getCurrentSkin()) - 1, Skins.getAll().size())));
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
    this.info.setVisible(false);
    this.updateClassName();
  }

  public void ready() {

  }

  public void unready() {

  }

  public void info() {
    this.info.setVisible(!this.info.isVisible());
  }

  private BufferedImage getPortrait() {
    if (this.getCurrentPlayerClass() == null || !this.hasPlayerAssigned()) {
      return null;
    }

    String defaultPortraitName = String.format("%s_portrait_default.png", this.getCurrentPlayerClass().toString().toLowerCase());
    String portraitName = String
        .format("%s_portrait_player-%d_skin-%d.png", this.getCurrentPlayerClass().toString().toLowerCase(), this.getPlayerIndex(),
            Skins.getAll().indexOf(this.getCurrentSkin()));

    Optional<BufferedImage> opt = Resources.images().tryGet(portraitName);
    if (opt.isPresent()) {
      return opt.get();
    }
    BufferedImage defaultPortrait = Resources.images().get(defaultPortraitName, true);
    BufferedImage replacedSkin = Imaging.replaceColors(defaultPortrait, this.getCurrentSkin().getColorMappings());
    BufferedImage replacedPlayer = Imaging.replaceColors(replacedSkin, LitiColors.getPlayerColorMappings(this.getPlayerIndex()));
    BufferedImage scaled = Imaging.scale(replacedPlayer, (int) (this.characterPortrait.getBoundingBox().getWidth() * 2 / 3d),
        (int) (this.characterPortrait.getBoundingBox().getHeight() * 2 / 3d), true);
    Resources.images().add(portraitName, scaled);
    return scaled;

  }

  private void setClass(PlayerClass newClass) {
    this.currentClass = newClass;
    this.updateClassName();
    this.info.setClass(newClass);
    if (Players.getConfigurations().size() > this.getPlayerIndex()) {
      Players.getConfiguration(this.getPlayerIndex()).setPlayerClass(this.getCurrentPlayerClass());
    }
  }

  private void setSkin(Skin newSkin) {
    this.currentSkin = newSkin;
  }

  private void updateClassName() {
    this.className.setText(this.playerAssigned ? this.getCurrentPlayerClass().name() : String.format("P%d", this.getPlayerIndex() + 1));
  }
}
