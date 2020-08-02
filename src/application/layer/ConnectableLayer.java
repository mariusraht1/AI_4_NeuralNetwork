package application.layer;

import application.network.Connection;
import application.network.Network;
import application.neuron.ConnectableNeuron;
import application.neuron.Neuron;
import library.MathManager;

public class ConnectableLayer extends Layer {
	private double bias = 0.0;

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

	public ConnectableLayer(String name) {
		super(name);
		this.bias = MathManager.getInstance().getRandom(-1.0, 1.0);
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
					String connectionID = this.getId() + "_" + connectableNeuron.getId() + sourceNeuron.getId();
					connectableNeuron.getInboundConnections().add(new Connection(connectionID, sourceNeuron));

				}
			}
		}
		Network.getInstance().getActivationFunction().initWeight(this);
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
				activationValue += this.bias;
				neuron.setActivationValue(activationValue);
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
			if (Network.getInstance().getHiddenLayerList().size() > 0) {
				int maxIndex = Network.getInstance().getHiddenLayerList().size() - 1;
				result = Network.getInstance().getHiddenLayerList().get(maxIndex);
			} else {
				result = Network.getInstance().getInputLayer();
			}
		}

		return result;
	}
}
