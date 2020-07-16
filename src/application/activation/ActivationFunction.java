package application.activation;

import application.layer.Layer;
import application.network.Connection;
import application.network.Neuron;

public enum ActivationFunction {
	Leaky_ReLu, ReLu, Sigmoid, Tanh;

	public void execute(Layer layer) {
		for (Neuron neuron : layer.getNeuronList()) {
			double activationValue = 0.0;

			for (Connection inboundConnection : neuron.getInboundConnectionList()) {
				double value = inboundConnection.getSourceNeuron().getActivationValue();

				switch (this) {
				case Leaky_ReLu:
					leaky_relu(value);
					break;
				case ReLu:
					relu(value);
					break;
				case Sigmoid:
					sigmoid(value);
					break;
				case Tanh:
					tanh(value);
					break;
				}

				activationValue += inboundConnection.getWeight()
						* inboundConnection.getSourceNeuron().getActivationValue();
			}

			neuron.setActivationValue(activationValue);
		}
	}

	public void initWeight(Layer layer) {
		for (Neuron neuron : layer.getNeuronList()) {
			double weight = 0.0;

			for (Connection inboundConnection : neuron.getInboundConnectionList()) {
				switch (this) {
				case Leaky_ReLu:
					weight = WeightInitialisation.Leaky_ReLu.execute(layer);
					break;
				case ReLu:
					weight = WeightInitialisation.ReLu.execute(layer);
					break;
				case Sigmoid:
					weight = WeightInitialisation.Sigmoid.execute(layer);
					break;
				case Tanh:
					weight = WeightInitialisation.Tanh.execute(layer);
					break;
				}

				inboundConnection.setWeight(weight);
			}
		}

	}

	private double relu(double value) {
		double result = value;

		if (result < 0) {
			result = 0.0;
		}

		return result;
	}

	private double sigmoid(double value) {
		return 1 / (1 + Math.exp(-value));
	}

	private double tanh(double value) {
		return Math.tanh(value);
	}

	public double leaky_relu(double value) {
		double a = 0.01;
		double result = value;

		if (result < 0) {
			result *= a;
		}

		return result;
	}
}
