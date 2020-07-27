package application.network;

import java.util.ArrayList;

import application.Log;
import application.activation.Distribution;
import application.layer.HiddenLayer;
import application.layer.InputLayer;
import application.layer.Layer;
import application.layer.OutputLayer;
import application.neuron.HiddenNeuron;
import application.neuron.InputNeuron;
import application.neuron.Neuron;
import application.neuron.OutputNeuron;
import application.utilities.ImageDecoder;
import application.view.MainScene;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class Network {
	private OperationMode operationMode = OperationMode.Train;

	public OperationMode getOperationMode() {
		return operationMode;
	}

	public void setOperationMode(OperationMode operationMode) {
		this.operationMode = operationMode;
	}

	private Distribution distribution = Distribution.NORMAL;

	public Distribution getDistribution() {
		return distribution;
	}

	public void setDistribution(Distribution distribution) {
		this.distribution = distribution;
	}

	private int numOfNeuronsInputLayer = 0;

	public int getNumOfNeurons() {
		return numOfNeuronsInputLayer;
	}

	public void setNumOfNeurons(int numOfNeurons) {
		this.numOfNeuronsInputLayer = numOfNeurons;
	}

	private InputLayer inputLayer;

	public InputLayer getInputLayer() {
		return inputLayer;
	}

	public void setInputLayer(InputLayer inputLayer) {
		this.inputLayer = inputLayer;
	}

	private final int numOfNeuronsHiddenLayer = 32;

	private ArrayList<HiddenLayer> hiddenLayerList;

	public ArrayList<HiddenLayer> getHiddenLayerList() {
		return hiddenLayerList;
	}

	public void setHiddenLayerList(ArrayList<HiddenLayer> hiddenLayerList) {
		this.hiddenLayerList = hiddenLayerList;
	}

	private OutputLayer outputLayer;

	public OutputLayer getOutputLayer() {
		return outputLayer;
	}

	public void setOutputLayer(OutputLayer outputLayer) {
		this.outputLayer = outputLayer;
	}

	private int numOfPredictions = 0;

	public int getNumOfPredictions() {
		return numOfPredictions;
	}

	public void setNumOfPredictions(int numOfPredictions) {
		this.numOfPredictions = numOfPredictions;
	}

	private int numOfErrors = 0;

	public int getNumOfErrors() {
		return numOfErrors;
	}

	public void setNumOfErrors(int numOfErrors) {
		this.numOfErrors = numOfErrors;
	}

	private Task<Void> playTask;

	public Task<Void> getPlayTask() {
		return playTask;
	}

	public void setPlayTask(Task<Void> playTask) {
		this.playTask = playTask;
	}

	private static Network instance;

	public static Network getInstance() {
		if (instance == null) {
			instance = new Network();
		}

		return instance;
	}

	protected Network() {
	}

	public void init() {
		this.numOfPredictions = 0;
		this.numOfErrors = 0;
		this.numOfNeuronsInputLayer = getNumOfInputNeurons();

		this.inputLayer = new InputLayer();
		this.hiddenLayerList = new ArrayList<HiddenLayer>();
		this.outputLayer = new OutputLayer();

		generateLayers();
	}

	public void generateLayers() {
		generateInputLayer();
		generateHiddenLayer(this.numOfNeuronsHiddenLayer);
		generateHiddenLayer(this.numOfNeuronsHiddenLayer);
		generateOutputLayer();
	}

	private void generateInputLayer() {
		for (int i = 0; i < this.numOfNeuronsInputLayer; i++) {
			this.inputLayer.getNeuronList().add(new InputNeuron());
		}
	}

	private void generateHiddenLayer(int numOfNeurons) {
		Layer prevLayer = null;
		if (!this.hiddenLayerList.isEmpty()) {
			prevLayer = this.hiddenLayerList.get(this.hiddenLayerList.size() - 1);
		} else {
			prevLayer = this.inputLayer;
		}

		HiddenLayer hiddenLayer = new HiddenLayer();
		for (int i = 0; i < this.numOfNeuronsHiddenLayer; i++) {
			hiddenLayer.getNeuronList().add(new HiddenNeuron());
		}
		hiddenLayer.connectWith(prevLayer);
		this.hiddenLayerList.add(hiddenLayer);
	}

	private void generateOutputLayer() {
		for (int i = 0; i < 10; i++) {
			this.outputLayer.getNeuronList().add(new OutputNeuron(i));
		}
		HiddenLayer lastHiddenLayer = this.hiddenLayerList.get(this.hiddenLayerList.size() - 1);
		this.outputLayer.connectWith(lastHiddenLayer);
	}

	public int getNumOfInputNeurons() {
		return ImageDecoder.getInstance().getImageWidth() * ImageDecoder.getInstance().getImageHeight();
	}

	public void runPlay(int numOfSteps, MainScene mainScene) {
		this.playTask = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				int step = 1;
				boolean play = true;

				while (play) {
					if (this.isCancelled()) {
						break;
					} else {
						Digit digit = ImageDecoder.getInstance().readNextDigit();
						play = play(step, numOfSteps, mainScene, digit);
						step++;
					}
				}
				return null;
			}
		};
		Thread thread = new Thread(this.playTask);
		thread.setDaemon(true);
		thread.start();
	}

	private boolean play(int step, int numOfSteps, MainScene mainScene, Digit digit) throws Exception {
		if (step <= numOfSteps) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					step(digit);
					updateUI(mainScene, digit);
				}
			});
			Thread.sleep(200);

			step++;
			return true;
		} else {
			return false;
		}
	}

	private void updateUI(MainScene mainScene, Digit digit) {
		mainScene.showResult(digit);
		// NEW Add results to history
		// History.getInstance().add();
	}

	public void step(Digit digit) {
		Log.getInstance().add("******************************************");
		Log.getInstance().add("* Label " + digit.getLabel());
		Log.getInstance().add("******************************************");

		double[] grayTones = digit.toGrayDoubleArray();
		this.inputLayer.setActivationValues(grayTones);

		for (HiddenLayer hiddenLayer : this.hiddenLayerList) {
			hiddenLayer.initWeights();
			hiddenLayer.calcActivationValues();
		}

		this.outputLayer.initWeights();
		this.outputLayer.calcActivationValues();
		this.outputLayer.calculateProbability();
		digit.setPrediction(this.outputLayer.getMostActiveNeuron().getRepresentationValue());

		Log.getInstance().add("Predictions for Label " + digit.getLabel() + ":");
		StringBuilder probabilities = new StringBuilder("[");
		for (Neuron neuron : this.outputLayer.getNeuronList()) {
			if (neuron instanceof OutputNeuron) {
				OutputNeuron outputNeuron = (OutputNeuron) neuron;
				probabilities.append(outputNeuron.getRepresentationValue() + ": "
						+ String.format("%.2f", outputNeuron.getProbability()));

				if (this.outputLayer.getNeuronList().indexOf(outputNeuron) < this.outputLayer.getNeuronList().size()
						- 1) {
					probabilities.append(", ");
				} else {
					probabilities.append("]");
				}
			}
		}
		Log.getInstance().add(probabilities.toString());

		// Certainty of predictions is larger if the network is uncertain of the prediction
		double totalError = this.outputLayer.getTotalError();
		Log.getInstance().add("Gesamtkosten: " + String.format("%.2f", totalError));

		this.numOfPredictions++;
		if (digit.getPrediction() != digit.getLabel()) {
			this.numOfErrors++;
		}

		// NEW Backpropagation
		if (this.operationMode.equals(OperationMode.Train)) {
			Log.getInstance().add("Backpropagation is enabled.");
			// Minimize cost over all ran predictions: Calculate slope to reduce cost
			// If slope is negative, reduce weight/bias; if it's positive, increase
			// weight/bias
		}
	}

	public void cancelPlayTask() {
		if (this.playTask != null && this.playTask.isRunning()) {
			this.playTask.cancel();
		}
	}

	public int getPrediction() {
		Neuron neuron = this.outputLayer.getMostActiveNeuron();
		return this.outputLayer.getNeuronList().indexOf(neuron);
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
