package entities.bomb;

import audio.Sound;
import control.Board;
import entities.Entity;
import graphic.Screen;

public class Flame extends Entity {

    protected Board board;
    protected int direction;//hướng boom
    private int radius;// độ dài
    protected int xOrigin, yOrigin;
    protected FlameSegment[] flameSegments = new FlameSegment[0];
    /**
     *
     * @param x hoành độ bắt đầu của Flame
     * @param y tung độ bắt đầu của Flame
     * @param direction là hướng của Flame
     * @param radius độ dài cực đại của Flame
     */
    public Flame(int x, int y, int direction, int radius, Board board) {
        xOrigin = x;
        yOrigin = y;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.radius = radius;
        this.board = board;
        createFlameSegments();
        Sound s = new Sound("res\\audio\\audioExplosion.wav");
        s.play();
    }


    /**
     * Tạo các FlameSegment, mỗi segment ứng một đơn vị độ dài
     */
    private void createFlameSegments() {
        /**
         * tính toán độ dài Flame, tương ứng với số lượng segment
         */
        flameSegments = new FlameSegment[calculatePermitedDistance()];

        /**
         * biến last dùng để đánh dấu cho segment cuối cùng
         */
        boolean last;

        // TODO: tạo các segment dưới đây
        int x0 = (int) this.x;
        int y0 = (int) this.y;
        for (int i = 0; i < flameSegments.length; i++) {
            last = i == flameSegments.length - 1 ? true : false;

            switch (direction) {
                case 0:
                    y--;
                    break;
                case 1:
                    x++;
                    break;
                case 2:
                    y++;
                    break;
                case 3:
                    x--;
                    break;
            }
            flameSegments[i] = new FlameSegment(x0, y0, direction, last);
        }
    }

    /**
     * Tính toán độ dài của Flame, nếu gặp vật cản là Brick/Wall, độ dài sẽ bị
     * cắt ngắn
     *
     * @return
     */
    private int calculatePermitedDistance() {
        // TODO: thực hiện tính toán độ dài của Flame
        int radius = 0;
        int x0 = (int) this.x;
        int y0 = (int) this.y;
        while (radius < this.radius) {
            if (this.direction == 0) {
                y0--;
            }
            if (this.direction == 1) {
                x0++;
            }
            if (this.direction == 2) {
                y0++;
            }
            if (this.direction == 3) {
                x0--;
            }

            Entity a = board.getEntity(x0, y0, null);

            if (a instanceof Bomb) {
                ++radius; //explosion has to be below the mob
            }
            if (a instanceof entities.character.Mob) {
                ++radius;
            }
            if (a.collide(this) == false) //cannot pass thru
            {
                break;
            }

            ++radius;
        }
        return radius;
    }

    public FlameSegment flameSegmentAt(int x, int y) {
        for (int i = 0; i < flameSegments.length; i++) {
            if (flameSegments[i].getX() == x && flameSegments[i].getY() == y) {
                return flameSegments[i];
            }
        }
        return null;
    }


    @Override
    public void update() {

    }

    @Override
    public void render(Screen screen) {
        for (int i = 0; i < flameSegments.length; i++) {
            flameSegments[i].render(screen);
        }
    }

    @Override
    public boolean collide(Entity e) {
        return true;
    }
}
