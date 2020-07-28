package application.layer;

import java.util.ArrayList;

import application.network.Connection;
import application.network.Network;
import application.neuron.ConnectableNeuron;
import application.neuron.HiddenNeuron;
import application.neuron.Neuron;

public class HiddenLayer extends ConnectableLayer {
	public HiddenLayer() {
		super();
		this.neuronList = new ArrayList<Neuron>();
	}
	
	public void calcActivationValues() {
		this.activationFunction.execute(this);
	}
	
	public void calcErrors() {
		Layer nextLayer = this.getNextLayer();

		for (Neuron neuron : this.neuronList) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;
				double error = 0.0;

				for (Neuron nextNeuron : nextLayer.getNeuronList()) {
					if (nextNeuron instanceof ConnectableNeuron) {
						ConnectableNeuron connectableNextNeuron = (ConnectableNeuron) nextNeuron;
						Connection inboundConnection = connectableNextNeuron.getInboundConnectionBySourceNeuron(neuron);
						if (inboundConnection != null) {
							error += connectableNextNeuron.getError() * inboundConnection.getWeight();
						}
					}
				}

				connectableNeuron.setError(error);
			}
		}
	}

	public static void generate(int numOfNeurons) {
		HiddenLayer hiddenLayer = new HiddenLayer();
		for (int i = 0; i < numOfNeurons; i++) {
			hiddenLayer.getNeuronList().add(new HiddenNeuron());
		}
		Network.getInstance().getHiddenLayerList().add(hiddenLayer);
		Layer prevLayer = hiddenLayer.getPreviousLayer();
		hiddenLayer.connectWith(prevLayer);
	}
}
