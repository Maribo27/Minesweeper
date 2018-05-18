package main;

import event.Click;
import graphics.GraphicsModule;

import java.util.Random;
import javax.swing.JOptionPane;

import static main.Constants.COUNT_CELLS_X;
import static main.Constants.COUNT_CELLS_Y;
import static main.Constants.SPAWN_CHANCE_OF_MINE;

/**
 * Хранит и изменяет состояние игрового поля
 */
public class GameField {
	
	private static GraphicsModule graphicsModule;
    
	
	private static boolean stop = false;

    /** Матрица, хранящая клетки поля */
    private Cell[][] theField;

    /** Матрица, хранящая для каждой клетки поля количество мин рядом с ней */
    private short[][] minesNear;

    /**
     * Конструктор инициализиурет поля класса, затем
     * создаёт на поле мины в случайном порядке и заполняет minesNear.
     */
    public GameField(){
        theField = new Cell[COUNT_CELLS_X][COUNT_CELLS_Y];
        minesNear = new short[COUNT_CELLS_X][COUNT_CELLS_Y];
       
        Random rnd = new Random();

        
        for(int x = 0; x < COUNT_CELLS_X; x++)
        	for(int y = 0; y < COUNT_CELLS_Y; y++)
        		theField[x][y] = new Cell(false);
        
        for(int iter = 0; iter < SPAWN_CHANCE_OF_MINE; iter++){
        	int x = rnd.nextInt(COUNT_CELLS_X);
        	int y = rnd.nextInt(COUNT_CELLS_Y);
        	if (theField[x][y].getState() != CellState.MINE){
        		theField[x][y] = new Cell(true);
        		for(int i = -1; i < 2; i++)
        			for(int j = -1; j < 2; j++)
        				if( (x+i >= 0) && (x+i < COUNT_CELLS_X) && (y+j >= 0) && (y+j < COUNT_CELLS_Y))
        					minesNear[x+i][y+j]++;
        		continue;
        	}
        	iter--;
        }
        
        /*for(int x = 0; x < COUNT_CELLS_X; x++){
            for(int y = 0; y < COUNT_CELLS_Y; y++){
                boolean isMine = rnd.nextInt(100) < SPAWN_CHANCE_OF_MINE;
                if(isMine){
                    theField[x][y] = new Cell(true);
                    for(int i = -1; i < 2; i++){
                        for(int j = -1; j < 2; j++){
                            if( (x+i >= 0) && (x+i < COUNT_CELLS_X) && (y+j >= 0) && (y+j < COUNT_CELLS_Y)){
                               minesNear[x+i][y+j]++;
                            }
                        }
                    }
                }else{
                    theField[x][y] = new Cell(false);
                }
            }
        }*/
    }

    /**
     * @param x Координата X клетки
     * @param y Координата Y клетки
     * @return Клетка поля с координатами (X, Y)
     */
    public Cell getCell(int x, int y) {
        return theField[x][y];
    }

    /**
     * @param x Координата X клетки
     * @param y Координата Y клетки
     * @return Количество мин рядом с клеткой с координатами (X, Y)
     */
    public int getMinesNear(int x, int y) {
        return minesNear[x][y];
    }

    /**
     * Метод предназначен для обработки кликов по полю.
     */
    public void recieveClick(Click click) {
    	if (click.x < (COUNT_CELLS_X/2) & click.y >= COUNT_CELLS_Y) 
    		{
    		System.out.println("kek");
    		//Main.endOfGame = true;
    		//graphicsModule.destroy();
    		Main.endOfGame = true;
    		return;
    		}
    	if (click.x >= (COUNT_CELLS_X/2) && click.x < COUNT_CELLS_X & click.y >= COUNT_CELLS_Y) System.exit(0);
    	
    	if (stop) return;
    	ClickResult clickResult = this.theField[click.x][click.y].recieveClick(click.button);
        
        switch(clickResult) {
            case EXPLOSED:
                showAll();
                int result = JOptionPane.showConfirmDialog(null, 
                		   "\tВы проиграли! \nВыйти из приложения?",null, JOptionPane.YES_NO_OPTION);
                		if(result == JOptionPane.YES_OPTION) {
                		    System.exit(0);
                		}
                		stop = true;
                		return;
            case OPENED:
                if(getMinesNear(click.x, click.y) == 0)
                    for (int i = -1; i < 2; i++)
                        for (int j = -1; j < 2; j++)
                            if ((click.x + i >= 0) && (click.x + i < COUNT_CELLS_X)
                                    && (click.y + j >= 0) && (click.y + j < COUNT_CELLS_Y)) {
                                Click pseudoClick = new Click(click.x + i, click.y + j, click.button);
                                recieveClick(pseudoClick);
                            }
                break;
            case REGULAR2:
            	int temp = 0;
            	
            	for (int i = -1; i < 2; i++)
                    for (int j = -1; j < 2; j++)
                        if ((click.x + i >= 0) && (click.x + i < COUNT_CELLS_X)
                                && (click.y + j >= 0) && (click.y + j < COUNT_CELLS_Y)) 
                                	if (theField[click.x + i][click.y + j].isMarked()) temp++;
            	
    			if (temp == getMinesNear(click.x, click.y)) 

    				for (int i = -1; i < 2; i++)
                        for (int j = -1; j < 2; j++)
                            if ((click.x + i >= 0) && (click.x + i < COUNT_CELLS_X)
                                    && (click.y + j >= 0) && (click.y + j < COUNT_CELLS_Y)) 
                                    	if (theField[click.x + i][click.y + j].isHiden()){
                                    		Click pseudoClick = new Click(click.x + i, click.y + j, click.button);
                                    		recieveClick(pseudoClick);
                                    	}
                break;
            case REGULAR:
            default:
                //ignore
                break;
        }
        if (stop) return;
        int tmp = 0;
		for(int i = 0; i < COUNT_CELLS_X; i++)
            for(int j = 0; j < COUNT_CELLS_Y; j++){
                if(theField[i][j].getState() != CellState.MINE 
                	&& theField[i][j].isHiden())
                    		tmp++;
            }
		//System.out.println(tmp);
        if (tmp == 0){
        	showAll();
        	int result = JOptionPane.showConfirmDialog(null, 
         		   "Вы выиграли! \nВыйти из приложения?",null, JOptionPane.YES_NO_OPTION);
         		if(result == JOptionPane.YES_OPTION) {
         		    System.exit(0);
         		}
         	stop = true;
            return;   
        }
    }

    /**
     * Делает содержимое клетки с координатами (X, Y) видимым
     */
    public void show(int x, int y){
        theField[x][y].show();
    }

    /**
     * Делает видимым содержимое всех клеток поля
     */
    public void showAll() {
        for(Cell[] row : theField){
            for(Cell cell : row){
                cell.show();
            }
        }
    }
}
