package application.network;

import java.util.ArrayList;

import application.utilities.MathManager;

public class Neuron {
	private int id = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private double activationValue = 0.0;

	public double getActivationValue() {
		return activationValue;
	}

	public void setActivationValue(double activationValue) {
		this.activationValue = activationValue;
	}

	private double bias = 0.0;

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

	private ArrayList<Connection> inboundConnectionList = new ArrayList<Connection>();

	public ArrayList<Connection> getInboundConnectionList() {
		return inboundConnectionList;
	}

	public void setInboundConnectionList(ArrayList<Connection> inboundConnectionList) {
		this.inboundConnectionList = inboundConnectionList;
	}

	public Neuron(int id) {
		this.id = id;
	}

	public Neuron(int id, double activationValue) {
		this.id = id;
		this.activationValue = activationValue;
	}

	public void activate() {
		double activationValue = sumInboundValues() + bias;
		this.activationValue = MathManager.getInstance().sigmoid(activationValue);
	}

	public double sumInboundValues() {
		double sum = 0.0;

		for (Connection connection : inboundConnectionList) {
			sum += (connection.getWeight() * connection.getSourceNeuron().getActivationValue());
		}

		return sum;
	}
}