package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.EntityYComparator;
import de.gurkenlabs.litiengine.entities.Prop;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.graphics.ShapeRenderer;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.util.Imaging;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class IngameScreen extends LitiScreen {
  private Hud hud;

  private static IngameScreen instance;
  private final EntityYComparator entityComparator = new EntityYComparator();

  private IngameScreen() {
    super("INGAME");
  }

  public static IngameScreen instance(){
    if(instance == null){
      instance = new IngameScreen();
    }

    return instance;
  }

  public Hud getHud(){
    return this.hud;
  }

  @Override
  public void prepare() {
    super.prepare();
    Environment plateau = Game.world().loadEnvironment("plateau2");
    plateau.onRendered(RenderType.OVERLAY, (g, t) -> {
      for(Player player : Players.getAll()){

        if(!Game.graphics().canRender(player)){
          continue;
        }

        for(Prop prop : Game.world().environment().getProps()){
          if(prop.getBoundingBox().intersects(player.getBoundingBox())
                  && (prop.getCenter().distance(player.getCenter()) < 10
                      || prop.getBoundingBox().contains(player.getCollisionBox().getBounds2D()))
                  && this.entityComparator.compare(prop, player) > 0){
            final IEntityAnimationController<?> animationController = player.animations();
              final BufferedImage img = animationController.getCurrentImage();
              if (img != null) {
                if (animationController.isAutoScaling()) {
                  final double ratioX = player.getWidth() / img.getWidth();
                  final double ratioY = player.getHeight() / img.getHeight();
                  ImageRenderer.renderScaled(g, img, Game.world().camera().getViewportLocation(player.getLocation()), ratioX, ratioY);
                } else {
                  // center the image relative to the entity dimensions -> the pivot point for rendering is the center of the entity
                  double deltaX = (player.getWidth() - img.getWidth()) / 2.0;
                  double deltaY = (player.getHeight() - img.getHeight()) / 2.0;

                  final AffineTransform transform = animationController.getAffineTransform();
                  if (transform != null) {
                    // center the scaled image relative to the desired render location if the transform provides a scaling element
                    deltaX += (img.getWidth() - (img.getWidth() * transform.getScaleX())) / 2.0;
                    deltaY += (img.getHeight() - (img.getHeight() * transform.getScaleY())) / 2.0;
                  }

                  Point2D renderLocation = Game.world().camera().getViewportLocation(player.getX() + deltaX, player.getY() + deltaY);

                  Composite comp = g.getComposite();
                  g.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT, 0.5f));
                  ImageRenderer.renderTransformed(g, img, renderLocation.getX(), renderLocation.getY(), transform);

                  g.setComposite(comp);
                }
              }

            break;
          }
        }
      }
    });
  }

  @Override
  protected void initializeComponents() {
    super.initializeComponents();
    this.hud = new Hud();
    this.getComponents().add(this.hud);
  }

  @Override
  public void render(final Graphics2D g) {
    if (Game.world().environment() != null) {
      Game.world().environment().render(g);
    }

    super.render(g);
  }

  @Override public boolean canPressDirection(int player, Direction direction) {
    return false;
  }

  @Override public boolean canPressMenu(int player) {
    return false;
  }

  @Override public boolean canPressInfo(int player) {
    return false;
  }

  @Override public boolean canPressConfirm(int player) {
    return false;
  }

  @Override public boolean canPressCancel(int player) {
    return false;
  }
}
