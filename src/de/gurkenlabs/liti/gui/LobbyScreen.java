package de.gurkenlabs.liti.gui;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.util.Imaging;

public class LobbyScreen extends LitiScreen {
  private CharacterSelectionComponent[] charSelects;
  private static final BufferedImage TITLE = Imaging.scale(Resources.images().get("lobby_title.png"), Game.window().getWidth() * 1 / 8);

  public LobbyScreen() {
    super("LOBBY");
  }

  @Override
  public void render(Graphics2D g) {
    super.render(g);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    double scale = 1.4 + 0.15 * Math.sin(Game.time().sinceEnvironmentLoad() / 400.0);
    ImageRenderer.render(g, Imaging.scale(TITLE, scale), Game.window().getCenter().getX() - (scale * TITLE.getWidth()) / 2,
        Game.window().getHeight() * 2 / 14d - (scale * TITLE.getHeight()) / 2);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

  }

  @Override
  public void dispatchCancel(int player) {
    super.dispatchCancel(player);
    CharacterSelectionComponent charSelect = this.charSelects[player - 1];
    if (!charSelect.hasPlayerAssigned()) {
      charSelect.assignPlayer();

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
    CharacterSelectionComponent charSelect = this.charSelects[player - 1];
    if (!charSelect.hasPlayerAssigned()) {
      charSelect.assignPlayer();

    } else if (!charSelect.isReady()) {
      charSelect.ready();
    }
  }

  @Override
  public void dispatchDirection(int player, Direction direction) {
    super.dispatchDirection(player, direction);
    CharacterSelectionComponent charSelect = this.charSelects[player - 1];
    if (!charSelect.hasPlayerAssigned()) {
      charSelect.assignPlayer();

    }
    switch (direction) {
    case UP:
      charSelect.previousCostume();
      break;
    case DOWN:
      charSelect.nextCostume();
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
    double compWidth = Game.window().getResolution().getWidth() / 6d;
    double compHeight = Game.window().getResolution().getHeight() * 4 / 8d;
    double compY = Game.window().getResolution().getHeight() * 3 / 10d;
    this.charSelects = new CharacterSelectionComponent[4];
    for (int i = 0; i <= 3; i++) {
      charSelects[i] = new CharacterSelectionComponent(compWidth / 2 + i * (compWidth + compWidth / 3d), compY, compWidth, compHeight);
      this.getComponents().add(charSelects[i]);
    }
  }
}
