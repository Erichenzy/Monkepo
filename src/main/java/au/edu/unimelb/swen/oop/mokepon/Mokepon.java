package au.edu.unimelb.swen.oop.mokepon;

import java.util.ArrayList;
import java.util.List;

public class Mokepon {
  private final String name;
  private final String image;
  private final int hp;
  private final int energy;
  private final int strength;
  private final int accuracy;
  private final int defence;
  private final int dodge;
  private final List<String> skills;
  private final MokeponType type;

  public Mokepon(String name, String image, int hp, int energy, int strength,
          int accuracy, int defence, int dodge, List<String> skills) {
    this(name, image, hp, energy, strength, accuracy, defence, dodge, skills,
        MokeponType.NORMAL);
  }

  public Mokepon(String name, String image, int hp, int energy, int strength,
          int accuracy, int defence, int dodge, List<String> skills, MokeponType type) {
    this.name = name;
    this.image = image;
    this.hp = hp;
    this.energy = energy;
    this.strength = strength;
    this.accuracy = accuracy;
    this.defence = defence;
    this.dodge = dodge;
    this.skills = new ArrayList<>(skills);
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public String getImage() {
    return image;
  }

  public int getHp() {
    return hp;
  }

  public int getEnergy() {
    return energy;
  }

  public int getStrength() {
    return strength;
  }

  public int getAccuracy() {
    return accuracy;
  }

  public int getDefence() {
    return defence;
  }

  public int getDodge() {
    return dodge;
  }

  public List<String> getSkills() {
    return new ArrayList<>(skills);
  }

  public MokeponType getType() {
    return type;
  }
}
