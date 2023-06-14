package entities.tile;

import entities.Entity;
import graphic.Screen;
import graphic.Sprite;
import gui.Coordinates;

public abstract class Tile extends Entity {
    public Tile(int x, int y, Sprite sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    /**
     * Mặc định không cho bất cứ một đối tượng nào đi qua
     */
    @Override
    public boolean collide(Entity e) {
        return false;
    }

    @Override
    public void render(Screen screen) {
        screen.renderEntity( Coordinates.tileToPixel(this.x), Coordinates.tileToPixel(this.y), this);
    }

    @Override
    public void update() {}
}
