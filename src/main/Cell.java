package main;

/**
 * Хранит информацию о клетке и обрабатывает клики по ней
 */
public class Cell {

    /** Состояние клетки (пустая клетка / клетка с миной / взорванная клетка) */
    private CellState state;
    /** Указывает на то, открыта ли клетка пользователем */
    private boolean isHiden;
    /** Указывает на то, была ли клетка отмечена пользователем (поставил ли он на неё флажок) */
    private boolean isMarked;

    /**
     * Инициализирует поля класса
     */
    public Cell(boolean isMine){
        this.isHiden = true;
        this.isMarked = false;
        this.state = isMine ? CellState.MINE : CellState.EMPTY;
    }

    /**
     * Метод предназначен для обработки кликов по полю.
     */
    public ClickResult recieveClick(int button) {
        
    	if(isHiden){ 
            /* Если клик был сделан левой кнопкой */
            if(button==0 && !this.isMarked){ 
                if(this.state == CellState.MINE){
                    this.state = CellState.EXPLOSED;
                    return ClickResult.EXPLOSED;
                }

                if(this.state == CellState.EMPTY){
                    this.isHiden = false;
                    return ClickResult.OPENED;
                }

            /* Если клик был сделан правой кнопкой */
            }else if(button==1){
                this.isMarked = ! this.isMarked;
            }
        }
        if(!isHiden && button == 0)
            return ClickResult.REGULAR2;
        return ClickResult.REGULAR;
    }

    public boolean isHiden(){
        return isHiden;
    }

    public boolean isMarked(){
        return isMarked;
    }

    /**
     * Возвращает состояние клетки (пустая клетка / клетка с миной / взорванная клетка)
     */
    public CellState getState() {
        return state;
    }

    public void show() {
        this.isHiden = false;
    }
}
