package level;

import control.Board;
import exception.LoadLevelException;

public abstract class Level implements ILevel{
    protected int width, height, level;
    // mảng này sẽ là map dùng thay cho mảng 2 chiều
    String [] line;
    protected Board board;
    protected static String[] codes = { //TODO: change this code system to actualy load the code from each level.txt
            "dominhduc",
            "diona",
            "eula",
            "beidou",
            "xingqiu",
    };

    public Level(String path, Board board) throws LoadLevelException {
        loadLevel(path);
        this.board = board;
    }

    public Level(Board board, int level) throws LoadLevelException {
        this.board = board;
        loadLevel(level);
    }

    public abstract void loadLevel(int level) throws LoadLevelException;

    @Override
    public abstract void loadLevel(String path) throws LoadLevelException;

    /**
     * mỗi một thời điểm nhận vào input sẽ phải xóa hết đi và tạo lại từ đầu
     */
    public abstract void createEntities();

    public int enterCode(String str) {
        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }

    public String getCode() {
        return codes[level -1];
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLevel() {
        return level;
    }

    public Board getBoard() {
        return board;
    }

    public static String[] getCodes() {
        return codes;
    }

    public String[] getLine() {
        return line;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
