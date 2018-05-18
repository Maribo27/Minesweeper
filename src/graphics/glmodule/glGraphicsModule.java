package graphics.glmodule;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.ImageIOImageData;

import graphics.GraphicsModule;
import main.*;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import static main.Constants.*;

/**
 * Реализует графический модуль игры на основе LWJGL.
 */
public class glGraphicsModule implements GraphicsModule {

    //private glDrawSystem drawSystem;

    /**
     * Инициализирует графический движок и необходимые поля модуля.
     */
    public glGraphicsModule() {
        initOpengl();
      //  drawSystem = new glDrawSystem();
    }

    private void initOpengl() {
        try {
            /* Задаём размер будущего окна */
            Display.setDisplayMode(new DisplayMode(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));

            /* Задаём имя будущего окна */
            Display.setTitle(Constants.SCREEN_NAME);

            /* Создаём окно */
            Display.create();
            
            /* Задаём иконку приложения */
            try {
				Display.setIcon(new ByteBuffer[] {
				        new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("res/icon16.png")), false, false, null),
				        new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("res/icon32.png")), false, false, null)
				        });
			} catch (IOException e) {
				ErrorCatcher.graphicsFailure(e);
			}
            
        } catch (LWJGLException e) {
            ErrorCatcher.graphicsFailure(e);
        }

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, Constants.SCREEN_WIDTH,0, Constants.SCREEN_HEIGHT,1,-1);
        glMatrixMode(GL_MODELVIEW);

		/* Для поддержки текстур */
        glEnable(GL_TEXTURE_2D);

		/* Для поддержки прозрачности */
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		/* Белый фоновый цвет */
        glClearColor(1,1,1,1);
    }

    /**
     * Отрисовывает переданное игровое поле
     */
    @Override
    public void draw(GameField field) {
        glClear(GL_COLOR_BUFFER_BIT);

        for(int i = 0; i < COUNT_CELLS_X; i++) {
            for (int j = 0; j < COUNT_CELLS_Y; j++) {
                drawSprite(CELL_SIZE*i, CELL_SIZE*j, calculate(field.getCell(i,j), field.getMinesNear(i,j)) );
            }
        }
        
        drawButton(0, SCREEN_HEIGHT, glDrawSystem.glDraw.NEWGAME );
        drawButton(BUTTON_WIDTH - 140, SCREEN_HEIGHT, glDrawSystem.glDraw.EXITGAME );
            
        
        

        Display.update();
        Display.sync(60);
    }

    private glDrawSystem.glDraw calculate(Cell cell, int minesNear) {
        if(cell.isMarked()){
            if(!cell.isHiden() && (cell.getState() != CellState.MINE)){
                ///Если эта клетка не скрыта, и на ней
                ///ошибочно стоит флажок...
                return glDrawSystem.glDraw.BROKEN_FLAG;
            }
            ///В другом случае --
            return glDrawSystem.glDraw.FLAG;
        }else if(cell.isHiden()){
            ///Если клетка не помечена, притом скрыта...
            return glDrawSystem.glDraw.SPACE;
        }else{
            ///Если не помечена и не скрыта, выводим как есть
            switch (cell.getState()){
                case EXPLOSED:
                    return glDrawSystem.glDraw.EXPLOSION;
                case MINE:
                    return glDrawSystem.glDraw.MINE;
                case EMPTY:
                default:
                    if(minesNear>8 || minesNear<0){
                        ErrorCatcher.cantDisplayCellWrongMinesNear();
                    }

                    return  glDrawSystem.drawByNumber[minesNear];
            }
        }
    }


    /**
     * Отрисовывает отдельную ячейку
     *
     * @param x Координата отрисовки X
     * @param y Координата отрисовки Y
     * @param drawing Текстура, которую надо отрисовывать
     */
    private void drawSprite(int x, int y, glDrawSystem.glDraw drawing) {
        drawing.getTexture().bind();

        glBegin(GL_QUADS);
        glTexCoord2f(0,0);
        glVertex2f(x,y+ Constants.CELL_SIZE);
        glTexCoord2f(1,0);
        glVertex2f(x+ Constants.CELL_SIZE,y+ Constants.CELL_SIZE);
        glTexCoord2f(1,1);
        glVertex2f(x+ Constants.CELL_SIZE, y);
        glTexCoord2f(0,1);
        glVertex2f(x, y);
        glEnd();
    }

    private void drawButton(int x, int y, glDrawSystem.glDraw drawing) {
        drawing.getTexture().bind();

        glBegin(GL_POLYGON);
        glTexCoord2f(0,0);
        glVertex2f(x,y);
        glTexCoord2f(1,0);
        glVertex2f(x+ Constants.BUTTON_WIDTH,y);
        glTexCoord2f(1,1);
        glVertex2f(x+ Constants.BUTTON_WIDTH, y - Constants.BUTTON_HEIGHT);
        glTexCoord2f(0,1);
        glVertex2f(x, y - Constants.BUTTON_HEIGHT);
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
