package application.layer;

import application.functions.ActivationFunction;
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
	
	private Layer previousLayer;

	public Layer getPreviousLayer() {
		return previousLayer;
	}

	public void setPreviousLayer(Layer previousLayer) {
		this.previousLayer = previousLayer;
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

	public void calcErrors() {
		for (Neuron neuron : this.neuronList) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;
				double error = connectableNeuron.getTargetValue() - connectableNeuron.getActivationValue();
				error = Math.sqrt(Math.pow(error, 2));
				connectableNeuron.setError(error);
			}
		}
	}

	public void initWeights() {
		this.activationFunction.initWeight(this);
	}

	public void initBiases() {
		for (Neuron neuron : this.neuronList) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;
				connectableNeuron.setBias(MathManager.getInstance().getRandom(-1.0, 1.0));
			}
		}
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
				for (Neuron sourceNeuron : this.previousLayer.getNeuronList()) {
					double weightDelta = sourceNeuron.getActivationValue() * gradient;
					double newWeight = sourceNeuron.getWeight() + weightDelta;
					sourceNeuron.setWeight(newWeight);
				}
			}
		}
	}
}
