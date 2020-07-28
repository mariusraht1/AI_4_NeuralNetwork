package application.neuron;

import java.util.ArrayList;

import application.network.Connection;
import application.utilities.MathManager;

public class ConnectableNeuron extends Neuron {
	private double bias = 0.0;

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

	private double targetValue = 0.0;

	public double getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(double targetValue) {
		this.targetValue = targetValue;
	}

	private double error = 0.0;

	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}

	private ArrayList<Connection> inboundConnections = new ArrayList<Connection>();

	public ArrayList<Connection> getInboundConnections() {
		return inboundConnections;
	}

	public void setInboundConnections(ArrayList<Connection> inboundConnections) {
		this.inboundConnections = inboundConnections;
	}

	public ConnectableNeuron() {
		super();
		this.bias = MathManager.getInstance().getRandom(-1.0, 1.0);
	}

	public Connection getInboundConnectionBySourceNeuron(Neuron sourceNeuron) {
		Connection result = null;

		for (Connection inboundConnection : this.inboundConnections) {
			if (inboundConnection.getSourceNeuron().equals(sourceNeuron)) {
				result = inboundConnection;
				break;
			}
		}

		return result;
	}
}
