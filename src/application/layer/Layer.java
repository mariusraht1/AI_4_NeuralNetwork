package application.layer;

import java.util.ArrayList;

import application.network.Connection;
import application.network.Network;
import application.network.Neuron;

public class Layer {
	protected int id = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	protected ArrayList<Neuron> neuronList = new ArrayList<Neuron>();

	public ArrayList<Neuron> getNeuronList() {
		return neuronList;
	}

	public void setNeuronList(ArrayList<Neuron> neuronList) {
		this.neuronList = neuronList;
	}

	protected Layer(int id) {
		this.id = id;
	}

	public void connectWith(Layer layer) {
		for (Neuron targetNeuron : this.neuronList) {
			for (Neuron sourceNeuron : layer.getNeuronList()) {
				targetNeuron.getInboundConnectionList().add(new Connection(sourceNeuron));
			}
		}
	}

	public void calcActivationValues() {
		Network.getInstance().getActivationFunction().execute(this);
	}

	public void initializeWeights() {
		Network.getInstance().getActivationFunction().initWeight(this);
	}

	public int getNumOfInboundConnections() {
		int result = 0;

		for (Neuron neuron : this.neuronList) {
			result += neuron.getInboundConnectionList().size();
		}

		return result;
	}
}
