package lab6;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

public class MonkeyFactory extends Thread {
  private Set<Monkey> monkeys =
      Collections.synchronizedSet(new TreeSet<>((a, b) -> a.compareTo(b)));
  private Set<Monkey> monkeysborned =
      Collections.synchronizedSet(new TreeSet<>((a, b) -> a.compareTo(b)));
  private Scene scene;
  private Set<String[]> save = new HashSet<>();
  private int num;

  public MonkeyFactory() {}

  public MonkeyFactory(int n, int h, int t, int N, int k, int MV, Scene scene) {
    int i;
    double random = 0;
    this.scene = scene;
    this.num = N;
    for (i = 0; i < N / k; ++i) {
      for (int j = 0; j < k; ++j) {
        random = Math.random();
        random = random == 0 ? 0.1 : random;
        while(random < 0.1) {
          random *= 10;
        }
        if(random == 0) {
          System.out.println("fuck");
        }
        monkey(i * t, i * k + j + 1, random > 0.5 ? "R->L" : "L->R", (int) ((double) MV * random));
      }
    }
    // N % k
    for (int j = 0; j < N % k; ++j) {
      monkey(i * t, i * k + j + 1, random > 0.5 ? "R->L" : "L->R", (int) ((double) MV * random));
    }
  }

  public void setScene(Scene scene) {
    this.scene = scene;
    num = save.size();
    for (String[] str : save) {
      monkey(Integer.valueOf(str[0]), Integer.valueOf(str[1]), str[2], Integer.valueOf(str[3]));
    }
    // save.clear();
  }

  public void monkey(int a, int b, String c, int d) {
    this.monkeys.add(new Monkey(a, b, c, d, scene));
  }

  @Override
  public void run() {
    new MyClock(scene).start();
    Monkey cur;
    Iterator<Monkey> iter;
    while (true) {
      iter = monkeys.iterator();
      while (iter.hasNext()) {
        if ((cur = iter.next()).bornTime() == MyClock.time()) {
          monkeysborned.add(cur);
          scene.addMonkey(cur);
          cur.start();
          iter.remove();
        } else if (cur.bornTime() > MyClock.time()) {
          continue;
        }
      }
      synchronized (Monkey.class) {
        try {
          Monkey.class.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      if (num == scene.num()) {
        scene.log(Level.INFO, "第" + MyClock.time() + "回合，猴子已全部下桥");
        System.out.println("猴子已全部下桥");
        return;
      }
    }
  }

  public void monkey(String[] str) {
    this.save.add(str);
  }

}
