public class Ladder {

  static Ladder[] ladders = null;
  static int n;
  static int h;


  final Monkey[] monkeys;
  Dir dir;
  private int monkeyNum = 0;
  private int finish = 0;
  private int tailSpeed = MonkeyGenerator.MV;
  private int ID;

  private Ladder(int ID, int rungNumber) {
    this.ID = ID;
    monkeys = new Monkey[rungNumber];
  }

  static void init(int ladderNumber, int rungNumber) {
    assert ladders == null;
    h = rungNumber;
    n = ladderNumber;
    ladders = new Ladder[n];
    for (int i = 0; i < ladderNumber; i++) {
      ladders[i] = new Ladder(i, h);
    }
  }

  static int countFinish() {
    int sum = 0;
    for (Ladder l : ladders) {
      sum = sum + l.finish;
    }
    return sum;
  }

  @SuppressWarnings("unused")
  static void printLadder() {
    StringBuilder sb = new StringBuilder();
    for (Ladder l : ladders) {
      sb.append(l.toString()).append('\n');
    }
    System.out.println(sb.toString());
  }

  private static int startAt(Dir dir){
    return dir == Dir.R ? 0 : h - 1;
  }

  void move() {
    assert dir == Dir.L || dir == Dir.R;

    if (dir == Dir.R) {
      for (int i = h - 1; i >= 0; i--) {
        if (monkeys[i] != null) {
          if (checkLR(i) > h - 1) {
            monkeys[i].dir = Dir.OFF;
            monkeys[i] = null;
            finish++;
            monkeyNum--;
            tailSpeed = MonkeyGenerator.MV;
          } else {
            monkeys[i].rNum = checkLR(i);
            monkeys[checkLR(i)] = monkeys[i];
            tailSpeed = Math.abs(checkLR(i) - i);
            monkeys[i] = null;
          }
        }
      }
    } else {
      for (int i = 0; i < h; i++) {
        if (monkeys[i] != null) {
          if (checkRL(i) < 0) {
            monkeys[i].dir = Dir.OFF;
            monkeys[i] = null;
            finish++;
            monkeyNum--;
            tailSpeed = MonkeyGenerator.MV;
          } else {
            monkeys[i].rNum = checkRL(i);
            monkeys[checkRL(i)] = monkeys[i];
            tailSpeed = Math.abs(checkRL(i) - i);
            monkeys[i] = null;
          }
        }
      }
    }
  }

  private int checkLR(int pos) {
    var brace = pos + monkeys[pos].MV;
    for (int i = pos + 1, l = Math.min(brace, h - 1); i <= l; i++) {
      if (monkeys[i] != null) {
        return i - 1;
      }
    }
    return brace;
  }

  private int checkRL(int pos) {
    int brace = pos - monkeys[pos].MV;
    for (int i = pos - 1, l = Math.max(0, brace); i >= l; i--) {
      if (monkeys[i] != null) {
        return i + 1;
      }
    }
    return brace;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (var monkey : monkeys) {
      sb.append(monkey == null ? 0 : monkey.ID).append(" ");
    }
    return sb.toString();
  }

  int tailSpeed() {
    return tailSpeed;
  }

  int size() {
    return monkeyNum;
  }

  void addMonkey(Monkey monkey){
    var i = startAt(monkey.dir);
    assert monkeys[i] == null;
    monkeys[i] = monkey;
    monkey.rNum = i;
    monkey.lNum = ID;
    monkeyNum++;
    dir = monkey.dir;
    monkey.dir = Dir.ON;
  }

  boolean firstEmpty(Dir dir){
    return monkeys[startAt(dir)] == null;
  }
}