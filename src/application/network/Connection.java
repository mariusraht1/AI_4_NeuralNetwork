package application.network;

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
	
	public Connection(Neuron sourceNeuron) {
		this.sourceNeuron = sourceNeuron;
	}
}
