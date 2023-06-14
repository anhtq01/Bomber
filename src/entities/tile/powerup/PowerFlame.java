package entities.tile.powerup;

import control.Game;
import entities.tile.Tile;
import graphic.Sprite;

public class PowerFlame extends Power {
    public PowerFlame(int x, int y, int level, Sprite sprite) {
        super(x, y, level, sprite);
    }

    @Override
    public void setValues() {
        active = true;
        Game.addBombRadius(1);
    }
}
