package application.network;

import java.util.ArrayList;

public class Layer {
	private int id = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private ArrayList<Neuron> neuronList = new ArrayList<Neuron>();

	public ArrayList<Neuron> getNeuronList() {
		return neuronList;
	}

	public void setNeuronList(ArrayList<Neuron> neuronList) {
		this.neuronList = neuronList;
	}

	public Layer(int id) {
		this.id = id;
	}

	public void connectWith(Layer layer) {
		for (Neuron targetNeuron : this.neuronList) {
			for (Neuron sourceNeuron : layer.getNeuronList()) {
				targetNeuron.getInboundConnectionList().add(new Connection(sourceNeuron));
			}
		}
	}
}
