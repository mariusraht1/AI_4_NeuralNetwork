package application.layer;

import java.util.ArrayList;

import application.network.Network;
import application.neuron.HiddenNeuron;
import application.neuron.Neuron;

public class HiddenLayer extends ConnectableLayer {
	public HiddenLayer(int id) {
		super("H" + id);
		this.neuronList = new ArrayList<Neuron>();
	}

	public void calcActivationValues() {
		super.calcActivationValues();
		Network.getInstance().getActivationFunction().execute(this);
	}

	public static void generate(int id, int numOfNeurons) {
		HiddenLayer hiddenLayer = new HiddenLayer(id);
		for (int i = 0; i < numOfNeurons; i++) {
			hiddenLayer.getNeuronList().add(new HiddenNeuron(i + 1));
		}
		Network.getInstance().getHiddenLayerList().add(hiddenLayer);
		Layer prevLayer = hiddenLayer.getPreviousLayer();
		hiddenLayer.connectWith(prevLayer);
	}
}
