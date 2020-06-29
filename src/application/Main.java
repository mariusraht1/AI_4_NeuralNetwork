package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Evolutionary System for Traveling Salesman Problem
 * 
 * @author Marius Raht
 * @version 25.06.2020-001
 */
public class Main extends Application {
	private static Stage primaryStage;

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			Main.primaryStage = primaryStage;

			primaryStage.setTitle("Fuzzy System");
			primaryStage.centerOnScreen();

			Scene scene = new Scene(FXMLLoader.load(Main.class.getResource("/application/view/MainScene.fxml")));
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
