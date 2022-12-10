package display;

/*
 * Personal Package Imports
 */
import algorithm.Pathfinding;
import cell.Data;
import game.StateInfo;

/*
 * JavaFX Imports
 */
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * 
 * Types of Cells 0 - Empty/White 1 - Wall/Black 2 - Start/Green 3 - Path/Blue 4
 * - End/Red
 * 
 * @author Lorenzo Stiavelli
 *
 */
public class Display extends Application {

	static {
		Data.fillBoard();
	}

	private Color[] colors = { Color.WHITE, Color.BLACK, Color.GREEN,
			Color.BLUE, Color.RED };
	private Group allCells = new Group();
	private Group menuOptions = new Group();
	private Group helpScreen = new Group();
 	private Rectangle[][] graphicalCells = new Rectangle[StateInfo.NUM_OF_CELLS][StateInfo.NUM_OF_CELLS];

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		menu(primaryStage);

		primaryStage.setTitle("Visualization of Pathfinding");
		primaryStage.show();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void menu(Stage primaryStage) {
		Scene scene = new Scene(menuOptions, StateInfo.WIDTH, StateInfo.HEIGHT, Color.DARKSLATEGRAY);
		
		Text title = new Text("Visualizing Pathfinding");
		title.setLayoutX(StateInfo.WIDTH / 3.5);
		title.setLayoutY(StateInfo.HEIGHT / 5);
		title.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 50));
		
		Button vis = new Button("Visualize");
		Button help = new Button("Help");
		
		vis.setTextAlignment(TextAlignment.CENTER);
		vis.setLayoutX(StateInfo.WIDTH / 2.65);
		vis.setLayoutY(StateInfo.HEIGHT * 2 / 5);
		vis.setMinWidth(300);
		vis.setMinHeight(100);
		vis.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				cellGrid(primaryStage);
			}
		});
		
		help.setTextAlignment(TextAlignment.CENTER);
		help.setLayoutX(StateInfo.WIDTH / 2.65);
		help.setLayoutY(StateInfo.HEIGHT * 3 / 5);
		help.setMinWidth(300);
		help.setMinHeight(100);
		help.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				help(primaryStage);
			}
		});
		
		menuOptions.getChildren().addAll(title, vis, help);
		
		primaryStage.setScene(scene);
	}
	
	private void help(Stage primaryStage) {
		Scene scene = new Scene(helpScreen, StateInfo.WIDTH, StateInfo.HEIGHT, Color.DARKSLATEGRAY);
		
		helpScreen.getChildren();
		
		primaryStage.setScene(scene);
	}
	
	private void cellGrid(Stage primaryStage) {
		Scene scene = new Scene(allCells, StateInfo.WIDTH, StateInfo.HEIGHT,
				Color.WHITE);
		fillGraphics();
		scene.setOnMouseClicked(this::processMouseClick);
		primaryStage.setScene(scene);
	}

	private void fillGraphics() {
		allCells.getChildren().clear();

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

					allCells.getChildren().add(graphicalCells[i][j]);
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
					allCells.getChildren().add(graphicalCells[i][j]);
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
