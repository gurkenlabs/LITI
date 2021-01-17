package de.gurkenlabs.liti.entities;

import de.gurkenlabs.liti.GameManager;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.EntityListener;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.environment.Environment;

import java.util.function.Consumer;

public class PlayerProgress implements IUpdateable {
  public static final int EP_GAIN_INTERVAL = 5000;
  public static final int EP_KILL = 200;
  public static final int EP_OBJECTIVE = 500;
  private static final int EP_GAIN_COMBAT_DISTANCE = 50;
  private static final int EP_GAIN_PERIODIC_CLOSETOACTION = 20;
  private static final int EP_GAIN_PERIODIC_INCOMBAT = 10;

  private final Player player;
  private EvolutionInterval interval;

  private Stage currentStage = Stage.STAGE0;
  private int evolutionPoints = 0;

  private long lastPeriodicEP;

  public PlayerProgress(Player player) {
    this.player = player;

    Game.loop().attach(this);

    player.addListener(new EntityListener() {
      @Override
      public void loaded(IEntity entity, Environment environment) {
        PlayerProgress.this.lastPeriodicEP = Game.time().now();
      }
    });

    this.interval = new EvolutionInterval(this.player);
  }

  public int getEvolutionPoints() {
    return this.evolutionPoints;
  }

  public Stage getCurrentStage() {
    return this.currentStage;
  }

  public void grantEP(int points) {
    if (this.getCurrentStage().next() == null || points <= 0) {
      // don't collect any XP if max stage is reached
      return;
    }

    if (this.getCurrentStage().next() != null && this.getEvolutionPoints() + points > this.getCurrentStage().next().requiredEP) {
      int remaining = this.getEvolutionPoints() + points - this.getCurrentStage().next().requiredEP;
      this.transitionToNextStage();
      this.grantEP(remaining);
      return;
    }

    this.evolutionPoints += points;
    System.out.println(this.player + ": +" + points + "EP - " + String.format("%.0f%%",this.getRelativeEP() * 100));
    //Game.world().environment().add(new FloatingCombatTextEmitter(points + "EP", new Point2D.Double(this.player.getCenter().getX() - 17, this.player.getY()), Color.YELLOW));
  }

  public double getRelativeEP() {
    return this.getCurrentStage().next() == null ? 0 : this.evolutionPoints / (double)this.getCurrentStage().next().requiredEP;
  }

  private void transitionToNextStage() {
    this.currentStage = this.currentStage.next();
    this.evolutionPoints = 0;
    this.currentStage.stageReached.accept(this.player);
    System.out.println(this.player + " reached stage " + this.currentStage.index);
  }

  @Override
  public void update() {
    if(!this.player.isLoaded() || GameManager.getGameState() != GameManager.GameState.INGAME){
      return;
    }

    if(Game.time().since(this.lastPeriodicEP) > EP_GAIN_INTERVAL){
      this.awardPeriodicEP();
    } else{
      this.getInterval().update();
    }
  }

  private void awardPeriodicEP() {
    this.lastPeriodicEP = Game.time().now();

    int periodicEP = 0;
    if(this.getInterval().didSomething) {
      if (this.getInterval().closeToPlayersOrObjective) {
        periodicEP += EP_GAIN_PERIODIC_CLOSETOACTION;
      }

      if (this.getInterval().inCombat) {
        periodicEP += EP_GAIN_PERIODIC_INCOMBAT;
      }
    }

    this.grantEP(periodicEP);

    // clean interval data
    this.interval = new EvolutionInterval(this.player);
  }

  public EvolutionInterval getInterval() { return interval; }

  public static class Stage {
    public static Stage STAGE0 = new Stage(0, 0, player -> {}, 3);
    public static Stage STAGE1 = new Stage(1, 1000, GameManager::stage1Reached, 5);
    public static Stage STAGE2 = new Stage(2, 2000, GameManager::stage2Reached, 8);
    public static Stage STAGE3 = new Stage(3, 2000, GameManager::stage3Reached, 8);

    private final int index;
    private final int requiredEP;
    private final Consumer<Player> stageReached;
    private final int respawn;

    private Stage(int index, int requiredEP, Consumer<Player> stageReached, int respawn) {
      this.index = index;
      this.requiredEP = requiredEP;
      this.stageReached = stageReached;
      this.respawn = respawn;
    }
    public int getIndex(){
      return this.index;
    }

    public int getRequiredEP(){
      return this.requiredEP;
    }

    public Stage next() {
      switch (this.index) {
        case 0:
          return STAGE1;
        case 1:
          return STAGE2;
        case 2:
          return STAGE3;
        default:
          return null;
      }
    }
  }

  public static class EvolutionInterval implements IUpdateable {
    private final Player player;
    private boolean didSomething;
    private boolean closeToPlayersOrObjective;
    private boolean inCombat;
    private EvolutionInterval(Player player){
      this.player = player;
    }

    public void inCombat(){
      this.inCombat = true;
    }

    /**
     * Enables a flag that indicates player activity and that EP should be awarded for this interval.
     */
    public void didSomething(){
      this.didSomething = true;
    }

    @Override
    public void update() {
      if(!this.didSomething) {
        this.didSomething = this.player.movement().getVelocity() > 0;
      }

      if(!this.closeToPlayersOrObjective){
        for(ICombatEntity other : Game.world().environment().getCombatEntities()) {
          if(other.equals(this.player)){
            continue;
          }

          if(!(other instanceof Player) && !(other instanceof Egg) && !(other instanceof Chicken)){
            continue;
          }

          if(other.getCenter().distance(this.player.getCenter()) < PlayerProgress.EP_GAIN_COMBAT_DISTANCE) {
            this.closeToPlayersOrObjective = true;
            break;
          }
        }
      }
    }
  }
}
