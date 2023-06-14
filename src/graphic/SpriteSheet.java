package graphic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SpriteSheet {
    private String _path;
    public final int SIZE;
    public int[] _pixels;

    public static SpriteSheet tiles = new SpriteSheet("res/textures/classic.png", 256);

    public SpriteSheet(String path, int size) {
        _path = path;
        SIZE = size;
        _pixels = new int[SIZE * SIZE];
        load();
    }

    private void load() {
        try {
            File file = new File(_path);
            //URL a = SpriteSheet.class.getResource(_path);
            BufferedImage image = ImageIO.read(file);
            int w = image.getWidth();
            int h = image.getHeight();
            image.getRGB(0, 0, w, h, _pixels, 0, w);
        } catch (IOException e) {
            //e.printStackTrace();
            //TODO: what should this do? stop the program? yes i think
            System.out.println("Sprite sheet");
            //System.exit(0);
        }
    }
}
