import static java.lang.System.exit;
import static java.lang.Thread.sleep;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MonkeyGenerator {

  public static long startTime=System.currentTimeMillis();
  public static Logger logger;
  public static FileHandler fh;

  static {
    logger = Logger.getLogger("MonkeyGenerator");
    logger.getParent().getHandlers()[0].setLevel(Level.OFF);
    try {

      File lp = new File("log/");
      if (!lp.exists()) {
        if (lp.mkdir()) {
          exit(0);
        }
      }
      fh = new FileHandler("log/temp.log");
      java.util.logging.Formatter fm = new Formatter() {
        @Override
        public String format(LogRecord record) {
          return record.getInstant() + "\t" + record.getLevel() + "\n" + record.getMessage() + "\n";
        }
      };
      fh.setFormatter(fm);
      logger.addHandler(fh);

    } catch (IOException e) {
      e.printStackTrace();
      exit(1);
    }
  }

  private int N;
  private int k;
  private int MV;
  private int t;
  private double mbps;
  private double fairness;

  public int getN() {
    return N;
  }

  public int getK() {
    return k;
  }

  public int getMV() {
    return MV;
  }

  public int getT() {
    return t;
  }

  public double getMbps() {
    return mbps;
  }

  public double getFairness() {
    return fairness;
  }

  public  ladders la;
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
      monkey.start = System.currentTimeMillis();
      //System.out.println(monkeys.size());
        while (!st.choose(monkey.getChoose(),la, monkey)) {
          try {
            sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }

  }

  public static void main(String[] args) {
    GUI g = new GUI();
    //MonkeyGenerator mg = new MonkeyGenerator(10, 20, 1, 90, 20, 5);
    //int mnum = 0;
    MonkeyGenerator mg = g.choose();
    if(mg != null) {
      mg.creat();
    }
    CrossGUI cg = new CrossGUI(mg);
    cg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    System.out.println(mg.monkeys.size());
    while (mg.la.checkFinish() != mg.monkeys.size()) {
      //System.out.println(mnum++);
      try {
        sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      for (ladder l : mg.la.ladderList) {
        //System.out.println(la.toString());
        l.move();
      }
      mg.writeLogging();
      cg.setVisible(true);
      cg.repaint();
      //System.out.println(mg.la.toString());
    }
    long endTime=System.currentTimeMillis();
    mg.mbps = mg.N  / ((endTime - startTime)/ 1000.0);
    mg.fairness = mg.calculateFair();
    JOptionPane.showMessageDialog(cg, "总用时："+ (endTime - startTime)/ 1000.0 +"s", "提示", JOptionPane.PLAIN_MESSAGE);
//    System.out.println(mg.mbps);
//    System.out.println(mg.fairness);


  }

  public void writeLogging(){
    for(monkey mo:monkeys){
      long endTime=System.currentTimeMillis();
      if(mo.direction.equals("L->R"))
        logger.info(mo.id + " 正在左岸等待，离出生已" + (endTime-startTime)/1000 +"秒");
      else if (mo.direction.equals("R->L"))
        logger.info(mo.id + " 正在右岸等待，离出生已" + (endTime-startTime)/1000 +"秒");
      else if(mo.direction.equals("on") && mo.lNum.dir == 0)
        logger.info(mo.id + " 正在第" + mo.lnum + "架梯子的第" +mo.rNum + "个踏板上，自左向右行进，离出生已" + (endTime-startTime)/1000 + "秒 ");
      else if(mo.direction.equals("on") && mo.lNum.dir == 1)
        logger.info(mo.id +" 正在第" + mo.lnum + "架梯子的第" +mo.rNum + "个踏板上，自右向左行进，离出生已" + (endTime-startTime)/1000 + "秒 ");
      else if(mo.direction.equals("end") && mo.t < 0 && mo.lNum.dir == 0){
        mo.t = (endTime-startTime)/1000;
        mo.end = endTime;
        logger.info(mo.id+" 已从左岸抵达右岸，共耗时" + mo.t + "秒 ");
      }
      else if(mo.direction.equals("end") && mo.t < 0 && mo.lNum.dir == 1) {
        mo.end = endTime;
        mo.t = (endTime - startTime) / 1000;
        logger.info(mo.id+" 已从右岸抵达左岸，共耗时" + mo.t + "秒 ");
      }
    }
  }

  public double calculateFair(){
    double  fairness = 0;
    for (int i = 0; i < monkeys.size() - 1; i++) {
      for (int j = i + 1; j < monkeys.size(); j++) {
        if (((monkeys.get(i).start - monkeys.get(j).start)
            ^ (monkeys.get(i).end - monkeys.get(j).end)) >= 0)
          fairness++;
        else
          fairness--;
      }
    }
    return fairness / (N * (N - 1) / 2.0);
  }
}



