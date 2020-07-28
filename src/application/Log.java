package application;

import java.util.ArrayList;

import application.data.DataItem;
import application.layer.OutputLayer;
import application.network.Network;
import application.neuron.Neuron;
import application.neuron.OutputNeuron;
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

	private boolean isActive;

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean active) {
		this.isActive = active;
	}

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

		control.scrollTo(control.getItems().size());
	}

	public void clear() {
		buffer.clear();
		control.getItems().clear();
	}

	public void logHeader(String processName) {
		Log.getInstance().add("=== " + processName + " ===============================================");
	}

	public void logPredictions(DataItem dataItem) {
		OutputLayer outputLayer = Network.getInstance().getOutputLayer();
		Log.getInstance().add("Predictions for Label " + dataItem.getLabel() + ":");
		StringBuilder probabilities = new StringBuilder("[");
		for (Neuron neuron : outputLayer.getNeuronList()) {
			if (neuron instanceof OutputNeuron) {
				OutputNeuron outputNeuron = (OutputNeuron) neuron;
				probabilities.append(outputNeuron.getRepresentationValue() + ": "
						+ String.format("%.2f", outputNeuron.getProbability()));

				if (outputLayer.getNeuronList().indexOf(outputNeuron) < outputLayer.getNeuronList().size() - 1) {
					probabilities.append(", ");
				} else {
					probabilities.append("]");
				}
			}
		}
		Log.getInstance().add(probabilities.toString());
	}
}
