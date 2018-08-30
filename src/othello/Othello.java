package othello;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import othello.Panel;
import othello.Panel.DiscColor;

/**
 * オセロ
 */
@SuppressWarnings("serial")
class Othello extends JPanel implements MouseListener {
    /**
     * パネルのサイズ
     */
    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;

    /**
     * オセロ盤のサイズ
     */
    private static final int SIZE_X = 8;
    private static final int SIZE_Y = 8;

    /**
     * オセロの石の初期位置
     */
    private static final int START_DISC_X = 3;
    private static final int START_DISC_Y = 3;

    /**
     * 盤の各枠の大きさ
     */
    public static final int FRAME_WIDTH = 64;
    public static final int FRAME_HEIGHT = 64;

    /**
     * ターンの名称
     */
    private enum TurnName {
        First,
        Second
    };

    /**
     * ゲーム状態の名称
     */
    private enum StateName {
        Playing,
        FirstWin,
        SecondWin
    };

    /**
     * コンピュータ難易度
     */
    private enum VsComputer {
        None
    }

    /**
     * 盤の状態
     */
    private ArrayList<ArrayList<Panel>> board;
    /**
     * 現在のターン
     */
    private TurnName turn;
    /**
     * 操作可能状態か
     */
    private boolean isControl;
    /**
     * コンピュータ難易度
     */
    private VsComputer vsComputer;

    /**
     * 画面処理のバッファ
     */
    private Image back;

    /**
     * コンストラクタ、ゲーム本体
     */
    Othello() {
        // 描画準備
        this.setBackground(Color.GREEN);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setLayout(null);
        this.setBounds(0, 0, WIDTH, HEIGHT);
        Dimension dimension = this.getSize();
        this.back = createImage(dimension.width, dimension.height);

        // オセロ盤の作成
        this.board = new ArrayList<ArrayList<Panel>>(SIZE_Y);
        for(int j = 0; j < SIZE_Y; j++) {
            ArrayList<Panel> row = new ArrayList<Panel>(SIZE_X);
            for(int i = 0; i < SIZE_X; i++) {
                int x = FRAME_WIDTH * i;
                int y = FRAME_HEIGHT * j;
                Panel panel = new Panel(x, y);
                row.add(panel);
            }
            this.board.add(row);
        }

        // 石を初期位置に配置
        this.board.get(START_DISC_Y  ).get(START_DISC_X).setDisc(Panel.DiscColor.White);
        this.board.get(START_DISC_Y  ).get(START_DISC_X+1).setDisc(Panel.DiscColor.Black);
        this.board.get(START_DISC_Y+1).get(START_DISC_X).setDisc(Panel.DiscColor.Black);
        this.board.get(START_DISC_Y+1).get(START_DISC_X+1).setDisc(Panel.DiscColor.White);

        // ゲーム状態の初期化
        this.turn = TurnName.First;
        this.isControl = true;
        this.vsComputer = VsComputer.None;

        // 操作受付
        addMouseListener(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
//        Graphics buffer = back.getGraphics();
        // 枠線の描画
        g.setColor(Color.BLACK);
        // 縦線
        for (int i = 0; i < SIZE_X; i++) {
            g.drawLine((FRAME_WIDTH * i), 0, (FRAME_WIDTH * i), HEIGHT);
            g.drawLine((FRAME_WIDTH * (i+1) - 1), 0, (FRAME_WIDTH * (i+1) - 1), HEIGHT);
        }
        // 横線
        for (int i = 0; i < SIZE_Y; i++) {
            g.drawLine(0, (FRAME_HEIGHT * i), WIDTH, (FRAME_WIDTH * i));
            g.drawLine(0, (FRAME_WIDTH * (i+1) - 1), WIDTH, (FRAME_WIDTH * (i+1) - 1));
        }

        // 石の描画
        for(ArrayList<Panel> row : this.board) {
            for(Panel panel : row) {
                panel.draw(g);
            }
        }
    }

    /**
     * 未使用
     */
    public void mouseEntered(MouseEvent e) {
        repaint();
    }

    /**
     * 未使用
     */
    public void mouseExited(MouseEvent e) {
        repaint();
    }

    /**
     * 未使用
     */
    public void mousePressed(MouseEvent e) {
        repaint();
    }

    /**
     * 未使用
     */
    public void mouseReleased(MouseEvent e) {
        repaint();
    }

    /**
     * マウスクリック時の操作
     */
    public void mouseClicked(MouseEvent e) {
        if (isControl) {
            if (e.getButton() == MouseEvent.BUTTON2) {
                // 中央クリック時はパスとみなす
                System.out.println("パス");
                this.turnNext();
            }
            Panel.DiscColor color = Panel.DiscColor.None;
            switch(this.turn) {
            case First:
                color = Panel.DiscColor.Black;
                break;
            case Second:
                color = Panel.DiscColor.White;
                break;
            }

            // クリック場所の取得
            Point boardPoint = this.getBoardPoint(e.getPoint());
            if (this.board.get(boardPoint.y).get(boardPoint.x).getDisc() == Panel.DiscColor.None) {
                ArrayList<Point> sandwichedDiscs = this.getSandwichedDisc(boardPoint, color);

                if(this.checkNextTurn(sandwichedDiscs)) {
                    // 石を配置して次ターンへ
                    this.setDisc(boardPoint, color);
                    this.changeDisc(sandwichedDiscs, color);
                    this.turnNext();
                }
            }
        }
        repaint();
    }

    /**
     * パス以外の選択肢がないか確認
     * @param color 石の色
     * @return パスしかないかどうか
     */
    private boolean checkPass(Panel.DiscColor color) {
        for(int j = 0; j < SIZE_Y; j++) {
            for(int i = 0; i < SIZE_X; i++) {
                if(this.board.get(j).get(i).getDisc() == Panel.DiscColor.None) {
                    Point pt = new Point(i, j);
                    if(this.getSandwichedDisc(pt, color).size() > 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 次のターンへ遷移できるか確認
     */
    private boolean checkNextTurn(ArrayList<Point> points) {
        if(points.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * リスト内の座標にある石を指定色に変更する
     * @param points 座標のリスト
     * @param color 石の色
     */
    private void changeDisc(ArrayList<Point> points, Panel.DiscColor color) {
        for(Point pt : points) {
            this.setDisc(pt, color);
        }
    }

    /**
     * 次のターンへ移行
     */
    private void turnNext() {
        this.isControl = false;
        if (this.checkFinish()) {
            // debug
            System.out.println("End");
            this.drawWinner();
            return;
        }
        if (this.turn == TurnName.First) {
            this.turn = TurnName.Second;
            Panel.DiscColor color = Panel.DiscColor.White;
            if(this.checkPass(color)) {
                this.turnNext();
            }
            this.isControl = true;
        } else {
            switch(this.vsComputer) {
            case None:
                this.turn = TurnName.First;
                this.isControl = true;
                Panel.DiscColor color = Panel.DiscColor.Black;
                if(this.checkPass(color)) {
                    this.turnNext();
                }
                break;
            }
        }
    }

    /**
     * 石を配置する
     * @param point 盤上の座標
     * @param color 石の色
     */
    private void setDisc(Point point, Panel.DiscColor color) {
        board.get(point.y).get(point.x).setDisc(color);
    }

    /**
     * マウス座標から盤上の位置を取得する
     * @param point マウス座標
     * @return 盤上の座標
     */
    private Point getBoardPoint(Point point) {
        int x = point.x / FRAME_WIDTH;
        int y = point.y / FRAME_HEIGHT;
        return new Point(x, y);
    }

    /**
     * 該当場所に石をおいたとき、挟まれていると判定できる石の座標リストを取得する
     * @param point 配置する石の盤上の座標
     * @param color 配置する石の色
     * @return 挟まれている石の座標リスト
     * TODO 画面端の処理
     */
    private ArrayList<Point> getSandwichedDisc(Point point, Panel.DiscColor setColor) {
        ArrayList<Point> sandwichedDiscs = new ArrayList<Point>();
        int x;
        int y;

        //   上方向
        x = point.x;
        y = point.y - 1;
        ArrayList<Point> addDiscs = new ArrayList<Point>();
        while(y >= 0) {
            Panel.DiscColor targetColor = board.get(y).get(x).getDisc();
            if(targetColor == Panel.DiscColor.None) {
                // 置いた石と異なる色の石がないので、石は挟まれていない
                addDiscs.clear();
                break;
            }
            if(targetColor == setColor) {
                // 置いた石と同じ色の石ならループ終了
                break;
            }
            addDiscs.add(new Point(x, y));
            y--;
        }
        if (y == -1) {
            // 端までいっても異なる色の石がないので、石は挟まれていない
            addDiscs.clear();
        }
        for(Point pt : addDiscs) {
            sandwichedDiscs.add(pt);
        }
        addDiscs.clear();

        // 右上方向
        x = point.x + 1;
        y = point.y - 1;
        while(x < SIZE_X && y >= 0) {
            Panel.DiscColor targetColor = board.get(y).get(x).getDisc();
            if(targetColor == Panel.DiscColor.None) {
                // 置いた石と異なる色の石がないので、石は挟まれていない
                addDiscs.clear();
                break;
            }
            if(targetColor == setColor) {
                // 置いた石と同じ色の石ならループ終了
                break;
            }
            addDiscs.add(new Point(x, y));
            x++;
            y--;
        }
        if (x == SIZE_X || y == -1) {
            // 端までいっても異なる色の石がないので、石は挟まれていない
            addDiscs.clear();
        }
        for(Point pt : addDiscs) {
            sandwichedDiscs.add(pt);
        }
        addDiscs.clear();

        //   右方向
        x = point.x + 1;
        y = point.y;
        while(x < SIZE_X) {
            Panel.DiscColor targetColor = board.get(y).get(x).getDisc();
            if(targetColor == Panel.DiscColor.None) {
                // 置いた石と異なる色の石がないので、石は挟まれていない
                addDiscs.clear();
                break;
            }
            if(targetColor == setColor) {
                // 置いた石と同じ色の石ならループ終了
                break;
            }
            addDiscs.add(new Point(x, y));
            x++;
        }
        if (x == SIZE_X) {
            // 端までいっても異なる色の石がないので、石は挟まれていない
            addDiscs.clear();
        }
        for(Point pt : addDiscs) {
            sandwichedDiscs.add(pt);
        }
        addDiscs.clear();

        // 右下方向
        x = point.x + 1;
        y = point.y + 1;
        while(x < SIZE_X && y < SIZE_Y) {
            Panel.DiscColor targetColor = board.get(y).get(x).getDisc();
            if(targetColor == Panel.DiscColor.None) {
                // 置いた石と異なる色の石がないので、石は挟まれていない
                addDiscs.clear();
                break;
            }
            if(targetColor == setColor) {
                // 置いた石と同じ色の石ならループ終了
                break;
            }
            addDiscs.add(new Point(x, y));
            x++;
            y++;
        }
        if (x == SIZE_X || y == SIZE_Y) {
            // 端までいっても異なる色の石がないので、石は挟まれていない
            addDiscs.clear();
        }
        for(Point pt : addDiscs) {
            sandwichedDiscs.add(pt);
        }
        addDiscs.clear();

        //   下方向
        x = point.x;
        y = point.y + 1;
        while(y < SIZE_Y) {
            Panel.DiscColor targetColor = board.get(y).get(x).getDisc();
            if(targetColor == Panel.DiscColor.None) {
                // 置いた石と異なる色の石がないので、石は挟まれていない
                addDiscs.clear();
                break;
            }
            if(targetColor == setColor) {
                // 置いた石と同じ色の石ならループ終了
                break;
            }
            addDiscs.add(new Point(x, y));
            y++;
        }
        if (y == SIZE_Y) {
            // 端までいっても異なる色の石がないので、石は挟まれていない
            addDiscs.clear();
        }
        for(Point pt : addDiscs) {
            sandwichedDiscs.add(pt);
        }
        addDiscs.clear();

        // 左下方向
        x = point.x - 1;
        y = point.y + 1;
        while(x >= 0 && y < SIZE_Y) {
            Panel.DiscColor targetColor = board.get(y).get(x).getDisc();
            if(targetColor == Panel.DiscColor.None) {
                // 置いた石と異なる色の石がないので、石は挟まれていない
                addDiscs.clear();
                break;
            }
            if(targetColor == setColor) {
                // 置いた石と同じ色の石ならループ終了
                break;
            }
            addDiscs.add(new Point(x, y));
            x--;
            y++;
        }
        if (x == -1 || y == SIZE_Y) {
            // 端までいっても異なる色の石がないので、石は挟まれていない
            addDiscs.clear();
        }
        for(Point pt : addDiscs) {
            sandwichedDiscs.add(pt);
        }
        addDiscs.clear();

        //   左方向
        x = point.x - 1;
        y = point.y;
        while(x >= 0) {
            Panel.DiscColor targetColor = board.get(y).get(x).getDisc();
            if(targetColor == Panel.DiscColor.None) {
                // 置いた石と異なる色の石がないので、石は挟まれていない
                addDiscs.clear();
                break;
            }
            if(targetColor == setColor) {
                // 置いた石と同じ色の石ならループ終了
                break;
            }
            addDiscs.add(new Point(x, y));
            x--;
        }
        if (x == -1) {
            // 端までいっても異なる色の石がないので、石は挟まれていない
            addDiscs.clear();
        }
        for(Point pt : addDiscs) {
            sandwichedDiscs.add(pt);
        }
        addDiscs.clear();

        // 左上方向
        x = point.x - 1;
        y = point.y - 1;
        while(x >= 0 && y >= 0) {
            Panel.DiscColor targetColor = board.get(y).get(x).getDisc();
            if(targetColor == Panel.DiscColor.None) {
                // 置いた石と異なる色の石がないので、石は挟まれていない
                addDiscs.clear();
                break;
            }
            if(targetColor == setColor) {
                // 置いた石と同じ色の石ならループ終了
                break;
            }
            addDiscs.add(new Point(x, y));
            x--;
            y--;
        }
        if (x == -1 || y == -1) {
            // 端までいっても異なる色の石がないので、石は挟まれていない
            addDiscs.clear();
        }
        for(Point pt : addDiscs) {
            sandwichedDiscs.add(pt);
        }
        addDiscs.clear();

        return sandwichedDiscs;
    }

    /**
     * 勝敗の表示
     * TODO
     */
    private void drawWinner() {
        JLabel label = new JLabel();
    }

    /**
     * ゲームの状態を確認
     * @return ゲームが終了したかどうか
     */
    private boolean checkFinish() {
        for (ArrayList<Panel> row : this.board) {
            for (Panel panel : row) {
                if(panel.getDisc() == Panel.DiscColor.None) {
                    return false;
                }
            }
        }
        return true;
    }
}