import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CrossGUI extends JFrame {

  private static Font defaultFont = new Font("Times New Roman", Font.BOLD, 16);
  private final int n;
  private final int h;
  private final int N;
  private Image img;

  CrossGUI() {
    this.n = Ladder.n;
    this.h = 20;
    this.N = MonkeyGenerator.N;
    this.setTitle("Demo");
    this.setBounds(300, 100, 800, 720);
    this.setLayout(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
  }

  void dualBufferedPainting(){
    img = createImage(getWidth(), getHeight());
    var g = (Graphics2D) img.getGraphics();
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
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
        final int t2 = 39 + 600 / n * i + 100 / n;
        final int t3 = 42 + 600 / n * i + 220 / n;
        if (Ladder.ladders[i].monkeys[j] != null
            && Ladder.ladders[i].dir == Dir.R) {
          g.setColor(Color.white);
          g.fillOval(t1, t2, 200 / n + 4,
              200 / n + 4);
          g.setColor(Color.BLACK);
          g.setFont(defaultFont);
          g.drawString(Ladder.ladders[i].monkeys[j].ID + ">",
              25 + 740 / (h + 1) * (j + 1) - 20 / n, t3);
        } else if (Ladder.ladders[i].monkeys[j] != null
            && Ladder.ladders[i].dir == Dir.L) {
          g.setColor(Color.lightGray);
          g.fillOval(t1, t2, 200 / n + 4,
              200 / n + 4);
          g.setColor(Color.BLACK);
          g.setFont(defaultFont);
          g.drawString("<" + Ladder.ladders[i].monkeys[j].ID,
              20 + 740 / (h + 1) * (j+1) - 20 / n, t3);
        }
      }
    }
    g.setColor(Color.BLACK);
    g.setFont(defaultFont);
    g.drawString("Monkey Number = " + N, 30, 630);
    g.drawString("Ladder Number = " + n, 30, 650);
    g.drawString("Finished Monkey = " + Ladder.countFinish(), 30, 670);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(img, 0, 0, this);
  }
}


