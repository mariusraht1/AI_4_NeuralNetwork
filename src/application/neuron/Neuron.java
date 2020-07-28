package application.neuron;

public abstract class Neuron {
	private double activationValue = 0.0;

	public double getActivationValue() {
		return activationValue;
	}

	public void setActivationValue(double activationValue) {
		this.activationValue = activationValue;
	}

	protected Neuron() {
	}

	protected Neuron(double activationValue) {
		this.activationValue = activationValue;
	}
}