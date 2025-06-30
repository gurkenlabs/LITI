package de.gurkenlabs.liti.entities;

import de.gurkenlabs.liti.abilities.Proficiency;
import de.gurkenlabs.liti.abilities.Trait;
import de.gurkenlabs.litiengine.attributes.Attribute;
import de.gurkenlabs.litiengine.attributes.RangeAttribute;
import java.util.Map;

public class PlayerTraits {
  private final Player player;
  private final RangeAttribute<Double> stamina;
  private final Attribute<Float> mobility;
  private final Attribute<Double> recovery;
  private final Attribute<Integer> damage;
  private final Attribute<Integer> range;

  PlayerTraits(Player player) {
    this.player = player;

    this.stamina =
        new RangeAttribute<>(Proficiency.get(player.getPlayerClass(), Trait.STAMINA), 0.0, Proficiency.get(player.getPlayerClass(), Trait.STAMINA));
    this.mobility = new Attribute<>((float) Proficiency.get(player.getPlayerClass(), Trait.MOBILITY));
    this.recovery = new Attribute<>(Proficiency.get(player.getPlayerClass(), Trait.RECOVERY));
    this.damage = new Attribute<>((int) Proficiency.get(player.getPlayerClass(), Trait.DAMAGE));
    this.range = new Attribute<>((int) Proficiency.get(player.getPlayerClass(), Trait.RANGE));
  }

  public RangeAttribute<Double> stamina() {
    return this.stamina;
  }

  public Attribute<Float> mobility() {
    return this.mobility;
  }

  public Attribute<Double> recovery() {
    return this.recovery;
  }

  public Attribute<Integer> damage() {
    return this.damage;
  }

  public Attribute<Integer> range() {
    return this.range;
  }

  public void buff() {
    Map<Trait, Double> buffedTraits = Proficiency.getBuffedTraits(this.player.getPlayerClass());
    for (Map.Entry<Trait, Double> trait : buffedTraits.entrySet()) {
      this.setTrait(trait.getKey(), trait.getValue());
    }
  }

  void init() {
    Map<Trait, Double> buffedTraits = Proficiency.getTraits(this.player.getPlayerClass());
    for (Map.Entry<Trait, Double> trait : buffedTraits.entrySet()) {
      this.setTrait(trait.getKey(), trait.getValue());
    }
  }

  private void setTrait(Trait trait, double value) {
    switch (trait) {
      case HEALTH:
        this.player.getHitPoints().setMax((int) value);
        break;
      case DAMAGE:
        this.damage().setValue((int) value);
        if (this.player.getBash() != null) {
          this.player.getBash().getAttributes().value().setValue((int) value);
        }
        break;
      case RANGE:
        this.range.setValue((int) value);
        if (this.player.getBash() != null) {
          this.player.getBash().getAttributes().impact().setValue((int) value);
        }
        break;
      case STAMINA:
        this.stamina().setMax(value);
        break;
      case MOBILITY:
        this.mobility().setValue((float) value);
        if (this.player.getDash() != null) {
          this.player.getDash().getAttributes().value().setValue((int) (this.mobility().getValue() * 2.2));
        }
        break;
      case RECOVERY:
        this.recovery().setValue(value);
        break;
    }
  }
}
