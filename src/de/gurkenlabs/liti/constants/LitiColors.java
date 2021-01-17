package de.gurkenlabs.liti.constants;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import de.gurkenlabs.litiengine.util.ColorHelper;

public final class LitiColors {
  public static final Color MAP_DARKGRASS = new Color(90, 123, 70);
  public static final Color MAP_GRASS = new Color(112, 153, 73);

  public static final Color SHADOW_COLOR = new Color(0, 0, 0, 80);
  public static final Color TEXT_DARK = ColorHelper.decode("#25160b");
  public static final Color TEXT_OUTLINE = ColorHelper.decode("#392515");

  public static final Color TRAIT_BAD = ColorHelper.decode("#cb231b");
  public static final Color TRAIT_MEDIUM = ColorHelper.decode("#ffcf00");
  public static final Color TRAIT_GOOD = ColorHelper.decode("#8fef6e");

  public static final Color LOADING_BACKGROUND = new Color(196, 190, 155);

  public static final Color COLOR_HEALTH_BG = new Color(40, 42, 43, 200);
  public static final Color COLOR_HEALTH = new Color(231, 69, 78);
  public static final Color COLOR_HEALTH_LOW = new Color(224, 7, 17);
  public static final Color COLOR_HEALTH_HIT = new Color(255, 173, 94);
  public static final Color COLOR_STAMINA = new Color(232, 230, 215);
  public static final Color COLOR_STAMINA_DEPLETED = new Color(135, 29, 68);

  public static final Color defaultMainSkinColor = ColorHelper.decode("#949494");
  public static final Color defaultSkinDetailColor = ColorHelper.decode("#857171");
  public static final Color defaultDarkSkinColor = ColorHelper.decode("#696969");
  public static final Color defaultBrightSkinColor = ColorHelper.decode("#ababab");
  public static final Color defaultMainHairColor = ColorHelper.decode("#353535");
  public static final Color defaultDarkHairColor = ColorHelper.decode("#212121");
  public static final Color defaultBrightHairColor = ColorHelper.decode("#434343");
  public static final Color defaultMainOutfitColor = ColorHelper.decode("#a400aa");
  public static final Color defaultDarkOutfitColor = ColorHelper.decode("#800085");
  public static final Color defaultBrightOutfitColor = ColorHelper.decode("#c900d0");

  private LitiColors() {

  }

  public static Map<Color, Color> getPlayerColorMappings(int playerIndex) {
    Map<Color, Color> colorMappings = new HashMap<>();

    switch (playerIndex) {
    case 0:
      colorMappings.put(defaultMainOutfitColor, ColorHelper.decode("#66ced6"));
      colorMappings.put(defaultDarkOutfitColor, colorMappings.get(defaultMainOutfitColor).darker());
      colorMappings.put(defaultBrightOutfitColor, colorMappings.get(defaultMainOutfitColor).brighter());
      break;
    case 1:
      colorMappings.put(defaultMainOutfitColor, ColorHelper.decode("#9b5de5"));
      colorMappings.put(defaultDarkOutfitColor, colorMappings.get(defaultMainOutfitColor).darker());
      colorMappings.put(defaultBrightOutfitColor, colorMappings.get(defaultMainOutfitColor).brighter());
      break;
    case 2:
      colorMappings.put(defaultMainOutfitColor, ColorHelper.decode("#ffcf00"));
      colorMappings.put(defaultDarkOutfitColor, colorMappings.get(defaultMainOutfitColor).darker());
      colorMappings.put(defaultBrightOutfitColor, colorMappings.get(defaultMainOutfitColor).brighter());
      break;
    case 3:
      colorMappings.put(defaultMainOutfitColor, ColorHelper.decode("#ee6123"));
      colorMappings.put(defaultDarkOutfitColor, colorMappings.get(defaultMainOutfitColor).darker());
      colorMappings.put(defaultBrightOutfitColor, colorMappings.get(defaultMainOutfitColor).brighter());
      break;
    default:
      break;
    }
    return colorMappings;
  }
}
