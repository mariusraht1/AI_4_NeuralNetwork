package application.layer;

import application.activation.ActivationFunction;
import application.network.Connection;
import application.neuron.ConnectableNeuron;
import application.neuron.Neuron;

public class ConnectableLayer extends Layer {
	protected ActivationFunction activationFunction = ActivationFunction.ReLu;

	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}

	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
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

	public void setError() {
		for (Neuron neuron : this.neuronList) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;
				double error = connectableNeuron.getTargetValue() - connectableNeuron.getActivationValue();
				error = Math.sqrt(Math.pow(error, 2));
				connectableNeuron.setError(error);
			}
		}
	}

	public void connectWith(Layer layer) {
		for (Neuron targetNeuron : this.neuronList) {
			if (targetNeuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableTargetNeuron = (ConnectableNeuron) targetNeuron;
				for (Neuron sourceNeuron : layer.getNeuronList()) {
					connectableTargetNeuron.getInboundConnectionList().add(new Connection(sourceNeuron));
				}
			}
		}
	}

	public int getNumOfInboundNeurons() {
		int result = 0;

		for (Neuron neuron : this.neuronList) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;
				result += connectableNeuron.getInboundConnectionList().size();
			}
		}

		return result;
	}

	public void initWeights() {
		this.activationFunction.initWeight(this);
	}

	public void calcActivationValues() {
		this.activationFunction.execute(this);
	}
}
