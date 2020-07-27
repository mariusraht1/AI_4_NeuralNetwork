package application.layer;

import java.util.ArrayList;

import application.neuron.Neuron;

public class HiddenLayer extends ConnectableLayer {
	public HiddenLayer() {
		super();
		this.neuronList = new ArrayList<Neuron>();
	}
}
