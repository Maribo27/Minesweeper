package main;

/**
 * Хранит ключевые для игры константы в public static final полях
 */
public class Constants {

	/* Размер одной плитки */
	public static final int CELL_SIZE = 32;
	public static final int BUTTON_HEIGHT = 38;
	public static final int BUTTON_WIDTH = 300;

	/* Количество ячеек на экране по горизонтали и вертикали */
    public static final int COUNT_CELLS_X = 10;
    public static final int COUNT_CELLS_Y = 10;

	/* Параметры окна */
    public static final int SCREEN_WIDTH = COUNT_CELLS_X *CELL_SIZE;
    public static final int SCREEN_HEIGHT = COUNT_CELLS_Y *CELL_SIZE + 32;
    public static final String SCREEN_NAME = "Minesweeper";

	/* Шанс спавна мины */
    public static final int SPAWN_CHANCE_OF_MINE = 10; //%
}
