package entities.tile.powerup;

import control.Game;
import entities.Entity;
import entities.character.Player;
import graphic.Sprite;

public class PowerSpeed extends Power{
    public PowerSpeed(int x, int y, int level, Sprite sprite) {
        super(x, y, level, sprite);
    }

    @Override
    public boolean collide(Entity e) {

        if(e instanceof Player) {
            ((Player) e).addPowerup(this);
            remove();
            return true;
        }

        return false;
    }

    @Override
    public void setValues() {
        active = true;
        Game.addPlayerSpeed(0.1);
    }
}
