package de.gurkenlabs.liti.gui;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.util.Imaging;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LobbyScreen extends LitiScreen {
  private CharacterSelectionComponent[] charSelects;
  private static final BufferedImage TITLE = Resources.images().get("lobby-title.png");
  private static final BufferedImage BACKGROUND = Resources.images().get("lobby-background.png");

  public LobbyScreen() {
    super("LOBBY");
  }

  @Override
  public void render(Graphics2D g) {
    ImageRenderer.render(g, BACKGROUND, 0, 0);
    super.render(g);
    //    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    double scale = 1.0 + 0.10 * Math.sin(Game.time().sinceEnvironmentLoad() / 500.0);
    double titleRefY = Game.window().getHeight() * 1 / 12d;
    ImageRenderer.render(g, Imaging.scale(TITLE, scale), Game.window().getCenter().getX() - (scale * TITLE.getWidth()) / 2,
        titleRefY - (scale * TITLE.getHeight()) / 2);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

  }

  @Override
  public void dispatchCancel(int player) {
    super.dispatchCancel(player);
    CharacterSelectionComponent charSelect = this.charSelects[player];
    if (!charSelect.hasPlayerAssigned()) {
      return;
    }
    if (charSelect.hasPlayerAssigned() && !charSelect.isReady()) {
      charSelect.removePlayer();

    } else if (charSelect.isReady()) {
      charSelect.unready();
    }
  }

  @Override
  public void dispatchConfirm(int player) {
    super.dispatchConfirm(player);
    CharacterSelectionComponent charSelect = this.charSelects[player];
    if (!charSelect.hasPlayerAssigned()) {
      charSelect.assignPlayer();

    } else if (!charSelect.isReady()) {
      charSelect.ready();
    }
  }

  @Override
  public void dispatchInfo(int player) {
    super.dispatchInfo(player);
    CharacterSelectionComponent charSelect = this.charSelects[player];
    if (charSelect.hasPlayerAssigned()) {
      charSelect.info();
    }
  }

  @Override
  public void dispatchDirection(int player, Direction direction) {
    super.dispatchDirection(player, direction);
    CharacterSelectionComponent charSelect = this.charSelects[player];
    if (!charSelect.hasPlayerAssigned()) {
      return;
    }
    switch (direction) {
    case UP:
      charSelect.previousSkin();
      break;
    case DOWN:
      charSelect.nextSkin();
      break;
    case LEFT:
      charSelect.previousClass();
      break;
    case RIGHT:
      charSelect.nextClass();
      break;
    default:
      break;
    }

  }

  @Override
  protected void initializeComponents() {
    super.initializeComponents();
    double compWidth = Game.window().getResolution().getWidth() * 1 / 8d;
    double compHeight = Game.window().getResolution().getHeight() * 1 / 3d;
    double compY = Game.window().getResolution().getHeight() * 1 / 5d;
    this.charSelects = new CharacterSelectionComponent[4];
    for (int i = 0; i <= 3; i++) {
      charSelects[i] = new CharacterSelectionComponent(compWidth / 2 + i * (compWidth * 2d), compY, compWidth, compHeight, i);
      this.getComponents().add(charSelects[i]);
    }
  }
}
