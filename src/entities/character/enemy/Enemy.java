package entities.character.enemy;

import control.Board;
import control.Game;
import entities.Entity;
import entities.LayerEntity;
import entities.Message;
import entities.bomb.Flame;
import entities.character.Mob;
import entities.character.Player;
import entities.destroyable.BrickTile;
import entities.tile.WallTile;
import graphic.Screen;
import graphic.Sprite;
import gui.Coordinates;

import java.awt.*;
import java.util.Random;

public abstract class Enemy extends Mob {
    // điểm của các con quái
    protected int points;
    // tốc độ của các quái
    protected double speed;
    // dự đoán đường đi của quái
    protected Random random;

    protected final double MAX_STEPS;
    protected final double rest;
    protected double steps;

    protected int finalAnimation = 30;
    protected Sprite deadSprite;

    public Enemy(int x, int y, Board board, Sprite dead, double speed, int points) {
        super(x, y, board);

        this.points = points;
        this.speed = speed;

        MAX_STEPS = Game.TILES_SIZE / this.speed;
        rest = (MAX_STEPS - (int) MAX_STEPS) / MAX_STEPS;
        this.steps = MAX_STEPS;

        this.timeAfter = 20;

        this.deadSprite = dead;
    }

    @Override
    public void update() {
        animate();
        if (alive) {
            calculateMove();
        } else {
            afterKill();
        }
    }

    @Override
    public void render(Screen screen) {

        if (alive) {
            chooseSprite();
        } else {
            if (timeAfter > 0) {
                sprite = deadSprite;
                animate = 0;
            } else {
                // quái sẽ thay đổi vị trí và di chuyển
                sprite = Sprite.movingSprite(Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, animate, 60);
            }
        }

        screen.renderEntity((int) this.x, (int) this.y - this.sprite.SIZE, this);
    }

    @Override
    public void calculateMove() {
        //Tính toán hướng đi và di chuyển Enemy cập nhật giá trị cho direction
        //sử dụng canMove() để kiểm tra xem có thể di chuyển tới điểm đã tính toán hay không
        //sử dụng move() để di chuyển
        //nhớ cập nhật lại giá trị cờ moving khi thay đổi trạng thái di chuyển
        int xa = 0, ya = 0;
        if (steps <= 0) {
            direction = random.nextInt(4);
            steps = MAX_STEPS;
            // đã đi xog
        }

        if (direction == 0) {
            ya--;
        }
        if (direction == 2) {
            ya++;
        }
        if (direction == 3) {
            xa--;
        }
        if (direction == 1) {
            xa++;
        }

        if (canMove(xa, ya)) {
            steps -= 1 + rest;
            move(xa * speed, ya * speed);
            moving = true;
        } else {
            steps = 0;
            moving = false;
        }
    }

    @Override
    public void move(double xa, double ya) {

        if (alive) {
            this.x += xa;
            this.y += ya;
        }
    }

    @Override
    public boolean canMove(double x, double y) {
        // kiểm tra có đối tượng tại vị trí chuẩn bị di chuyển đến và có thể di chuyển tới đó hay không
        double xr = this.x, yr = this.y - 16; //subtract y to get more accurate results
        if (direction == 0) {
            yr += sprite.getSize() - 1;
            xr += sprite.getSize() / 2;
        }
        if (direction == 1) {
            yr += sprite.getSize() / 2;
            xr += 1;
        }
        if (direction == 2) {
            xr += sprite.getSize() / 2;
            yr += 1;
        }
        if (direction == 3) {
            xr += sprite.getSize() - 1;
            yr += sprite.getSize() / 2;
        }

        int xx = Coordinates.pixelToTile(xr) + (int) x;
        int yy = Coordinates.pixelToTile(yr) + (int) y;

        Entity a = board.getEntity(xx, yy, this); //entity of the position we want to go

        return a.collide(this);

    }

    @Override
    public boolean collide(Entity e) {
        /**
         * xử lý 2 va chạm với player và Flame(người chơi và lửa)
         */
        if (e instanceof Flame) {
            kill();
            return false;
        }

        if (e instanceof Player) {
            ((Player) e).kill();
            return false;
        }
        if (e instanceof WallTile) {
            return false;
        }
        if (e instanceof BrickTile) {
            return false;
        }
        if (e instanceof LayerEntity) {
            return e.collide(this);
        }
        return true;
    }

    @Override
    public void kill() {

        if (alive) {
            alive = false;

            board.addPoints(points);

            Message msg = new Message("+" + points, getXMessage(), getYMessage(), 2, Color.white, 14);
            board.addMessage(msg);
        }
    }

    @Override
    protected void afterKill() {
        if (timeAfter > 0) {
            --timeAfter;
        } else {
            if (finalAnimation > 0) {
                --finalAnimation;
            } else {
                // xóa đối tượng quái đó đi
                remove();
            }
        }
    }

    protected abstract void chooseSprite();
}
