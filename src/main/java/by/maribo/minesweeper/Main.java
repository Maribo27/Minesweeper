package by.maribo.minesweeper;

import by.maribo.minesweeper.event.Click;
import by.maribo.minesweeper.event.MouseModule;
import by.maribo.minesweeper.event.glmodule.glMouseModule;
import by.maribo.minesweeper.graphics.GraphicsModule;
import by.maribo.minesweeper.graphics.glmodule.GraphicsException;
import by.maribo.minesweeper.graphics.glmodule.MinesNumberException;
import by.maribo.minesweeper.graphics.glmodule.glGraphicsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 * Главный управляющий класс.
 * Считывает действия пользователя, передаёт данные в graphicsModule, затем обрабатывает результат.
 */

public class Main {

	static boolean endOfGame; //Флаг для завершения основного цикла программы
	private static GraphicsModule graphicsModule;
	private static MouseModule mouseModule;
	private static LinkedList<Click> clicksStack;
	private static GameField gameField;
	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		while (true) {
			start();
		}
	}


	private static void start() {
		initFields();

		while (!endOfGame) {
			input();
			logic();
			graphicsModule.draw(gameField);
		}

		graphicsModule.destroy();
	}


	/**
	 * Задаёт значения полей для начала игры
	 */
	private static void initFields() {
		try {
			endOfGame = false;
			graphicsModule = new glGraphicsModule();
			mouseModule = new glMouseModule();
			gameField = new GameField();
			clicksStack = new LinkedList<>();
		} catch (GraphicsException | MinesNumberException e) {
			logger.error(e.getMessage());
			System.exit(-1);
		}
	}

	private static void input() {
		mouseModule.update();
		clicksStack = mouseModule.getClicksStack();
		endOfGame = endOfGame || graphicsModule.isCloseRequested();
	}

	private static void logic() {
		for (Click click : clicksStack) {
			gameField.receiveClick(click);
		}
	}

}
