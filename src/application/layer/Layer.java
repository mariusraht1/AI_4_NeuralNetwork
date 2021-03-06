package application.layer;

import java.util.ArrayList;
import java.util.Locale;

import application.network.Network;
import application.neuron.Neuron;

public abstract class Layer {
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	protected ArrayList<Neuron> neuronList = new ArrayList<Neuron>();

	public ArrayList<Neuron> getNeuronList() {
		return neuronList;
	}

	public void setNeuronList(ArrayList<Neuron> neuronList) {
		this.neuronList = neuronList;
	}

	protected Layer(String name) {
		this.setId(name);
	}

	public Layer getNextLayer() {
		Layer result = null;

		if (this.equals(Network.getInstance().getInputLayer())) {
			if (Network.getInstance().getHiddenLayerList().isEmpty()) {
				result = Network.getInstance().getOutputLayer();
			} else {
				result = Network.getInstance().getHiddenLayerList().get(0);
			}
		} else if (this instanceof HiddenLayer) {
			int indexOfHiddenLayer = Network.getInstance().getHiddenLayerList().indexOf(this);
			if (Network.getInstance().getHiddenLayerList().size() - 1 > indexOfHiddenLayer) {
				result = Network.getInstance().getHiddenLayerList().get(indexOfHiddenLayer + 1);
			} else {
				result = Network.getInstance().getOutputLayer();
			}
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder logMessage = new StringBuilder(this.getId() + ": [");
		Neuron lastNeuron = this.neuronList.get(this.neuronList.size() - 1);
		for (Neuron neuron : this.neuronList) {
			logMessage.append(String.format(Locale.US, "%.2f", neuron.getActivationValue().get()));
			if (!neuron.equals(lastNeuron)) {
				logMessage.append(", ");
			}
		}
		logMessage.append("]");

		return logMessage.toString();
	}

	public static Layer getbyId(String layerID) {
		Layer result = null;

		ArrayList<Layer> layerList = new ArrayList<Layer>();
		layerList.add(Network.getInstance().getInputLayer());
		layerList.addAll(Network.getInstance().getHiddenLayerList());
		layerList.add(Network.getInstance().getOutputLayer());

		for (Layer layer : layerList) {
			if (layer.getId().equals(layerID)) {
				result = layer;
				break;
			}
		}

		return result;
	}
}
