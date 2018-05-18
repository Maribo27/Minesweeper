package by.maribo.minesweeper.graphics;

import by.maribo.minesweeper.GameField;


public interface GraphicsModule {
    void draw(GameField field);
    boolean isCloseRequested();
    void destroy();
}