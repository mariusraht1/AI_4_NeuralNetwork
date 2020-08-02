package application.network;

import java.util.ArrayList;

import application.Main;
import application.data.DataInputType;
import application.data.DataItem;
import application.functions.ActivationFunction;
import application.functions.Distribution;
import application.layer.HiddenLayer;
import application.layer.InputLayer;
import application.layer.OutputLayer;
import application.view.MainScene;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Cursor;

public class Network {
	private DataInputType dataInputType = DataInputType.MNIST_DIGIT;

	public DataInputType getDataInputType() {
		return dataInputType;
	}

	public void setDataInputType(DataInputType dataInputType) {
		this.dataInputType = dataInputType;
	}

	private Distribution distribution = Distribution.UNIFORM;

	public Distribution getDistribution() {
		return distribution;
	}

	public void setDistribution(Distribution distribution) {
		this.distribution = distribution;
	}

	protected ActivationFunction activationFunction = ActivationFunction.LEAKY_RELU;

	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}

	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}

	private OperationMode operationMode = OperationMode.TRAIN;

	public OperationMode getOperationMode() {
		return operationMode;
	}

	public void setOperationMode(OperationMode operationMode) {
		this.operationMode = operationMode;
	}

	private InputLayer inputLayer;

	public InputLayer getInputLayer() {
		return inputLayer;
	}

	public void setInputLayer(InputLayer inputLayer) {
		this.inputLayer = inputLayer;
	}

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
		this.hiddenLayerList = new ArrayList<HiddenLayer>();
		Feedforwarding.getInstance().init();
		generateLayers();
	}

	public void generateLayers() {
		InputLayer.generate();
//		HiddenLayer.generate(1, 128);
//		HiddenLayer.generate(2, 64);
		OutputLayer.generate();
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
			int step = 0;
			boolean play = true;

			while (play) {
				step++;
				DataItem dataItem = dataItems.get(step - 1);
				play = play(animate, step, numOfSteps, mainScene, dataItem);
			}
		}
	}

	private boolean play(boolean animate, int step, int numOfSteps, MainScene mainScene, DataItem dataItem)
			throws Exception {
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

		if (step <= numOfSteps) {
			return true;
		} else {
			return false;
		}
	}

	private void updateUI(boolean animate, MainScene mainScene, DataItem dataItem) {
		mainScene.showResult(animate, dataItem);
	}

	public void step(DataItem dataItem) {
		Feedforwarding.getInstance().execute(dataItem);
		Backpropagation.getInstance().execute();
	}

	public void cancelPlayTask() {
		if (this.playTask != null && this.playTask.isRunning()) {
			this.playTask.cancel();
			Main.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
		}
	}
}
