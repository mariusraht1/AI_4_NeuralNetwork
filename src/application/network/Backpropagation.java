package application.network;

import application.Main;
import application.layer.ConnectableLayer;
import application.layer.HiddenLayer;
import application.neuron.ConnectableNeuron;
import application.neuron.Neuron;

public class Backpropagation {
	private static Backpropagation instance;

	public static Backpropagation getInstance() {
		if (instance == null) {
			instance = new Backpropagation();
		}

		return instance;
	}

	private double learningRate;

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	private Backpropagation() {
	}

	public void init() {
		this.learningRate = Main.DefaultLearningRate;
	}

	// FIX Error is not getting smaller
	public void execute() {
		if (Network.getInstance().getOperationMode().equals(OperationMode.TRAIN)) {
			Network.getInstance().getOutputLayer().calcNewWeights();

			for (int i = Network.getInstance().getHiddenLayerList().size() - 1; i > 0; i--) {
				HiddenLayer hiddenLayer = Network.getInstance().getHiddenLayerList().get(i);
				hiddenLayer.calcErrors();
				hiddenLayer.calcNewWeights();
			}
		}
	}

	// FIX Calculation seems wrong: Error is getting larger
	public void calcNewWeights(ConnectableLayer connectableLayer) {
		for (Neuron neuron : connectableLayer.getNeuronList()) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;

				// Gradient: activationValue * (1 - activationValue) * error * learningRate
				double gradient = connectableLayer.getActivationFunction()
						.gradient(connectableNeuron.getActivationValue());
				gradient *= connectableNeuron.getError() * -Backpropagation.getInstance().getLearningRate();

				// Calculate weight deltas and new weight
				for (Connection inboundConnection : connectableNeuron.getInboundConnections()) {
					Neuron sourceNeuron = inboundConnection.getSourceNeuron();
					double weightDelta = sourceNeuron.getActivationValue() * gradient;
					double newWeight = inboundConnection.getWeight() + weightDelta;
					inboundConnection.setWeight(newWeight);
				}
			}
		}

		// NEW Set new bias
//		double newBias = connectableLayer.getBias() + gradient;
//		connectableLayer.setBias(newBias);
	}
}
