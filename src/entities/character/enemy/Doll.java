package entities.character.enemy;

import control.Board;
import control.Game;
import graphic.Sprite;

public class Doll extends Enemy{


    public Doll(int x, int y, Board board) {
        super(x, y, board, Sprite.doll_dead, Game.getPlayerSpeed(), 400);

        sprite = Sprite.doll_right1;

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
                    sprite = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, animate, 60);
                } else {
                    sprite = Sprite.doll_left1;
                }
                break;
            case 2:
            case 3:
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.doll_left1, Sprite.doll_left2, Sprite.doll_left3, animate, 60);
                } else {
                    sprite = Sprite.doll_left1;
                }
                break;
        }
    }
}
