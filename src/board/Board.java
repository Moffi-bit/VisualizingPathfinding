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
				/*
				 * Check to see if this rectangle has already been made. If not, make the
				 * rectangle.
				 */
				if (graphicalCells[i][j] != null) {
					/*
					 * See if it's type has changed after user interaction.
					 */
					if (Data.board[i][j].getType() != 0) {
						graphicalCells[i][j]
								.setFill(colors[Data.board[i][j].getType()]);
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
		if (StateInfo.finished) {
			System.out.println("Done pathfinding.");
			return;
		}
		
		/*
		 * If the pathfinding has started do not permit new clicks.
		 */
		if (StateInfo.started) {
			/*
			 * Display something to the user to indicate that they cannot click during
			 * pathfinding and replace the print statement with that.
			 */
			System.out.println(
					"Cannot click on additional cells while the program is pathfinding!");

			return;
		}

		int x = (int) event.getX(), y = (int) event.getY();
		int yIndex = y / StateInfo.CELL_HEIGHT,
				xIndex = x / StateInfo.CELL_WIDTH;
		x = xIndex * StateInfo.CELL_WIDTH;
		y = yIndex * StateInfo.CELL_HEIGHT;
		
		System.out.println("Mouse coordinates: " + x + ", " + y);
		System.out.println("Data indices: " + xIndex + ", " + yIndex);

		/*
		 * Need to increment state after it is done comparing it's current value so next
		 * cells are correctly assigned.
		 */
		switch (StateInfo.state++) {
		case 0:
			Data.getCell(xIndex, yIndex).setType(2);
			StateInfo.START_X = xIndex;
			StateInfo.START_Y = yIndex;
			break;
		case 1:
			Data.getCell(xIndex, yIndex).setType(4);
			StateInfo.END_X = xIndex;
			StateInfo.END_Y = yIndex;
			StateInfo.END_MOUSE_X = x;
			StateInfo.END_MOUSE_Y = y;
			break;
		default:
			Data.getCell(xIndex, yIndex).setType(1);
			break;
		}

		System.out.println("Type: " + Data.getCell(xIndex, yIndex).getType()
				+ " State: " + StateInfo.state);

		/*
		 * Start pathfinding! (currently pathfinding will start after 8 walls have been
		 * set.
		 */
		if (StateInfo.state == 15) {
			StateInfo.started = true;
			Pathfinding.findTheOptimalPath();
		}
		
		/*
		 * Redraw the graphics after the user has clicked cells.
		 */
		fillGraphics();
	}

}
