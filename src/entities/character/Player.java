package entities.character;

import audio.Sound;
import control.Board;
import control.Game;
import entities.Entity;
import entities.LayerEntity;
import entities.Message;
import entities.bomb.Bomb;
import entities.bomb.Flame;
import entities.character.enemy.Enemy;
import entities.destroyable.BrickTile;
import entities.tile.WallTile;
import entities.tile.powerup.Power;
import entities.tile.powerup.PowerBomb;
import graphic.Screen;
import graphic.Sprite;
import gui.Coordinates;
import input.KeyBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player extends Mob{
    private final List<Bomb> bombs;
    protected KeyBoard input;

    /**
     * nếu giá trị này < 0 thì cho phép đặt đối tượng Bomb tiếp theo, cứ mỗi lần
     * đặt 1 Bomb mới, giá trị này sẽ được reset về 0 và giảm dần trong mỗi lần
     * update()
     */
    protected int _timeBetweenPutBombs = 0;
    public static int _bombRateNow = 0;
    public static List<Power> _items = new ArrayList<Power>();

    public Player(int x, int y, Board board) {
        super(x, y, board);
        this.bombs = (List<Bomb>) board.getBombs();
        this.input = board.getInput();
        this.sprite = Sprite.player_right;
    }

    @Override
    public void update() {
        clearBombs();
        if (!alive) {
            afterKill();
            return;
        }

        if (_timeBetweenPutBombs < -7500) {
            _timeBetweenPutBombs = 0;
        } else {
            _timeBetweenPutBombs--;
        }

        animate();

        calculateMove();

        detectPlaceBomb();
    }

    @Override
    public void render(Screen screen) {
        calculateXOffset();

        if (alive) {
            chooseSprite();
        } else {
            sprite = Sprite.player_dead1;
        }

        screen.renderEntity((int) this.x, (int) this.y - this.sprite.SIZE, this);
    }

    public void calculateXOffset() {
        int xScroll = Screen.calculateXOffset(this.board, this);
        Screen.setOffset(xScroll, 0);
    }

    /**
     * Kiểm tra xem có đặt được bom hay không? nếu có thì đặt bom tại vị trí
     * hiện tại của Bomber
     */
    private void detectPlaceBomb() {
        // TODO: kiểm tra xem phím điều khiển đặt bom có được gõ và giá trị _timeBetweenPutBombs, Game.getBombRate() có thỏa mãn hay không
        // TODO:  Game.getBombRate() sẽ trả về số lượng bom có thể đặt liên tiếp tại thời điểm hiện tại
        // TODO: _timeBetweenPutBombs dùng để ngăn chặn Bomber đặt 2 Bomb cùng tại 1 vị trí trong 1 khoảng thời gian quá ngắn
        // TODO: nếu 3 điều kiện trên thỏa mãn thì thực hiện đặt bom bằng placeBomb()
        // TODO: sau khi đặt, nhớ giảm số lượng Bomb Rate và reset _timeBetweenPutBombs về 0
        if (input.space && Game.getBombRate() > 0 && _timeBetweenPutBombs < 0) {

            int xt = Coordinates.pixelToTile(this.x + sprite.getSize() / 2);
            int yt = Coordinates.pixelToTile((this.y + sprite.getSize() / 2) - sprite.getSize()); //subtract half player height and minus 1 y position

            placeBomb(xt, yt);
            Game.addBombRate(-1);

            _timeBetweenPutBombs = 30;
        }
    }

    protected void placeBomb(int x, int y) {
        // TODO: thực hiện tạo đối tượng bom, đặt vào vị trí (x, y)
        Bomb b = new Bomb(x, y, board);
        board.addBomb(b);
    }

    private void clearBombs() {
        Iterator<Bomb> bs = bombs.iterator();

        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
                Game.addBombRate(1);
            }
        }

    }

    @Override
    public void kill() {
        if (alive) {
            alive = false;
            board.subLives(1);
            Message message = new Message("-1 LIVE", getXMessage(), getYMessage(), 2, Color.white, 14);
            board.addMessage(message);
            Sound die = new Sound("res\\audio\\audioBomberDead.wav");
            die.play();
        }
    }

    @Override
    protected void afterKill() {
        if (timeAfter > 0) {
            --timeAfter;
        } else {
            if (board.getLives() == 0) {
                board.endGame();
            } else {
                board.restartLevel();
                Game.bombRate = 1;
            }
        }
    }

    @Override
    protected void calculateMove() {
        // TODO: xử lý nhận tín hiệu điều khiển hướng đi từ _input và gọi move() để thực hiện di chuyển
        // TODO: nhớ cập nhật lại giá trị cờ _moving khi thay đổi trạng thái di chuyển
        double xa = 0, ya = 0;
        if (input.right) { // sang phải
            xa += Game.bomberSpeed;
        } else if (input.left) { // sang trái
            xa -= Game.bomberSpeed;
        } else if (input.up) { // lên trên
            ya -= Game.bomberSpeed;
        } else if (input.down) { // xuống dưới
            ya += Game.bomberSpeed;
        }

        // TODO: nhớ cập nhật lại giá trị cờ _moving khi thay đổi trạng thái di chuyển
        if (xa != 0 || ya != 0) {
            move(xa, ya); // gọi hàm move() để di chuyển
            moving = true; // thay đổi cờ
        } else {
            moving = false; // thay đổi cờ
        }

    }

    public void moveCenterX() {
        int pixelOfEntity = Coordinates.tileToPixel(1);
        double centerX = x + sprite.getRealWidth() / 2;
        int tileCenterX = Coordinates.pixelToTile(centerX);
        x = Coordinates.tileToPixel(tileCenterX) + pixelOfEntity / 2 - sprite.getRealWidth() / 2;
    }

    public void moveCenterY() {
        int pixelOfEntity = Coordinates.tileToPixel(1);
        double centerY = y - sprite.getRealHeight() / 2;
        int tileCenterY = Coordinates.pixelToTile(centerY);
        y = Coordinates.tileToPixel(tileCenterY) + pixelOfEntity / 2 + sprite.getRealHeight() / 2;
    }

    public void autoMoveCenter() {
        int pixelOfEntity = Coordinates.tileToPixel(1);
        double centerX = x + sprite.getRealWidth() / 2;
        double centerY = y - sprite.getRealHeight() / 2;

        boolean contactTop = !canMove(centerX, centerY - pixelOfEntity / 2);
        boolean contactDown = !canMove(centerX, centerY + pixelOfEntity / 2);
        boolean contactLeft = !canMove(centerX - pixelOfEntity / 2, centerY);
        boolean contactRight = !canMove(centerX + pixelOfEntity / 2, centerY);

        // Các trường hợp đi một nửa người vào tường cũng tự động căn giữa.
        if (direction != 0 && contactDown) {
            moveCenterY();
        }
        if (direction != 1 && contactLeft) {
            moveCenterX();
        }
        if (direction != 2 && contactTop) {
            moveCenterY();
        }
        if (direction != 3 && contactRight) {
            moveCenterX();
        }
    }

    @Override
    public boolean canMove(double x, double y) {
        // TODO: kiểm tra có đối tượng tại vị trí chuẩn bị di chuyển đến và có thể di chuyển tới đó hay không

        int tileX = Coordinates.pixelToTile(x);
        int tileY = Coordinates.pixelToTile(y);
//        System.out.println(tileX + " " + tileY);
        Entity nextEntity = board.getEntity(tileX, tileY, this);
        return collide(nextEntity);
    }

    @Override
    public void move(double xa, double ya) {
        // TODO: sử dụng canMove() để kiểm tra xem có thể di chuyển tới điểm đã tính toán hay không và thực hiện thay đổi tọa độ _x, _y
        // TODO: nhớ cập nhật giá trị _direction sau khi di chuyển
        // TODO: Di chuyển nhân vật ra giữa.
        // Tính tọa độ tâm người
        double centerX = this.x + sprite.getRealWidth() / 2;
        double centerY = this.y - sprite.getRealHeight() / 2;

        if (xa > 0) {
            direction = 1;
        }
        if (xa < 0) {
            direction = 3;
        }
        if (ya > 0) {
            direction = 2;
        }
        if (ya < 0) {
            direction = 0;
        }
        if (canMove(centerX + xa, centerY + ya)) {
            this.x += xa;
            this.y += ya;
        }

        autoMoveCenter();
    }

    @Override
    public boolean collide(Entity e) {
        // TODO: xử lý va chạm với Flame
        // TODO: xử lý va chạm với Enemy
        if (e instanceof Flame) {
            kill();
            return false;
        }
        // TODO: xử lý va chạm với Enemy
        if (e instanceof Enemy) {
            kill();
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

    private void chooseSprite() {
        switch (direction) {
            case 0:
                sprite = Sprite.player_up;
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, animate, 20);
                }
                break;
            case 1:
                sprite = Sprite.player_right;
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, animate, 20);
                }
                break;
            case 2:
                sprite = Sprite.player_down;
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, animate, 20);
                }
                break;
            case 3:
                sprite = Sprite.player_left;
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, animate, 20);
                }
                break;
            default:
                sprite = Sprite.player_right;
                if (moving) {
                    sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, animate, 20);
                }
                break;
        }
    }

    /*
	|--------------------------------------------------------------------------
	| Powerups
	|--------------------------------------------------------------------------
     */
    public void addPowerup(Power p) {
        if (p.isRemoved()) {
            return;
        }
        _items.add(p);
        p.setValues();
        Sound itemSound = new Sound("res\\audio\\audioGetItem.wav");
        itemSound.play();
        if (p instanceof PowerBomb) {
            _bombRateNow = Game.getBombRate();
        }
    }

    public void clearUsedItems() {
        Power p;
        for (int i = 0; i < _items.size(); i++) {
            p = _items.get(i);
            if (p.isActive() == false) {
                _items.remove(i);
            }
        }
    }

    public void removeItems() {
        for (int i = 0; i < _items.size(); i++) {
            _items.remove(i);
        }
    }
}
