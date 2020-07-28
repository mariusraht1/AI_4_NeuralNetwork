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
					double value = sourceNeuron.getActivationValue();

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

					activationValue += inboundConnection.getWeight() * sourceNeuron.getActivationValue();
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
