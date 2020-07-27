package application.layer;

import java.util.ArrayList;

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
}
