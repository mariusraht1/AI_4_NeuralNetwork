package application.functions;

import application.layer.Layer;
import application.neuron.Neuron;
import application.neuron.OutputNeuron;
import library.MathManager;

public class Sigmoid {
	private static Sigmoid instance;

	public static Sigmoid getInstance() {
		if (instance == null) {
			instance = new Sigmoid();
		}

		return instance;
	}

	public double getActivationValue(double value) {
		value = 1.0 / (1.0 + Math.exp(-value));
		return value;
	}

	public double getInitializedWeight(double min, double max) {
		min *= 4.0;
		max *= 4.0;

		return MathManager.getInstance().getRandom(min, max);
	}

	public double getDerivative(double value) {
		value = value * (1.0 - value);
		return value;
	}

	public void setActivationValue(Layer layer) {
		for (Neuron neuron : layer.getNeuronList()) {
			if (neuron instanceof OutputNeuron) {
				OutputNeuron outputNeuron = (OutputNeuron) neuron;
				outputNeuron.setActivationValue(getActivationValue(outputNeuron.getActivationValue()));
			}
		}
	}
}
