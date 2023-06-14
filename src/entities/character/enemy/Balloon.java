package entities.character.enemy;

import control.Board;
import control.Game;
import graphic.Sprite;

public class Balloon extends Enemy{


    public Balloon(int x, int y, Board board) {
        /**
         * chạy nhanh bằng 1/2 player
         */
        super(x, y, board, Sprite.balloom_dead, Game.getBomberSpeed() / 2, 100);
        this.sprite = Sprite.balloom_left1;
        this.direction = random.nextInt(4);
    }


    /**
     * hình động khi di chuyển Balloon
     */
    @Override
    protected void chooseSprite() {
        switch (direction) {
            case 0:
            case 1:
                sprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, animate, 60);
                break;
            case 2:
            case 3:
                sprite = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3, animate, 60);
                break;
        }
    }
}
