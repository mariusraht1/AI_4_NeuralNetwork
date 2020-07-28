package application.layer;

import application.functions.ActivationFunction;
import application.network.Connection;
import application.network.Network;
import application.neuron.ConnectableNeuron;
import application.neuron.Neuron;
import application.utilities.MathManager;

public class ConnectableLayer extends Layer {
	protected ActivationFunction activationFunction = ActivationFunction.ReLu;

	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}

	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}

	public ConnectableLayer() {
	}

	public double getTotalError() {
		double totalError = 0.0;

		for (Neuron neuron : this.neuronList) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;
				totalError += connectableNeuron.getError();
			}
		}

		return totalError;
	}

	public void connectWith(Layer prevLayer) {
		for (Neuron neuron : this.neuronList) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;
				for (Neuron sourceNeuron : prevLayer.getNeuronList()) {
					connectableNeuron.getInboundConnections().add(new Connection(sourceNeuron));
				}
			}
		}
		this.activationFunction.initWeight(this);
	}

	public void calcActivationValues() {
		this.activationFunction.execute(this);
	}

	// Gradient: (activationValue * (1 - activationValue)) * error * learningRate
	public void calcGradientActivationValues() {
		for (Neuron neuron : this.neuronList) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;
				double gradient = MathManager.getInstance().getGradient(connectableNeuron.getActivationValue());
				neuron.setActivationValue(
						gradient * connectableNeuron.getError() * Network.getInstance().getLearningRate());
			}
		}
	}

	public void calcNewWeights() {
		for (Neuron neuron : this.neuronList) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;

				// Calculate gradient:
				// activationValue * (1 - actionValue) * error * learningRate
				double gradient = MathManager.getInstance().getGradient(connectableNeuron.getActivationValue())
						* connectableNeuron.getError() * Network.getInstance().getLearningRate();
				double newBias = connectableNeuron.getBias() + gradient;
				connectableNeuron.setBias(newBias);

				// Calculate weight deltas and new weight
				for (Connection inboundConnection : connectableNeuron.getInboundConnections()) {
					Neuron sourceNeuron = inboundConnection.getSourceNeuron();
					double weightDelta = sourceNeuron.getActivationValue() * gradient;
					double newWeight = inboundConnection.getWeight() + weightDelta;
					inboundConnection.setWeight(newWeight);
				}
			}
		}
	}

	public int getNumOfInboundConnections() {
		return getPreviousLayer().getNeuronList().size() * this.neuronList.size();
	}

	public Layer getPreviousLayer() {
		Layer result = null;

		if (this instanceof HiddenLayer) {
			int indexOfHiddenLayer = Network.getInstance().getHiddenLayerList().indexOf(this);
			if (indexOfHiddenLayer == 0) {
				result = Network.getInstance().getInputLayer();
			} else {
				result = Network.getInstance().getHiddenLayerList().get(indexOfHiddenLayer - 1);
			}
		} else if (this.equals(Network.getInstance().getOutputLayer())) {
			int maxIndex = Network.getInstance().getHiddenLayerList().size() - 1;
			result = Network.getInstance().getHiddenLayerList().get(maxIndex);
		}

		return result;
	}
}
