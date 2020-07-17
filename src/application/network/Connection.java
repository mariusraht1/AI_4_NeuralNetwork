package application.network;

import application.neuron.Neuron;

public class Connection {
	private int id = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private Neuron sourceNeuron;

	public Neuron getSourceNeuron() {
		return sourceNeuron;
	}

	public void setSourceNeuron(Neuron sourceNeuron) {
		this.sourceNeuron = sourceNeuron;
	}

	private double weight = 0.0;

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public Connection(Neuron sourceNeuron) {
		this.sourceNeuron = sourceNeuron;
	}
}
