package application.network;

public class OutputNeuron extends Neuron {
	private int representationValue = 0;

	public int getRepresentationValue() {
		return representationValue;
	}

	public void setRepresentationValue(int representationValue) {
		this.representationValue = representationValue;
	}
	
	private double bias = 0.0;

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

	private double probability = 0.0;

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	public OutputNeuron(int id) {
		super(id);
	}

}
