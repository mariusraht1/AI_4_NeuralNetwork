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

	private double learningRate = Main.DefaultLearningRate;

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	private Backpropagation() {
	}

	public void execute() {
		if (Network.getInstance().getOperationMode().equals(OperationMode.TRAIN)) {
			calcNewWeights(Network.getInstance().getOutputLayer());

			for (int i = Network.getInstance().getHiddenLayerList().size() - 1; i >= 0; i--) {
				HiddenLayer hiddenLayer = Network.getInstance().getHiddenLayerList().get(i);
				calcNewWeights(hiddenLayer);
			}
		}
	}

	public double calcTotalError(ConnectableLayer connectableLayer) {
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

		return totalErrorWithRespectToOutput;
	}

	public void calcNewWeights(ConnectableLayer connectableLayer) {
		double totalErrorWithRespectToOutput = 0.0;
		if (connectableLayer instanceof HiddenLayer) {
			totalErrorWithRespectToOutput = calcTotalError(connectableLayer);
		}

		for (Neuron neuron : connectableLayer.getNeuronList()) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableOutputNeuron = (ConnectableNeuron) neuron;

				// How much does the total error change with respect to the output?
				double errorWithRespectToOutput = 0.0;
				if (connectableLayer instanceof OutputLayer) {
					errorWithRespectToOutput = connectableOutputNeuron.getError();
				} else {
					errorWithRespectToOutput = totalErrorWithRespectToOutput;
				}

				// How much does the output output change with respect to the output input?
				double outputWithRespectToInput = connectableLayer
						.getDerivativeActivationValue(connectableOutputNeuron);

				for (Connection inboundConnection : connectableOutputNeuron.getInboundConnections()) {
					// How much does the input of o_n change with respect to w_n?
					double inputWithRespectToWeight = inboundConnection.getSourceNeuron().getActivationValue();
					double gradient = errorWithRespectToOutput * outputWithRespectToInput * inputWithRespectToWeight
							* Backpropagation.getInstance().getLearningRate();
					double newWeight = inboundConnection.getWeight() - gradient;
					inboundConnection.setWeight(newWeight);
				}

				// How much does the output input change with respect to the bias?
				double inputWithRespectToBias = connectableOutputNeuron.getActivationValue();
				double gradient = errorWithRespectToOutput * outputWithRespectToInput * inputWithRespectToBias
						* Backpropagation.getInstance().getLearningRate();
				double newBias = connectableLayer.getBias() - gradient;
				connectableLayer.setBias(newBias);
			}
		}
	}
}
