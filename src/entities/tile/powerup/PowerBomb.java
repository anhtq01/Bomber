package entities.tile.powerup;

import control.Game;
import graphic.Sprite;

public class PowerBomb extends Power{
    public PowerBomb(int x, int y, int level, Sprite sprite) {
        super(x, y, level, sprite);
    }


    @Override
    public void setValues() {
        active = true;
        Game.addBombRate(1);
    }
}
