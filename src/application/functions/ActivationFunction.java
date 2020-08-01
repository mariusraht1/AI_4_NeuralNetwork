package application.functions;

import application.layer.ConnectableLayer;
import application.layer.Layer;
import application.layer.OutputLayer;
import application.network.Connection;
import application.neuron.ConnectableNeuron;
import application.neuron.Neuron;

public enum ActivationFunction {
	LEAKY_RELU, RELU, SIGMOID, SOFTMAX, TANH;

	public void execute(Layer layer) {
		if (this.equals(SOFTMAX) && layer instanceof OutputLayer) {
			Softmax.getInstance().setActivationValue(layer);
		} else if (!this.equals(SOFTMAX)) {
			executeInLoop(layer);
		}
	}

	public void initWeight(ConnectableLayer layer) {
		for (Neuron neuron : layer.getNeuronList()) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;
				for (Connection inboundConnection : connectableNeuron.getInboundConnections()) {
					double weight = 0.0;

					switch (this) {
					case LEAKY_RELU:
						weight = WeightInitialisation.LEAKY_RELU.execute(layer);
						break;
					case RELU:
						weight = WeightInitialisation.RELU.execute(layer);
						break;
					case SIGMOID:
						weight = WeightInitialisation.SIGMOID.execute(layer);
						break;
					case TANH:
						weight = WeightInitialisation.TANH.execute(layer);
						break;
					default:
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
		case LEAKY_RELU:
		case RELU:
		case TANH:
			result = ((value <= 0) ? 0.0 : 1.0);
			break;
		default:
			result = value * (1 - value);
			break;
		}

		return result;
	}

	private void executeInLoop(Layer layer) {
		for (Neuron neuron : layer.getNeuronList()) {
			double activationValue = neuron.getActivationValue();

			switch (this) {
			case LEAKY_RELU:
				activationValue = Leaky_Relu.getInstance().getActivationValue(activationValue);
				break;
			case RELU:
				activationValue = Relu.getInstance().getActivationValue(activationValue);
				break;
			case SIGMOID:
				activationValue = Sigmoid.getInstance().getActivationValue(activationValue);
				break;
			case TANH:
				activationValue = Tanh.getInstance().getActivationValue(activationValue);
				break;
			default:
				break;
			}

			neuron.setActivationValue(activationValue);
		}
	}
}
