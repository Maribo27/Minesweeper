package event;

import java.util.LinkedList;

/**
 * Определяет параметры, которые игре необходимо считывать с мыши.
 */
public interface MouseModule {

    /**
     * Считывание последних данных из стека событий, если модулю это необходимо
     */
    void update();

    /**
     * @return Возвращает информацию о кликах пользователя за последнюю итерацию
     */
    LinkedList<Click> getClicksStack();
}
