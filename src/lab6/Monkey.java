package lab6;

public class Monkey extends Thread {
  private final int born;
  private final int id;
  private final boolean left;// born on the left
  private final int speed;
  private final Scene scene;

  public Monkey(int born, int id, String dest, int speed, Scene scene) {
    this.born = born;
    this.id = id;
    switch (dest.replaceAll("\\s*", "")) {
      case "L->R":
        this.left = true;
        break;
      case "R->L":
        this.left = false;
        break;
      default:
        assert false : "Mistakes when dealing with input.";
        left = false;// never reached.
    }
    this.speed = speed;
    this.scene = scene;
  }

  /**
   * Make a decision about which bridge to go.
   * 
   * @return a ladder.
   */
  private Ladder decisionAndMove() {
    return this.scene.decisionAndMove(this);
  }

  @Override
  public void run() {
    OUT: while (true) {
      // run
      switch (this.state()) {
        case 0:
          // born
          decisionAndMove();
          break;
        case 1:
          // on bridge
          move();
          break;
        case 2:
          // finish
          break OUT;
        default:
          assert false : this.id + " monkey run method.";
          break;
      }

      // wait
      synchronized (Monkey.class) {
        try {
          Monkey.class.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
    scene.count();
    System.out.println("ºï×Ó" + this.id + "ÒÑÏÂÇÅ");
  }

  public void move() {
    this.scene.monkeyMove(this);
  }

  public int bornTime() {
    return this.born;
  }

  public int speed() {
    return this.speed;
  }

  public boolean left() {
    return this.left;
  }

  public int state() {
    return this.scene.getState(this);
  }

  public int id() {
    return this.id;
  }

  public int compareTo(Monkey that) {
    return that == null ? 0 : this.id - that.id;
  }
}
