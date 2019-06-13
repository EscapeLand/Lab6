package lab6;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Ladder {
//0 means nobody here; a positive integer means the id of monkey here.
  private Monkey[] ladder;
  private final int index;
  private boolean lef;
  private int numofmonkeys = 0;
//following variables are created to note the speed of whole ladder
  // and judge whether it's visible to some monkey. 
  private List<Monkey> monkeys = Collections.synchronizedList(new LinkedList<>());
  private int lastdist;//speed of whole ladder
  
  public Ladder(int lenoflad, int index) {
    this.ladder = new Monkey[lenoflad];
    this.index = index;
  }

  public void set(Monkey monkey, int index) {
    if (index == -1 || index == ladder.length) {
      return;
    }
    ladder[index] = monkey;
  }
  
  /**
   * Get the whole speed of ladder.
   * -1 means invisible.
   * 
   * @param borntime
   * @return
   */
  public int getSpeed(int borntime) {
    return MyClock.time() > borntime ? lastdist : -1;
  }
  
  public Monkey get(int index) {
    return this.ladder[index];
  }

  public int index() {
    return this.index;
  }
  
  public boolean left() {
    return this.lef;
  }

  public boolean isEmpty() {
    return numofmonkeys == 0 ? true : false;
  }

  public void setLastmonkey(Monkey monkey) {
    monkeys.add(monkey);
  }
  
  public void setDirection(boolean lef) {
    this.lef = lef;
  }
  
  /**
   * Refresh the monkeys on the ladder.
   * change to:
   * remove the first monkey.
   * (change on 6.12)
   */
  public synchronized void refresh() {
    /*
     * if(ladder[index].equals(monkeys.get(monkeys.size() - 1))) { monkeys.remove(0); }
     */
    monkeys.remove(0);
    count(false);
  }
  
  public Monkey getLastMonkey() {
    return this.monkeys.get(monkeys.size() - 1);
  }
  
  public void setLastdist(int dist) {
    this.lastdist = dist;
  }
  
  public int num() {
    return this.numofmonkeys;
  }
  
  public void count(boolean add) {
    this.numofmonkeys += add ? 1 : -1;
  }
}
