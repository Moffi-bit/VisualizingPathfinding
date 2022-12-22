package algorithm;

import java.util.HashSet;

import cell.Cell;
import cell.Data;
import game.StateInfo;

/**
 * 
 * @author Lorenzo Stiavelli
 *
 */
public class Pathfinding {

//	private static int[][] costs = new int[StateInfo.NUM_OF_CELLS][StateInfo.NUM_OF_CELLS];
//	private static int[][] ids = new int[StateInfo.NUM_OF_CELLS][StateInfo.NUM_OF_CELLS];
//	private static HashSet<Cell> cells = new HashSet<Cell>();
//
//	public static void dijkstra() {
//		Data.calculateAllValues();
//
//		/*
//		 * Set up starting costs
//		 */
//		for (int i = 0; i < costs.length; i++) {
//			for (int j = 0; j < costs[i].length; j++) {
//				costs[i][j] = StateInfo.INFINITY;
//			}
//		}
//		costs[StateInfo.START_X][StateInfo.START_Y] = Data.board[StateInfo.START_X][StateInfo.START_Y].getValue();
//
//		for (int i = 0; i < Data.board.length; i++) {
//			for (int j = 0; j < Data.board[i].length; j++) {
//				int smallestValue = findSmallestValue();
//				
//				System.out.println(smallestValue);
//				if (!cells.contains(Data.board[i][j])
//						&& Data.board[i][j].getValue() == smallestValue) {
//					cells.add(Data.board[i][j]);
//				}
//
//				/*
//				 * DOWN
//				 */
//				if (i + 1 < StateInfo.NUM_OF_CELLS
//						&& !cells.contains(Data.board[i + 1][j])
//						&& (costs[i + 1][j] > (costs[i][j]
//								+ Math.abs(Data.board[i][j].getValue()
//										- Data.board[i + 1][j].getValue())))) {
//					costs[i + 1][j] = costs[i][j]
//							+ Math.abs(Data.board[i][j].getValue()
//									- Data.board[i + 1][j].getValue());
//					ids[i + 1][j] = Data.board[i][j].getID();
//				}
//				
//				/*
//				 * UP
//				 */
//				if (i - 1 > 0
//						&& !cells.contains(Data.board[i - 1][j])
//						&& (costs[i - 1][j] > (costs[i][j]
//								+ Math.abs(Data.board[i][j].getValue()
//										- Data.board[i - 1][j].getValue())))) {
//					costs[i - 1][j] = costs[i][j]
//							+ Math.abs(Data.board[i][j].getValue()
//									- Data.board[i - 1][j].getValue());
//					ids[i - 1][j] = Data.board[i][j].getID();
//				}
//				
//				/*
//				 * RIGHT
//				 */
//				if (j + 1 < StateInfo.NUM_OF_CELLS
//						&& !cells.contains(Data.board[i][j + 1])
//						&& (costs[i][j + 1] > (costs[i][j]
//								+ Math.abs(Data.board[i][j].getValue()
//										- Data.board[i][j + 1].getValue())))) {
//					costs[i][j + 1] = costs[i][j]
//							+ Math.abs(Data.board[i][j].getValue()
//									- Data.board[i][j + 1].getValue());
//					ids[i][j + 1] = Data.board[i][j].getID();
//				}
//				
//				/*
//				 * LEFT
//				 */
//				if (j - 1 > 0
//						&& !cells.contains(Data.board[i][j - 1])
//						&& (costs[i][j - 1] > (costs[i][j]
//								+ Math.abs(Data.board[i][j].getValue()
//										- Data.board[i][j - 1].getValue())))) {
//					costs[i][j - 1] = costs[i][j]
//							+ Math.abs(Data.board[i][j].getValue()
//									- Data.board[i][j - 1].getValue());
//					ids[i][j - 1] = Data.board[i][j].getID();
//				}
//			}
//		}
//		
//		System.out.println(cells.toString());
//	}
//
//	public static int findSmallestValue() {
//		int smallest = StateInfo.INFINITY;
//
//		for (int i = 0; i < costs.length; i++) {
//			for (int j = 0; j < costs[i].length; j++) {
//				if (costs[i][j] < smallest) {
//					smallest = costs[i][j];
//				}
//			}
//		}
//
//		return smallest;
//	}

	public static void findTheOptimalPath() {
		//dijkstra();
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
		return StateInfo.START_X == StateInfo.END_X
				&& StateInfo.START_Y == StateInfo.END_Y;
	}

	public static int findShortestDistanceIndex(Cell[] directions) {
		int j = StateInfo.INFINITY;
		int index = 0;

		for (int i = 0; i < directions.length; i++) {
			if (directions[i].getValue() < j
					&& directions[i].getType() != 1) {
				j = directions[i].getValue();
				index = i;
			}
		}

		return index;
	}
}
