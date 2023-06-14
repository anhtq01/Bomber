package level;

import control.Board;
import control.Game;
import entities.LayerEntity;
import entities.character.Player;
import entities.character.enemy.*;
import entities.destroyable.BrickTile;
import entities.tile.GrassTile;
import entities.tile.PortalTile;
import entities.tile.WallTile;
import entities.tile.powerup.PowerBomb;
import entities.tile.powerup.PowerFlame;
import entities.tile.powerup.PowerSpeed;
import exception.LoadLevelException;
import graphic.Screen;
import graphic.Sprite;
import gui.Coordinates;

import java.io.*;
import java.net.URL;
import java.util.StringTokenizer;

public class FileLevel extends Level{

    public FileLevel(Board board, int level) throws LoadLevelException {
        super(board, level);
    }

    @Override
    public void loadLevel(String path) throws LoadLevelException {
        FileInputStream file = null;

        try {
            file = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            URL absPath = FileLevel.class.getResource("/" + path);
            BufferedReader in = new BufferedReader(new InputStreamReader(file));

            String data = in.readLine();
            StringTokenizer tokens = new StringTokenizer(data);
            this.level = Integer.parseInt(tokens.nextToken());
            this.height = Integer.parseInt(tokens.nextToken());
            this.width = Integer.parseInt(tokens.nextToken());
            this.line = new String[this.height];

            for(int i = 0; i < this.height; ++i) {
                this.line[i] = in.readLine().substring(0, this.width);
            }

            in.close();
        } catch (IOException e) {
            throw new LoadLevelException("Error loading level " + path, e);
        }
    }

    @Override
    public void loadLevel(int level) throws LoadLevelException {
        //TODO : lấy giá trị width height level từ file nạp vào
        /*String path = "res/levels/Level" + level + ".txt";
        //
        try {
            FileInputStream file = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //
        try {
            URL absPath = FileLevelLoader.class.getResource("/" + path);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(absPath.openStream()))) {
                String data = in.readLine();
                StringTokenizer tokens = new StringTokenizer(data);

                _level = Integer.parseInt(tokens.nextToken());
                _height = Integer.parseInt(tokens.nextToken());
                _width = Integer.parseInt(tokens.nextToken());
                _map = new String[_height];

                // full map
                for (int i = 0; i < _height; i++) {
                    _map[i] = in.readLine().substring(0, _width);
                }
            }
        } catch (IOException e) {
            throw new LoadLevelException("Error loading level " + path, e);

            //System.out.println("File level error");
        }*/
        String path = "res/levels/Level" + level + ".txt";
        //
        FileInputStream file = null;
        try {
            file = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //
        try {
            URL absPath = FileLevel.class.getResource("/" + path);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(file))) {
                String data = in.readLine();
                StringTokenizer tokens = new StringTokenizer(data);

                level = Integer.parseInt(tokens.nextToken());
                height = Integer.parseInt(tokens.nextToken());
                width = Integer.parseInt(tokens.nextToken());
                line = new String[height];

                // full map
                for (int i = 0; i < height; i++) {
                    line[i] = in.readLine().substring(0, width);
                }
            }
        } catch (IOException e) {
            throw new LoadLevelException("Error loading level " + path, e);
            //System.out.println("File level error");
        }

    }

    @Override
    public void createEntities() {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                addLevelEntity(line[y].charAt(x), x, y );
            }
        }
    }

    public void addLevelEntity(char c, int x, int y) {
        int pos = x + y * getWidth();

        switch(c) { // TODO: minimize this method
            case '#':
                board.addEntity(pos, new WallTile(x, y, Sprite.wall));
                break;
            case 'b':
                LayerEntity layer = new LayerEntity(x, y,
                        new GrassTile(x ,y, Sprite.grass),
                        new BrickTile(x ,y, Sprite.brick));

                if(board.isItemUsed(x, y, level) == false) {
                    layer.addBeforeTop(new PowerBomb(x, y, level, Sprite.powerup_bombs));
                }

                board.addEntity(pos, layer);
                break;
            case 's':
                layer = new LayerEntity(x, y,
                        new GrassTile(x ,y, Sprite.grass),
                        new BrickTile(x ,y, Sprite.brick));

                if(board.isItemUsed(x, y, level) == false) {
                    layer.addBeforeTop(new PowerSpeed(x, y, level, Sprite.powerup_speed));
                }

                board.addEntity(pos, layer);
                break;
            case 'f':
                layer = new LayerEntity(x, y,
                        new GrassTile(x ,y, Sprite.grass),
                        new BrickTile(x ,y, Sprite.brick));

                if(board.isItemUsed(x, y, level) == false) {
                    layer.addBeforeTop(new PowerFlame(x, y, level, Sprite.powerup_flames));
                }

                board.addEntity(pos, layer);
                break;
            case '*':
                board.addEntity(pos, new LayerEntity(x, y,
                        new GrassTile(x ,y, Sprite.grass),
                        new BrickTile(x ,y, Sprite.brick)) );
                break;
            case 'x':
                board.addEntity(pos, new LayerEntity(x, y,
                        new GrassTile(x ,y, Sprite.grass),
                        new PortalTile(x ,y, board, Sprite.portal),
                        new BrickTile(x ,y, Sprite.brick)) );
                break;
            case ' ':
                board.addEntity(pos, new GrassTile(x, y, Sprite.grass) );
                break;
            case 'p':
                board.addMob( new Player(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, board) );
                Screen.setOffset(0, 0);

                board.addEntity(pos, new GrassTile(x, y, Sprite.grass) );
                break;
            //Enemies
            case '1':
                board.addMob( new Balloon(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, board));
                board.addEntity(pos, new GrassTile(x, y, Sprite.grass) );
                break;
            case '2':
                board.addMob( new Oneal(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, board));
                board.addEntity(pos, new GrassTile(x, y, Sprite.grass) );
                break;
            case '3':
                board.addMob( new Doll(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, board));
                board.addEntity(pos, new GrassTile(x, y, Sprite.grass) );
                break;
            case '4':
                board.addMob( new Minvo(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, board));
                board.addEntity(pos, new GrassTile(x, y, Sprite.grass) );
                break;
            case '5':
                board.addMob( new Kondoria(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, board));
                board.addEntity(pos, new GrassTile(x, y, Sprite.grass) );
                break;
            default:
                board.addEntity(pos, new GrassTile(x, y, Sprite.grass) );
                break;
        }
    }
}
