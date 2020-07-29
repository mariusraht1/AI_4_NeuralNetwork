package application.functions;

import application.layer.ConnectableLayer;
import application.network.Connection;
import application.neuron.ConnectableNeuron;
import application.neuron.Neuron;

public enum ActivationFunction {
	Leaky_ReLu, ReLu, Sigmoid, Tanh;

	public void execute(ConnectableLayer layer) {
		for (Neuron neuron : layer.getNeuronList()) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;

				double activationValue = 0.0;
				for (Connection inboundConnection : connectableNeuron.getInboundConnections()) {
					Neuron sourceNeuron = inboundConnection.getSourceNeuron();
					activationValue += (sourceNeuron.getActivationValue() * inboundConnection.getWeight());
				}
				activationValue += connectableNeuron.getBias();

				switch (this) {
				case Leaky_ReLu:
					activationValue = leaky_relu(activationValue);
					break;
				case ReLu:
					activationValue = relu(activationValue);
					break;
				case Sigmoid:
					activationValue = sigmoid(activationValue);
					break;
				case Tanh:
					activationValue = tanh(activationValue);
					break;
				}

				neuron.setActivationValue(activationValue);
			}
		}
	}

	public void initWeight(ConnectableLayer layer) {
		for (Neuron neuron : layer.getNeuronList()) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;
				for (Connection inboundConnection : connectableNeuron.getInboundConnections()) {
					double weight = 0.0;

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
	}

	public double gradient(double value) {
		double result = 0.0;

		switch (this) {
		case Leaky_ReLu:
		case ReLu:
		case Tanh:
			result = ((value <= 0) ? 0.0 : 1.0);
			break;
		default:
			result = value * (1 - value);
			break;
		}

		return result;
	}

	private double relu(double value) {
		if (value < 0) {
			value = 0.0;
		}

		return value;
	}

	private double sigmoid(double value) {
		return 1 / (1 + Math.exp(-value));
	}

	private double tanh(double value) {
		return Math.tanh(value);
	}

	public double leaky_relu(double value) {
		double a = 0.01;

		if (value < 0) {
			value *= a;
		}

		return value;
	}
}
