package application.utilities;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;

import application.Main;
import library.GeneralUtilities;
import library.GeneralUtilities.OSType;

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

	private String divider = "==========";
	private ArrayList<String[]> weights = new ArrayList<String[]>();
	private ArrayList<String[]> biases = new ArrayList<String[]>();

	private SetupManager() {
		try {
			this.file = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			this.file = new File(this.file.getParentFile().getPath() + "//" + this.file.getName());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		initHeader();
	}

	private void initHeader() {
		this.weights.add(new String[] { "ID", "Weight" });
		this.biases.add(new String[] { "ID", "Bias" });
	}

	public void clear() {
		this.weights.clear();
		this.biases.clear();
		initHeader();
	}

	public void addWeight(String connectionID, double weight) {
		this.weights.add(new String[] { connectionID, Double.toString(weight) });
	}

	public void export() {
		try {
			StringBuilder stringBuilder = new StringBuilder();
			for (String[] x : this.weights) {
				for (int i = 0; i < x.length; i++) {
					stringBuilder.append(x[i]);
					if (i < x.length - 1) {
						stringBuilder.append(";");
					}
				}
				stringBuilder.append("\n");
			}
			stringBuilder.append(this.divider);
			for (String[] x : this.biases) {
				for (int i = 0; i < x.length; i++) {
					stringBuilder.append(x[i]);
					if (i < x.length - 1) {
						stringBuilder.append(";");
					}
				}
				stringBuilder.append("\n");
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.append(stringBuilder);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showExport() {

		try {
			if (GeneralUtilities.getInstance().getOperatingSystemType().equals(OSType.Windows)) {
				Runtime.getRuntime().exec("explorer.exe /select, " + file);
			} else {
				Desktop.getDesktop().open(this.file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
