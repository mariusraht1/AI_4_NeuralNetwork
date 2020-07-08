package application.network;

import java.util.ArrayList;

public class Neuron {
	private int id = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	private double value = 0.0;	

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
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
	
	public Neuron(int id, ArrayList<Connection> connectionList) {
		this.id = id;
		this.inboundConnectionList = connectionList;
	}
	
	public double sumInboundValues() {
		double sum = 0.0;
		
		for(Connection connection : inboundConnectionList) {
			sum += (connection.getWeight() * connection.getSourceNeuron().getValue());
		}
		
		return sum;
	}
}