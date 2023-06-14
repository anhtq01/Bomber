package entities.tile;

import entities.Entity;
import graphic.Sprite;

public class GrassTile extends Tile{
    public GrassTile(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    /**
     * Cho bất kì đối tượng khác đi qua
     * @param e
     * @return
     */
    @Override
    public boolean collide(Entity e) {
        return true;
    }
}
