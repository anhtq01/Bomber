package entities.destroyable;

import entities.Entity;
import entities.bomb.Flame;
import entities.character.enemy.Kondoria;
import graphic.Screen;
import graphic.Sprite;
import gui.Coordinates;

public class BrickTile extends DestroyableTile{
    public BrickTile(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render(Screen screen) {
        int x = Coordinates.tileToPixel(this.x);
        int y = Coordinates.tileToPixel(this.y);

        if (destroyed) {
            sprite = movingSprite(Sprite.brick_exploded, Sprite.brick_exploded1, Sprite.brick_exploded2);

            screen.renderEntityWithBelowSprite(x, y, this, belowSprite);
        } else {
            screen.renderEntity(x, y, this);
        }
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Flame) {
            destroy();
        }
        if (e instanceof Kondoria) {
            return true;
        }
        return false;
    }
}
