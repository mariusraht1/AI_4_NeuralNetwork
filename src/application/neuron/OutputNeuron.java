package application.neuron;

public class OutputNeuron extends ConnectableNeuron {
	private int representationValue = 0;

	public int getRepresentationValue() {
		return representationValue;
	}

	public void setRepresentationValue(int representationValue) {
		this.representationValue = representationValue;
	}

	private double probability = 0.0;

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	public OutputNeuron(int representationValue) {
		super();
		this.representationValue = representationValue;
	}

}
