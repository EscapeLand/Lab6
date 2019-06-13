import static java.lang.Thread.sleep;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MonkeyGenerator {

  private long startTime = System.currentTimeMillis();

  private int num = 0;
  final int N;
  final int k;
  final int MV;
  final int t;
  private double MBps;
  private double fairness;


  double getMBps() {
    return MBps;
  }

  double getFairness() {
    return fairness;
  }

  private List<Monkey> monkeys = new ArrayList<>();

  MonkeyGenerator(int n, int h, int t, int N, int k, int MV) {
    Ladder.init(n, h);
    this.N = N;
    this.k = k;
    this.MV = MV;
    this.t = t * 1000;
  }

  boolean create() throws InterruptedException {
    for (int i = 0, s = N / k; i < s; i++) {
      birth(k);
      sleep(t);
    }
    birth(N % k);

    return monkeys.size() == N;
  }

  private void birth(int n) {
    var rnd = ThreadLocalRandom.current();
    for (int j = 0; j < n; j++) {
      int random = rnd.nextInt(2);
      Monkey newm = new Monkey(num++, rnd.nextInt(1, MV + 1), random == 0 ? Dir.R : Dir.L,
          rnd.nextInt(3));
      monkeys.add(newm);
      Main.cachedThreadPool.execute(newm.run);
    }
  }


  void writeLogging() {
    var endTime = System.currentTimeMillis();
    int s = (int) ((endTime - startTime) / 1000.0);

    for (int i = 0; i < monkeys.size(); i++) {
      Monkey mo = monkeys.get(i);
      if (mo.direction == Dir.R) {
        Main.logger.info(mo.ID + " 正在左岸等待，离出生已" + s + "秒");
      } else if (mo.direction == Dir.L) {
        Main.logger.info(mo.ID + " 正在右岸等待，离出生已" + s + "秒");
      } else if (mo.direction == Dir.ON && mo.lNum.dir == Dir.R) {
        Main.logger.info(mo.ID
            + " 正在第" + mo.lnum + "架梯子的第" + mo.rNum + "个踏板上，自左向右行进，离出生已"
            + s + "秒 ");
      } else if (mo.direction == Dir.ON && mo.lNum.dir == Dir.L) {
        Main.logger.info(mo.ID
            + " 正在第" + mo.lnum + "架梯子的第" + mo.rNum + "个踏板上，自右向左行进，离出生已"
            + s + "秒 ");
      } else if (mo.direction == Dir.OFF && mo.t < 0 && mo.lNum.dir == Dir.R) {
        mo.t = s;
        mo.end = endTime;
        Main.logger.info(mo.ID + " 已从左岸抵达右岸，共耗时" + mo.t + "秒 ");
      } else if (mo.direction == Dir.OFF && mo.t < 0 && mo.lNum.dir == Dir.L) {
        mo.end = endTime;
        mo.t = s;
        Main.logger.info(mo.ID + " 已从右岸抵达左岸，共耗时" + mo.t + "秒 ");
      }
    }
  }

  public double calculateFair() {
    double fairness = 0;
    for (int i = 0; i < monkeys.size() - 1; i++) {
      for (int j = i + 1; j < monkeys.size(); j++) {
        if (((monkeys.get(i).start - monkeys.get(j).start)
            ^ (monkeys.get(i).end - monkeys.get(j).end)) >= 0) {
          fairness++;
        } else {
          fairness--;
        }
      }
    }
    return fairness / (N * (N - 1) / 2.0);
  }

  public static void start() throws InterruptedException {
    GUI g = new GUI();
    MonkeyGenerator mg = g.choose();

    Main.cachedThreadPool.execute(() -> {
      if (mg != null) {
        try {
          mg.create();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    CrossGUI cg = new CrossGUI(mg);
    cg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    System.out.println(mg.monkeys.size());
    sleep(1000);
    while (Ladder.checkFinish() != mg.monkeys.size()) {
      sleep(1000);
      for (Ladder l : Ladder.ladders) {
        l.move();
      }
      synchronized (mg) {
        mg.writeLogging();
        cg.setVisible(true);
        cg.repaint();
      }
    }
    double s = (System.currentTimeMillis() - mg.startTime) / 1000.0;
    mg.MBps = mg.N / s;
    mg.fairness = mg.calculateFair();
    JOptionPane.showMessageDialog(cg, "used " + s + " s", "Finished", JOptionPane.PLAIN_MESSAGE);
  }
}



