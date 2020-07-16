package application.network;

import java.util.ArrayList;

public abstract class Neuron {
	private double activationValue = 0.0;

	public double getActivationValue() {
		return activationValue;
	}

	public void setActivationValue(double activationValue) {
		this.activationValue = activationValue;
	}
	
	private ArrayList<Connection> inboundConnectionList = new ArrayList<Connection>();

	public ArrayList<Connection> getInboundConnectionList() {
		return inboundConnectionList;
	}

	public void setInboundConnectionList(ArrayList<Connection> inboundConnectionList) {
		this.inboundConnectionList = inboundConnectionList;
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