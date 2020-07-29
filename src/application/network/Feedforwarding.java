package application.network;

import java.util.ArrayList;

import application.Log;
import application.data.DataItem;
import application.layer.HiddenLayer;
import application.layer.InputLayer;
import application.layer.OutputLayer;

public class Feedforwarding {
	private static Feedforwarding instance;

	public static Feedforwarding getInstance() {
		if (instance == null) {
			instance = new Feedforwarding();
		}

		return instance;
	}

	private int numOfPredictions = 0;

	public int getNumOfPredictions() {
		return numOfPredictions;
	}

	public void setNumOfPredictions(int numOfPredictions) {
		this.numOfPredictions = numOfPredictions;
	}

	public void increaseNumOfPredictions() {
		this.numOfPredictions++;
	}

	private int numOfErrors = 0;

	public int getNumOfErrors() {
		return numOfErrors;
	}

	public void setNumOfErrors(int numOfErrors) {
		this.numOfErrors = numOfErrors;
	}

	public void increaseNumOfErrors() {
		this.numOfErrors++;
	}

	private Feedforwarding() {
	}

	public void init() {
		this.numOfPredictions = 0;
		this.numOfErrors = 0;
	}

	public void execute(DataItem dataItem) {
		InputLayer inputLayer = Network.getInstance().getInputLayer();
		ArrayList<HiddenLayer> hiddenLayerList = Network.getInstance().getHiddenLayerList();
		OutputLayer outputLayer = Network.getInstance().getOutputLayer();

		inputLayer.setActivationValues(dataItem.getInitialValues());
		Log.getInstance().logActivationValues(inputLayer);
		
		outputLayer.setTargetValues(dataItem.getLabel());

		for (HiddenLayer hiddenLayer : hiddenLayerList) {
			hiddenLayer.calcActivationValues();
			Log.getInstance().logActivationValues(hiddenLayer);
		}

		outputLayer.calcActivationValues();
		Log.getInstance().logActivationValues(outputLayer);

		dataItem.setPrediction(outputLayer.getMostActiveNeuron().getRepresentationValue());
		Log.getInstance().logPredictions(dataItem);

		this.numOfPredictions++;
		if (dataItem.getPrediction() != dataItem.getLabel()) {
			this.numOfErrors++;
		}

		// Larger if the network is uncertain of the prediction
		Network.getInstance().getOutputLayer().calcErrors();
		double totalError = outputLayer.getTotalError();
		Log.getInstance().add("Gesamtkosten: " + String.format("%.2f", totalError));
	}

	public double getSuccessRate() {
		double rightPredictions = this.numOfPredictions - this.numOfErrors;
		double totalPredictions = this.numOfPredictions;
		double successRate = rightPredictions / totalPredictions * 100.0;
		Log.getInstance().add("Erfolgsrate: " + String.format("%.2f", successRate) + " % (" + (int) rightPredictions
				+ "/" + (int) totalPredictions + ")");
		return successRate;
	}
}
