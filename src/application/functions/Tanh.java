package application.functions;

import library.MathManager;

public class Tanh {
	private static Tanh instance;

	public static Tanh getInstance() {
		if (instance == null) {
			instance = new Tanh();
		}

		return instance;
	}

	public double getActivationValue(double value) {
		return Math.tanh(value);
	}

	public double getInitializedWeight(double min, double max) {
		return MathManager.getInstance().getRandom(min, max);
	}

	public double getDerivative(double value) {
		value = Math.tanh(value);
		return (1 / value) - value;
	}
}
