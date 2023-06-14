package entities.bomb;

import control.Board;
import control.Game;
import entities.AnimatedEntitiy;
import entities.Entity;
import entities.character.Player;
import graphic.Screen;
import graphic.Sprite;
import gui.Coordinates;

public class Bomb extends AnimatedEntitiy {
    protected double timeToExplode = 120; //2 seconds
    public int timeAfter = 20;

    protected Board board;
    protected Flame[] flames;
    protected boolean exploded = false;
    protected boolean allowedToPassThru = true;

    public Bomb(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
        this.sprite = Sprite.bomb;
    }

    @Override
    public void update() {
        if (this.timeToExplode > 0) {
            this.timeToExplode--;
        } else {
            if (!this.exploded) {
                explode();
            } else {
                updateFlames();
            }

            if (this.timeAfter > 0) {
                this.timeAfter--;
            } else {
                remove();
            }
        }

        animate();
    }

    @Override
    public void render(Screen screen) {
        if (this.exploded) {
            this.sprite = Sprite.bomb_exploded2;
            renderFlames(screen);
        } else {
            this.sprite = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, this.animate, 60);
        }

        int xt = (int) this.x << 4;
        int yt = (int) this.y << 4;

        screen.renderEntity(xt, yt, this);
    }

    public void renderFlames(Screen screen) {
        for (int i = 0; i < this.flames.length; i++) {
            this.flames[i].render(screen);
        }
    }

    public void updateFlames() {
        for (int i = 0; i < this.flames.length; i++) {
            this.flames[i].update();
        }
    }

    /**
     * Xử lý Bomb nổ
     */
    protected void explode() {
        allowedToPassThru = true;
        exploded = true;

        // TODO: xử lý khi Character đứng tại vị trí Bomb
        entities.character.Mob a = board.getCharacterAtExcluding((int) this.x, (int) this.y, null);
        if (a != null) {
            a.kill();
        }
        // TODO: tạo các Flame
        flames = new Flame[4];

        for (int i = 0; i < flames.length; i++) {
            flames[i] = new Flame((int) this.x, (int) this.y, i, Game.getBombRadius(), board);
        }
    }

    public FlameSegment flameAt(int x, int y) {
        if (!exploded) {
            return null;
        }

        for (int i = 0; i < flames.length; i++) {
            if (flames[i] == null) {
                return null;
            }
            FlameSegment e = flames[i].flameSegmentAt(x, y);
            if (e != null) {
                return e;
            }
        }
        return null;
    }

    public boolean isExploded() {
        return exploded;
    }

    @Override
    public boolean collide(Entity e) {
        // xử lý khi Player đi ra sau khi vừa đặt bom (_allowedToPassThru)

        if (e instanceof Player){
            double diffX = e.getX() - Coordinates.tileToPixel(getX());
            double diffY = e.getY() - Coordinates.tileToPixel(getY());

            if (!(diffX >= -10 && diffX < 16 && diffY >= 1 && diffY <= 28)) { // differences to see if the player has moved out of the bomb, tested values
                allowedToPassThru = false;
            }

            return allowedToPassThru;
        }
        // TODO: xử lý va chạm với Flame của Bomb khác
        if (e instanceof Flame && !isExploded()) {
            explode();
            return true;
        }
        return false;
    }
}
