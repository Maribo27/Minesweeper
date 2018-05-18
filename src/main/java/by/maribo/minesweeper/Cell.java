package by.maribo.minesweeper;

/**
 * Хранит информацию о клетке и обрабатывает клики по ней
 */
public class Cell {

	/**
	 * Состояние клетки (пустая клетка / клетка с миной / взорванная клетка)
	 */
	private CellState state;
	/**
	 * Указывает на то, открыта ли клетка пользователем
	 */
	private boolean hidden;
	/**
	 * Указывает на то, была ли клетка отмечена пользователем (поставил ли он на неё флажок)
	 */
	private boolean marked;

	/**
	 * Инициализирует поля класса
	 */
	Cell(boolean mine) {
		this.hidden = true;
		this.marked = false;
		this.state = mine ? CellState.MINE : CellState.EMPTY;
	}

	/**
	 * Метод предназначен для обработки кликов по полю.
	 */
	ClickResult receiveClick(int button) {
		if (hidden) {
			/* Если клик был сделан левой кнопкой */
			if (button == 0 && !this.marked) {
				if (this.state == CellState.MINE) {
					this.state = CellState.EXPLOSION;
					return ClickResult.EXPLOSION;
				}

				if (this.state == CellState.EMPTY) {
					this.hidden = false;
					return ClickResult.OPENED;
				}

				/* Если клик был сделан правой кнопкой */
			} else if (button == 1) {
				this.marked = !this.marked;
			}
		}
		if (!hidden && button == 0)
			return ClickResult.REGULAR2;
		return ClickResult.REGULAR;
	}

	public boolean isHidden() {
		return hidden;
	}

	public boolean isMarked() {
		return marked;
	}

	/**
	 * Возвращает состояние клетки (пустая клетка / клетка с миной / взорванная клетка)
	 */
	public CellState getState() {
		return state;
	}

	void show() {
		this.hidden = false;
	}
}
