package application.activation;

import application.layer.Layer;
import application.network.Network;
import application.utilities.MathManager;

public enum WeightInitialisation {
	Leaky_ReLu, ReLu, Sigmoid, Tanh;

	public double execute(Layer layer) {
		double weight = 0.0;
		int numOfInboundNeurons = layer.getNumOfInboundConnections();
		int numOfOutboundNeurons = layer.getNeuronList().size();

		switch (this) {
		case Leaky_ReLu:
		case ReLu:
			weight = relu(numOfInboundNeurons, numOfOutboundNeurons);
			break;
		case Sigmoid:
			weight = sigmoid(numOfInboundNeurons, numOfOutboundNeurons);
			break;
		case Tanh:
			weight = tanh(numOfInboundNeurons, numOfOutboundNeurons);
			break;
		}

		return weight;
	}

	private double relu(int numOfInboundNeurons, int numOfOutboundNeurons) {
		final double min = 0.0;
		double max = 0.0;

		switch (Network.getInstance().getDistribution()) {
		case NORMAL:
			max = Math.sqrt(2) * Math.sqrt(2 / (numOfInboundNeurons + numOfOutboundNeurons));
			break;
		case UNIFORM:
			max = Math.sqrt(2) * Math.sqrt(6 / (numOfInboundNeurons + numOfOutboundNeurons));
			break;
		}

		return MathManager.getInstance().getRandom(min, max);
	}

	private double sigmoid(int numOfInboundNeurons, int numOfOutboundNeurons) {
		final double min = 0.0;
		double max = 0.0;

		switch (Network.getInstance().getDistribution()) {
		case NORMAL:
			max = 4 * Math.sqrt(2 / (numOfInboundNeurons + numOfOutboundNeurons));
			break;
		case UNIFORM:
			max = 4 * Math.sqrt(6 / (numOfInboundNeurons + numOfOutboundNeurons));
			break;
		}

		return MathManager.getInstance().getRandom(min, max);
	}

	private double tanh(int numOfInboundNeurons, int numOfOutboundNeurons) {
		final double min = -1.0;
		double max = 0.0;

		switch (Network.getInstance().getDistribution()) {
		case NORMAL:
			max = Math.sqrt(2 / (numOfInboundNeurons + numOfOutboundNeurons));
			break;
		case UNIFORM:
			max = Math.sqrt(6 / (numOfInboundNeurons + numOfOutboundNeurons));
			break;
		}

		return MathManager.getInstance().getRandom(min, max);
	}
}
