package application.network;

import application.Log;
import application.data.DataItem;
import application.layer.HiddenLayer;

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
		prepareInputAndOutput(dataItem);
		calcActivationValues();
		setPrediction(dataItem);
		calcErrors();
	}

	private void prepareInputAndOutput(DataItem dataItem) {
		Network.getInstance().getInputLayer().setActivationValues(dataItem.getInitialValues());
		Network.getInstance().getOutputLayer().setTargetValues(dataItem.getLabel());
	}

	private void calcActivationValues() {
		for (HiddenLayer hiddenLayer : Network.getInstance().getHiddenLayerList()) {
			hiddenLayer.calcActivationValues();
		}
		Network.getInstance().getOutputLayer().calcActivationValues();
	}

	private void setPrediction(DataItem dataItem) {
		dataItem.setPrediction(Network.getInstance().getOutputLayer().getPrediction(dataItem).getRepresentationValue());
	}

	private void calcErrors() {
		// Larger if the network is uncertain of the prediction
		Network.getInstance().getOutputLayer().calcErrors();
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
