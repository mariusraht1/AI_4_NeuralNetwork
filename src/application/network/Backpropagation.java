package application.network;

import application.Main;
import application.layer.ConnectableLayer;
import application.layer.HiddenLayer;
import application.layer.OutputLayer;
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

	public void execute() {
		if (Network.getInstance().getOperationMode().equals(OperationMode.TRAIN)) {
			calcNewWeights(Network.getInstance().getOutputLayer());

			for (int i = Network.getInstance().getHiddenLayerList().size() - 1; i > 0; i--) {
				HiddenLayer hiddenLayer = Network.getInstance().getHiddenLayerList().get(i);
				hiddenLayer.calcErrors();
				calcNewWeights(hiddenLayer);
			}
		}
	}

	// FIX Calculation seems wrong: Predictions are unsure or wrong
	public void calcNewWeights(ConnectableLayer connectableLayer) {
		if (connectableLayer instanceof HiddenLayer) {
			double totalErrorWithRespectToOutput = 0.0;
			for (Neuron outputNeuron : connectableLayer.getNextLayer().getNeuronList()) {
				if (outputNeuron instanceof ConnectableNeuron) {
					ConnectableNeuron connectableOutputNeuron = (ConnectableNeuron) outputNeuron;
					// How much does the total error change with respect to the output?
					double errorWithRespectToOutput = connectableOutputNeuron.getError();

					// How much does the output of o_n change with respect to its total net input?
					double outputWithRespectToInput = connectableOutputNeuron.getActivationValue()
							* (1 - connectableOutputNeuron.getActivationValue());

					// How much does the total error change with respect to the total net input?
					double errorWithRespectToInput = errorWithRespectToOutput * outputWithRespectToInput;

					for (Connection inboundConnection : connectableOutputNeuron.getInboundConnections()) {
						totalErrorWithRespectToOutput += errorWithRespectToInput * inboundConnection.getWeight();
					}
				}
			}

			for (Neuron hiddenNeuron : connectableLayer.getNeuronList()) {
				if (hiddenNeuron instanceof ConnectableNeuron) {
					ConnectableNeuron connectableHiddenNeuron = (ConnectableNeuron) hiddenNeuron;

					double outputWithRespectToInput = connectableHiddenNeuron.getActivationValue()
							* (1 - connectableHiddenNeuron.getActivationValue());

					for (Connection inboundConnection : connectableHiddenNeuron.getInboundConnections()) {
						double inputWithRespectToWeight = inboundConnection.getSourceNeuron().getActivationValue();
						double gradient = totalErrorWithRespectToOutput * outputWithRespectToInput
								* inputWithRespectToWeight;
						double newWeight = inboundConnection.getWeight()
								- (Backpropagation.getInstance().getLearningRate() * gradient);
						inboundConnection.setWeight(newWeight);
					}
				}
			}
		} else if (connectableLayer instanceof OutputLayer) {
			for (Neuron outputNeuron : connectableLayer.getNeuronList()) {
				if (outputNeuron instanceof ConnectableNeuron) {
					ConnectableNeuron connectableOutputNeuron = (ConnectableNeuron) outputNeuron;

					// How much does the total error change with respect to the output?
					double errorWithRespectToOutput = connectableOutputNeuron.getError();

					// How much does the output of o_n change with respect to its total net input?
					double outputWithRespectToInput = connectableOutputNeuron.getActivationValue()
							* (1 - connectableOutputNeuron.getActivationValue());

					for (Connection inboundConnection : connectableOutputNeuron.getInboundConnections()) {
						// How much does the total net input of o_n change with respect to w_n?
						double inputWithRespectToWeight = inboundConnection.getSourceNeuron().getActivationValue();
						double gradient = errorWithRespectToOutput * outputWithRespectToInput
								* inputWithRespectToWeight;
						double newWeight = inboundConnection.getWeight()
								- (Backpropagation.getInstance().getLearningRate() * gradient);
						inboundConnection.setWeight(newWeight);
					}

//				// Gradient: activationValue * (1 - activationValue) * error * learningRate
//				double gradient = connectableLayer.getActivationFunction()
//						.gradient(connectableNeuron.getActivationValue());
//				gradient *= connectableNeuron.getError() * -Backpropagation.getInstance().getLearningRate();
//
//				// Calculate weight deltas and new weight
//				for (Connection inboundConnection : connectableNeuron.getInboundConnections()) {
//					Neuron sourceNeuron = inboundConnection.getSourceNeuron();
//					double weightDelta = sourceNeuron.getActivationValue() * gradient;
//					double newWeight = inboundConnection.getWeight() + weightDelta;
//					inboundConnection.setWeight(newWeight);
//				}
				}
			}
		}

		// NEW Set new bias
//		double newBias = connectableLayer.getBias() + gradient;
//		connectableLayer.setBias(newBias);
	}
}
