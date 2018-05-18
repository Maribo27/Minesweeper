package by.maribo.minesweeper;
/**
 * Содержит все возможные состояния клетки
 */
public enum CellState {
    EMPTY, /* В клетке нет мины */
    MINE, /* В клетке есть мина, но она не взорвана */
    EXPLOSION /* В клетке есть мина и она взорвана*/
}
