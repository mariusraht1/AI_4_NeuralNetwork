package application.layer;

import java.util.ArrayList;

import application.network.Network;
import application.neuron.Neuron;

public abstract class Layer {
	protected ArrayList<Neuron> neuronList = new ArrayList<Neuron>();

	public ArrayList<Neuron> getNeuronList() {
		return neuronList;
	}

	public void setNeuronList(ArrayList<Neuron> neuronList) {
		this.neuronList = neuronList;
	}

	protected Layer() {
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
}
