package lab6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Scene {
  private final int numoflad;
  private final int lenoflad;
  private List<Ladder> ladders = Collections.synchronizedList(new ArrayList<>());
  // 0 born 1 on bridge 2 finish
  private Map<Monkey, Integer> state =
      Collections.synchronizedMap(new TreeMap<>((a, b) -> a.compareTo(b))); // can be removed if
  // add some changes
  // in "positions"
  private Map<Monkey, Position> positions =
      Collections.synchronizedMap(new TreeMap<>((a, b) -> a.compareTo(b)));
  private Logger log = Logger.getLogger("log");

  private AtomicInteger num = new AtomicInteger(0);// finish sign
  private AtomicInteger waitnum = new AtomicInteger(0);// wait number in one second

  public Scene(int numoflad, int lenoflad) {
    this.numoflad = numoflad;
    this.lenoflad = lenoflad;
    buildLadder();
  }

  /**
   * Make decision for a monkey. null means no ladder was chosen, just wait.
   * 
   * @param monkey
   * @return not valuable.
   */
  public Ladder decisionAndMove(Monkey monkey) {
    // 1 empty
    int speed = 0;// keep fastest
    int ladspeed;
    int monkeyspeed = monkey.speed();
    Ladder ret = null;
    boolean monkeylef = monkey.left();
    synchronized (Ladder.class) {
      for (Ladder a : ladders) {
        if (a.isEmpty()) {
          a.setDirection(monkeylef);
          a.count(true);
          a.setLastmonkey(monkey);
          log.log(Level.INFO,
              "第" + MyClock.time() + "回合，猴子" + monkey.id() + "选择了第" + a.index() + "座桥");
          monkeyMove(monkey, a);
          return a;
        }
        //整体推进速度最快
        ladspeed = a.getSpeed(monkey.bornTime());
        if (a.get(monkeylef ? 0 : 19) == null && ladspeed != -1 && !(monkeylef ^ a.left())) {
          if (speed > ladspeed && ladspeed > monkeyspeed) {
            speed = ladspeed;
            ret = a;// ?
          } else if (speed < monkeyspeed && speed < ladspeed) {
            speed = ladspeed;
            ret = a;
          }
        }
      }
      // 速度都看不了的话 增加 方向可以就上 找猴子最远的
      // 可以整合到上面
      // ...
      if (ret == null) {
        assert speed == 0 : "speed != 0";
        for (Ladder a : ladders) {
          if (a.get(monkeylef ? 0 : 19) == null && !(monkeylef ^ a.left())) {
            if(monkeylef) {//可以改成更新的形式
              for(int i = 0; i < lenoflad; ++i) {
                if(a.get(i) != null && i > speed) {
                  speed = i;
                  ret = a;
                }
              }
            }else {
              for(int i = lenoflad - 1; i >= 0; --i) {
                if(a.get(i) != null && lenoflad - 1 - i > speed) {
                  speed = i;
                  ret = a;
                }
              }
            }
          }
        }
      }


      if (ret == null) {
        waitcount();
        // log.log(Level.INFO, "第" + MyClock.time() + "回合，猴子" + monkey.id() + "等待");
        return ret; // null
      }
      ret.setLastmonkey(monkey);
      ret.count(true);
      log.log(Level.INFO,
          "第" + MyClock.time() + "回合，猴子" + monkey.id() + "选择了第" + ret.index() + "座桥");
      monkeyMove(monkey, ret);
    }
    return ret;
  }

  private void buildLadder() {
    for (int i = 0; i < numoflad; ++i) {
      ladders.add(new Ladder(lenoflad, i + 1));
    }
  }

  public Ladder getLadder(int index) {
    return ladders.get(index);
  }

  public void addMonkey(Monkey monkey) {
    this.state.put(monkey, 0);
  }

  public int getState(Monkey monkey) {
    return this.state.get(monkey);
  }

  /**
   * Monkeys' movements. (born cases) The monkey must be able to go on bridge if you want to invoke
   * this method.
   * 
   * @param monkey
   * @param lad
   */
  private void monkeyMove(Monkey monkey, Ladder lad) {
    synchronized (lad) {
      int pos = monkey.left() ? -1 : lenoflad;
      positions.put(monkey, new Position(lad, pos));
      state.put(monkey, 1);
      monkeyMove(monkey);
    }
  }

  /**
   * Monkeys' movements. (on bridge cases)
   * 
   * @param monkey
   */
  public void monkeyMove(Monkey monkey) {
    Ladder lad = positions.get(monkey).lad();
    int flag = 0;
    synchronized (lad) {
      int pos = positions.get(monkey).pos();
      if (monkey.left()) {
        for (int i = pos + 1; i <= pos + monkey.speed(); ++i) {
          if (!judge(i, monkey, lad, pos, true)) {
            flag = 1;
            break;
          }
        }
      } else {
        for (int i = pos - 1; i >= pos - monkey.speed(); --i) {
          if (!judge(i, monkey, lad, pos, false)) {
            flag = 1;
            break;
          }
        }
      }
    }
    assert flag == 1 : "???";
  }

  private final boolean judge(int i, Monkey monkey, Ladder lad, int pos, boolean lef) {
    if (lef ? i == lenoflad : i == -1) {
      this.state.put(monkey, 2);// finish
      lad.set(null, pos);
      lad.refresh();
      log.log(Level.INFO,
          "第" + MyClock.time() + "回合，猴子" + monkey.id() + "走下了第" + lad.index() + "座桥");
      return false;
    }
    if (lad.get(i) != null) {
      lad.set(null, pos);
      lad.set(monkey, lef ? i - 1 : i + 1);
      positions.get(monkey).setPos(lef ? i - 1 : i + 1);
      lad.setLastdist(Math.abs(i - pos));
      log.log(Level.INFO, "第" + MyClock.time() + "回合，猴子" + monkey.id() + "在第" + lad.index()
          + "座桥上从第" + pos + "位走到了第" + (lef ? i - 1 : i + 1) + "位");
      return false;
    }
    if (lef ? i == pos + monkey.speed() : i == pos - monkey.speed()) {
      lad.set(monkey, i);
      lad.set(null, pos);
      positions.get(monkey).setPos(i);
      lad.setLastdist(monkey.speed());
      log.log(Level.INFO, "第" + MyClock.time() + "回合，猴子" + monkey.id() + "在第" + lad.index()
          + "座桥上从第" + pos + "位走到了第" + i + "位");
      return false;
    }
    return true;
  }

  public int num() {
    return num.intValue();
  }

  public synchronized void count() {
    num.addAndGet(1);
  }

  public int waitcount() {
    return waitnum.getAndAdd(1);// -1?
  }

  public void waitclear() {
    waitnum.getAndSet(0);
  }

  public void log(Level level, String message) {
    log.log(level, message);
  }

  public int getNumofLad() {
    return this.numoflad;
  }

  public int getLenofLad() {
    return this.lenoflad;
  }

  public int numonlad() {
    int ret = 0;
    for (Ladder lad : ladders) {
      ret += lad.num();
    }
    return ret;
  }
}
