public class Ladder {

  static Ladder[] ladders = null;
  static int n;
  static int h;


  public final int rungNumber;
  public final rung[] rungList;
  public Dir dir;
  public int monkeyNum = 0;
  public int finish = 0;
  public int speed;

  Ladder(int rungNumber) {
    this.rungNumber = rungNumber;
    rungList = new rung[rungNumber];
    for (int i = 0; i < rungNumber; i++) {
      rungList[i] = new rung();
    }
  }

  static void init(int ladderNumber, int rungNumber) {
    assert ladders == null;
    h = rungNumber;
    n = ladderNumber;
    ladders = new Ladder[n];
    for (int i = 0; i < ladderNumber; i++) {
      ladders[i] = new Ladder(h);
    }
  }

  static int checkFinish() {
    int tmp = 0;
    for (Ladder l : ladders) {
      tmp = tmp + l.finish;
    }
    return tmp;
  }

  public static String printLadder(Ladder[] ladders) {
    StringBuilder sb = new StringBuilder();
    for (Ladder l : ladders) {
      sb.append(l.toString()).append('\n');
    }
    return sb.toString();
  }

  void move() {
    if (dir == Dir.R) {
      for (int i = rungNumber - 1; i >= 0; i--) {
        if (rungList[i].monkey != null) {
          if (i == rungNumber - 1 || (i + rungList[i].monkey.getVelocity() > rungNumber - 1
              && checkLR(i, rungList[i].monkey.getVelocity()) == i + rungList[i].monkey
              .getVelocity())) {
            rungList[i].monkey.direction = Dir.OFF;
            rungList[i].monkey = null;
            finish++;
            monkeyNum--;
          } else {
            rungList[i].monkey.rNum = checkLR(i, rungList[i].monkey.getVelocity());
            rungList[checkLR(i, rungList[i].monkey.getVelocity())].monkey = rungList[i].monkey;
            speed = Math.abs(checkLR(i, rungList[i].monkey.getVelocity()) - i);
            rungList[i].monkey = null;

          }
        }
      }
    } else if (dir == Dir.L) {
      for (int i = 0; i < rungNumber; i++) {
        if (rungList[i].monkey != null) {
          if (i == 0 || (i - rungList[i].monkey.getVelocity() < 0
              && checkRL(i, rungList[i].monkey.getVelocity()) == i - rungList[i].monkey
              .getVelocity())) {
            rungList[i].monkey.direction = Dir.OFF;
            rungList[i].monkey = null;
            finish++;
            monkeyNum--;
          } else {
            rungList[i].monkey.rNum = checkRL(i, rungList[i].monkey.getVelocity());
            rungList[checkRL(i, rungList[i].monkey.getVelocity())].monkey = rungList[i].monkey;
            speed = Math.abs(checkRL(i, rungList[i].monkey.getVelocity()) - i);
            rungList[i].monkey = null;
          }
        }
      }
    }
  }

  private int checkLR(int i, int MV) {
    int num;
    if (i + MV > rungNumber - 1) {
      num = rungNumber - 1;
    } else {
      num = i + MV;
    }
    for (i = i + 1; i <= num; i++) {
      if (rungList[i].monkey != null) {
        return i - 1;
      }
    }
    return num;
  }

  private int checkRL(int i, int MV) {
    int num;
    if (i - MV < 0) {
      num = 0;
    } else {
      num = i - MV;
    }
    for (i = i - 1; i >= num; i--) {
      if (rungList[i].monkey != null) {
        return i + 1;
      }
    }
    return num;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (rung r : rungList) {
      sb.append(r.monkey == null ? 0 : r.monkey.ID).append(" ");
    }
    return sb.toString();
  }
}

class rung {
  Monkey monkey;
}
