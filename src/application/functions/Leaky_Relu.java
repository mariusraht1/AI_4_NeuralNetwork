package application.functions;

import library.MathManager;

public class Leaky_Relu {
	private static Leaky_Relu instance;

	public static Leaky_Relu getInstance() {
		if (instance == null) {
			instance = new Leaky_Relu();
		}

		return instance;
	}

	private final double slope = 0.1;

	public double getActivationValue(double value) {
		if (value < 0) {
			value *= this.slope;
		}

		return value;
	}

	public double getInitializedWeight(double min, double max) {
		min *= Math.sqrt(2);
		max *= Math.sqrt(2);

		return MathManager.getInstance().getRandom(min, max);
	}

	public double getDerivative(double value) {
		if (value < 0) {
			value = this.slope;
		} else {
			value = 1.0;
		}

		return value;
	}
}
