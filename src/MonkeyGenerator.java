import static java.lang.Thread.sleep;

import java.util.ArrayList;
import java.util.List;

public class MonkeyGenerator {

  private int N;
  private int k;
  private int MV;
  private int t;
  public static ladders la;
  public Strategy st = new Strategy();
  List<monkey> monkeys = new ArrayList<>();

  public MonkeyGenerator(int n, int h, int t, int N, int k, int MV) {
    this.la = new ladders(n, h);
    this.N = N;
    this.k = k;
    this.MV = MV;
    this.t = t;
  }

  public boolean creat() {
    int id = 0;
    for (int i = 0; i < N / k; i++) {
      for (int j = 0; j < k; j++) {
        int random = (int) (Math.random() * 100 + 1);
        monkey mtemp;
        if (random % 2 == 0) {
          mtemp = new monkey(id, (int) (Math.random() * (MV - 1) + 2), "L->R",
              (int) (Math.random() * 3));
        } else {
          mtemp = new monkey(id, (int) (Math.random() * (MV - 1) + 2), "R->L",
              (int) (Math.random() * 3));
        }
        monkeys.add(mtemp);
        MonkeyThread mt = new MonkeyThread(mtemp);
        mt.start();
        id++;
      }
      try {
        sleep(1000 * t);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      ;
    }

    for (int i = 0; i < N % k; i++) {
      int random = (int) (Math.random() * 100 + 1);
      monkey mtemp;
      if (random % 2 == 0) {
        mtemp = new monkey(id, (int) (Math.random() * (MV - 1) + 2), "L->R",
            (int) (Math.random() * 3));
      } else {
        mtemp = new monkey(id, (int) (Math.random() * (MV - 1) + 2), "R->L",
            (int) (Math.random() * 3));
      }
      monkeys.add(mtemp);
      MonkeyThread mt = new MonkeyThread(mtemp);
      mt.start();
      id++;
    }

    if (monkeys.size() == N) {
      return true;
    }
    return false;
  }

  class MonkeyThread extends Thread {

    private monkey monkey;

    public MonkeyThread(monkey monkey) {
      this.monkey = monkey;
    }

    public void run() {
      boolean temp;
      System.out.println(monkey.id + " " + "开始过河！");
      //System.out.println(monkeys.size());
        while (!st.choose(1,la, monkey)) {
          try {
            sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }

  }

  public static void main(String[] args) {
    MonkeyGenerator mg = new MonkeyGenerator(13, 15, 1, 23, 3, 5);
    mg.creat();
    System.out.println(mg.monkeys.size());
    while (mg.la.checkFinish() != mg.monkeys.size()) {
      System.out.println(mg.la.checkFinish());
      try {
        sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      for (ladder l : mg.la.ladderList) {
        l.move();
      }
      System.out.println(la.toString());
    }
    System.out.println(mg.la.checkFinish());

  }
}



