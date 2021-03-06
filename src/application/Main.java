package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Neural Network for handwritten digit recognition
 * 
 * @author Marius Raht
 * @version 18.08.2020-001
 */
public class Main extends Application {
	private static Stage primaryStage;

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	public final static int MinNumOfSteps = 1;
	public final static int DefaultNumOfSteps = 1;
	public final static double DefaultLearningRate = 0.1;

	public final static File DefaultTrainImageFile = new File(getDataDirectory().getPath() + "train-images.idx3-ubyte");
	public final static File DefaultTrainLabelFile = new File(getDataDirectory().getPath() + "train-labels.idx1-ubyte");
	public final static File DefaultTestImageFile = new File(getDataDirectory().getPath() + "t10k-images-idx3-ubyte");
	public final static File DefaultTestLabelFile = new File(getDataDirectory().getPath() + "t10k-labels-idx1-ubyte");

	@Override
	public void start(Stage primaryStage) {
		try {
			Main.primaryStage = primaryStage;
			Locale.setDefault(new Locale("en", "US"));

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

	public static URL getDataDirectory() {
		return Main.class.getResource("/data/");
	}
}
