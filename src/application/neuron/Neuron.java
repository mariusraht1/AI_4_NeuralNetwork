package application.neuron;

public abstract class Neuron {
	private double activationValue = 0.0;

	public double getActivationValue() {
		return activationValue;
	}

	public void setActivationValue(double activationValue) {
		this.activationValue = activationValue;
	}

	private double weight = 0.0;

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	protected Neuron() {
	}

	protected Neuron(double activationValue) {
		this.activationValue = activationValue;
	}
}