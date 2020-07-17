package application.layer;

import java.util.ArrayList;

import application.neuron.Neuron;

public class HiddenLayer extends Layer {	
	public HiddenLayer() {
		super();
		this.neuronList = new ArrayList<Neuron>();
	}
	
	public void setActivationValues() {
		this.activationFunction.execute(this);
	}
}
