package application.network;

import java.util.ArrayList;

public class HiddenNeuron extends Neuron{
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
	
	public HiddenNeuron(int id) {
		super(id);
	}
}
