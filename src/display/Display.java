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
import javafx.scene.layout.StackPane;
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
	private Scene scene = new Scene(menuOptions, StateInfo.WIDTH + 150,
			StateInfo.HEIGHT, Color.DARKSLATEGRAY);
	private Button path = new Button("Path Find");
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
		scene.setRoot(menuOptions);

		Text title = new Text("Visualizing Pathfinding");
		title.setLayoutX(StateInfo.WIDTH / 3.5);
		title.setLayoutY(StateInfo.HEIGHT / 5);
		title.setFont(Font.font("Helvetica", FontWeight.BOLD,
				FontPosture.REGULAR, 50));

		Button vis = new Button("Visualize");
		Button help = new Button("Help");

		modifyButtons(vis, 0);
		vis.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				cellGrid(primaryStage);
			}
		});

		modifyButtons(help, 1);
		help.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				help(primaryStage);
			}
		});

		menuOptions.getChildren().addAll(title, vis, help);

		primaryStage.setScene(scene);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
						+ "The click afterwards");
		Text text2 = new Text(
				"will place walls which can try to obstruct the path to the ending cell."
						+ "To make the program being pathfinding");
		Text text3 = new Text("press the button labeled: \"Path Find\".");
		
		modifyText(text, 0);
		modifyText(text2, 1);
		modifyText(text3, 2);
		
		Button back = new Button("Go back to menu.");
		
		modifyButtons(back, 1);
		back.setOnAction(new EventHandler() {
			
			@Override
			public void handle(Event arg0) {
				menu(primaryStage);
			}
		});
		
		helpScreen.getChildren().addAll(text, text2, text3, back);
	}
	
	private void modifyButtons(Button button, int index) {
		double[] modifier = {2, 3};
		
		button.setTextAlignment(TextAlignment.CENTER);
		button.setLayoutX(StateInfo.WIDTH / 2.65);
		button.setLayoutY(StateInfo.HEIGHT * modifier[index] / 5);
		button.setMinWidth(300);
		button.setMinHeight(100);
	}
	
	private void modifyText(Text text, int index) {
		double[] modifier = {5.0, 4.5, 4.1};
		
		text.setLayoutX(StateInfo.WIDTH / 6);
		text.setLayoutY(StateInfo.HEIGHT / modifier[index]);
		text.setFont(Font.font("Helvetica", FontWeight.BOLD,
				FontPosture.REGULAR, 15));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void cellGrid(Stage primaryStage) {
		scene.setRoot(allCells);
		fillGraphics();
		
		path.setLayoutX(StateInfo.WIDTH + 25);
		path.setLayoutY(StateInfo.HEIGHT / 2);
		path.setTextAlignment(TextAlignment.CENTER);
		path.setMinWidth(100);
		path.setMinHeight(50);
		path.setOnAction(new EventHandler() {
			
			@Override
			public void handle(Event arg0) {
				StateInfo.started = true;
				Pathfinding.findTheOptimalPath();
				fillGraphics();
			}
		});
		
		scene.setOnMouseClicked(this::processMouseClick);
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
		
		/*
		 * Add path find button again.
		 */
		allCells.getChildren().add(path);
	}

	public void processMouseClick(MouseEvent event) {
		int x = (int) event.getX(), y = (int) event.getY();
		
		if (x > StateInfo.WIDTH) {
			System.out.println("Out of bounds.");
			return;
		}
		
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
		 * Redraw the graphics after the user has clicked cells.
		 */
		fillGraphics();
	}

}