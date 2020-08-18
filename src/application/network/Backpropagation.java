package application.network;

import application.Main;
import application.functions.ActivationFunction;
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

	public void calcError(ConnectableLayer connectableLayer) {
		ActivationFunction activationFunction = Network.getInstance().getActivationFunction();

		for (Neuron hiddenNeuron : connectableLayer.getNeuronList()) {
			if (hiddenNeuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableHiddenNeuron = (ConnectableNeuron) hiddenNeuron;

				// How much does the total error change with respect to the hidden output?
				double totalErrorWithRespectToHiddenOutput = 0.0;
				for (Neuron outputNeuron : connectableLayer.getNextLayer().getNeuronList()) {
					if (outputNeuron instanceof ConnectableNeuron) {
						ConnectableNeuron connectableOutputNeuron = (ConnectableNeuron) outputNeuron;

						// How much does the output error change with respect to the output output?
						double outputErrorWithRespectToOutputOutput = connectableOutputNeuron.getError();

						// How much does the output output change with respect to the output input?
						double outputOutputWithRespectToOutputInput = connectableOutputNeuron.getActivationValue()
								.getDerivative(activationFunction);

						// How much does the output error change with respect to the output input?
						double outputErrorWithRespectToOutputInput = outputErrorWithRespectToOutputOutput
								* outputOutputWithRespectToOutputInput;

						// How much does the output input change with respect to the hidden output?
						Connection connection = connectableOutputNeuron
								.getInboundConnectionBySourceNeuron(connectableHiddenNeuron);
						double outputInputWithRespectToHiddenOutput = connection.getWeight();

						// How much does the output error change with respect to the hidden output?
						double outputErrorWithRespectToHiddenOutput = outputErrorWithRespectToOutputInput
								* outputInputWithRespectToHiddenOutput;

						totalErrorWithRespectToHiddenOutput += outputErrorWithRespectToHiddenOutput;
					}
				}

				connectableHiddenNeuron.setError(totalErrorWithRespectToHiddenOutput);
			}
		}
	}

	public void calcNewWeights(ConnectableLayer connectableLayer) {
		if (connectableLayer instanceof HiddenLayer) {
			calcError(connectableLayer);
		}

		ActivationFunction activationFunction = Network.getInstance().getActivationFunction();
		for (Neuron neuron : connectableLayer.getNeuronList()) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;

				// How much does the total error change with respect to the output?
				double totalErrorWithRespectToOutput = connectableNeuron.getError();

				// How much does the output output change with respect to the output input?
				double outputWithRespectToInput = connectableNeuron.getActivationValue()
						.getDerivative(activationFunction);

				for (Connection inboundConnection : connectableNeuron.getInboundConnections()) {
					// How much does the input of o_n change with respect to w_n?
					double inputWithRespectToWeight = inboundConnection.getSourceNeuron().getActivationValue().get();
					double gradient = totalErrorWithRespectToOutput * outputWithRespectToInput
							* inputWithRespectToWeight * Backpropagation.getInstance().getLearningRate();
					double newWeight = inboundConnection.getWeight() - gradient;
					inboundConnection.setWeight(newWeight);
				}

				// How much does the output input change with respect to the bias?
				double inputWithRespectToBias = connectableNeuron.getActivationValue().get();
				double gradient = totalErrorWithRespectToOutput * outputWithRespectToInput * inputWithRespectToBias
						* Backpropagation.getInstance().getLearningRate();
				double newBias = connectableLayer.getBias() - gradient;
				connectableLayer.setBias(newBias);
			}
		}
	}
}
