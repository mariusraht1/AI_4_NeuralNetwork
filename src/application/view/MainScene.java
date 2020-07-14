package application.view;

import java.io.File;

import application.History;
import application.Log;
import application.image.ImageDecoder;
import application.network.Digit;
import application.network.Network;
import application.utilities.FileManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class MainScene {
	@FXML
	private TextField tf_imageFile;
	@FXML
	private TextField tf_labelFile;
	@FXML
	private ImageView iv_digit;
	@FXML
	private Label lbl_digit;
	@FXML
	private Label lbl_label;
	@FXML
	private ListView<String> lv_console;

	@FXML
	private void initialize() {
		Log.getInstance().setOutputControl(lv_console);

		Network.getInstance().init();
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
	private void onAction_setOptions() {
		File imageFile = new File(tf_imageFile.getText());
		File labelFile = new File(tf_labelFile.getText());

		if (!imageFile.exists()) {
			tf_imageFile.setText("");
		} else if (!labelFile.exists()) {
			tf_labelFile.setText("");
		} else {
			ImageDecoder.getInstance().readFiles(imageFile, labelFile);

			Digit digit = ImageDecoder.getInstance().readNextDigit();
			iv_digit.setImage(digit.toWritableImage());
			lbl_digit.setText("N.A.");
			lbl_label.setText("(Label: " + digit.getLabel() + ")");
		}
	}

	@FXML
	private void onAction_btnPlay() {
		if (ImageDecoder.getInstance().getImageFileContent() != null
				&& ImageDecoder.getInstance().getLabelFileContent() != null) {
			Digit digit = ImageDecoder.getInstance().readNextDigit();
			iv_digit.setImage(digit.toWritableImage());

			Network.getInstance().play(digit);
			int prediction = Network.getInstance().getPrediction();

			if (prediction < 0) {
				lbl_digit.setText("N.A.");
			} else {
				lbl_digit.setText(Integer.toString(prediction));
			}

			lbl_label.setText("(Label: " + digit.getLabel() + ")");
		}
	}

	@FXML
	private void onAction_btnReset() {
		initialize();
	}

	@FXML
	private void onAction_btnExport() {
		History.getInstance().export();
		History.getInstance().showExport();
	}
}
