package entities.tile;

import audio.Music;
import audio.Sound;
import control.Board;
import control.Game;
import entities.Entity;
import entities.character.Player;
import graphic.Sprite;

public class PortalTile extends Tile {
    protected Board _board;

    public PortalTile(int x, int y, Board board, Sprite sprite) {
        super(x, y, sprite);
        _board = board;
    }

    /**
     * nếu player xử lý hết các quái thì đi vào sẽ pass còn nếu không thì không đi vào được
     */
    @Override
    public boolean collide(Entity e) {
        // TODO: xử lý khi Bomber đi vào
        if (e instanceof Player) {
            if (Game.getBombRate() < Player._bombRateNow) {
                Game.addBombRate(1);
            }
            if (_board.detectNoEnemies() == false) {
                return false;
            }
            try {
                if (e.getXTile() == getX() && e.getYTile() == getY()) {
                    if (_board.detectNoEnemies()) {
                        _board.nextLevel();
                        Sound nextLv = new Sound("res\\audio\\power04.wav");
                        nextLv.play();
                        Music.clipNextLevel.stop();
                        Music.clipScreen.stop();
                        Music.clipTheme.stop();
                    }
                }
            } catch (Exception ex) {
                System.out.println("WIN");
                _board.winGame();
            }

            return true;
        }
        return false;
    }
}
