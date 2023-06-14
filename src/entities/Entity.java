package entities;

import graphic.IRender;
import graphic.Screen;
import graphic.Sprite;
import gui.Coordinates;


public abstract class Entity implements IRender {
    protected double x, y;
    protected boolean removed = false;
    protected Sprite sprite;

    @Override
    public abstract void update();

    @Override
    public abstract void render(Screen screen);

    public void remove() {
        removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public abstract boolean collide(Entity e);

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getXTile() {
        return Coordinates.pixelToTile(x + sprite.SIZE / 2); //plus half block for precision
    }

    public int getYTile() {
        return Coordinates.pixelToTile(y - sprite.SIZE / 2); //plus half block
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

}
