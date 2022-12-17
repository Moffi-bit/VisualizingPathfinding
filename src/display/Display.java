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
import javafx.event.ActionEvent;
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
 * - End/Red 5 - Purple/FinalPath
 * 
 * @author Lorenzo Stiavelli
 *
 */
public class Display extends Application {

	static {
		Data.fillBoard();
	}

	private Color[] colors = { Color.WHITE, Color.BLACK, Color.GREEN,
			Color.BLUE, Color.RED, Color.PURPLE};
	private Group allCells = new Group();
	private Group menuOptions = new Group();
	private Group helpScreen = new Group();
	private Scene scene = new Scene(menuOptions, StateInfo.SCREEN_WIDTH,
			StateInfo.SCREEN_HEIGHT, Color.DARKSLATEGRAY);
	private Button path = new Button("Path Find");
	private Button reset = new Button("Reset Grid");
	private Rectangle[][] graphicalCells = new Rectangle[StateInfo.NUM_OF_CELLS][StateInfo.NUM_OF_CELLS];

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		menu(primaryStage);

		primaryStage.setTitle("Visualization of Pathfinding");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void menu(Stage primaryStage) {
		scene.setRoot(menuOptions);

		Text title = new Text("Visualizing Pathfinding");
		title.setLayoutX(StateInfo.SCREEN_WIDTH / 3.5);
		title.setLayoutY(StateInfo.SCREEN_HEIGHT / 5);
		title.setFont(Font.font("Helvetica", FontWeight.BOLD,
				FontPosture.REGULAR, 50));

		Button vis = new Button("Visualize");
		Button help = new Button("Help");

		modifyHomeButtons(vis, 0);
		vis.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				cellGrid(primaryStage);
			}
		});

		modifyHomeButtons(help, 1);
		help.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				help(primaryStage);
			}
		});

		menuOptions.getChildren().addAll(title, vis, help);
	}

	private void help(Stage primaryStage) {
		scene.setRoot(helpScreen);
		/*
		 * To center the text on the screen (tried using multiple types of panes, stack,
		 * flow, etc. but none could center the text correctly). The code below could
		 * probably be drastically improved I just am not sure how to (even after
		 * researching/looking for possible options).
		 */
		Text text = new Text(
				"The first click will place the starting cell and the second click will place the ending cell. "
						+ "The clicks afterwards");
		Text text2 = new Text(
				"will place walls which can try to obstruct the path to the ending cell."
						+ "To begin pathfinding");
		Text text3 = new Text("press the button labeled: \"Path Find\" on the right side.");

		modifyText(text, 0);
		modifyText(text2, 1);
		modifyText(text3, 2);

		Button back = new Button("Go back to menu.");

		modifyHomeButtons(back, 1);
		back.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				menu(primaryStage);
			}
		});

		helpScreen.getChildren().addAll(text, text2, text3, back);
	}

	private void modifyHomeButtons(Button button, int index) {
		double[] modifier = { 2, 3 };

		button.setTextAlignment(TextAlignment.CENTER);
		button.setLayoutX(StateInfo.SCREEN_WIDTH / 2.65);
		button.setLayoutY(StateInfo.SCREEN_HEIGHT * modifier[index] / 5);
		button.setMinWidth(300);
		button.setMinHeight(100);
	}

	private void modifyGridButtons(Button button, int index) {
		double[] modifier = { 2, 3 };

		button.setLayoutX(StateInfo.SCREEN_WIDTH - 125);
		button.setLayoutY(StateInfo.SCREEN_HEIGHT / modifier[index]);
		button.setTextAlignment(TextAlignment.CENTER);
		button.setMinWidth(100);
		button.setMinHeight(50);
	}

	private void modifyText(Text text, int index) {
		double[] modifier = { 5.0, 4.5, 4.1 };

		text.setLayoutX(StateInfo.SCREEN_WIDTH / 6);
		text.setLayoutY(StateInfo.SCREEN_HEIGHT / modifier[index]);
		text.setFont(Font.font("Helvetica", FontWeight.BOLD,
				FontPosture.REGULAR, 15));
	}

	private void cellGrid(Stage primaryStage) {
		scene.setRoot(allCells);
		fillGraphics();

		modifyGridButtons(path, 0);
		path.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (StateInfo.startPlaced && StateInfo.endPlaced) {
					StateInfo.started = true;
					Pathfinding.findTheOptimalPath();
					fillGraphics();
				}
			}
		});

		modifyGridButtons(reset, 1);
		reset.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				resetGrid();
			}
		});

		mouseEvents();
		
		if (!allCells.getChildren().contains(path) && !allCells.getChildren().contains(reset)) {
			allCells.getChildren().addAll(path, reset);
		}
	}

	private void resetGrid() {
		StateInfo.endPlaced = false;
		StateInfo.startPlaced = false;
		StateInfo.started = false;
		StateInfo.finished = false;
		StateInfo.state = 0;
		StateInfo.START_X = 0;
		StateInfo.START_Y = 0;
		StateInfo.END_X = 0;
		StateInfo.END_Y = 0;
		StateInfo.END_MOUSE_X = 0;
		StateInfo.END_MOUSE_Y = 0;
		clearGrid();
	}

	private void clearGrid() {
		for (int i = 0; i < Data.board.length; i++) {
			for (int j = 0; j < Data.board[i].length; j++) {
				Data.board[i][j].setType(0);
			}
		}

		fillGraphics();
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
					graphicalCells[i][j]
							.setFill(colors[Data.board[i][j].getType()]);

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
					graphicalCells[i][j].setStyle(
							"-fx-stroke: black; -fx-stroke-width: 1;");
					allCells.getChildren().add(graphicalCells[i][j]);
				}
			}
		}

		/*
		 * Add grid page buttons
		 */
		allCells.getChildren().addAll(path, reset);
	}
	
	public void updateCell(int x, int y) {
		if (graphicalCells[x][y] != null) {
			allCells.getChildren().remove(graphicalCells[x][y]);
			graphicalCells[x][y]
					.setFill(colors[Data.board[x][y].getType()]);
			allCells.getChildren().add(graphicalCells[x][y]);
		}
	}
	
	private void mouseEvents() {
		scene.addEventHandler(MouseEvent.ANY, event -> {
			int x = (int) event.getX(), y = (int) event.getY();
			
			/*
			 * Don't let someone click outside of the grid
			 */
			if (x > StateInfo.GRID_WIDTH) {
				return;
			}

			/*
			 * If it has done pathfinding do not do anything.
			 */
			if (StateInfo.finished) {
				return;
			}
			
			/*
			 * If the pathfinding has started do not permit new clicks.
			 */
			if (StateInfo.started) {
				return;
			}
			
			int yIndex = y / StateInfo.CELL_HEIGHT,
					xIndex = x / StateInfo.CELL_WIDTH;
			x = xIndex * StateInfo.CELL_WIDTH;
			y = yIndex * StateInfo.CELL_HEIGHT;
			
			if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				if (StateInfo.state < 2) {
					return;
				}
				
				/*
				 * Recalculate positions
				 */
				x = (int) event.getX();
				y = (int) event.getY();
				yIndex = y / StateInfo.CELL_HEIGHT;
				xIndex = x / StateInfo.CELL_WIDTH;
				
				if (Data.getCell(xIndex, yIndex).getType() == 0) {
					Data.getCell(xIndex, yIndex).setType(1);
					updateCell(xIndex, yIndex);
				}
			}
			
			if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {	
				if (Data.getCell(xIndex, yIndex).getType() != 0) {
					return;
				}
				
				/*
				 * Need to increment state after it is done comparing it's current value so next
				 * cells are correctly assigned.
				 */
				switch (StateInfo.state++) {
				case 0:
					Data.getCell(xIndex, yIndex).setType(2);
					updateCell(xIndex, yIndex);
					StateInfo.START_X = xIndex;
					StateInfo.START_Y = yIndex;
					StateInfo.startPlaced = true;
					break;
				case 1:
					Data.getCell(xIndex, yIndex).setType(4);
					updateCell(xIndex, yIndex);
					StateInfo.END_X = xIndex;
					StateInfo.END_Y = yIndex;
					StateInfo.END_MOUSE_X = x;
					StateInfo.END_MOUSE_Y = y;
					StateInfo.endPlaced = true;
					break;
				default:
					Data.getCell(xIndex, yIndex).setType(1);
					updateCell(xIndex, yIndex);
					break;
				}
	        }
		});
	}
}
