package main;

import event.Click;
import event.MouseModule;
import event.glmodule.glMouseModule;
import graphics.GraphicsModule;
import graphics.glmodule.glGraphicsModule;

import java.util.LinkedList;

/**
 * Главный управляющий класс.
 * Считывает действия пользователя, передаёт данные в graphicsModule, затем обрабатывает результат.
 */

public class Main {

	public static boolean endOfGame; //Флаг для завершения основного цикла программы
	private static GraphicsModule graphicsModule;
	private static MouseModule mouseModule;
	private static LinkedList<Click> clicksStack;
	private static GameField gameField;

	public static void main(String[] args) {
		while (true) {
			start();
		}
	}


	public static void start() {
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
	public static void initFields() {
		endOfGame = false;
		graphicsModule = new glGraphicsModule();
		mouseModule = new glMouseModule();
		gameField = new GameField();
		clicksStack = new LinkedList<>();
	}


	private static void input() {
		mouseModule.update();

		clicksStack = mouseModule.getClicksStack();

		endOfGame = endOfGame || graphicsModule.isCloseRequested();
	}

	private static void logic() {
		for (Click click : clicksStack) {
			gameField.recieveClick(click);
		}
	}

}
