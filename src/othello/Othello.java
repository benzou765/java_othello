package othello;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import othello.OthelloPanel;

@SuppressWarnings("serial")
class Othello extends JFrame {
    private static int WIDTH  = 512;
    private static int HEIGHT = 512;

    /**
     * メインクラス
     * @param String[] 引数
     */
    public static void main(String args[]) {
        JFrame frame = new Othello("オセロ");
        frame.setVisible(true);
    }

    Othello(String title) {
        setTitle(title);
        setBounds(100, 100, Othello.WIDTH, Othello.HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        OthelloPanel panel = new OthelloPanel();

        Container contentPane = getContentPane();
        contentPane.add(panel);
    }
}