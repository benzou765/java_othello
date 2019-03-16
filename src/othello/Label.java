package othello;

import java.io.File;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

class Label {
    private static final String IMG_WIN_PATH = "../img/win.png";
    private static final String IMG_LOSE_PATH = "../img/lose.png";
    private static final String IMG_DRAW_PATH = "../img/draw.png";
    public static final int IMAGE_WIDTH = 200;
    public static final int IMAGE_HEIGHT = 30;

    private static final int X = 220;
    private static final int Y = 100;

    private BufferedImage image;

    public enum Issue {
        Win,
        Lose,
        Draw
    }

    public Label() {
        this.image = null;
    }

    public void loadImage(Issue issue) {
        String path = "";
        switch(issue) {
        case Win:
            path = IMG_WIN_PATH;
        case Lose:
            path = IMG_LOSE_PATH;
        default:
            path = IMG_DRAW_PATH;
        }
        try {
            this.image = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        g.drawImage(this.image, X, Y, null);
    }
}