package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.abilities.Proficiency;
import de.gurkenlabs.liti.abilities.Trait;
import de.gurkenlabs.liti.constants.LitiFonts;
import de.gurkenlabs.liti.entities.PlayerClass;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.tweening.TweenFunction;
import de.gurkenlabs.litiengine.tweening.TweenType;

import javax.naming.spi.ResolveResult;

public class CharacterInfoComponent extends GuiComponent {
  private TraitComponent[] traits;
  private ImageComponent ultimate;
  private boolean displayInfo;
  private PlayerClass playerClass;

  protected CharacterInfoComponent(double x, double y, double width, double height) {
    super(x, y, width, height);
  }

  protected void toggleSurvivalSkillInfo() {
    displayInfo = !displayInfo;
    Game.tweens().begin(this.ultimate, TweenType.POSITION_Y, 100).targetRelative(+10).ease(TweenFunction.BACK_OUT);
    Game.loop().perform(100, () -> {
      Game.tweens().begin(this.ultimate, TweenType.POSITION_Y, 100).targetRelative(0);
    });
    this.updateInfoText();
  }

  @Override
  protected void initializeComponents() {
    super.initializeComponents();
    this.traits = new TraitComponent[Trait.values().length];
    double cellPadding = this.getHeight() * 1 / 30d;
    double cellHeight = (this.getHeight() - (Trait.values().length * cellPadding)) / Trait.values().length;
    for (int i = 0; i < Trait.values().length; i++) {
      traits[i] = new TraitComponent(this.getX(), this.getY() + i * (cellHeight + cellPadding), this.getWidth(), cellHeight,
          Trait.values()[i]);
      this.getComponents().add(traits[i]);
    }
    this.ultimate = new ImageComponent(this.getX(), this.getY() + 6 * (cellHeight + cellPadding), this.getWidth(), cellHeight * 3);
    this.ultimate.setFont(LitiFonts.ETCHED);
    this.ultimate.setTextAntialiasing(true);
    this.ultimate.setAutomaticLineBreaks(true);
    this.ultimate.setSpriteSheet(Resources.spritesheets().get("frame-survivalskill"));
    this.getComponents().add(ultimate);
  }

  protected void setClass(PlayerClass newClass) {
    this.playerClass = newClass;
    for (int i = 0; i < Trait.values().length; i++) {
      traits[i].setProficiencyLevel(Proficiency.getLevel(newClass, Trait.values()[i]));
    }
    this.updateInfoText();
  }

  private void updateInfoText() {
    if (displayInfo) {
      this.ultimate.setText(Resources.strings().get(String.format("skill-%s-desc", this.playerClass.toString().toLowerCase())));
      this.ultimate.setFontSize((float) this.ultimate.getHeight() * 1 / 5f);
    } else {
      this.ultimate.setText(Resources.strings().get(String.format("skill-%s-name", this.playerClass.toString().toLowerCase())));
      this.ultimate.setFontSize((float) this.ultimate.getHeight() * 1 / 3f);
    }
  }
}
