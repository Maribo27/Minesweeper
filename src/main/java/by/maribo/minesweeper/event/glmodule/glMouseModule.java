package by.maribo.minesweeper.event.glmodule;

import org.lwjgl.input.Mouse;
import by.maribo.minesweeper.event.Click;
import by.maribo.minesweeper.event.MouseModule;

import java.util.LinkedList;

import static by.maribo.minesweeper.Constants.CELL_SIZE;

/**
 * Реализует считывание с мыши необходимых игре параметров
 */
public class glMouseModule implements MouseModule {

    private LinkedList<Click> stack;

    /**
     * Считывание последних данных из стека событий
     */
    @Override
    public void update() {
        resetValues();

        while(Mouse.next()){
            if(Mouse.getEventButton()>=0 && Mouse.getEventButtonState()){
                int x = Mouse.getEventX()/CELL_SIZE;
                int y = Mouse.getEventY()/CELL_SIZE;
                int button = Mouse.getEventButton();

                stack.add(new Click(x, y, button));
            }
        }
    }

    /**
     * Обнуление данных, полученых при предыдущих запросах
     */
    private void resetValues(){
        stack = new LinkedList<>();
    }

    /**
     * @return Возвращает стек кликов, произешдших за последнюю итерацию.
     */
    @Override
    public LinkedList<Click> getClicksStack() {
        return stack;
    }
}
