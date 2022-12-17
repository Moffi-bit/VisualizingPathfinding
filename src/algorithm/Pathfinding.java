package algorithm;

import cell.Cell;
import cell.Data;
import game.StateInfo;

/**
 * 
 * @author Lorenzo Stiavelli
 *
 */
public class Pathfinding {
	
	public static void findTheOptimalPath() {
		Data.calculateAllValues();
		
		System.out.println("Pathfinding started...");
		
		int shortest = 0;
		Cell[] directions = new Cell[4];
		
		while(!pathfindingComplete()) {
			/*
			 * UP
			 */
			if (StateInfo.START_X - 1 >= 0) {
				directions[0] = Data.board[StateInfo.START_X - 1][StateInfo.START_Y];
			}
			
			/*
			 * RIGHT
			 */
			if (StateInfo.START_Y + 1 < StateInfo.NUM_OF_CELLS) {
				directions[1] = Data.board[StateInfo.START_X][StateInfo.START_Y + 1];
			}
			
			/*
			 * DOWN
			 */
			if (StateInfo.START_X + 1 < StateInfo.NUM_OF_CELLS) {
				directions[2] = Data.board[StateInfo.START_X + 1][StateInfo.START_Y];
			}
			
			/*
			 * LEFT
			 */
			if (StateInfo.START_Y - 1 >= 0) {
				directions[3] = Data.board[StateInfo.START_X][StateInfo.START_Y - 1];
			}
			
			shortest = findShortestDistanceIndex(directions);
			
			StateInfo.START_X = directions[shortest].getxPos() / StateInfo.CELL_WIDTH;
			StateInfo.START_Y = directions[shortest].getyPos() / StateInfo.CELL_HEIGHT;
			
			directions[shortest].setType(3);
		}
		
		StateInfo.finished = true;
		
		if (directions[shortest] != null) {
			directions[shortest].setType(4);
		}
	}
	
	public static boolean pathfindingComplete() {
//		System.out.println("Start: " + StateInfo.START_X + ", " + StateInfo.START_Y + "\nEnd: " + StateInfo.END_X + ", " + StateInfo.END_Y);
		return StateInfo.START_X == StateInfo.END_X && StateInfo.START_Y == StateInfo.END_Y;
	}
	
	public static int findShortestDistanceIndex(Cell[] directions) {
		int j = StateInfo.INFINITY;
		int index = 0;
		
		
		for (int i = 0; i < directions.length; i++) {
			if (directions[i].getValue() < j && directions[i].getType() != 1) {
				j = directions[i].getValue();
				index = i;
			} 
		}
		
		return index;
	}
}
