package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.constants.ButtonPrompts;
import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.constants.LitiFonts;
import de.gurkenlabs.liti.constants.Skins;
import de.gurkenlabs.liti.entities.PlayerClass;
import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.liti.entities.Skin;
import de.gurkenlabs.liti.input.InputBinding;
import de.gurkenlabs.litiengine.Align;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.entities.Rotation;
import de.gurkenlabs.litiengine.graphics.animation.AnimationController;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.tweening.TweenFunction;
import de.gurkenlabs.litiengine.tweening.TweenType;
import de.gurkenlabs.litiengine.util.Imaging;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class CharacterSelectionComponent extends GuiComponent {
  private ImageComponent characterPortrait;
  private ImageComponent className;
  private ImageComponent readyText;
  private ImageComponent arrowLeft, arrowRight, arrowUp, arrowDown;
  private CharacterInfoComponent info;
  private ImageComponent infoFrame, infoQuestionMark, infoButtonPrompt;
  private boolean playerAssigned;
  private boolean ready;
  private PlayerClass currentClass;
  private int playerIndex;
  private Skin currentSkin;
  private AnimationController inactivePlayerPrompt;
  private static final BufferedImage QUESTION_MARK = Resources.images().get("info.png");
  private static final BufferedImage ARROW_RIGHT = Resources.images().get("arrow.png");
  private static final BufferedImage ARROW_UP = Imaging.scale(Imaging.rotate(ARROW_RIGHT, Rotation.ROTATE_270), 0.5d);
  private static final BufferedImage ARROW_LEFT = Imaging.rotate(ARROW_RIGHT, Rotation.ROTATE_180);
  private static final BufferedImage ARROW_DOWN = Imaging.scale(Imaging.rotate(ARROW_RIGHT, Rotation.ROTATE_90), 0.5d);

  protected CharacterSelectionComponent(double x, double y, double width, double height, int playerIndex) {
    super(x, y, width, height);
    this.playerIndex = playerIndex;
    this.inactivePlayerPrompt = new AnimationController(Resources.spritesheets().get("confirm_prompt"), true);
    this.updateClassName();
    this.className.getAppearance().setForeColor(LitiColors.getPlayerColorMappings(this.getPlayerIndex()).get(LitiColors.defaultMainOutfitColor));
    this.infoButtonPrompt.setImage(Imaging
        .scale(ButtonPrompts.get(this.getPlayerIndex(), InputBinding.InputEvent.INFO), (int) (infoButtonPrompt.getWidth() * 4 / 5),
            (int) (infoButtonPrompt.getHeight() * 4 / 5d), true));
  }

  @Override
  public void render(Graphics2D g) {
    this.updatePortrait();
    double textHeight = this.getHeight() * 1 / 5d;
    super.render(g);
    if (this.readyText.isVisible()) {
      this.readyText.setFontSize((float) (textHeight + textHeight * 0.3 * Math.sin(Game.time().sinceEnvironmentLoad() / 400.0)));
    }
  }

  @Override
  public void prepare() {
    super.prepare();
    this.info.setVisible(false);
    this.readyText.setVisible(false);
    this.showInfoPrompt(false);
    this.showArrows(false);
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
    this.className.setTextShadow(true);
    this.className.setFont(LitiFonts.ROUND.deriveFont((float) (this.className.getHeight() * 2 / 3d)));
    this.className.setTextShadowColor(LitiColors.TEXT_OUTLINE);
    this.className.setTextShadowStroke(3f);
    this.className.setTextValign(Valign.MIDDLE);
    this.className.setTextAlign(Align.CENTER);
    this.className.setTextAntialiasing(true);

    this.characterPortrait = new ImageComponent(this.getX(), this.getY() + this.className.getHeight(), this.getWidth(), portraitHeight);
    this.characterPortrait.setSpriteSheet(Resources.spritesheets().get("frame-portrait"));

    double arrowWidth = this.getWidth() * 1 / 7d;
    double arrowHeight = this.getHeight() * 1 / 7d;

    this.arrowLeft = new ImageComponent(characterPortrait.getX() - arrowWidth,
        characterPortrait.getY() + characterPortrait.getHeight() / 2d - arrowHeight / 2d,
        arrowWidth, arrowHeight, ARROW_LEFT);
    this.arrowRight = new ImageComponent(characterPortrait.getX() + characterPortrait.getWidth(),
        characterPortrait.getY() + characterPortrait.getHeight() / 2d - arrowHeight / 2d,
        arrowWidth, arrowHeight, ARROW_RIGHT);
    this.arrowUp = new ImageComponent(characterPortrait.getX() + characterPortrait.getWidth() / 2d - arrowWidth / 2d,
        characterPortrait.getY() + arrowHeight * 3 / 4d,
        arrowWidth, arrowHeight / 2d, ARROW_UP);
    this.arrowDown = new ImageComponent(characterPortrait.getX() + characterPortrait.getWidth() / 2d - arrowWidth / 2d,
        characterPortrait.getY() + characterPortrait.getHeight() - arrowHeight * 5 / 4d,
        arrowWidth, arrowHeight / 2d, ARROW_DOWN);
    this.info = new CharacterInfoComponent(characterPortrait.getX(), this.characterPortrait.getY() + this.characterPortrait.getHeight(),
        this.getWidth(), textHeight * 4);

    this.infoFrame = new ImageComponent(this.getX() + arrowWidth, characterPortrait.getY() + characterPortrait.getHeight(),
        this.getWidth() - 2 * arrowWidth,
        characterPortrait.getHeight() * 1 / 6d);
    this.infoFrame.setSpriteSheet(Resources.spritesheets().get("frame-info.png"));
    this.infoQuestionMark = new ImageComponent(infoFrame.getX(), infoFrame.getY(), infoFrame.getWidth() / 2d, infoFrame.getHeight(), QUESTION_MARK);
    this.infoButtonPrompt = new ImageComponent(infoFrame.getX() + infoFrame.getWidth() / 2d, infoFrame.getY(), infoFrame.getWidth() / 2d,
        infoFrame.getHeight());

    this.readyText = new ImageComponent(this.characterPortrait.getX() - this.characterPortrait.getWidth() / 4d, this.characterPortrait.getY(),
        this.characterPortrait.getWidth() * 1.5d, this.characterPortrait.getHeight(), Resources.strings().get("ready"));
    this.readyText.getAppearance().setForeColor(LitiColors.TRAIT_GOOD);
    this.readyText.setFont(LitiFonts.ROUND.deriveFont((float) textHeight));
    this.readyText.setTextShadow(true);
    this.readyText.setTextShadowColor(LitiColors.TEXT_OUTLINE);
    this.readyText.setTextShadowStroke(3f);
    this.readyText.setTextAntialiasing(true);
    this.readyText.setTextValign(Valign.MIDDLE);
    this.readyText.setTextAlign(Align.CENTER);

    this.getComponents().add(characterPortrait);
    this.getComponents().add(arrowLeft);
    this.getComponents().add(arrowRight);
    this.getComponents().add(arrowUp);
    this.getComponents().add(arrowDown);
    this.getComponents().add(className);
    this.getComponents().add(info);
    this.getComponents().add(infoFrame);
    this.getComponents().add(infoQuestionMark);
    this.getComponents().add(infoButtonPrompt);
    this.getComponents().add(readyText);
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
    Game.tweens().begin(this.arrowDown, TweenType.POSITION_Y, 150).targetRelative(+10).ease(TweenFunction.BACK_OUT);
    Game.loop().perform(150, () -> {
      Game.tweens().begin(this.arrowDown, TweenType.POSITION_Y, 150).targetRelative(0);
    });
  }

  public void previousSkin() {
    setSkin(Skins.getAll().get(Math.floorMod(Skins.getAll().indexOf(this.getCurrentSkin()) - 1, Skins.getAll().size())));
    Game.tweens().begin(this.arrowUp, TweenType.POSITION_Y, 150).targetRelative(-10).ease(TweenFunction.BACK_OUT);
    Game.loop().perform(150, () -> {
      Game.tweens().begin(this.arrowUp, TweenType.POSITION_Y, 150).targetRelative(0);
    });
  }

  public void nextClass() {
    setClass(PlayerClass.values()[(currentClass.ordinal() + 1) % PlayerClass.values().length]);
    Game.tweens().begin(this.arrowRight, TweenType.POSITION_X, 150).targetRelative(+10).ease(TweenFunction.BACK_OUT);
    Game.loop().perform(150, () -> {
      Game.tweens().begin(this.arrowRight, TweenType.POSITION_X, 150).targetRelative(0);
    });
  }

  public void previousClass() {
    setClass(PlayerClass.values()[Math.floorMod(currentClass.ordinal() - 1, PlayerClass.values().length)]);
    Game.tweens().begin(this.arrowLeft, TweenType.POSITION_X, 150).targetRelative(-10).ease(TweenFunction.BACK_OUT);
    Game.loop().perform(150, () -> {
      Game.tweens().begin(this.arrowLeft, TweenType.POSITION_X, 150).targetRelative(0);
    });
  }

  public void assignPlayer() {
    playerAssigned = true;
    this.updateClassName();
    this.showInfoPrompt(true);
    this.showArrows(true);
  }

  public void removePlayer() {
    playerAssigned = false;
    this.updateClassName();
    this.showInfoPrompt(false);
    this.showArrows(false);
    this.info.setVisible(false);
  }

  public void ready() {
    this.ready = true;
    this.showInfoPrompt(false);
    this.showArrows(false);
    this.info.setVisible(false);
    this.showReadyText(true);
    this.updateClassName();
  }

  public void unready() {
    this.ready = false;
    this.showInfoPrompt(true);
    this.showArrows(true);
    this.showReadyText(false);
    this.updateClassName();
  }

  public void showReadyText(boolean visible) {
    this.readyText.setVisible(visible);
  }

  public void info() {
    this.showInfoPrompt(!this.infoFrame.isVisible());
    this.info.setVisible(!this.info.isVisible());
    Game.tweens().begin(this.info, TweenType.POSITION_Y, 100).targetRelative(+10).ease(TweenFunction.BACK_OUT);
    Game.loop().perform(100, () -> {
      Game.tweens().begin(this.info, TweenType.POSITION_Y, 100).targetRelative(0);
    });
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

  private void updatePortrait() {
    this.characterPortrait.setImage(this.getPortrait() == null ?
        Imaging.scale(this.inactivePlayerPrompt.getCurrentImage(), (int) (this.characterPortrait.getWidth() * 2 / 4d),
            (int) (this.characterPortrait.getHeight() * 2 / 4d),
            true) :
        this.getPortrait());
  }

  private void showInfoPrompt(boolean show) {
    this.infoFrame.setVisible(show);
    this.infoQuestionMark.setVisible(show);
    this.infoButtonPrompt.setVisible(show);
  }

  private void showArrows(boolean show) {
    this.arrowUp.setVisible(show);
    this.arrowDown.setVisible(show);
    this.arrowLeft.setVisible(show);
    this.arrowRight.setVisible(show);
  }

  private void updateClassName() {
    this.className
        .setText(this.playerAssigned && !this.isReady() ? this.getCurrentPlayerClass().name() : String.format("P%d", this.getPlayerIndex() + 1));
  }
}
