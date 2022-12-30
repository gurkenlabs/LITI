package de.gurkenlabs.liti.constants;

import de.gurkenlabs.liti.GameManager;

public class Timings {
  public static final int COUNTDOWN_RESPAWN = 5000;
  public static final int COOLDOWN_OBJECTIVE = 20000;

  public static final int COUNTDOWN_PREGAME_DEBUG = 500;
  public static final int COUNTDOWN_PREGAME = GameManager.DBG_SKIP_TO_INGAME ? COUNTDOWN_PREGAME_DEBUG : 5000;

  public static final int COUNTDOWN_LOBBY = 5000;
  public static final int LOADING_DURATION = 6000;
  public static final int CLIFF_FALL_DURATION = 1000;

  public static final int DELAY_UI_INPUT = 250;
  public static final int DELAY_UI_LOGEVENT = 5000;

  public static final int DURATION_MUSHROOMFRENZY = 5000;
  public static final int DELAY_HIT_MUSHROOMPROJECTILE = 100;

  public static final int EP_GAIN_INTERVAL = 5000;

  public static final int INTERACT_COOLDOWN = 1000;
  public static final int STAMINA_DEPLETION_DELAY = 3000;
  public static final int BLOCK_COOLDOWN = 500;

  private Timings() {

  }
}
