import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class GUI {
  public String[] promptForm(JFrame owner, String title) {
    String[] form = {"梯子数量","猴子生成间隔时间","猴子总数","每次生成猴子数量","最大速度"};
    JTextField[] formArray = new JTextField[form.length];
    String[] input = new String[form.length];

    class PromptDialog extends JDialog {

      private PromptDialog() {
        super(owner, title);
        int y = 8;
        Container panel = getContentPane();
        panel.setLayout(null);

        for (int i = 0; i < form.length; i++) {
          JLabel lbl;
          panel.add(lbl = new JLabel(form[i]));
          panel.add(formArray[i] = new JTextField());
          lbl.setBounds(8, y, 256, 24);
          y += 32;
          formArray[i].setBounds(8, y, 256, 24);
          y += 32;
        }

        JButton btn = new JButton("OK");
        panel.add(btn);
        btn.setBounds(200, y, 56, 24);
        setBounds(400, 200, 292, y + 68);

        btn.addActionListener(e -> {
          for (int i = 0; i < formArray.length; i++) {
            input[i] = formArray[i].getText();
          }
          this.dispose();
        });
        setModal(true);
      }
    }

    PromptDialog dialog = new PromptDialog();
    dialog.setVisible(true);
    return input;
  }
  public static String prompt(JFrame owner, String title, String msg, String def) {
    StringBuffer p = new StringBuffer();
    class PromptDialog extends JDialog {

      private PromptDialog() {
        super(owner, title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(400, 200, 368, 128);
        Container panel = getContentPane();
        panel.setLayout(null);
        JLabel lbl;
        JTextField txt;
        JButton btn;
        panel.add(lbl = new JLabel(msg));
        panel.add(txt = new JTextField(def));
        panel.add(btn = new JButton("OK"));

        lbl.setBounds(8, 8, 256, 24);
        txt.setBounds(8, 40, 256, 24);
        btn.setBounds(288, 40, 56, 24);

        if (def != null) {
          txt.setCaretPosition(def.length());
        }

        ActionListener act = e -> {
          p.append(txt.getText());
          this.dispose();
        };
        btn.addActionListener(act);

        txt.registerKeyboardAction(act,
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
            JComponent.WHEN_FOCUSED);

        setModal(true);
      }
    }

    var dialog = new PromptDialog();
    dialog.setVisible(true);
    return p.toString();
  }

  public MonkeyGenerator choose() {
    String p = prompt(null, "选择", "1.随机生成   2.读入文件", "1");
    if(p.equals("1")){
      String [] s = promptForm(null,"输入");
      return new MonkeyGenerator(Integer.valueOf(s[0]),20,Integer.valueOf(s[1]),Integer.valueOf(s[2]),Integer.valueOf(s[3]),Integer.valueOf(s[4]));
    }
    return null;
  }
  public static void main(String[] args) {
    GUI g = new GUI();
    g.choose();
  }
}
