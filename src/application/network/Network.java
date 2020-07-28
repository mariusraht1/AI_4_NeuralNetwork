package application.network;

import java.util.ArrayList;

import application.Log;
import application.Main;
import application.data.DataInputType;
import application.data.DataItem;
import application.functions.Backpropagation;
import application.functions.Distribution;
import application.layer.HiddenLayer;
import application.layer.InputLayer;
import application.layer.Layer;
import application.layer.OutputLayer;
import application.neuron.HiddenNeuron;
import application.neuron.InputNeuron;
import application.neuron.Neuron;
import application.neuron.OutputNeuron;
import application.view.MainScene;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Cursor;

public class Network {
	private DataInputType dataInputType = DataInputType.XOR;

	public DataInputType getDataInputType() {
		return dataInputType;
	}

	public void setDataInputType(DataInputType dataInputType) {
		this.dataInputType = dataInputType;
	}

	private OperationMode operationMode = OperationMode.TRAIN;

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

	private final int numOfNeuronsHiddenLayer = 16;

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

	private double learningRate;

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
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
		this.numOfNeuronsInputLayer = dataInputType.getNumOfInputNeurons();
		this.learningRate = Main.DefaultLearningRate;

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
		HiddenLayer hiddenLayer = new HiddenLayer();
		for (int i = 0; i < this.numOfNeuronsHiddenLayer; i++) {
			hiddenLayer.getNeuronList().add(new HiddenNeuron());
		}
		this.hiddenLayerList.add(hiddenLayer);
		Layer prevLayer = hiddenLayer.getPreviousLayer();
		hiddenLayer.connectWith(prevLayer);
	}

	private void generateOutputLayer() {
		for (int i = 0; i < dataInputType.getPossibleTargetValues().size(); i++) {
			this.outputLayer.getNeuronList().add(new OutputNeuron(dataInputType.getPossibleTargetValues().get(i)));
		}
		Layer prevLayer = this.outputLayer.getPreviousLayer();
		this.outputLayer.connectWith(prevLayer);
	}

	public void runPlay(boolean animate, int numOfSteps, MainScene mainScene) throws Exception {
		ArrayList<DataItem> dataItems = dataInputType.getList(numOfSteps);

		if (animate) {
			this.playTask = new Task<Void>() {
				@Override
				public Void call() throws Exception {
					int step = 1;
					boolean play = true;

					while (play) {
						if (this.isCancelled()) {
							break;
						} else {
							DataItem dataItem = dataItems.get(step - 1);
							play = play(animate, step, numOfSteps, mainScene, dataItem);
							step++;
						}
					}
					return null;
				}
			};
			Thread thread = new Thread(this.playTask);
			thread.setDaemon(true);
			thread.start();
		} else {
			int step = 1;
			boolean play = true;

			while (play) {
				DataItem dataItem = dataItems.get(step - 1);
				play = play(animate, step, numOfSteps, mainScene, dataItem);
				step++;
			}
		}
	}

	private boolean play(boolean animate, int step, int numOfSteps, MainScene mainScene, DataItem dataItem)
			throws Exception {
		if (step <= numOfSteps) {
			if (animate) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						step(dataItem);
						updateUI(animate, mainScene, dataItem);
					}
				});
				Thread.sleep(200);
			} else {
				step(dataItem);
				updateUI(animate, mainScene, dataItem);
			}

			step++;
			return true;
		} else {
			return false;
		}
	}

	private void updateUI(boolean animate, MainScene mainScene, DataItem dataItem) {
		mainScene.showResult(animate, dataItem);
		// NEW Add results to history
		// History.getInstance().add();
	}

	public void step(DataItem dataItem) {
		Log.getInstance().add("******************************************");
		Log.getInstance().add("* Label " + dataItem.getLabel());
		Log.getInstance().add("******************************************");

		// dataItem.setInitialValues(digit.toGrayDoubleArray());
		this.inputLayer.setActivationValues(dataItem.getInitialValues());

		for (HiddenLayer hiddenLayer : this.hiddenLayerList) {
			hiddenLayer.calcActivationValues();
		}

		this.outputLayer.calcActivationValues();
		this.outputLayer.calculateProbabilities();

		dataItem.setPrediction(this.outputLayer.getMostActiveNeuron().getRepresentationValue());
		Log.getInstance().logPredictions(dataItem);

		// Larger if the network is uncertain of the prediction
		double totalError = this.outputLayer.getTotalError();
		Log.getInstance().add("Gesamtkosten: " + String.format("%.2f", totalError));

		this.numOfPredictions++;
		if (dataItem.getPrediction() != dataItem.getLabel()) {
			this.numOfErrors++;
		}

		Backpropagation.getInstance().execute();
	}

	public void cancelPlayTask() {
		if (this.playTask != null && this.playTask.isRunning()) {
			this.playTask.cancel();
			Main.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
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
