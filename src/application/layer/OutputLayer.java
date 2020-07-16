package application.layer;

import application.network.Digit;
import application.network.Neuron;
import application.network.OutputNeuron;

public class OutputLayer extends Layer {
	public OutputLayer() {
		super();
	}

	public OutputNeuron getMostActiveNeuron() {
		OutputNeuron mostActiveNeuron = (OutputNeuron) this.neuronList.get(0);
		for (int i = 1; i < this.neuronList.size(); i++) {
			if (this.neuronList.get(i).getActivationValue() > mostActiveNeuron.getActivationValue()) {
				mostActiveNeuron = (OutputNeuron) this.neuronList.get(i);
			}
		}

		return mostActiveNeuron;
	}

	public double getCost(Digit digit) {
		double cost = 0.0;

		for (Neuron neuron : this.neuronList) {
			if (neuron instanceof OutputNeuron) {
				OutputNeuron outputNeuron = (OutputNeuron) neuron;
				double resultBias = 0.0;
				
				if (outputNeuron.getRepresentationValue() == digit.getLabel()) {
					resultBias = 1.0;
				}

				cost += Math.pow((neuron.getActivationValue() - resultBias), 2);
			}
		}

		return cost;
	}

	public void calculateProbability() {
		double total = 0.0;

		for (Neuron neuron : this.neuronList) {
			total += Math.exp(neuron.getActivationValue());
		}

		for (Neuron neuron : this.neuronList) {
			if (neuron instanceof OutputNeuron) {
				OutputNeuron outputNeuron = (OutputNeuron) neuron;
				outputNeuron.setProbability(Math.exp(neuron.getActivationValue()) / total);
			}
		}
	}
}
