package application.functions;

import library.MathManager;

public class Relu {
	private static Relu instance;

	public static Relu getInstance() {
		if (instance == null) {
			instance = new Relu();
		}

		return instance;
	}

	public double getActivationValue(double value) {
		return Math.max(0, value);
	}

	public double getInitializedWeight(double min, double max) {
		min *= Math.sqrt(2);
		max *= Math.sqrt(2);

		return MathManager.getInstance().getRandom(min, max);
	}

	public double getDerivative(double value) {
		if (value < 0) {
			value = 0.0;
		} else {
			value = 1.0;
		}

		return value;
	}
}
