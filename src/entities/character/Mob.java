package entities.character;

import control.Board;
import control.Game;
import entities.AnimatedEntitiy;
import graphic.Screen;

public abstract class Mob extends AnimatedEntitiy {
    protected Board board;
    protected int direction = -1;
    protected boolean alive = true;
    protected boolean moving = false;
    public int timeAfter = 40;

    public Mob(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
    }

    @Override
    public abstract void update();

    @Override
    public abstract void render(Screen screen);

    /**
     * Tính toán hướng đi
     */
    protected abstract void calculateMove();

    protected abstract void move(double xa, double ya);

    /**
     * Được gọi khi đối tượng bị tiêu diệt
     */
    public abstract void kill();

    /**
     * Xử lý hiệu ứng bị tiêu diệt
     */
    protected abstract void afterKill();

    /**
     * Kiểm tra xem đối tượng có di chuyển tới vị trí đã tính toán hay không
     * @param x
     * @param y
     * @return
     */
    protected abstract boolean canMove(double x, double y);

    protected double getXMessage() {
        return (this.x * Game.SCALE) + (this.sprite.SIZE / 2 * Game.SCALE);
    }

    protected double getYMessage() {
        return (this.y* Game.SCALE) - (this.sprite.SIZE / 2 * Game.SCALE);
    }
}
