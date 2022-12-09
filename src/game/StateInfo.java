package game;

/**
 * 
 * @author Lorenzo Stiavelli
 *
 */
public class StateInfo {
	
	public final static int WIDTH = 1200;
	public final static int HEIGHT = 800;
	public final static int NUM_OF_CELLS = 50;
	public final static int CELL_WIDTH = WIDTH / NUM_OF_CELLS;
	public final static int CELL_HEIGHT = HEIGHT / NUM_OF_CELLS;
	
	public static int STARTING_X = 0;
	public static int STARTING_Y = 0;
	public static int END_X = 0;
	public static int END_Y = 0;
	/*
	 * If 0, no cells have been selected.
	 * If 1, a start cell has been selected.
	 * If 2, an end cell has been selected.
	 * After 2, indicates how many walls have been placed.
	 */
	public static int state = 0;
}
