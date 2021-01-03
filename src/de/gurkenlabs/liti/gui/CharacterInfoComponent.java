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

public class CharacterInfoComponent extends GuiComponent {
  private TraitComponent[] traits;
  private ImageComponent survivalSKillFrame;
  private ImageComponent survivalSkillName;
  private ImageComponent survivalSkillDescription;
  private PlayerClass playerClass;

  protected CharacterInfoComponent(double x, double y, double width, double height) {
    super(x, y, width, height);
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
    this.survivalSKillFrame = new ImageComponent(this.getX(), this.getY() + 6 * (cellHeight + cellPadding), this.getWidth(), cellHeight * 3);
    this.survivalSKillFrame.setSpriteSheet(Resources.spritesheets().get("frame-survivalskill"));
    this.survivalSkillName = new ImageComponent(this.getX() + cellPadding, this.getY() + 6 * (cellHeight + cellPadding),
        this.getWidth() - 2 * cellPadding, cellHeight);
    this.survivalSkillName.setFont(LitiFonts.ETCHED);
    this.survivalSkillName.setFontSize((float) this.survivalSkillName.getHeight() * 2 / 3f);

    this.survivalSkillDescription = new ImageComponent(this.getX() + cellPadding, this.getY() + 7 * (cellHeight + cellPadding) - cellPadding / 2d,
        this.getWidth() - 2 * cellPadding, 2 * cellHeight - cellPadding);
    this.survivalSkillDescription.setFont(LitiFonts.ETCHED);
    this.survivalSkillDescription.setAutomaticLineBreaks(true);
    this.survivalSkillDescription.setFontSize((float) this.survivalSKillFrame.getHeight() * 1 / 6f);

    this.getComponents().add(survivalSKillFrame);
    this.getComponents().add(survivalSkillName);
    this.getComponents().add(survivalSkillDescription);
  }

  protected void setClass(PlayerClass newClass) {
    this.playerClass = newClass;
    for (int i = 0; i < Trait.values().length; i++) {
      traits[i].setProficiencyLevel(Proficiency.getLevel(newClass, Trait.values()[i]));
    }
    this.updateInfoText();
  }

  private void updateInfoText() {
    this.survivalSkillName.setText(Resources.strings().get(String.format("skill-%s-name", this.playerClass.toString().toLowerCase())));
    this.survivalSkillDescription.setText(Resources.strings().get(String.format("skill-%s-desc", this.playerClass.toString().toLowerCase())));
  }
}

