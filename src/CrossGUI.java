import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;

public class CrossGUI extends JFrame {

  MonkeyGenerator mg;
  private final int n;
  private final int t;
  private final int h;
  private final int N;
  private final int k;
  private final int MV;

  public CrossGUI(MonkeyGenerator mg) {
    this.n = mg.la.ladderNumber;
    this.t = mg.getT();
    this.h = 20;
    this.N = mg.getN();
    this.k = mg.getK();
    this.MV = mg.getMV();
    this.mg = mg;
    JFrame thisFrame = this;
    this.setTitle("过河");
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
    for (int i = 0; i < mg.la.ladderNumber; i++) {
      for (int j = 0; j < mg.la.rungNumber; j++) {
        if (mg.la.ladderList[i].rungList[j].monkey != null
            && mg.la.ladderList[i].dir == 0) {
          g.setColor(Color.white);
          g.fillOval(30 + 740 / (h + 1) * (j + 1) - 100 / n, 45 + 600 / n * i + 100 / n, 200 / n,
              200 / n);
          g.setColor(Color.BLACK);
          g.setFont(new Font("Tahoma", Font.BOLD, 70 / n));
          g.drawString(mg.la.ladderList[i].rungList[j].monkey.id + ">",
              25 + 740 / (h + 1) * (j + 1) - 20 / n, 45 + 600 / n * i + 220 / n);
        } else if (mg.la.ladderList[i].rungList[j].monkey != null
            && mg.la.ladderList[i].dir == 1) {
          g.setColor(Color.lightGray);
          g.fillOval(30 + 740 / (h + 1) * (j+1) - 100 / n, 45 + 600 / n * i + 100 / n, 200 / n,
              200 / n);
          g.setColor(Color.BLACK);
          g.setFont(new Font("Tahoma", Font.BOLD, 70 / n));
          g.drawString("<" + mg.la.ladderList[i].rungList[j].monkey.id,
              20 + 740 / (h + 1) * (j+1) - 20 / n, 45 + 600 / n * i + 220 / n);
        }
      }
    }
    g.setColor(Color.BLACK);
    g.setFont(new Font("Tahoma", Font.BOLD, 15));
    g.drawString("Monkey Number = " + N, 30, 630);
    g.drawString("Ladder Number = " + n, 30, 650);
    g.drawString("Finished Monkey = " + mg.la.checkFinish(), 30, 670);
    g.drawString("Mbps = " + mg.getMbps(), 30, 690);
    g.drawString("Fireness = " + mg.getFairness(), 30, 710);
  }
}


