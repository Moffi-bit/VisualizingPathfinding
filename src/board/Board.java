package board;

import algorithm.Pathfinding;
/*
 * Personal Package Imports
 */
import cell.Data;
import game.StateInfo;

/*
 * JavaFX Imports
 */
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * 
 * Types of Cells 0 - Empty/White 1 - Wall/Black 2 - Start/Green 3 - Path/Blue 4
 * - End/Red
 * 
 * @author Lorenzo Stiavelli
 *
 */
public class Board extends Application {

	static {
		Data.fillBoard();
	}

	private Color[] colors = { Color.WHITE, Color.BLACK, Color.GREEN,
			Color.BLUE, Color.RED };
	private Group root = new Group();
	private Rectangle[][] graphicalCells = new Rectangle[StateInfo.NUM_OF_CELLS][StateInfo.NUM_OF_CELLS];

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		Scene scene = new Scene(root, StateInfo.WIDTH, StateInfo.HEIGHT,
				Color.WHITE);

		fillGraphics();
		scene.setOnMouseClicked(this::processMouseClick);

		primaryStage.setTitle("Visualization of Pathfinding");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void fillGraphics() {
		root.getChildren().clear();

		for (int i = 0; i < graphicalCells.length; i++) {
			for (int j = 0; j < graphicalCells[i].length; j++) {
				if (graphicalCells[i][j] != null) {
					if (Data.board[i][j].getType() != 0) {
						graphicalCells[i][j].setFill(colors[Data.board[i][j].getType()]);
					}
					
					root.getChildren().add(graphicalCells[i][j]);
				} else {
					graphicalCells[i][j] = new Rectangle(
							i * StateInfo.CELL_WIDTH,
							j * StateInfo.CELL_HEIGHT, StateInfo.CELL_WIDTH,
							StateInfo.CELL_HEIGHT);
					graphicalCells[i][j]
							.setFill(colors[Data.board[i][j].getType()]);
					/*
					 * To help indicate different cells by creating a border.
					 */
					graphicalCells[i][j]
							.setStyle(Data.board[i][j].getType() == 1
									? "-fx-stroke: white; -fx-stroke-width: 1;"
									: "-fx-stroke: black; -fx-stroke-width: 1;");
					root.getChildren().add(graphicalCells[i][j]);
				}
			}
		}
	}

	public void processMouseClick(MouseEvent event) {
		int x = (int) event.getX(), y = (int) event.getY();
		int xIndex = y / StateInfo.CELL_HEIGHT,
				yIndex = x / StateInfo.CELL_WIDTH;

		System.out.println("Mouse coordinates: " + x + ", " + y);
		System.out.println("Data indices: " + xIndex + ", " + yIndex);

		/*
		 * Need to increment state after it is done comparing it's current value so next
		 * cells are correctly assigned.
		 */
		switch (StateInfo.state++) {
		case 0: 
			Data.getCell(xIndex, yIndex).setType(2);
			StateInfo.STARTING_X = xIndex;
			StateInfo.STARTING_Y = yIndex;
			break;
		case 1:
			Data.getCell(xIndex, yIndex).setType(4);
			StateInfo.END_X = xIndex;
			StateInfo.END_Y = yIndex;
			break;
		default:
			Data.getCell(xIndex, yIndex).setType(1);
			break;
		}

		System.out.println("Type: " + Data.getCell(xIndex, yIndex).getType() + " State: "
				+ StateInfo.state);
		/*
		 * Redraw the graphics after the user has clicked cells.
		 */
		fillGraphics();
		
		/*
		 * Start pathfinding! (currently pathfinding will start after 7 walls have been set.
		 */
		if (StateInfo.state == 10) {
			Pathfinding.findTheOptimalPath();
		}
	}

}
