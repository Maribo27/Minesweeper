package by.maribo.minesweeper.graphics.glmodule;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Система загрузки и хранения изображений для LWJGL
 */
class glDrawSystem {

	/**
	 * Загружает и хранит все доступные в игре текстуры
	 */
	enum CellType {
		ZERO("0"),
		ONE("1"),
		TWO("2"),
		THREE("3"),
		FOUR("4"),
		FIVE("5"),
		SIX("6"),
		SEVEN("7"),
		EIGHT("8"),
		SPACE("space"),
		MINE("mine"),
		FLAG("flag"),
		BROKEN_FLAG("broken_flag"),
		EXPLOSION("explosion"),
		NEW_GAME("new_game"),
		EXIT_GAME("exit");

		private Texture texture;

		CellType(String textureName) {
			try {
				URL resource = getClass().getClassLoader().getResource(textureName + ".png");
				File file = new File(Objects.requireNonNull(resource).getFile());
				this.texture = TextureLoader.getTexture("PNG", new FileInputStream(file));
			} catch (IOException e) {
				throw new DrawSystemException("Error: cannot draw field");
			}
		}

		public Texture getTexture() {
			return this.texture;
		}
	}
}
