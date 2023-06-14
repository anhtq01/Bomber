package entities.bomb;

import entities.Entity;
import graphic.Screen;
import graphic.Sprite;

public class FlameSegment extends Entity {
    protected boolean last;

    /**
     *
     * @param x
     * @param y
     * @param direction
     * @param last cho biết segment này là cuối cùng của Flame hay không,
     * segment cuối có sprite khác so với các segment còn lại
     */
    public FlameSegment(int x, int y, int direction, boolean last) {
        this.x = x;
        this.y = y;
        this.last = last;

        switch (direction) {
            case 0:
                if (!last) {
                    sprite = Sprite.explosion_vertical2;
                } else {
                    sprite = Sprite.explosion_vertical_top_last2;
                }
                break;
            case 1:
                if (!last) {
                    sprite = Sprite.explosion_horizontal2;
                } else {
                    sprite = Sprite.explosion_horizontal_right_last2;
                }
                break;
            case 2:
                if (!last) {
                    sprite = Sprite.explosion_vertical2;
                } else {
                    sprite = Sprite.explosion_vertical_down_last2;
                }
                break;
            case 3:
                if (!last) {
                    sprite = Sprite.explosion_horizontal2;
                } else {
                    sprite = Sprite.explosion_horizontal_left_last2;
                }
                break;
        }
    }

    @Override
    public void render(Screen screen) {
        /**
         *
         */
        int xt = (int) this.x << 4;
        int yt = (int) this.y << 4;

        screen.renderEntity(xt, yt, this);
    }

    @Override
    public void update() {
    }

    @Override
    public boolean collide(Entity e) {
        /**
         * FlameSegment va chạm với Character
         * kill luôn character(ở đây là Mob)
         */

        if (e instanceof entities.character.Mob) {
            ((entities.character.Mob) e).kill();
        }

        return true;
    }
}
