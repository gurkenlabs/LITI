package de.gurkenlabs.liti.constants;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.gurkenlabs.liti.entities.Skin;
import com.litiengine.Game;
import com.litiengine.resources.Resources;

public final class Skins {
  private static final String[] skinStrings;
  private static final List<Skin> skinInstances;

  private Skins() {

  }

  static {
    skinStrings = Resources.strings().getList("skins.txt");
    skinInstances = new ArrayList<>();
    for (int i = 0; i < skinStrings.length; i++) {
      skinInstances.add(new Skin(Arrays.asList(skinStrings[i].split(",")).stream().map(Color::decode).collect(Collectors.toList())));
    }
  }

  public static List<Skin> getAll() {
    return skinInstances;
  }

  public static Skin getRandom() { return Game.random().choose(getAll()); }
}
