public class Monkey {
  final int ID;
  final int MV;
  Dir dir;
  private int choose;
  int lNum;
  int rNum;
  long t = -1;
  long start;
  long end;

  public Monkey(int ID, int MV, Dir dir, int choose) {
    this.ID = ID;
    this.MV = MV;
    this.dir = dir;
    this.choose = choose;
  }

  final Runnable run = () -> {
    start = System.currentTimeMillis();
    while (!Strategy.choose(this, choose)) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    assert dir != Dir.L && dir != Dir.R;
  };

  @Override
  public String toString() {
    return Integer.toString(ID);
  }
}
