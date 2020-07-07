package application.view;

import java.io.File;

import application.History;
import application.Log;
import application.Utilities;
import application.filemanager.MNISTImageDecoder;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MainScene {
	@FXML
	private TextField tf_imageFile;
	@FXML
	private TextField tf_labelFile;
	@FXML
	private ListView<String> lv_console;

	@FXML
	private void initialize() {
		Log.getInstance().setOutputControl(lv_console);

	}

	@FXML
	private void onAction_chooseImageFile() {
		File imageFile = Utilities.getInstance().chooseFile();
		if (imageFile != null) {
			tf_imageFile.setText(imageFile.getAbsolutePath());
		}
	}

	@FXML
	private void onAction_chooseLabelFile() {
		File labelFile = Utilities.getInstance().chooseFile();
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
			MNISTImageDecoder.getInstance().readFiles(imageFile, labelFile);
		}
	}

	@FXML
	private void onAction_btnPlay() {

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
