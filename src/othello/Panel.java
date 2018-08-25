package othello;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
class Panel extends JLabel {
    private static final String IMG_BLACK_PATH = "../img/black.png";
    private static final String IMG_WHITE_PATH = "../img/white.png";
    public static final int IMAGE_WIDTH = 64;
    public static final int IMAGE_HEIGHT = 64;

    private ImageIcon whiteIcon;
    private ImageIcon blackIcon;

    public enum DiscColor {
        None,
        White,
        Black
    };

    private DiscColor state;

    /**
     * コンストラクタ、オセロの枠
     */
    Panel() {
        whiteIcon = new ImageIcon(IMG_WHITE_PATH);
        blackIcon = new ImageIcon(IMG_BLACK_PATH);
        this.state = DiscColor.None;
    }

    /**
     * 指定色の石を配置する
     * @param discColor 石の色
     */
    public void setDisc(DiscColor discColor) {
        switch(discColor) {
        case White:
            this.state = DiscColor.White;
            this.setWhite();
            break;
        case Black:
            this.state = DiscColor.Black;
            this.setBlack();
            break;
        default:
            break;
        }
    }

    /**
     * 石を白に変更
     */
    public void setWhite() {
        setIcon(whiteIcon);
        setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
    }

    /**
     * 石を黒に変更
     */
    public void setBlack() {
        setIcon(blackIcon);
        setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
    }

    /**
     * 石の色を取得する
     */
    public DiscColor getDisc() {
        return this.state;
    }
}