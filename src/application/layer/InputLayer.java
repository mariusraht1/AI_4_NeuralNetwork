package application.layer;

import java.util.ArrayList;

import application.neuron.Neuron;

public class InputLayer extends Layer {
	public InputLayer() {
		super();
		this.neuronList = new ArrayList<Neuron>();
	}

	public void setActivationValues(double[] values) {
		for (int i = 0; i < this.neuronList.size(); i++) {
			Neuron neuron = this.neuronList.get(i);
			neuron.setActivationValue(values[i]);
		}
	}
}
