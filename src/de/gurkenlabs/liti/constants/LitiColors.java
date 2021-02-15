package de.gurkenlabs.liti.constants;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

  public static final Color player0OutfitColor = new Color(102, 206, 214);
  public static final Color player1OutfitColor = new Color(155, 93, 229);
  public static final Color player2OutfitColor = new Color(255, 207, 0);
  public static final Color player3OutfitColor = new Color(238, 97, 35);


  private LitiColors() {

  }

  public static Map<Color, Color> getPlayerColorMappings(int playerIndex) {
    Map<Color, Color> colorMappings = new HashMap<>();

    switch (playerIndex) {
      case 0:
        colorMappings.put(defaultMainOutfitColor, player0OutfitColor);
        colorMappings.put(defaultDarkOutfitColor, player0OutfitColor.darker());
        colorMappings.put(defaultBrightOutfitColor, player0OutfitColor.brighter());
        break;
      case 1:
        colorMappings.put(defaultMainOutfitColor, player1OutfitColor);
        colorMappings.put(defaultDarkOutfitColor, player1OutfitColor.darker());
        colorMappings.put(defaultBrightOutfitColor, player1OutfitColor.brighter());
        break;
      case 2:
        colorMappings.put(defaultMainOutfitColor, player2OutfitColor);
        colorMappings.put(defaultDarkOutfitColor, player2OutfitColor.darker());
        colorMappings.put(defaultBrightOutfitColor, player2OutfitColor.brighter());
        break;
      case 3:
        colorMappings.put(defaultMainOutfitColor, player3OutfitColor);
        colorMappings.put(defaultDarkOutfitColor, player3OutfitColor.darker());
        colorMappings.put(defaultBrightOutfitColor, player3OutfitColor.brighter());
        break;
      default:
        break;
    }
    return colorMappings;
  }
}
