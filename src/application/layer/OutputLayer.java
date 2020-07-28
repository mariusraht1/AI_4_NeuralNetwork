package application.layer;

import java.util.ArrayList;

import application.data.DataInputType;
import application.network.Network;
import application.neuron.ConnectableNeuron;
import application.neuron.Neuron;
import application.neuron.OutputNeuron;

public class OutputLayer extends ConnectableLayer {
	public OutputLayer() {
		super();
		this.neuronList = new ArrayList<Neuron>();
	}

	public static void generate() {
		OutputLayer outputLayer = new OutputLayer();
		DataInputType dataInputType = Network.getInstance().getDataInputType();

		for (int i = 0; i < dataInputType.getPossibleTargetValues().size(); i++) {
			outputLayer.getNeuronList().add(new OutputNeuron(dataInputType.getPossibleTargetValues().get(i)));
		}
		Network.getInstance().setOutputLayer(outputLayer);
		Layer prevLayer = outputLayer.getPreviousLayer();
		outputLayer.connectWith(prevLayer);
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

	// Softmax
	public void calcActivationValues() {
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

	public void calcErrors() {
		for (Neuron neuron : this.neuronList) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;
				double error = connectableNeuron.getActivationValue() - connectableNeuron.getTargetValue();
				connectableNeuron.setError(error);
			}
		}
	}

	public void setTargetValues(int target) {
		for (Neuron neuron : this.neuronList) {
			if (neuron instanceof OutputNeuron) {
				OutputNeuron outputNeuron = (OutputNeuron) neuron;
				if (outputNeuron.getRepresentationValue() == target) {
					outputNeuron.setTargetValue(1.0);
				} else {
					outputNeuron.setTargetValue(0.0);
				}
			}
		}
	}
}
