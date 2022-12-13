package cell;

import game.StateInfo;

/**
 * 
 * @author Lorenzo Stiavelli
 *
 */
public class Data {
	
	public static Cell[][] board = new Cell[StateInfo.NUM_OF_CELLS][StateInfo.NUM_OF_CELLS];
	
	public static Cell getCell(int i, int j) {
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
		System.out.println("Value calculations initiated...");
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				int distance = distanceBetweenCurrentAndEnd(board[i][j].getxPos(), board[i][j].getyPos());
				
				board[i][j].setValue(distance);
				if (board[i][j].getType() == 1) {
					board[i][j].setValue(StateInfo.INFINITY);
				}
			}
		}
	}
	
	private static int distanceBetweenCurrentAndEnd(int currentX, int currentY) {
		return Math.abs((int) Math.sqrt(Math.pow((StateInfo.END_MOUSE_X - currentX), 2) + Math.pow((StateInfo.END_MOUSE_Y - currentY), 2)));
	}
}
