package level;

import exception.LoadLevelException;

public interface ILevel {
    public void loadLevel(String path) throws LoadLevelException;
}
