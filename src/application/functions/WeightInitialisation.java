package application.functions;

import application.layer.ConnectableLayer;
import application.network.Network;

public enum WeightInitialisation {
	LEAKY_RELU, RELU, SIGMOID, TANH;

	// Xavier initialization
	public double execute(ConnectableLayer layer) {
		double weight = 0.0;
		double min = 0.0;
		double max = 0.0;
		int numOfInboundNeurons = layer.getPreviousLayer().getNeuronList().size();
		int numOfOutboundNeurons = layer.getNeuronList().size();

		switch (Network.getInstance().getDistribution()) {
		case NORMAL:
			max = Math.sqrt(2.0 / (numOfInboundNeurons + numOfOutboundNeurons));
			min = 0.0;
			break;
		case UNIFORM:
			max = Math.sqrt(6.0 / (numOfInboundNeurons + numOfOutboundNeurons));
			min -= max;
			break;
		}

		switch (this) {
		case LEAKY_RELU:
			weight = Leaky_Relu.getInstance().getInitializedWeight(min, max);
			break;
		case RELU:
			weight = Relu.getInstance().getInitializedWeight(min, max);
			break;
		case SIGMOID:
			weight = Sigmoid.getInstance().getInitializedWeight(min, max);
			break;
		case TANH:
			weight = Tanh.getInstance().getInitializedWeight(min, max);
			break;
		}

		return weight;
	}
}
