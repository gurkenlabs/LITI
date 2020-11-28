package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.IRenderable;
import de.gurkenlabs.litiengine.gui.GuiComponent;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//TODO: dispatching delay
public final class Hud extends GuiComponent implements IRenderable {
  private static final Color COLOR_HEALTH_BG = new Color(40, 42, 43, 200);
  private static final Color COLOR_HEALTH = new Color(225, 109, 115);
  private static final Color COLOR_HEALTH_LOW = Color.RED;
  private static final Color COLOR_STAMINA = new Color(232, 230, 215);

  private static final int UI_INPUT_DELAY = 100;
  private static Map<Integer, Long> lastInputs = new ConcurrentHashMap<>();

  Hud() {
    super(0, 0);
  }

  @Override
  public void render(Graphics2D g) {
    super.render(g);

    for (Player player : Players.getAll().values()) {
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

      g.setColor(COLOR_HEALTH_BG);
      Game.graphics().renderShape(g, rect);

      if (player.getHitPoints().get() < 33) {
        g.setColor(COLOR_HEALTH_LOW);
      } else {
        g.setColor(COLOR_HEALTH);
      }

      Game.graphics().renderShape(g, healthbar);

      g.setColor(COLOR_HEALTH_BG);
      Rectangle2D staminaRect = new Rectangle2D.Double(x + (width - staminaWidth) / 2.0, y + height + 1 + (staminaHeight - staminaBgHeight) / 2.0, staminaWidth, staminaBgHeight);
      Game.graphics().renderShape(g, staminaRect);

      g.setColor(COLOR_STAMINA);

      double currentStaminaWidth = player.getStamina().getRelativeCurrentValue() * staminaWidth;
      double staminaX = x + (width - staminaWidth) / 2.0 + (staminaWidth - currentStaminaWidth) / 2.0;
      Rectangle2D stamina = new Rectangle2D.Double(staminaX, y + height + 1, currentStaminaWidth, staminaHeight);

      Game.graphics().renderShape(g, stamina);
    }
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

  public static void direction(int player, Direction direction) {
    if (!checkInputDelay(player)) {
      return;
    }

    getLitiScreen().dispatchDirection(player, direction);
    lastInputs.put(player, Game.time().now());
  }

  private static LitiScreen getLitiScreen() {
    if (Game.screens().current() instanceof LitiScreen) {
      return (LitiScreen) Game.screens().current();
    }

    throw new IllegalArgumentException("Sceens need to inherit from LitiScreen!");
  }

  private static boolean checkInputDelay(int player) {
    if (!lastInputs.containsKey(player)) {
      return true;
    }

    return Game.time().since(lastInputs.get(player)) > UI_INPUT_DELAY;
  }
}
