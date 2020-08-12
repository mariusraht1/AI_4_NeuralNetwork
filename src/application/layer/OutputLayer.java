package application.layer;

import java.util.ArrayList;

import application.Log;
import application.data.DataInputType;
import application.data.DataItem;
import application.functions.ActivationFunction;
import application.network.Feedforwarding;
import application.network.Network;
import application.neuron.ConnectableNeuron;
import application.neuron.Neuron;
import application.neuron.OutputNeuron;

public class OutputLayer extends ConnectableLayer {
	private final ActivationFunction toProbabilitiesFunction = ActivationFunction.SOFTMAX;

	public OutputLayer() {
		super("OL");
		this.neuronList = new ArrayList<Neuron>();
	}

	public static void generate() {
		OutputLayer outputLayer = new OutputLayer();
		DataInputType dataInputType = Network.getInstance().getDataInputType();

		for (int i = 0; i < dataInputType.getPossibleTargetValues().size(); i++) {
			outputLayer.getNeuronList().add(new OutputNeuron(i + 1, dataInputType.getPossibleTargetValues().get(i)));
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

	public void calcActivationValues() {
		super.calcActivationValues();
		Network.getInstance().getActivationFunction().execute(this);
		this.toProbabilitiesFunction.execute(this);
	}

	public void calcErrors() {
		for (Neuron neuron : this.neuronList) {
			if (neuron instanceof ConnectableNeuron) {
				ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;
				double error = connectableNeuron.getActivationValue() - connectableNeuron.getTargetValue();
				connectableNeuron.setError(error);
			}
		}

		double totalError = Network.getInstance().getOutputLayer().getTotalError();
		Log.getInstance().add("Gesamtkosten: " + String.format("%.2f", totalError));
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

	public OutputNeuron getPrediction(DataItem dataItem) {
		OutputNeuron mostActiveNeuron = Network.getInstance().getOutputLayer().getMostActiveNeuron();
		dataItem.setPrediction(mostActiveNeuron.getRepresentationValue());
		Log.getInstance().logPredictions(dataItem);

		Feedforwarding.getInstance().increaseNumOfPredictions();
		if (dataItem.getPrediction() != dataItem.getLabel()) {
			Feedforwarding.getInstance().increaseNumOfErrors();
		}

		return mostActiveNeuron;
	}
}
