package lab6;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class MyClock extends Thread {

  private static AtomicInteger clock = new AtomicInteger(0);

  private Scene scene;

  public MyClock(Scene scene) {
    this.scene = scene;
  }

  @Override
  public void run() {
    while (true) {
      synchronized (Monkey.class) {
        Monkey.class.notifyAll();
      }
      try {
        Thread.sleep(1000);
        scene.log(Level.INFO,
            "第" + time() + "回合，" + scene.waitcount() + "只猴子等待，" + scene.num() + "只猴子已下桥");
        scene.log(Level.INFO,
          "第" + time() + "回合，" + scene.numonlad() + "只猴子在桥上");
        scene.waitclear();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      clock.addAndGet(1);
    }
  }

  public static int time() {
    return clock.intValue();
  }

}
