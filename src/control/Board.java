package control;

import entities.Entity;
import entities.Message;
import entities.bomb.Bomb;
import entities.bomb.FlameSegment;
import entities.character.Mob;
import entities.character.Player;
import entities.character.enemy.Doll;
import entities.tile.powerup.Power;
import exception.LoadLevelException;
import graphic.IRender;
import graphic.Screen;
import input.KeyBoard;
import level.FileLevel;
import level.Level;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board implements IRender {
    protected Level _levelLoader;
    protected Level _level;
    protected Game _game;
    protected KeyBoard _input;
    protected Screen _screen;

    public Entity[] _entities;
    public java.util.List<Mob> _characters = new ArrayList<>();
    protected java.util.List<Bomb> _bombs = new ArrayList<>();
    private final java.util.List<Message> _messages = new ArrayList<>();

    private int _screenToShow = -1; //1:endgame, 2:changelevel, 3:paused

    private int _time = Game.TIME;
    private int _points = Game.POINTS;
    private int _lives = Game.LIVES;

    public Board(Game game, KeyBoard input, Screen screen) {
        _game = game;
        _input = input;
        _screen = screen;

        loadLevel(5); //start in level 1
    }

    @Override
    public void update() {
        if (_game.isPaused()) {
            return;
        }

        updateEntities();
        updateCharacters();
        updateBombs();
        updateMessages();
        detectEndGame();

        for (int i = 0; i < _characters.size(); i++) {
            Mob a = _characters.get(i);
            if (a.isRemoved()) {
                _characters.remove(i);
            }
        }
    }

    @Override
    public void render(Screen screen) {
        if (_game.isPaused()) {
            return;
        }

        //only render the visible part of screen
        int x0 = Screen.xOffset >> 4; //tile precision, -> left X
        int x1 = (Screen.xOffset + screen.getWidth() + Game.TILES_SIZE) / Game.TILES_SIZE; // -> right X
        int y0 = Screen.yOffset >> 4;
        int y1 = (Screen.yOffset + screen.getHeight()) / Game.TILES_SIZE; //render one tile plus to fix black margins

        for (int y = y0; y < y1; y++) {
            for (int x = x0; x < x1; x++) {
                _entities[x + y * _levelLoader.getWidth()].render(screen);
            }
        }

        renderBombs(screen);
        renderCharacter(screen);

    }

    /*
	|--------------------------------------------------------------------------
	| ChangeLevel
	|--------------------------------------------------------------------------
     */
//    public void newGame() {
//        _game.getBoard().newGame();
//    }
    public void newGame() {
        resetProperties();
        changeLevel(1);
    }

    @SuppressWarnings("static-access")
    private void resetProperties() {
        _points = Game.POINTS;
        _lives = Game.LIVES;
        Player._items.clear();

        _game.bomberSpeed = 1.0;
        _game.bombRadius = 1;
        _game.bombRate = 1;

    }

    public void restartLevel() {
        changeLevel(_levelLoader.getLevel());
    }

    public void nextLevel() {
        loadLevel(_levelLoader.getLevel() + 1);
    }

    public void changeLevel(int level) {
        _time = Game.TIME;
        _screenToShow = 2;
        _game.resetScreenDelay();
        _game.pause();
        _characters.clear();
        _bombs.clear();
        _messages.clear();

        try {
            _levelLoader = new FileLevel(this, level);
            _entities = new Entity[_levelLoader.getHeight() * _levelLoader.getWidth()];

            _levelLoader.createEntities();
            if (level == 1) {
                //Music.clipDelay.setMicrosecondPosition(0);
                //Music.clipNextLevel.setMicrosecondPosition(0);
                //Music.clipDelay.start();
                //Music.clipNextLevel.start();
            }
        } catch (LoadLevelException e) {
            endGame(); //failed to load.. so.. no more levels?
            //winGame();
        }
    }

    public void changeLevelByCode(String str) {
        int i = _levelLoader.enterCode(str);

        if (i != -1) {
            changeLevel(i + 1);
        }
    }

    public boolean isItemUsed(int x, int y, int level) {
        Power p;
        for (int i = 0; i < Player._items.size(); i++) {
            p = Player._items.get(i);
            if (p.getX() == x && p.getY() == y && level == p.getLevel()) {
                return true;
            }
        }

        return false;
    }

    /*
	|--------------------------------------------------------------------------
	| Detections
	|--------------------------------------------------------------------------
     */
    protected void detectEndGame() {
        if (_time <= 0) {
            endGame();
        }
    }

    public void endGame() {
        _screenToShow = 1;
        _game.resetScreenDelay();
        _game.pause();
    }

    public boolean detectNoEnemies() {
        int total = 0;
        for (int i = 0; i < _characters.size(); i++) {
            if (_characters.get(i) instanceof Player == false) {
                ++total;
            }
        }

        return total == 0;
    }

    /*
	|--------------------------------------------------------------------------
	| Pause & Resume
	|--------------------------------------------------------------------------
     */
    public void gameResume() {
        _game.resetScreenDelay();
        _screenToShow = -1;
        _game.run();
    }

    public void gamePause() {
        _game.resetScreenDelay();
        if (_screenToShow <= 0) {
            _screenToShow = 3;
        }
        _game.pause();
    }

    public void winGame() {
        _screenToShow = 4;
        _game.resetScreenDelay();
        _game.pause();
    }

    public void loadLevel(int level) {
        _time = Game.TIME;
        _screenToShow = 2;
        _game.resetScreenDelay();
        _game.pause();
        _characters.clear();
        _bombs.clear();
        _messages.clear();

        try {
            _levelLoader = new FileLevel(this, level);
            _entities = new Entity[_levelLoader.getHeight() * _levelLoader.getWidth()];

            _levelLoader.createEntities();
            if (level == 1) {
                //Music.clipScreen.setMicrosecondPosition(0);
                //Music.clipScreen.start();
            }
        } catch (LoadLevelException e) {
            endGame();
        }
    }

    /*
	|--------------------------------------------------------------------------
	| Screens
	|--------------------------------------------------------------------------
     */
    public void drawScreen(Graphics g) {
        switch (_screenToShow) {
            case 1:
                _screen.drawEndGame(g, _points);
                break;
            case 2:
                _screen.drawChangeLevel(g, _levelLoader.getLevel());
                break;
            case 3:
                _screen.drawPaused(g);
                break;
            case 4:
                _screen.drawWinGame(g, _points);
                break;
        }
    }

    /*
	|--------------------------------------------------------------------------
	| Getters And Setters
	|--------------------------------------------------------------------------
     */
    public Entity getEntity(double x, double y, Mob m) {

        Entity res = null;

        res = getFlameSegmentAt((int) x, (int) y);
        if (res != null) {
            return res;
        }

        res = getBombAt(x, y);
        if (res != null) {
            return res;
        }

        res = getCharacterAtExcluding((int) x, (int) y, m);
        if (res != null) {
            return res;
        }

        res = getEntityAt((int) x, (int) y);

        return res;
    }

    public List<Bomb> getBombs() {
        return _bombs;
    }

    public Bomb getBombAt(double x, double y) {
        Iterator<Bomb> bs = _bombs.iterator();
        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.getX() == (int) x && b.getY() == (int) y) {
                return b;
            }
        }

        return null;
    }

    public Player getBomber() {
        Iterator<Mob> itr = _characters.iterator();

        Mob cur;
        while (itr.hasNext()) {
            cur = itr.next();

            if (cur instanceof Player) {
                return (Player) cur;
            }
        }

        return null;
    }

    public Mob getCharacterAtExcluding(int x, int y, Mob a) {
        Iterator<Mob> itr = _characters.iterator();

        Mob cur;
        while (itr.hasNext()) {
            cur = itr.next();
            if (cur == a) {
                continue;
            }

            if (cur.getXTile() == x && cur.getYTile() == y) {
                return cur;
            }

        }

        return null;
    }

    public FlameSegment getFlameSegmentAt(int x, int y) {
        Iterator<Bomb> bs = _bombs.iterator();
        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();

            FlameSegment e = b.flameAt(x, y);
            if (e != null) {
                return e;
            }
        }

        return null;
    }

    public Entity getEntityAt(double x, double y) {
        return _entities[(int) x + (int) y * _levelLoader.getWidth()];
    }

    /*
	|--------------------------------------------------------------------------
	| Adds and Removes
	|--------------------------------------------------------------------------
     */
    public void addEntity(int pos, Entity e) {
        _entities[pos] = e;
    }

    public void addCharacter(Mob e) {
        _characters.add(e);
    }
    public void addMob(Mob e) {
        _characters.add(e);
    }

    public void addBomb(Bomb e) {
        _bombs.add(e);
    }

    public void addMessage(Message e) {
        _messages.add(e);
    }

    public void addPoints(int points) {
        this._points += points;
    }

    public void addLives(int lives) {
        this._lives += lives;
    }

    /*
	|--------------------------------------------------------------------------
	| Renders
	|--------------------------------------------------------------------------
     */
    protected void renderEntities(Screen screen) {
        for (int i = 0; i < _entities.length; i++) {
            _entities[i].render(screen);
        }
    }

    protected void renderCharacter(Screen screen) {
        Iterator<Mob> itr = _characters.iterator();

        while (itr.hasNext()) {
            itr.next().render(screen);
        }
    }

    protected void renderBombs(Screen screen) {
        Iterator<Bomb> itr = _bombs.iterator();

        while (itr.hasNext()) {
            itr.next().render(screen);
        }
    }

    public void renderMessages(Graphics g) {
        Message m;
        for (int i = 0; i < _messages.size(); i++) {
            m = _messages.get(i);

            g.setFont(new Font("Arial", Font.PLAIN, m.getSize()));
            g.setColor(m.getColor());
            g.drawString(m.getMessage(), (int) m.getX() - Screen.xOffset * Game.SCALE, (int) m.getY());
        }
    }

    /*
	|--------------------------------------------------------------------------
	| Updates
	|--------------------------------------------------------------------------
     */
    protected void updateEntities() {
        if (_game.isPaused()) {
            return;
        }
        for (int i = 0; i < _entities.length; i++) {
            _entities[i].update();
        }
    }

    protected void updateCharacters() {
        if (_game.isPaused()) {
            return;
        }
        Iterator<Mob> itr = _characters.iterator();

        while (itr.hasNext() && !_game.isPaused()) {
            itr.next().update();
        }
    }

    protected void updateBombs() {
        if (_game.isPaused()) {
            return;
        }
        Iterator<Bomb> itr = _bombs.iterator();

        while (itr.hasNext()) {
            itr.next().update();
        }
    }

    protected void updateMessages() {
        if (_game.isPaused()) {
            return;
        }
        Message m;
        int left;
        for (int i = 0; i < _messages.size(); i++) {
            m = _messages.get(i);
            left = m.getDuration();

            if (left > 0) {
                m.setDuration(--left);
            } else {
                _messages.remove(i);
            }
        }
    }

    public int subtractTime() {
        if (_game.isPaused()) {
            return this._time;
        } else {
            return this._time--;
        }
    }

    public KeyBoard getInput() {
        return _input;
    }

    public Level getLevel() {
        return _level;
    }

    public Game getGame() {
        return _game;
    }

    public int getShow() {
        return _screenToShow;
    }

    public void setShow(int i) {
        _screenToShow = i;
    }

    public int getTime() {
        return _time;
    }

    public int getPoints() {
        return _points;
    }

    public int getWidth() {
        return _levelLoader.getWidth();
    }

    public int getHeight() {
        return _levelLoader.getHeight();
    }

    public int getLives() {
        return _lives;
    }

    public void subLives(int live) {
        this._lives -= live;
    }



}
