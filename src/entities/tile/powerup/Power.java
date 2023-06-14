package entities.tile.powerup;

import entities.Entity;
import entities.character.Player;
import entities.tile.Tile;
import graphic.Sprite;

public abstract class Power extends Tile {
    protected int duration = -1; // -1 is infinite, duration in lifes
    protected boolean active = false;
    protected int level;

    public Power(int x, int y, int level, Sprite sprite) {
        super(x, y, sprite);
        this.level = level;
    }

    public abstract void setValues();

    public void removeLive() {
        if (duration > 0) {
            duration--;
        }else if (duration == 0) {
            active = false;
        }
    }

    public int getDuration() {
        return duration;
    }

    public int getLevel() {
        return level;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean collide(Entity e) {

        if (e instanceof Player) {
            ((Player) e).addPowerup(this);
            remove();
        }

        return false;
    }
}
