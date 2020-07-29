package application.layer;

import application.functions.ActivationFunction;
import application.network.Backpropagation;
import application.network.Connection;
import application.network.Network;
import application.neuron.ConnectableNeuron;
import application.neuron.Neuron;

public class ConnectableLayer extends Layer {
	protected ActivationFunction activationFunction = ActivationFunction.Sigmoid;

	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}

	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}

	public ConnectableLayer(String name) {
		super(name);
	}

	public double getTotalError() {
		double totalError = 0.0;

		for (Neuron neuron : this.neuronList) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;
				totalError += Math.sqrt(Math.pow(connectableNeuron.getError(), 2));
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
		for (Neuron neuron : this.neuronList) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;

				double activationValue = 0.0;
				for (Connection inboundConnection : connectableNeuron.getInboundConnections()) {
					Neuron sourceNeuron = inboundConnection.getSourceNeuron();
					activationValue += (sourceNeuron.getActivationValue() * inboundConnection.getWeight());
				}
				activationValue += connectableNeuron.getBias();
				neuron.setActivationValue(activationValue);
			}
		}
	}

	public void calcNewWeights() {
		Backpropagation.getInstance().calcNewWeights(this);
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
