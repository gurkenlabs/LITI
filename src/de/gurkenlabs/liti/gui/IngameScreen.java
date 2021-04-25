package de.gurkenlabs.liti.gui;

import de.gurkenlabs.liti.entities.Chicken;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.liti.entities.Players;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.EntityYComparator;
import de.gurkenlabs.litiengine.entities.ICollisionEntity;
import de.gurkenlabs.litiengine.entities.IEntity;
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
import java.util.ArrayList;

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
    plateau.onRendered(RenderType.OVERLAY, this::renderPlayersBehindProps);
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

  private void renderPlayersBehindProps(Graphics2D g, RenderType renderType) {
    ArrayList<ICollisionEntity> entities = new ArrayList<>();

    boolean chickenAdded = false;
    for(Player player : Players.getAll()){
      if(!Game.graphics().canRender(player) || !player.isLoaded()){
        continue;
      }
      entities.add(player);
      if(player.getCurrentChicken() != null){
        entities.add(player.getCurrentChicken());
        chickenAdded = true;
      }
    }

    if(!chickenAdded) {
      entities.addAll(Game.world().environment().getEntities(Chicken.class));
    }

    for(ICollisionEntity entity : entities){
      for(Prop prop : Game.world().environment().getProps()){
        if(prop.getBoundingBox().intersects(entity.getBoundingBox())
                && (prop.getCenter().distance(entity.getCenter()) < 10
                || prop.getBoundingBox().contains(entity.getCollisionBox())
                && this.entityComparator.compare(prop, entity) > 0)){
          final IEntityAnimationController<?> animationController = entity.animations();
          final BufferedImage img = animationController.getCurrentImage();
          if (img != null) {
            // center the image relative to the entity dimensions -> the pivot point for rendering is the center of the entity
            double deltaX = (entity.getWidth() - img.getWidth()) / 2.0;
            double deltaY = (entity.getHeight() - img.getHeight()) / 2.0;

            final AffineTransform transform = animationController.getAffineTransform();
            if (transform != null) {
              // center the scaled image relative to the desired render location if the transform provides a scaling element
              deltaX += (img.getWidth() - (img.getWidth() * transform.getScaleX())) / 2.0;
              deltaY += (img.getHeight() - (img.getHeight() * transform.getScaleY())) / 2.0;
            }

            Point2D renderLocation = Game.world().camera().getViewportLocation(entity.getX() + deltaX, entity.getY() + deltaY);

            Composite comp = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT, 0.5f));
            ImageRenderer.renderTransformed(g, img, renderLocation.getX(), renderLocation.getY(), transform);

            g.setComposite(comp);
          }

          break;
        }
      }
    }
  }
}
