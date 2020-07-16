package application.layer;

import java.util.ArrayList;

import application.activation.ActivationFunction;
import application.network.Connection;
import application.network.Neuron;

public abstract class Layer {	
	protected ActivationFunction activationFunction = ActivationFunction.ReLu;
	
	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}

	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}

	protected ArrayList<Neuron> neuronList = new ArrayList<Neuron>();

	public ArrayList<Neuron> getNeuronList() {
		return neuronList;
	}

	public void setNeuronList(ArrayList<Neuron> neuronList) {
		this.neuronList = neuronList;
	}
	
	protected Layer() {
	}
	
	public void connectWith(Layer layer) {
		for (Neuron targetNeuron : this.neuronList) {
			for (Neuron sourceNeuron : layer.getNeuronList()) {
				targetNeuron.getInboundConnectionList().add(new Connection(sourceNeuron));
			}
		}
	}
	
	public int getNumOfInboundConnections() {
		int result = 0;

		for (Neuron neuron : this.neuronList) {
			result += neuron.getInboundConnectionList().size();
		}

		return result;
	}
	
	public void calculateActivationValues() {
		this.activationFunction.execute(this);
	}
	
	public void initializeWeights() {
		this.activationFunction.initWeight(this);
	}
}
