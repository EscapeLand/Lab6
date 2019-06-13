import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;

public class CrossGUI extends JFrame {

  MonkeyGenerator mg;
  private static Font defaultFont = new Font("Tahoma", Font.BOLD, 15);
  private final int n;
  private final int t;
  private final int h;
  private final int N;
  private final int k;
  private final int MV;

  public CrossGUI(MonkeyGenerator mg) {
    this.n = Ladder.n;
    this.t = mg.t;
    this.h = 20;
    this.N = mg.N;
    this.k = mg.k;
    this.MV = mg.MV;
    this.mg = mg;
    this.setTitle("Demo");
    this.setBounds(300, 0, 800, 750);
    this.setLayout(null);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    g.setColor(Color.BLACK);
    for (int i = 0; i < n; i++) {
      g.drawLine(30, 40 + 600 / n * i, 770, 40 + 600 / n * i);
      g.drawLine(30, 10 + 600 / n * (i + 1), 770, 10 + 600 / n * (i + 1));
      for (int j = 1; j <= h; j++) {
        g.drawLine(30 + 740 / (h + 1) * j, 40 + 600 / n * i, 30 + 740 / (h + 1) * j,
            10 + 600 / n * (i + 1));
      }
    }
    for (int i = 0; i < Ladder.n; i++) {
      for (int j = 0; j < Ladder.h; j++) {
        final int t1 = 30 + 740 / (h + 1) * (j + 1) - 100 / n;
        final int t2 = 45 + 600 / n * i + 100 / n;
        final int t3 = 45 + 600 / n * i + 220 / n;
        if (Ladder.ladders[i].rungList[j].monkey != null
            && Ladder.ladders[i].dir == Dir.R) {
          g.setColor(Color.white);
          g.fillOval(t1, t2, 200 / n,
              200 / n);
          g.setColor(Color.BLACK);
          g.setFont(defaultFont);
          g.drawString(Ladder.ladders[i].rungList[j].monkey.ID + ">",
              25 + 740 / (h + 1) * (j + 1) - 20 / n, t3);
        } else if (Ladder.ladders[i].rungList[j].monkey != null
            && Ladder.ladders[i].dir == Dir.L) {
          g.setColor(Color.lightGray);
          g.fillOval(t1, t2, 200 / n,
              200 / n);
          g.setColor(Color.BLACK);
          g.setFont(defaultFont);
          g.drawString("<" + Ladder.ladders[i].rungList[j].monkey.ID,
              20 + 740 / (h + 1) * (j+1) - 20 / n, t3);
        }
      }
    }
    g.setColor(Color.BLACK);
    g.setFont(defaultFont);
    g.drawString("Monkey Number = " + N, 30, 630);
    g.drawString("Ladder Number = " + n, 30, 650);
    g.drawString("Finished Monkey = " + Ladder.checkFinish(), 30, 670);
    g.drawString("Mbps = " + mg.getMBps(), 30, 690);
    g.drawString("Fairness = " + mg.getFairness(), 30, 710);
  }
}


