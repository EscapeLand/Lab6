import static java.lang.Thread.sleep;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JOptionPane;

class MonkeyGenerator {

  private long startTime = System.currentTimeMillis();

  private int num = 0;
  static int N;
  static int k;
  static int MV;
  static int t;
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
    MonkeyGenerator.N = N;
    MonkeyGenerator.k = k;
    MonkeyGenerator.MV = MV;
    MonkeyGenerator.t = t * 1000;
  }

  private void create() throws InterruptedException {
    for (int i = 0, s = N / k; i < s; i++) {
      birth(k);
      sleep(t);
    }
    birth(N % k);
  }

  private void birth(int n) {
    var rnd = ThreadLocalRandom.current();
    for (int j = 0; j < n; j++) {
      int random = rnd.nextInt(2);
      Monkey newm = new Monkey(num++, rnd.nextInt(1, MV + 1), random == 0 ? Dir.R : Dir.L,
          //rnd.nextInt(3)
      2
      );
      monkeys.add(newm);
      Main.cachedThreadPool.execute(newm.run);
    }
  }


  private void writeLog() {
    var endTime = System.currentTimeMillis();
    int s = (int) ((endTime - startTime) / 1000.0);

    for (int i = 0; i < monkeys.size(); i++) {
      Monkey mo = monkeys.get(i);
      if (mo.dir == Dir.R) {
        Main.logger.info("Monkey(" + mo.ID + ") waiting at left shell, " + s + "s from birth. ");
      } else if (mo.dir == Dir.L) {
        Main.logger.info("Monkey(" + mo.ID + ") waiting at right shell, " + s + "s from birth. ");
      } else if (mo.dir == Dir.ON && Ladder.ladders[mo.lNum].dir == Dir.R) {
        Main.logger.info("Monkey(" + mo.ID
            + ") on Ladder(" + mo.lNum + "), Rung(" + mo.rNum + "). L->R, "
            + s + "s from birth. ");
      } else if (mo.dir == Dir.ON && Ladder.ladders[mo.lNum].dir == Dir.L) {
        Main.logger.info("Monkey(" + mo.ID
            + ") on Ladder(" + mo.lNum + "), Rung(" + mo.rNum + "). R->L, "
            + s + "s from birth. ");
      } else if (mo.dir == Dir.OFF && mo.t < 0 && Ladder.ladders[mo.lNum].dir == Dir.R) {
        mo.t = s;
        mo.end = endTime;
        Main.logger.info("Monkey(" + mo.ID + ")has reached right shell, " + mo.t + "s cost. ");
      } else if (mo.dir == Dir.OFF && mo.t < 0 && Ladder.ladders[mo.lNum].dir == Dir.L) {
        mo.end = endTime;
        mo.t = s;
        Main.logger.info("Monkey(" + mo.ID + ")has reached left shell, " + mo.t + "s cost. ");
      }
    }
  }

  private double calculateFair() {
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

  static void start() throws InterruptedException {
    GUIHelper g = new GUIHelper();
    //MonkeyGenerator mg = g.choose();
    var mg = new MonkeyGenerator(10, 20, 1, 500, 20, 20);

    Main.cachedThreadPool.execute(() -> {
      if (mg != null) {
        try {
          mg.create();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    CrossGUI cg = new CrossGUI();
    //System.out.println(mg.monkeys.size());
    while (Ladder.countFinish() != mg.monkeys.size()) {
      sleep(1000);
      for (Ladder l : Ladder.ladders) {
        l.move();
      }
      synchronized (mg) {
        //Ladder.printLadder();
        mg.writeLog();
        cg.setVisible(true);
        cg.dualBufferedPainting();
      }
      cg.repaint();
    }
    double s = (System.currentTimeMillis() - mg.startTime) / 1000.0;
    mg.MBps = N / s;
    mg.fairness = mg.calculateFair();
    JOptionPane.showMessageDialog(cg,
        "used " + s + " s\nMBps = " + mg.MBps + "\nFairness = " + mg.fairness, "Finished",
        JOptionPane.PLAIN_MESSAGE);
  }

  @SuppressWarnings("unused")
  private List<Monkey> findAllUnload() {
    ArrayList<Monkey> ms = new ArrayList<>();
    monkeys.forEach(m -> {
      if (m.dir != Dir.OFF) {
        ms.add(m);
      }
    });
    return ms;
  }
}



