package au.edu.unimelb.swen.oop.mokepon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Turn processing without JavaFX, so battle rules can be tested independently. */
public class BattleEngine {
  private final Fighter player;
  private final Fighter enemy;
  private final GameSession session;
  private final Random random;
  private String log = "";

  public BattleEngine(GameSession session, Mokepon opponent, Terrain terrain) {
    this(session, opponent, terrain, new Random());
  }

  public BattleEngine(GameSession session, Mokepon opponent, Terrain terrain, Random random) {
    this.session = session;
    this.random = random;
    player = new Fighter(session.player(), terrain);
    enemy = new Fighter(opponent, terrain);
    log = "Player chose " + player.getMokepon().getName()
        + ". Began a fight against enemy " + opponent.getName() + "!\n";
  }

  public Fighter getPlayer() {
    return player;
  }

  public Fighter getEnemy() {
    return enemy;
  }

  public boolean isOver() {
    return player.isFainted() || enemy.isFainted();
  }

  public boolean hasWon() {
    return enemy.isFainted();
  }

  public String getLog() {
    return log;
  }

  public boolean canUse(String name) {
    return !isOver() && player.getMokepon().getSkills().contains(name)
        && player.getEnergy() >= Skills.get(name).getCost();
  }

  public void playTurn(String name) {
    if (!canUse(name)) {
      return;
    }
    act(player, enemy, Skills.get(name), true);
    if (!isOver()) {
      List<Skill> affordable = new ArrayList<>();
      for (String enemySkill : enemy.getMokepon().getSkills()) {
        Skill skill = Skills.get(enemySkill);
        if (enemy.getEnergy() >= skill.getCost()) {
          affordable.add(skill);
        }
      }
      act(enemy, player, affordable.get(random.nextInt(affordable.size())), false);
    }
    if (!isOver()) {
      processBleeding(enemy);
    }
    if (!isOver()) {
      processBleeding(player);
    }
    player.endRound();
    enemy.endRound();
    if (isOver()) {
      String owner = hasWon() ? "Enemy's " : "Player's ";
      Fighter fainted = hasWon() ? enemy : player;
      log += "\n\n" + owner + fainted.getMokepon().getName() + " fainted!\n"
          + (hasWon() ? "You win\n" : "You lose\n");
    }
  }

  private void act(Fighter user, Fighter target, Skill skill, boolean playerTurn) {
    user.beginAction();
    log += (playerTurn ? "\nPlayer's " : "Enemy ")
        + user.getMokepon().getName() + " used " + skill.getName() + "!\n";
    log += "\t" + skill.use(user, target, random).replace("\n", "\n\t") + "\n";
  }

  private void processBleeding(Fighter target) {
    if (target.getBleedTurns() > 0) {
      target.bleed();
      if (target.getBleedTurns() == 0) {
        log += "\tThe bleeding stopped.\n";
      }
    }
  }

  public void useItem(ItemType type) {
    if (isOver() || !session.consume(type)) {
      return;
    }
    String targetName = player.getMokepon().getName();
    String result;
    if (type == ItemType.HEALTH) {
      player.restoreHealth();
      result = "It healed you to max hp!";
    } else if (type == ItemType.COFFEE) {
      player.restoreEnergy();
      result = "It restored all your energy!";
    } else if (type == ItemType.POISON) {
      enemy.poison();
      targetName = enemy.getMokepon().getName();
      result = "It devastated them!";
    } else {
      throw new IllegalArgumentException("Unknown item");
    }
    log += "You used your " + type.displayName() + " on " + targetName + ". "
        + result + "\n";
  }
}
