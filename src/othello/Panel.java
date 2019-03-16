package othello;

import java.io.File;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

class Panel {
    private static final String IMG_BLACK_PATH = "../img/black.png";
    private static final String IMG_WHITE_PATH = "../img/white.png";
    public static final int IMAGE_WIDTH = 64;
    public static final int IMAGE_HEIGHT = 64;

    private BufferedImage whiteImage;
    private BufferedImage blackImage;

    public enum DiscColor {
        None,
        White,
        Black
    };

    private DiscColor state;

    private final int x;
    private final int y;

    /**
     * コンストラクタ、オセロの枠
     * @param x 描画時のX座標の位置
     * @param y 描画時のY座標の位置
     */
    Panel(int x, int y) {
        try {
            this.whiteImage = ImageIO.read(new File(IMG_WHITE_PATH));
            this.blackImage = ImageIO.read(new File(IMG_BLACK_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.state = DiscColor.None;
        this.x = x;
        this.y = y;
    }

    /**
     * 指定色の石を配置する
     * @param discColor 石の色
     */
    public void setDisc(DiscColor discColor) {
        switch(discColor) {
        case White:
            this.state = DiscColor.White;
            break;
        case Black:
            this.state = DiscColor.Black;
            break;
        default:
            break;
        }
    }

    /**
     * 石の色を取得する
     */
    public DiscColor getDisc() {
        return this.state;
    }

    /**
     * 描画処理
     */
    public void draw(Graphics g) {
        switch(this.state) {
        case White:
            g.drawImage(this.whiteImage, this.x, this.y, null);
            break;
        case Black:
            g.drawImage(this.blackImage, this.x, this.y, null);
            break;
        default:
            break;
        }
    }
}