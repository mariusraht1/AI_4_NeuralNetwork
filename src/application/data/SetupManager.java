package application.data;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;

import application.Log;
import application.Main;
import application.layer.ConnectableLayer;
import application.layer.Layer;
import application.network.Connection;
import application.network.Network;
import application.neuron.ConnectableNeuron;
import application.neuron.Neuron;
import library.GeneralUtilities;
import library.GeneralUtilities.OSType;

public class SetupManager {
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
			File file = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			this.file = new File(file.getParentFile().getPath() + "//" + this.file.getName());
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

	public void importSetup() {
		if (this.file.exists()) {
			try {
				boolean importWeights = true;
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line;
				while ((line = reader.readLine()) != null) {
					if (line.equals(divider)) {
						importWeights = false;
					} else {
						int indexCSVDivider = line.indexOf(";");
						if (importWeights) {
							String connectID = line.substring(0, indexCSVDivider);
							Double weight = Double.parseDouble(line.substring(indexCSVDivider + 1, line.length()));
							Connection inboundConnection = Connection.getById(connectID);
							inboundConnection.setWeight(weight);
						} else {
							String layerID = line.substring(0, 2);
							Double bias = Double.parseDouble(line.substring(indexCSVDivider + 1, line.length()));
							ConnectableLayer connectableLayer = (ConnectableLayer) Layer.getbyId(layerID);
							connectableLayer.setBias(bias);
						}
					}
				}
				reader.close();
				Log.getInstance().addCritical("Gewichte und Bias wurden importiert.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Log.getInstance()
					.addCritical("setup.csv nicht gefunden: Gewichte und Bias m�ssen zuerst exportiert werden.");
		}
	}

	public void exportSetup() {
		try {
			StringBuilder stringBuilder = new StringBuilder();
			ArrayList<ConnectableLayer> layerList = new ArrayList<ConnectableLayer>();
			layerList.addAll(Network.getInstance().getHiddenLayerList());
			layerList.add(Network.getInstance().getOutputLayer());

			for (ConnectableLayer layer : layerList) {
				for (Neuron neuron : layer.getNeuronList()) {
					if (neuron instanceof ConnectableNeuron) {
						ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;
						for (Connection inboundConnection : connectableNeuron.getInboundConnections()) {
							stringBuilder.append(inboundConnection.getId() + ";" + inboundConnection.getWeight());
							stringBuilder.append("\n");
						}
					}
				}
			}
			stringBuilder.append(this.divider + "\n");
			for (ConnectableLayer layer : layerList) {
				stringBuilder.append(layer.getId() + ";" + layer.getBias());
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
