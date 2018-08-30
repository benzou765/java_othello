package othello;

import javax.swing.JFrame;

import othello.Othello;

@SuppressWarnings("serial")
class Game extends JFrame {
    /**
     * ゲーム画面の大きさを定義する定数
     */
    public static final int WIDTH  = 512;
    public static final int TITLE_BAR = 22;
    public static final int HEIGHT = 512 + TITLE_BAR;

    /**
     * メインクラス
     * @param String[] 引数
     */
    public static void main(String args[]) {
        JFrame frame = new Game("オセロ");
        frame.setVisible(true);
    }

    /**
     * コンストラクタ、ゲーム本体
     * @param String ウィンドウタイトル
     */
    Game(String title) {
        // ウィンドウの初期化
        this.setTitle(title);
        this.setBounds(100, 100, WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);

        Othello othello = new Othello();
        this.getContentPane().add(othello);
    }

}