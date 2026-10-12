package au.edu.unimelb.swen.oop.mokepon;

import java.util.Random;

/** BattleEngine depends on this behaviour, not on particular skill classes. */
public interface Skill {
  String getName();
  int getCost();
  String use(Fighter user, Fighter target, Random random);
}
