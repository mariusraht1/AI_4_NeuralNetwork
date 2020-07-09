package application.layer;

import application.network.Neuron;

public class OutputLayer extends Layer {
	public OutputLayer(int id) {
		super(id);
	}

	public Neuron getMostActiveNeuron() {
		Neuron mostActiveNeuron = this.neuronList.get(0);
		for (int i = 1; i < this.neuronList.size(); i++) {
			if (this.neuronList.get(0).getActivationValue() > mostActiveNeuron.getActivationValue()) {
				mostActiveNeuron = this.neuronList.get(i);
			}
		}

		return mostActiveNeuron;
	}
}
