package au.edu.unimelb.swen.oop.mokepon;

import java.util.HashMap;
import java.util.Map;

/** A battle owns two independent fighters, even when both use the same Mokepon. */
public class Fighter {
  private static final int BLEED_DURATION = 2;
  private static final int STATUS_DURATION = 3;
  private static final int DAMAGE_SCALE = 50;
  private static final double TERRAIN_ADJUSTMENT = 0.5;
  private static final double REDUCED_DAMAGE = 0.5;
  private final Mokepon mokepon;
  private final Map<Stat, Integer> stats = new HashMap<>();
  private int hp;
  private int energy;
  private int bleedTurns;
  private Fighter bleedSource;
  private int stunTurns;
  private int blindTurns;
  private int stunVersion;
  private int blindVersion;
  private int actionStunVersion = -1;
  private int actionBlindVersion = -1;

  public Fighter(Mokepon mokepon) {
    this(mokepon, Terrain.PATH);
  }

  public Fighter(Mokepon mokepon, Terrain terrain) {
    this.mokepon = mokepon;
    double multiplier = 1 + TERRAIN_ADJUSTMENT * mokepon.getType().advantage(terrain);
    for (Stat stat : Stat.getAll()) {
      int adjusted = (int) Math.round(stat.baseValue(mokepon) * multiplier);
      stats.put(stat, Math.max(1, Math.min(stat.maximum(), adjusted)));
    }
    restoreHealth();
    restoreEnergy();
  }

  public Mokepon getMokepon() {
    return mokepon;
  }

  public int getHp() {
    return hp;
  }

  public int getEnergy() {
    return energy;
  }

  public int getStat(Stat stat) {
    return stats.get(stat);
  }

  public int getBleedTurns() {
    return bleedTurns;
  }

  public int getStunTurns() {
    return stunTurns;
  }

  public int getBlindTurns() {
    return blindTurns;
  }

  public boolean isFainted() {
    return hp == 0;
  }

  public void damage(int amount) {
    hp = Math.max(0, hp - amount);
  }

  public void restoreHealth() {
    hp = getStat(Stat.HP);
  }

  public void restoreEnergy() {
    energy = getStat(Stat.ENERGY);
  }

  public void poison() {
    hp = 1;
  }

  public void spendEnergy(int cost) {
    energy = Math.max(0, Math.min(getStat(Stat.ENERGY), energy - cost));
  }

  public int raise(Stat stat, int amount) {
    int increase = Math.min(amount, stat.maximum() - getStat(stat));
    stats.put(stat, getStat(stat) + increase);
    return increase;
  }

  public int attackDamage(Fighter target, double strengthMultiplier) {
    double damage = getStat(Stat.STRENGTH) * strengthMultiplier
        * DAMAGE_SCALE / target.getStat(Stat.DEFENCE);
    if (stunTurns > 0) {
      damage *= REDUCED_DAMAGE;
    }
    return (int) damage;
  }

  public void startBleeding(Fighter attacker) {
    bleedSource = attacker;
    bleedTurns = BLEED_DURATION;
  }

  public int bleed() {
    int amount = bleedSource.attackDamage(this, REDUCED_DAMAGE);
    damage(amount);
    bleedTurns--;
    return amount;
  }

  public void stun() {
    stunTurns = STATUS_DURATION;
    stunVersion++;
  }

  public void blind() {
    blindTurns = STATUS_DURATION;
    blindVersion++;
  }

  public void beginAction() {
    actionStunVersion = stunTurns > 0 ? stunVersion : -1;
    actionBlindVersion = blindTurns > 0 ? blindVersion : -1;
  }

  public void endRound() {
    // A newly applied/refreshed effect must still cover three affected actions.
    if (actionStunVersion == stunVersion && stunTurns > 0) {
      stunTurns--;
    }
    if (actionBlindVersion == blindVersion && blindTurns > 0) {
      blindTurns--;
    }
    actionStunVersion = -1;
    actionBlindVersion = -1;
  }
}
