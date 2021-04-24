package de.gurkenlabs.liti.gui;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.screens.Screen;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.awt.*;

public class SplashScreen extends LitiScreen {
  private EmbeddedMediaPlayer mediaPlayer;

  public SplashScreen() {
    super("SPLASH");
  }

  @Override
  public void prepare() {
    super.prepare();
    mediaPlayer.controls().play();

  }

  @Override
  public void render(Graphics2D g) {
    super.render(g);
  }

  @Override
  public void suspend() {
    super.suspend();
  }

  @Override
  protected void initializeComponents() {
    super.initializeComponents();
    MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
    mediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
    mediaPlayer.videoSurface().set(mediaPlayerFactory.videoSurfaces().newVideoSurface(Game.window().getRenderComponent()));
    mediaPlayer.media().prepare("videos/intro.mp4");
    mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
      @Override
      public void finished(MediaPlayer mediaPlayer) {
        super.playing(mediaPlayer);
        ((EmbeddedMediaPlayer) mediaPlayer).videoSurface().set(null);
        mediaPlayer.release();
        Game.screens().display("MENU");
        Game.window().getRenderComponent().repaint();
      }
    });

  }

}
