package entities.character.enemy;

import control.Board;
import control.Game;
import graphic.Sprite;

public class Minvo extends Enemy{


    public Minvo(int x, int y, Board board) {
        super(x, y, board, Sprite.minvo_dead, Game.getPlayerSpeed() * 2, 800);

        this.sprite = Sprite.minvo_right1;

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
                    sprite = Sprite.movingSprite(Sprite.minvo_right1, Sprite.minvo_right2, Sprite.minvo_right3, animate, 60);
                } else {
                    sprite = Sprite.minvo_left1;
                }
                break;
            case 2:
            case 3:
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.minvo_left1, Sprite.minvo_left2, Sprite.minvo_left3, animate, 60);
                } else {
                    sprite = Sprite.minvo_left1;
                }
                break;
            default:
                System.out.println("Minvo moving choose sprite");
        }
    }
}
