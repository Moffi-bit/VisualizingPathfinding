package board;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Board extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) {
		Group root = new Group();
		Scene scene = new Scene(root, 600, 600, Color.WHITE);

		scene.setOnMouseClicked(this::processMouseClick);

		primaryStage.setTitle("Visualization of Pathfinding");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void processMouseClick(MouseEvent event) {
		
	}

}
