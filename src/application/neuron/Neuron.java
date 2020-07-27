package application.neuron;

import java.util.ArrayList;

import application.network.Connection;

public abstract class Neuron {
	private double activationValue = 0.0;

	public double getActivationValue() {
		return activationValue;
	}

	public void setActivationValue(double activationValue) {
		this.activationValue = activationValue;
	}

	protected Neuron() {
	}

	protected Neuron(double activationValue) {
		this.activationValue = activationValue;
	}

	public double sumInboundValues(ArrayList<Connection> inboundConnectionList) {
		double sum = 0.0;

		for (Connection connection : inboundConnectionList) {
			sum += (connection.getWeight() * connection.getSourceNeuron().getActivationValue());
		}

		return sum;
	}


}