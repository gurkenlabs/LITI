package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.GameManager;
import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.constants.Timings;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.graphics.IRenderable;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.util.MathUtilities;
import de.gurkenlabs.litiengine.util.TimeUtilities;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public final class Hud extends GuiComponent implements IRenderable {


  private CountdownComponent preGameCountdown;

  Hud() {
    super(0, 0);
  }

  @Override
  public void render(Graphics2D g) {
    super.render(g);

    this.renderHealthAndStaminaBars(g);
    this.renderRespawnTimer(g);

    if (this.preGameCountdown.hasFinished()) {
      this.preGameCountdown.stop();

      // TODO: reset for consequtive games after game is finished or before a new one is started
    }

    this.renderWinner(g);
  }

  private void renderWinner(Graphics2D g) {
    if (GameManager.getGameState() == GameManager.GameState.FINISHED) {

      // TODO: component for this + animation via tweening
      TextRenderer.render(g, "Player " + (GameManager.getWinner().getConfiguration().getIndex() + 1) + " won!", Game.window().getCenter());
    }

  }

  public CountdownComponent getPregameCountdown() {
    return preGameCountdown;
  }


  @Override
  protected void initializeComponents() {
    super.initializeComponents();
    double countdownSize = Game.window().getResolution().getHeight() * 1 / 3d;

    this.preGameCountdown = new CountdownComponent(Game.window().getCenter().getX() - countdownSize / 2d, Game.window().getCenter().getY() - countdownSize / 2d, countdownSize, countdownSize, Timings.COUNTDOWN_PREGAME, true);

    this.getComponents().add(this.preGameCountdown);
  }


  private void renderHealthAndStaminaBars(Graphics2D g) {
    for (Player player : Players.getAll()) {
      if (!player.isLoaded() || player.isFalling() || player.isDead()) {
        continue;
      }

      double width = 16;
      double height = 1.5;
      final double staminaWidth = 14;
      final double staminaHeight = .75;
      final double staminaBgHeight = .5;

      double x = player.getX() - (width - player.getWidth()) / 2.0;
      double y = player.getY() - height * 3;

      if (player.wasHit(100)) {
        x += Game.random().nextInt(-2, 2);
        x -= 1;
        y -= 1;
        width += 2;
        height += 2;
      }

      g.setColor(LitiColors.COLOR_HEALTH_BG);
      RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 1.5, 1.5);

      final double currentWidth = width * (player.getHitPoints().get() / (double) player.getHitPoints().getMax());
      RoundRectangle2D healthbar = new RoundRectangle2D.Double(x, y, currentWidth, height, 1.5, 1.5);


      Game.graphics().renderShape(g, rect);

      if (player.getCombatStatistics().getRecentDamageReceived() > 0) {
        final double previousWidth = width * Math.min(1, (player.getHitPoints().get() + player.getCombatStatistics().getRecentDamageReceived()) / (double) player.getHitPoints().getMax());
        RoundRectangle2D previousHealthbar = new RoundRectangle2D.Double(x, y, previousWidth, height, 1.5, 1.5);

        g.setColor(LitiColors.COLOR_HEALTH_HIT);
        Game.graphics().renderShape(g, previousHealthbar);
      }

      if (player.getHitPoints().get() < 33) {
        g.setColor(LitiColors.COLOR_HEALTH_LOW);
      } else {
        g.setColor(LitiColors.COLOR_HEALTH);
      }

      Game.graphics().renderShape(g, healthbar);

      g.setColor(player.isStaminaDepleted() ? Color.getHSBColor(.9f, 0.785f, (Game.time().now() % 100) / 100f) : LitiColors.COLOR_HEALTH_BG);
      Rectangle2D staminaRect = new Rectangle2D.Double(x + (width - staminaWidth) / 2.0, y + height + 1 + (staminaHeight - staminaBgHeight) / 2.0, staminaWidth, staminaBgHeight);
      Game.graphics().renderShape(g, staminaRect);

      if (!player.isStaminaDepleted()) {
        g.setColor(LitiColors.COLOR_STAMINA);

        double currentStaminaWidth = player.getStamina().getRelativeCurrentValue() * staminaWidth;
        double staminaX = x + (width - staminaWidth) / 2.0 + (staminaWidth - currentStaminaWidth) / 2.0;
        Rectangle2D stamina = new Rectangle2D.Double(staminaX, y + height + 1, currentStaminaWidth, staminaHeight);

        Game.graphics().renderShape(g, stamina);
      }

      if (player.isBlocking()) {
        Game.graphics().renderText(g, "block", player.getCenter());
      }
    }
  }

  private void renderRespawnTimer(Graphics2D g) {
    for (Player player : Players.getAll()) {
      if (!player.isDead() || player.isLoaded()) {
        continue;
      }

      Spawnpoint spawn = GameManager.getSpawn(player);
      if (spawn == null) {
        continue;
      }

      String timeRemaining = TimeUtilities.toTimerFormat(player.getResurrection() - Game.time().since(player.getLastDeath()), TimeUtilities.TimerFormat.S_0);
      Game.graphics().renderText(g, timeRemaining, spawn.getCenter());
    }
  }
}
