package lab6;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.WindowConstants;



public class GUI {

  public static void draw(Scene scene) {
    JFrame frame = new JFrame("My first frame");
    Font font = new Font("Monospaced", Font.BOLD, 32);
    JPanel panel = new JPanel();
    // frame.setLayout(null);
    frame.setSize(977, 977);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    panel.setBackground(Color.WHITE);
    for (int i = 0; i < scene.getNumofLad(); ++i) {
      for (int j = 0; j < scene.getLenofLad(); ++j) {
        JLabel label = new JLabel();
        label.setFont(font);
        label.setSize(120, 120);
        label.setLocation(i * 120, j * 120);
        if ((i + j) % 2 == 0) {
          // label.setForeground(Color.WHITE);
          // label.setBackground(Color.black);
          label.setOpaque(true);
        } else {// label.setForeground(Color.BLACK);
          label.setBackground(Color.white);
          label.setOpaque(true);
        }
        frame.add(label);
      }
    } 
    frame.add(panel);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    
  }
}
