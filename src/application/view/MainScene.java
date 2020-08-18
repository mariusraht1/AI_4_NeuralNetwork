package application.view;

import java.awt.image.BufferedImage;
import java.io.File;

import application.Log;
import application.Main;
import application.data.DataInputType;
import application.data.DataItem;
import application.data.Digit;
import application.data.HandwrittenDigit;
import application.data.SetupManager;
import application.functions.ActivationFunction;
import application.functions.Distribution;
import application.network.Backpropagation;
import application.network.Feedforwarding;
import application.network.Network;
import application.network.OperationMode;
import application.utilities.FileManager;
import application.utilities.ImageDecoder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.StrokeLineCap;
import library.MathManager;

public class MainScene {
	@FXML
	private TabPane tp_inputData;
	@FXML
	private TextField tf_imageFile;
	@FXML
	private Button btn_imageFile;
	@FXML
	private TextField tf_labelFile;
	@FXML
	private Button btn_labelFile;
	@FXML
	private StackPane sp_parentOfCanvas;
	@FXML
	private Canvas cv_canvas;
	@FXML
	private TextField tf_labelDrawing;
	@FXML
	private TextField tf_learningRate;
	@FXML
	private ComboBox<Distribution> cb_distribution;
	@FXML
	private ComboBox<ActivationFunction> cb_activationFunction;
	@FXML
	private ComboBox<OperationMode> cb_operationMode;
	@FXML
	private TextField tf_numOfSteps;
	@FXML
	private Label lbl_results;
	@FXML
	private CheckBox chk_animate;
	@FXML
	private CheckBox chk_disableLogging;
	@FXML
	private ListView<Digit> lv_results;
	@FXML
	private ListView<String> lv_console;

	private boolean useInternalFiles = true;

	@FXML
	private void initialize() {
		Log.getInstance().setOutputControl(lv_console);

		initInputData();
		initUI();

		graphicsContext = cv_canvas.getGraphicsContext2D();
		graphicsContext.setStroke(javafx.scene.paint.Color.WHITE);
		graphicsContext.setLineWidth(5.0);
		graphicsContext.setLineCap(StrokeLineCap.SQUARE);

		// Bind canvas to stackpane
		cv_canvas.widthProperty().bind(sp_parentOfCanvas.widthProperty());
		cv_canvas.heightProperty().bind(sp_parentOfCanvas.heightProperty());
	}

	private void initInputData() {
		File imageFile = Main.DefaultTrainImageFile;
		File labelFile = Main.DefaultTrainLabelFile;

		ImageDecoder.getInstance().readFiles(imageFile, labelFile);
		Network.getInstance().init();

		Log.getInstance().addCritical("Dateien wurden erfolgreich geladen.");
	}

	private void initUI() {
		if (tf_numOfSteps.getText().isEmpty()) {
			tf_numOfSteps.setText(String.valueOf(Main.DefaultNumOfSteps));
		}

		if (tf_learningRate.getText().isEmpty()) {
			tf_learningRate.setText(String.valueOf(Main.DefaultLearningRate));
		}

		if (cb_distribution.getItems().isEmpty()) {
			cb_distribution.getItems().addAll(Distribution.values());
			cb_distribution.getSelectionModel().select(Network.getInstance().getDistribution());
		}

		if (cb_activationFunction.getItems().isEmpty()) {
			cb_activationFunction.getItems().addAll(ActivationFunction.values());
			cb_activationFunction.getItems().remove(ActivationFunction.SOFTMAX);
			cb_activationFunction.getSelectionModel().select(Network.getInstance().getActivationFunction());
		}

		if (cb_operationMode.getItems().isEmpty()) {
			cb_operationMode.getItems().addAll(OperationMode.values());
			cb_operationMode.getSelectionModel().select(Network.getInstance().getOperationMode());
		}

		lbl_results.setText("Ergebnisse (" + String.format("%.2f", 0.00) + " %)");
		lv_results.getItems().clear();
	}

	public void showResult(boolean animate, DataItem dataItem) {
		double successRate = Feedforwarding.getInstance().getSuccessRate();
		lbl_results.setText("Ergebnisse (" + String.format("%.2f", successRate) + " %)");

		if (animate && dataItem instanceof Digit) {
			Digit digit = (Digit) dataItem;
			lv_results.getItems().add(digit);
			lv_results.scrollTo(digit);
			lv_results.setCellFactory(listview -> new ListCell<Digit>() {
				private ImageView imageView = new ImageView();

				@Override
				protected void updateItem(final Digit item, final boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setText("");
						setGraphic(null);
					} else {
						setText(item.getLabel() + " => " + item.getPrediction());
						imageView.setImage(item.toWritableImage());
						setGraphic(imageView);
					}
				}
			});
		}
	}

	@FXML
	private void onAction_chooseImageFile() {
		File imageFile = FileManager.getInstance().chooseFile();
		if (imageFile != null) {
			tf_imageFile.setText(imageFile.getAbsolutePath());
		}
	}

	@FXML
	private void onAction_chooseLabelFile() {
		File labelFile = FileManager.getInstance().chooseFile();
		if (labelFile != null) {
			tf_labelFile.setText(labelFile.getAbsolutePath());
		}
	}

	@FXML
	private void onAction_chkInternalFiles(ActionEvent event) {
		CheckBox chkInternalFiles = (CheckBox) event.getSource();
		if (chkInternalFiles.isSelected()) {
			useInternalFiles = true;
			tf_imageFile.setDisable(true);
			btn_imageFile.setDisable(true);
			tf_labelFile.setDisable(true);
			btn_labelFile.setDisable(true);
		} else {
			useInternalFiles = false;
			tf_imageFile.setDisable(false);
			btn_imageFile.setDisable(false);
			tf_labelFile.setDisable(false);
			btn_labelFile.setDisable(false);
		}
	}

	@FXML
	private void onAction_loadData() {
		boolean setOptions = true;
		File imageFile = new File(tf_imageFile.getText());
		File labelFile = new File(tf_labelFile.getText());

		if (!this.useInternalFiles) {
			if (!imageFile.exists()) {
				setOptions = false;
				tf_imageFile.setText("");
			}

			if (!labelFile.exists()) {
				setOptions = false;
				tf_labelFile.setText("");
			}
		} else {
			imageFile = Main.DefaultTrainImageFile;
			labelFile = Main.DefaultTrainLabelFile;
		}

		if (setOptions) {
			ImageDecoder.getInstance().readFiles(imageFile, labelFile);
			Network.getInstance().init();
			initialize();

			Log.getInstance().add("Dateien wurden erfolgreich geladen.");
		}
	}

	private GraphicsContext graphicsContext;

	@FXML
	private void onMouseDragged_canvas(MouseEvent event) {
		graphicsContext.lineTo(event.getX(), event.getY());
		graphicsContext.stroke();
	}

	@FXML
	private void onMousePressed_canvas(MouseEvent event) {
		graphicsContext.beginPath();
		graphicsContext.moveTo(event.getX(), event.getY());
		graphicsContext.stroke();
	}

	@FXML
	private void onAction_btnClearCanvas() {
		clearCanvas();
	}

	private void clearCanvas() {
		graphicsContext.clearRect(0, 0, cv_canvas.getWidth(), cv_canvas.getHeight());
		tf_labelDrawing.clear();
	}

	@FXML
	private void onAction_btnSaveDrawing() {
		int label = MathManager.getInstance().parseInt(tf_labelDrawing.getText());

		if (label < 0) {
			tf_labelDrawing.clear();
			Log.getInstance().addCritical("Bitte Label eingeben, um Vorhersage validieren zu können.");
		} else {
			try {
				Image handwrittenDigit = ImageDecoder.getInstance().getImageFromCanvas(cv_canvas);
				BufferedImage scaledImage = ImageDecoder.getInstance().resize(handwrittenDigit);
				byte[] scaledImageBytes = ImageDecoder.getInstance().toBytes(scaledImage);

				if (scaledImageBytes.length > 0) {
					scaledImageBytes = ImageDecoder.getInstance().toMNIST(scaledImageBytes);
					new HandwrittenDigit(label, scaledImageBytes);
					Network.getInstance().setDataInputType(DataInputType.HANDWRITTEN_DIGIT);
					clearCanvas();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void onAction_setOptions() {
		double learningRate = MathManager.getInstance().parseDouble(tf_learningRate.getText());
		if (learningRate < 0.0 || learningRate > 1.0) {
			tf_learningRate.clear();
		} else {
			Feedforwarding.getInstance().init();
			Backpropagation.getInstance().setLearningRate(learningRate);
			Network.getInstance().setDistribution(cb_distribution.getSelectionModel().getSelectedItem());
			Network.getInstance().setActivationFunction(cb_activationFunction.getSelectionModel().getSelectedItem());
			
			Log.getInstance().addCritical("Optionen gespeichert.");
		}
	}

	@FXML
	private void onAction_btnPlay() {
		try {
			int numOfSteps = Integer.parseInt(tf_numOfSteps.getText());

			if (numOfSteps <= 0) {
				tf_numOfSteps.setText(String.valueOf(Main.DefaultNumOfSteps));
			} else {
				boolean animate = chk_animate.isSelected();
				boolean disableLogging = chk_disableLogging.isSelected();
				OperationMode operationMode = cb_operationMode.getSelectionModel().getSelectedItem();
				if (!Network.getInstance().getOperationMode().equals(operationMode)) {
					Network.getInstance().setOperationMode(operationMode);
					Feedforwarding.getInstance().init();
				}

				Log.getInstance().setDisable(disableLogging);
				if (animate) {
					Log.getInstance().setIsActive(true);
				} else {
					Log.getInstance().setIsActive(false);
				}

				switch (tp_inputData.getSelectionModel().getSelectedIndex()) {
				case 0:
					if (!ImageDecoder.getInstance().isNull()) {
						Network.getInstance().setDataInputType(DataInputType.MNIST_DIGIT);
						Network.getInstance().runPlay(animate, numOfSteps, this);
					} else {
						Log.getInstance().addCritical("Bitte zuerst Eingabedaten laden.");
					}
					break;
				case 1:
					if (!HandwrittenDigit.isNull()) {
						Network.getInstance().setDataInputType(DataInputType.HANDWRITTEN_DIGIT);
						Network.getInstance().runPlay(animate, numOfSteps, this);
					} else {
						Log.getInstance().addCritical("Bitte zuerst eine handschriftliche Nummer eingeben.");
					}
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void onAction_btnReset() {
		Network.getInstance().init();
		initialize();
	}

	@FXML
	private void onAction_btnImport() {
		SetupManager.getInstance().importSetup();
	}

	@FXML
	private void onAction_btnExport() {
		SetupManager.getInstance().exportSetup();
		SetupManager.getInstance().showExport();
	}
}
