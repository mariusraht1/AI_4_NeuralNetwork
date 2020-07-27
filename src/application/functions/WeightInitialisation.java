package application.functions;

import application.layer.ConnectableLayer;
import application.network.Network;
import application.utilities.MathManager;

public enum WeightInitialisation {
	Leaky_ReLu, ReLu, Sigmoid, Tanh;

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
		case Leaky_ReLu:
		case ReLu:
			weight = relu(min, max);
			break;
		case Sigmoid:
			weight = sigmoid(min, max);
			break;
		case Tanh:
			weight = tanh(min, max);
			break;
		}

		return weight;
	}

	private double relu(double min, double max) {
		min *= Math.sqrt(2);
		max *= Math.sqrt(2);

		return MathManager.getInstance().getRandom(min, max);
	}

	private double sigmoid(double min, double max) {
		min *= 4;
		max *= 4;

		return MathManager.getInstance().getRandom(min, max);
	}

	private double tanh(double min, double max) {
		return MathManager.getInstance().getRandom(min, max);
	}
}
