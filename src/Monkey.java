import java.util.Objects;


public class Monkey {
  final int ID;
  private int v;
  public Dir direction;
  private int choose;
  public Ladder lNum;
  public int lnum;
  public int rNum;
  public long t = -1;
  public long start;
  public long end;

  public Monkey(int ID, int velocity, Dir direction, int choose) {
    this.ID = ID;
    this.v = velocity;
    this.direction = direction;
    this.choose = choose;
  }

  public int getVelocity() {
    return v;
  }

  public int getChoose() {
    return choose;
  }

  public Monkey clone(){
    return new Monkey(ID, v, direction, choose);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Monkey)) {
      return false;
    }
    Monkey monkey = (Monkey) o;
    return v == monkey.v &&
        choose == monkey.choose &&
        direction.equals(monkey.direction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(v, direction, choose);
  }

  public final Runnable run = () -> {
    start = System.currentTimeMillis();
    while (!Strategy.st.choose(getChoose(), this)) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  };
}
