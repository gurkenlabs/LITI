package de.gurkenlabs.liti.entities;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.gurkenlabs.liti.constants.LitiColors;

public class Skin {

  private Color mainSkinColor;
  private Color skinDetailColor;
  private Color skinDarkShadeColor;
  private Color skinBrightShadeColor;
  private Color mainHairColor;
  private Color hairDarkShadeColor;
  private Color hairBrightShadeColor;
  private final Map<Color, Color> colorMappings;

  public Skin(List<Color> colors) {
    this.mainSkinColor = colors.get(0);
    this.skinDetailColor = colors.get(1);
    this.skinDarkShadeColor = colors.get(2);
    this.skinBrightShadeColor = colors.get(3);
    this.mainHairColor = colors.get(4);
    this.hairDarkShadeColor = colors.get(5);
    this.hairBrightShadeColor = colors.get(6);
    this.colorMappings = new HashMap<>();
    this.colorMappings.put(LitiColors.defaultMainSkinColor, mainSkinColor);
    this.colorMappings.put(LitiColors.defaultSkinDetailColor, skinDetailColor);
    this.colorMappings.put(LitiColors.defaultDarkSkinColor, skinDarkShadeColor);
    this.colorMappings.put(LitiColors.defaultBrightSkinColor, skinBrightShadeColor);
    this.colorMappings.put(LitiColors.defaultMainHairColor, mainHairColor);
    this.colorMappings.put(LitiColors.defaultDarkHairColor, hairDarkShadeColor);
    this.colorMappings.put(LitiColors.defaultBrightHairColor, hairBrightShadeColor);
  }

  public Map<Color, Color> getColorMappings() {
    return colorMappings;
  }

  public Color getMainSkinColor() {
    return mainSkinColor;
  }

  public Color getSkinDetailColor() {
    return skinDetailColor;
  }

  public Color getSkinDarkShadeColor() {
    return skinDarkShadeColor;
  }

  public Color getSkinBrightShadeColor() {
    return skinBrightShadeColor;
  }

  public Color getMainHairColor() {
    return mainHairColor;
  }

  public Color getHairDarkShadeColor() {
    return hairDarkShadeColor;
  }

  public Color getHairBrightShadeColor() {
    return hairBrightShadeColor;
  }

}
