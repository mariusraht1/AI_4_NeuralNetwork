package application.view;

import java.io.File;

import application.History;
import application.Log;
import application.Main;
import application.data.DataItem;
import application.data.DigitImage;
import application.network.Network;
import application.network.OperationMode;
import application.utilities.FileManager;
import application.utilities.ImageDecoder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class MainScene {
	@FXML
	private TextField tf_imageFile;
	@FXML
	private Button btn_imageFile;
	@FXML
	private TextField tf_labelFile;
	@FXML
	private Button btn_labelFile;
	@FXML
	private ComboBox<OperationMode> cb_operationMode;
	@FXML
	private TextField tf_numOfSteps;
	@FXML
	private Label lbl_results;
	@FXML
	private CheckBox chk_animate;
	@FXML
	private ListView<DigitImage> lv_results;
	@FXML
	private ListView<String> lv_console;

	private boolean useInternalFiles = true;

	@FXML
	private void initialize() {
		Log.getInstance().setOutputControl(lv_console);

		tf_numOfSteps.setText(String.valueOf(Main.DefaultNumOfSteps));
		tf_numOfSteps.setPromptText("Mind. " + String.valueOf(Main.MinNumOfSteps));

		if (cb_operationMode.getItems().isEmpty()) {
			cb_operationMode.getItems().addAll(OperationMode.values());
			cb_operationMode.getSelectionModel().select(Network.getInstance().getOperationMode());
		}

		lv_results.getItems().clear();
	}

	public void showResult(boolean animate, DataItem dataItem) {
		double successRate = Network.getInstance().getSuccessRate();
		lbl_results.setText("Ergebnisse (" + String.format("%.2f", successRate) + " %)");

		if (animate && dataItem instanceof DigitImage) {
			DigitImage digit = (DigitImage) dataItem;
			lv_results.getItems().add(digit);
			lv_results.scrollTo(digit);
			lv_results.setCellFactory(listview -> new ListCell<DigitImage>() {
				private ImageView imageView = new ImageView();

				@Override
				protected void updateItem(final DigitImage item, final boolean empty) {
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
	private void onAction_setOptions() {
		boolean setOptions = true;
		File imageFile = new File(tf_imageFile.getText());
		File labelFile = new File(tf_labelFile.getText());

		if (!useInternalFiles) {
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

			Log.getInstance().add("Optionen erfolgreich gespeichert und Dateien geladen.");
		}
	}

	@FXML
	private void onAction_cbOperationMode(ActionEvent e) {
		Network.getInstance().setOperationMode(cb_operationMode.getSelectionModel().getSelectedItem());
	}

	@FXML
	private void onAction_btnPlay() {
		try {
			int numOfSteps = Integer.parseInt(tf_numOfSteps.getText());

			if (numOfSteps <= 0) {
				tf_numOfSteps.setText(String.valueOf(Main.DefaultNumOfSteps));
			} else {
				boolean animate = chk_animate.isSelected();
				if (animate) {
					Log.getInstance().setIsActive(true);
				} else {
					Log.getInstance().setIsActive(false);
				}

				if (ImageDecoder.getInstance().getImageFileContent() != null
						&& ImageDecoder.getInstance().getLabelFileContent() != null) {
					Network.getInstance().runPlay(animate, numOfSteps, this);
				} else {
					Log.getInstance().add("Zuerst bitte Optionen setzen und bestätigen.");
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
	private void onAction_btnExport() {
		History.getInstance().export();
		History.getInstance().showExport();
	}
}
