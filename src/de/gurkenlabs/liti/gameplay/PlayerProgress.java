package de.gurkenlabs.liti.gameplay;

import de.gurkenlabs.liti.GameManager;
import de.gurkenlabs.liti.GameState;
import de.gurkenlabs.liti.constants.Timings;
import de.gurkenlabs.liti.entities.Chicken;
import de.gurkenlabs.liti.entities.Egg;
import de.gurkenlabs.liti.entities.Player;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.EntityListener;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.environment.Environment;

public class PlayerProgress implements IUpdateable {
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

    if (this.getCurrentStage().next() != null && this.getEvolutionPoints() + points > this.getCurrentStage().next().getRequiredEP()) {
      int remaining = this.getEvolutionPoints() + points - this.getCurrentStage().next().getRequiredEP();
      this.transitionToNextStage();
      this.grantEP(remaining);
      return;
    }

    this.evolutionPoints += points;
    System.out.println(this.player + ": +" + points + "EP - " + String.format("%.0f%%",this.getRelativeEP() * 100));
    //Game.world().environment().add(new FloatingCombatTextEmitter(points + "EP", new Point2D.Double(this.player.getCenter().getX() - 17, this.player.getY()), Color.YELLOW));
  }

  public double getRelativeEP() {
    return this.getCurrentStage().next() == null ? 0 : this.evolutionPoints / (double)this.getCurrentStage().next().getRequiredEP();
  }

  private void transitionToNextStage() {
    this.currentStage = this.currentStage.next();
    this.evolutionPoints = 0;
    this.currentStage.uponStageReached().accept(this.player);
    System.out.println(this.player + " reached stage " + this.currentStage.getIndex());
  }

  @Override
  public void update() {
    if(!this.player.isLoaded() || GameManager.getGameState() != GameState.INGAME){
      return;
    }

    if(Game.time().since(this.lastPeriodicEP) > Timings.EP_GAIN_INTERVAL){
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
