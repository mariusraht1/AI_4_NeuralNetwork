package application.layer;

import java.util.ArrayList;

import application.neuron.Neuron;
import application.neuron.OutputNeuron;

public class OutputLayer extends ConnectableLayer {
	public OutputLayer() {
		super();
		this.neuronList = new ArrayList<Neuron>();
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

	public void calculateProbabilities() {
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
