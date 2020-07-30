package application.utilities;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import application.Main;

public class SetupManager {
	// NEW Export weights and biases
	// NEW Import weights and biases and init network with them
	private static SetupManager instance;

	public static SetupManager getInstance() {
		if (instance == null) {
			instance = new SetupManager();
		}

		return instance;
	}

	private File file = new File("setup.csv");

	public File getFile() {
		return file;
	}

	private ArrayList<String[]> weights = new ArrayList<String[]>();
	private ArrayList<String[]> biases = new ArrayList<String[]>();

	private SetupManager() {
		try {
			file = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			file = new File(file.getParentFile().getPath() + "//" + file.getName());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		initHeader();
	}

	private void initHeader() {
		weights.add(new String[] { "ID", "Weight" });
		biases.add(new String[] { "ID", "Bias" });
	}
}
