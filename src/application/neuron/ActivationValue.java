package application.neuron;

import application.functions.ActivationFunction;

public class ActivationValue {
	private double value = 0.0;

	public double get() {
		return value;
	}

	public void set(double value) {
		this.value = value;
	}

	public ActivationValue(double value) {
		this.value = value;
	}

	public double getDerivative(ActivationFunction activationFunction) {
		return activationFunction.getDerivative(value);
	}
}
