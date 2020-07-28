package application.layer;

import java.util.ArrayList;

import application.network.Network;
import application.neuron.InputNeuron;
import application.neuron.Neuron;

public class InputLayer extends Layer {
	public InputLayer() {
		super();
		this.neuronList = new ArrayList<Neuron>();
	}

	public void setActivationValues(double[] values) {
		for (int i = 0; i < this.neuronList.size(); i++) {
			Neuron neuron = this.neuronList.get(i);
			neuron.setActivationValue(values[i]);
		}
	}

	public static void generate() {
		InputLayer inputLayer = new InputLayer();
		int numOfNeurons = Network.getInstance().getDataInputType().getNumOfInputNeurons();
		for (int i = 0; i < numOfNeurons; i++) {
			inputLayer.getNeuronList().add(new InputNeuron());
		}
		Network.getInstance().setInputLayer(inputLayer);
	}
}
