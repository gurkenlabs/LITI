package de.gurkenlabs.liti.constants;

import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.Font;

public final class LitiFonts {
  public static final Font ETCHED = Resources.fonts().get("Etchstone-Regular.ttf");
  public static final Font ROUND = Resources.fonts().get("RobinGraffitiFilledin.ttf");
  public static final Font NUMBERS = Resources.fonts().get("unifont.ttf").deriveFont(Font.BOLD);
  public static final Font ABILITIES = Resources.fonts().get("abilityIcons.ttf");
  public static final Font EMOJI_BLACK = Resources.fonts().get("OpenMoji-Black.ttf");

  private LitiFonts() {

  }
}
