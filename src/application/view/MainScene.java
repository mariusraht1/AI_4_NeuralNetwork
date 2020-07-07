package application.view;

import java.io.File;

import application.History;
import application.Log;
import application.Utilities;
import application.filemanager.MNISTImageDecoder;
import application.model.Digit;
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
			
			Digit digit = MNISTImageDecoder.getInstance().readNextDigit();
			iv_digit.setImage(digit.toWritableImage());
			lbl_digit.setText("N.A.");
			lbl_label.setText("(Label: " + digit.getLabel() + ")");
		}
	}

	@FXML
	private void onAction_btnPlay() {
		if (MNISTImageDecoder.getInstance().getImageFileContent() != null
				&& MNISTImageDecoder.getInstance().getLabelFileContent() != null) {
			Digit digit = MNISTImageDecoder.getInstance().readNextDigit();
			iv_digit.setImage(digit.toWritableImage());

			// NEW Determine digit

			lbl_digit.setText("N.A."); // NEW Set determined digit
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
