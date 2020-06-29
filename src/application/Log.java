package application;

import java.util.ArrayList;

import javafx.scene.control.ListView;

public class Log {
	private static Log instance;

	public static Log getInstance() {
		if (instance == null) {
			instance = new Log();
		}

		return instance;
	}

	private Log() {
	}

	private ArrayList<String> buffer = new ArrayList<String>();

	private ListView<String> control;

	public void setOutputControl(ListView<String> control) {
		this.control = control;
		clear();
	}

	public void add(String message) {
		System.out.println(message);

		if (control != null) {
			if (buffer.size() > 0) {
				for (String msg : buffer) {
					control.getItems().add(msg + "\n");
				}

				buffer.clear();
			}

			control.getItems().add(message + "\n");
		} else {
			buffer.add(message);
		}
	}

	public void clear() {
		buffer.clear();
		control.getItems().clear();
	}

	public void logHeader(String processName) {
		Log.getInstance().add("=== " + processName + " ===============================================");
	}
}
