package graphics.glmodule;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Система загрузки и хранения изображений для LWJGL
 */
class glDrawSystem {

    /**
	 * Загружает и хранит все доступные в игре текстуры
     */
	enum glDraw {

		ZERO("0"), ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"),
		SEVEN("7"), EIGHT("8"), SPACE("space"), MINE("mine"), FLAG("flag"), BROKEN_FLAG("broken_flag"),
		EXPLOSION("explosion"), NEWGAME("new_game"), EXITGAME("exit");

		private Texture texture;

		glDraw(String texturename) {
			try {
				this.texture = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/" + texturename + ".png")));
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public Texture getTexture() {
			return this.texture;
		}
	}

    /**
     * Хранит в себе ссылки на все доступные в игре текстуры с ключом, равным изображённой на текстуре цифре
     */
	public static final glDraw[] drawByNumber = {
		glDraw.ZERO,
		glDraw.ONE,
		glDraw.TWO,
		glDraw.THREE,
		glDraw.FOUR,
		glDraw.FIVE,
		glDraw.SIX,
		glDraw.SEVEN,
		glDraw.EIGHT
	};

}
