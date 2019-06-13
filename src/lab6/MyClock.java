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
            "��" + time() + "�غϣ�" + scene.waitcount() + "ֻ���ӵȴ���" + scene.num() + "ֻ����������");
        scene.log(Level.INFO,
          "��" + time() + "�غϣ�" + scene.numonlad() + "ֻ����������");
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
