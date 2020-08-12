package application.functions;

import application.layer.Layer;
import application.neuron.Neuron;
import application.neuron.OutputNeuron;

public class Softmax {
	private static Softmax instance;

	public static Softmax getInstance() {
		if (instance == null) {
			instance = new Softmax();
		}

		return instance;
	}

	public void setActivationValue(Layer layer) {
		double total = 0.0;
		for (Neuron neuron : layer.getNeuronList()) {
			total += Math.exp(neuron.getActivationValue());
		}

		for (Neuron neuron : layer.getNeuronList()) {
			if (neuron instanceof OutputNeuron) {
				OutputNeuron outputNeuron = (OutputNeuron) neuron;
				outputNeuron.setActivationValue(Math.exp(neuron.getActivationValue()) / total);
			}
		}
	}

	public double getDerivative(double value) {
		value = value * (1.0 - value);
		return value;
	}
}
