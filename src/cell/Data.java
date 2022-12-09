package cell;

import game.StateInfo;

/**
 * 
 * @author Lorenzo Stiavelli
 *
 */
public class Data {
	
	public static Cell[][] board = new Cell[StateInfo.NUM_OF_CELLS][StateInfo.NUM_OF_CELLS];
	
	public static Cell getCell(int j, int i) {
		return board[i][j];
	}
	
	public static void fillBoard() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = new Cell(0, i * StateInfo.CELL_WIDTH, j * StateInfo.CELL_HEIGHT, 0);
			}
		}
	}
	
	public static void calculateAllValues() {
		// TODO: Implement this method.
		
	}
}
