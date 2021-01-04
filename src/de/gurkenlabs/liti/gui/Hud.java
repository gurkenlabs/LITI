package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.GameManager;
import de.gurkenlabs.liti.constants.LitiColors;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.graphics.IRenderable;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.util.TimeUtilities;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//TODO: dispatching delay
public final class Hud extends GuiComponent implements IRenderable {

  private static final int UI_INPUT_DELAY = 250;
  private static final Map<Integer, Long> lastInputs = new ConcurrentHashMap<>();

  Hud() {
    super(0, 0);
  }

  @Override
  public void render(Graphics2D g) {
    super.render(g);

    this.renderHealthAndStaminaBars(g);
    this.renderRespawnTimer(g);
  }

  public static void cancel(int player) {
    if (!checkInputDelay(player)) {
      return;
    }

    getLitiScreen().dispatchCancel(player);
    lastInputs.put(player, Game.time().now());
  }

  public static void confirm(int player) {
    if (!checkInputDelay(player)) {
      return;
    }

    getLitiScreen().dispatchConfirm(player);
    lastInputs.put(player, Game.time().now());
  }

  public static void menu(int player) {
    if (!checkInputDelay(player)) {
      return;
    }

    getLitiScreen().dispatchMenu(player);
    lastInputs.put(player, Game.time().now());
  }

  public static void direction(int player, Direction direction) {
    if (!checkInputDelay(player)) {
      return;
    }

    getLitiScreen().dispatchDirection(player, direction);
    lastInputs.put(player, Game.time().now());
  }

  public static void info(int player) {
    if (!checkInputDelay(player)) {
      return;
    }

    getLitiScreen().dispatchInfo(player);
    lastInputs.put(player, Game.time().now());
  }

  private static LitiScreen getLitiScreen() {
    if (Game.screens().current() instanceof LitiScreen) {
      return (LitiScreen) Game.screens().current();
    }

    throw new IllegalArgumentException("Screens need to inherit from LitiScreen!");
  }

  private static boolean checkInputDelay(int player) {
    if (!lastInputs.containsKey(player)) {
      return true;
    }

    return Game.time().since(lastInputs.get(player)) > UI_INPUT_DELAY;
  }

  private void renderHealthAndStaminaBars(Graphics2D g) {
    for (Player player : Players.getAll()) {
      if (!player.isLoaded() || player.isFalling()) {
        continue;
      }

      final double width = 16;
      final double height = 2;
      final double staminaWidth = 14;
      final double staminaHeight = .75;
      final double staminaBgHeight = .5;

      double x = player.getX() - (width - player.getWidth()) / 2.0;
      double y = player.getY() - height * 3;
      RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 1.5, 1.5);

      final double currentWidth = width * (player.getHitPoints().get() / (double) player.getHitPoints().getMax());
      RoundRectangle2D healthbar = new RoundRectangle2D.Double(x, y, currentWidth, height, 1.5, 1.5);

      g.setColor(LitiColors.COLOR_HEALTH_BG);
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
      if (!player.isDead()) {
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
