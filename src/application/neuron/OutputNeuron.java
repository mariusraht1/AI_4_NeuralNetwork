package application.neuron;

public class OutputNeuron extends ConnectableNeuron {
	private int representationValue = 0;

	public int getRepresentationValue() {
		return representationValue;
	}

	public void setRepresentationValue(int representationValue) {
		this.representationValue = representationValue;
	}

	public OutputNeuron(int representationValue) {
		super();
		this.representationValue = representationValue;
	}

}
