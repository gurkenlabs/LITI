package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.entities.PlayerConfiguration;
import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class LobbyScreen extends LitiScreen {
  private CharacterSelectionComponent[] charSelects;
  private static final BufferedImage TITLE = Resources.images().get("lobby-title.png");
  private static final BufferedImage BACKGROUND = Resources.images().get("lobby-background.png");
  private CountdownComponent countdown;

  public LobbyScreen() {
    super("LOBBY");
  }

  @Override
  public void render(Graphics2D g) {
    ImageRenderer.render(g, BACKGROUND, 0, 0);
    super.render(g);
    if (!countdown.isActive()) {
      double offsetY = TITLE.getHeight() / 4d * Math.sin(Game.time().sinceEnvironmentLoad() / 600.0);
      double titleRefY = Game.window().getHeight() / 11d;
      ImageRenderer.render(g, TITLE, Game.window().getCenter().getX() - TITLE.getWidth() / 2d,
          titleRefY - TITLE.getHeight() / 2d + offsetY);
      //    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
  }

  public boolean allPlayersReady() {
    int readyPlayers = 0;
    for (PlayerConfiguration c : Players.getConfigurations()) {
      if (charSelects[c.getIndex()].isReady()) {
        readyPlayers++;
      }
    }
    return readyPlayers > 1;
  }

  @Override public boolean canPressDirection(int player, Direction direction) {
    return this.charSelects[player].hasPlayerAssigned() && !this.charSelects[player].isReady();
  }

  @Override public boolean canPressMenu(int player) {
    return !this.charSelects[player].hasPlayerAssigned();
  }

  @Override public boolean canPressInfo(int player) {
    return this.charSelects[player].hasPlayerAssigned() && !this.charSelects[player].isReady();
  }

  @Override public boolean canPressConfirm(int player) {
    return !this.charSelects[player].isReady();
  }

  @Override public boolean canPressCancel(int player) {
    return this.charSelects[player].hasPlayerAssigned();
  }

  @Override
  public void dispatchCancel(int player) {
    if (!this.canPressCancel(player)) {
      return;
    }
    super.dispatchCancel(player);
    CharacterSelectionComponent charSelect = this.charSelects[player];
    if (charSelect.hasPlayerAssigned() && !charSelect.isReady()) {
      charSelect.removePlayer();

    } else if (charSelect.isReady()) {
      charSelect.unready();
      if (countdown.isActive()) {
        countdown.stop();
        for (CharacterSelectionComponent c : this.charSelects) {
          c.showReadyText(c.isReady());
        }
      }
    }
  }

  @Override public void dispatchMenu(int player) {
    if (!this.canPressMenu(player)) {
      return;
    }
    super.dispatchMenu(player);
  }

  @Override
  public void dispatchConfirm(int player) {
    if (!this.canPressConfirm(player)) {
      return;
    }
    super.dispatchConfirm(player);

    CharacterSelectionComponent charSelect = this.charSelects[player];
    if (!charSelect.hasPlayerAssigned()) {
      charSelect.assignPlayer();

    } else if (!charSelect.isReady()) {
      charSelect.ready();
      if (allPlayersReady()) {
        for (CharacterSelectionComponent c : this.charSelects) {
          c.showReadyText(false);
        }
        countdown.start();

      }
    }
  }

  @Override
  public void dispatchInfo(int player) {
    if (!this.canPressInfo(player)) {
      return;
    }
    super.dispatchInfo(player);
    this.charSelects[player].info();
  }

  @Override
  public void dispatchDirection(int player, Direction direction) {
    super.dispatchDirection(player, direction);
    if (!this.canPressDirection(player, direction)) {
      return;
    }
    switch (direction) {
    case UP:
      this.charSelects[player].previousSkin();
      break;
    case DOWN:
      this.charSelects[player].nextSkin();
      break;
    case LEFT:
      this.charSelects[player].previousClass();
      break;
    case RIGHT:
      this.charSelects[player].nextClass();
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

    countdown = new CountdownComponent(Game.window().getCenter().getX() - compHeight / 2d, Game.window().getCenter().getY() - compHeight / 2d,
        compHeight, compHeight, 5000, true);

    countdown.addListener(new CountdownListener() {
      @Override public void stopped() {
        startGame();
      }
    });
    this.getComponents().add(countdown);
  }

  private void startGame() {
    Game.screens().display("LOADING");
  }
}
