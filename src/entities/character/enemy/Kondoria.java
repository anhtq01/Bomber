package entities.character.enemy;

import control.Board;
import control.Game;
import graphic.Sprite;

public class Kondoria extends Enemy{


    public Kondoria(int x, int y, Board board) {
        super(x, y, board, Sprite.kondoria_dead, Game.getPlayerSpeed(), 1000);

        this.sprite = Sprite.kondoria_right1;
        this.direction = random.nextInt(4);
    }
    /*
	|--------------------------------------------------------------------------
	| Mob Sprite
	|--------------------------------------------------------------------------
     */
    @Override
    protected void chooseSprite() {
        switch (direction) {
            case 0:
            case 1:
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, animate, 60);
                } else {
                    sprite = Sprite.kondoria_left1;
                }
                break;
            case 2:
            case 3:
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2, Sprite.kondoria_left3, animate, 60);
                } else {
                    sprite = Sprite.kondoria_left1;
                }
                break;
        }
    }
}
