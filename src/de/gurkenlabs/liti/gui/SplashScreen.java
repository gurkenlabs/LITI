package de.gurkenlabs.liti.gui;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class SplashScreen extends LitiScreen {

  private MediaPlayer mediaPlayer;
  private JFXPanel fxPanel;


  public SplashScreen() {
    super("SPLASH");
    initVideoPlayer();
  }

  @Override
  public void prepare() {
    super.prepare();
    playIntro();
  }

  @Override
  public void render(Graphics2D g) {
    super.render(g);
  }

  @Override
  public void suspend() {
    super.suspend();
    mediaPlayer.stop();
    fxPanel.removeAll();
    fxPanel.setVisible(false);
    mediaPlayer.dispose();
    Game.window().getHostControl().remove(fxPanel);
  }

  @Override
  public void dispatchCancel(int player) {
    super.dispatchCancel(player);
    endIntro();
  }

  @Override
  public void dispatchMenu(int player) {
    super.dispatchMenu(player);
    endIntro();
  }

  @Override
  public void dispatchInfo(int player) {
    super.dispatchInfo(player);
    endIntro();
  }

  @Override
  public void dispatchDirection(int player, Direction direction) {
    super.dispatchDirection(player, direction);
    endIntro();
  }

  @Override
  public void dispatchConfirm(int player) {
    super.dispatchConfirm(player);
    endIntro();
  }

  @Override
  protected void initializeComponents() {
    super.initializeComponents();
    getAppearance().setBackgroundColor1(new Color(5, 67, 23, 10));
    getAppearance().setBackgroundColor2(new Color(5, 67, 23, 120));
    getAppearance().setTransparentBackground(true);
  }

  private void initVideoPlayer() {
    this.fxPanel = new JFXPanel();
    Media media = new Media(new File("videos/LITI-intro.mp4").toURI().toString());
    this.mediaPlayer = new MediaPlayer(media);
    MediaView mediaView = new MediaView(mediaPlayer);
    fxPanel.setPreferredSize(Game.window().getSize());
    Scene scene = new Scene(new Group(mediaView));
    fxPanel.setScene(scene);
  }

  private void playIntro() {
    Game.window().getHostControl().add(fxPanel);
    fxPanel.getGraphics().setColor(Color.CYAN);
    fxPanel.getGraphics().drawRect(20, 20, 50, 50);
    fxPanel.setVisible(true);
    mediaPlayer.play();
  }


  private void endIntro() {
    suspend();
    Game.screens().display("LOBBY");
  }
}
