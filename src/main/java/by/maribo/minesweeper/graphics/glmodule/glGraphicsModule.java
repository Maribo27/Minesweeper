package by.maribo.minesweeper.graphics.glmodule;

import by.maribo.minesweeper.Cell;
import by.maribo.minesweeper.CellState;
import by.maribo.minesweeper.GameField;
import by.maribo.minesweeper.graphics.GraphicsModule;
import by.maribo.minesweeper.graphics.glmodule.glDrawSystem.CellType;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.ImageIOImageData;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Objects;

import static by.maribo.minesweeper.Constants.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Реализует графический модуль игры на основе LWJGL.
 */
public class glGraphicsModule implements GraphicsModule {

	private static final String SCREEN_NAME = "Minesweeper";
	private static final int SCREEN_HEIGHT = COUNT_CELLS_Y *CELL_SIZE + 32;
	private static final int SCREEN_WIDTH = COUNT_CELLS_X *CELL_SIZE;
	private static final int BUTTON_WIDTH = 300;
	private static final int BUTTON_HEIGHT = 38;

	/**
	 * Инициализирует графический движок и необходимые поля модуля.
	 */
	public glGraphicsModule() {
		initOpenGL();
	}

	private void initOpenGL() {
		try {
			/* Задаём размер будущего окна */
			Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));

			/* Задаём имя будущего окна */
			Display.setTitle(SCREEN_NAME);

			/* Создаём окно */
			Display.create();

			/* Задаём иконку приложения */
			try {
				URL resource = getClass().getClassLoader().getResource("icon/icon16.png");
				File icon16 = new File(Objects.requireNonNull(resource).getFile());
				resource = getClass().getClassLoader().getResource("icon/icon32.png");
				File icon32 = new File(Objects.requireNonNull(resource).getFile());
				Display.setIcon(new ByteBuffer[]{
						new ImageIOImageData().imageToByteBuffer(ImageIO.read(icon16), false, false, null),
						new ImageIOImageData().imageToByteBuffer(ImageIO.read(icon32), false, false, null)
				});
			} catch (IOException e) {
				throw new GraphicsException("Error: cannot load icon");
			}

		} catch (LWJGLException e) {
			throw new GraphicsException("Error: cannot load graphics module");
		}

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, SCREEN_WIDTH, 0, SCREEN_HEIGHT, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		/* Для поддержки текстур */
		glEnable(GL_TEXTURE_2D);

		/* Для поддержки прозрачности */
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		/* Белый фоновый цвет */
		glClearColor(1, 1, 1, 1);
	}

	/**
	 * Отрисовывает переданное игровое поле
	 */
	@Override
	public void draw(GameField field) {
		glClear(GL_COLOR_BUFFER_BIT);

		for (int i = 0; i < COUNT_CELLS_X; i++) {
			for (int j = 0; j < COUNT_CELLS_Y; j++) {
				drawSprite(CELL_SIZE * i, CELL_SIZE * j, calculate(field.getCell(i, j), field.getMinesNear(i, j)));
			}
		}

		drawButton(0, CellType.NEW_GAME);
		drawButton(BUTTON_WIDTH - 140, CellType.EXIT_GAME);


		Display.update();
		Display.sync(60);
	}

	private CellType calculate(Cell cell, int minesNear) {
		if (cell.isMarked()) {
			if (!cell.isHidden() && (cell.getState() != CellState.MINE)) {
				///Если эта клетка не скрыта, и на ней
				///ошибочно стоит флажок...
				return CellType.BROKEN_FLAG;
			}
			///В другом случае --
			return CellType.FLAG;
		} else if (cell.isHidden()) {
			///Если клетка не помечена, притом скрыта...
			return CellType.SPACE;
		} else {
			///Если не помечена и не скрыта, выводим как есть
			switch (cell.getState()) {
				case EXPLOSION:
					return CellType.EXPLOSION;
				case MINE:
					return CellType.MINE;
				case EMPTY:
				default:
					if (minesNear > 8 || minesNear < 0) {
						throw new MinesNumberException("Error: wrong count of mines");
					}
					return CellType.values()[minesNear];
			}
		}
	}


	/**
	 * Отрисовывает отдельную ячейку
	 *
	 * @param x       Координата отрисовки X
	 * @param y       Координата отрисовки Y
	 * @param drawing Текстура, которую надо отрисовывать
	 */
	private void drawSprite(int x, int y, CellType drawing) {
		drawing.getTexture().bind();

		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(x, y + CELL_SIZE);
		glTexCoord2f(1, 0);
		glVertex2f(x + CELL_SIZE, y + CELL_SIZE);
		glTexCoord2f(1, 1);
		glVertex2f(x + CELL_SIZE, y);
		glTexCoord2f(0, 1);
		glVertex2f(x, y);
		glEnd();
	}

	private void drawButton(int x, CellType drawing) {
		drawing.getTexture().bind();

		glBegin(GL_POLYGON);
		glTexCoord2f(0, 0);
		glVertex2f(x, SCREEN_HEIGHT);
		glTexCoord2f(1, 0);
		glVertex2f(x + BUTTON_WIDTH, SCREEN_HEIGHT);
		glTexCoord2f(1, 1);
		glVertex2f(x + BUTTON_WIDTH, SCREEN_HEIGHT - BUTTON_HEIGHT);
		glTexCoord2f(0, 1);
		glVertex2f(x, SCREEN_HEIGHT - BUTTON_HEIGHT);
		glEnd();
	}


	@Override
	public boolean isCloseRequested() {
		return Display.isCloseRequested();
	}

	@Override
	public void destroy() {
		Display.destroy();
	}
}
