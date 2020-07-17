package application.network;

import java.util.ArrayList;

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

public class Network {
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

	private InputLayer inputLayer = new InputLayer();

	public InputLayer getInputLayer() {
		return inputLayer;
	}

	public void setInputLayer(InputLayer inputLayer) {
		this.inputLayer = inputLayer;
	}

	private final int numOfNeuronsHiddenLayer = 32;

	private ArrayList<HiddenLayer> hiddenLayerList = new ArrayList<HiddenLayer>();

	public ArrayList<HiddenLayer> getHiddenLayerList() {
		return hiddenLayerList;
	}

	public void setHiddenLayerList(ArrayList<HiddenLayer> hiddenLayerList) {
		this.hiddenLayerList = hiddenLayerList;
	}

	private OutputLayer outputLayer = new OutputLayer();

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
		this.numOfNeuronsInputLayer = getNumOfInputNeurons();
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
			this.inputLayer.getNeuronList().add(new InputNeuron(i));
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
			hiddenLayer.getNeuronList().add(new HiddenNeuron(i));
		}
		hiddenLayer.connectWith(prevLayer);
		hiddenLayer.initializeWeights();
		this.hiddenLayerList.add(hiddenLayer);
	}

	private void generateOutputLayer() {
		for (int i = 0; i < 10; i++) {
			this.outputLayer.getNeuronList().add(new OutputNeuron(i));
		}
		HiddenLayer lastHiddenLayer = this.hiddenLayerList.get(this.hiddenLayerList.size() - 1);
		this.outputLayer.connectWith(lastHiddenLayer);
		this.outputLayer.initializeWeights();
	}

	public int getNumOfInputNeurons() {
		return ImageDecoder.getInstance().getImageWidth() * ImageDecoder.getInstance().getImageHeight();
	}

	public void play(MainScene mainScene) {
		Digit digit = ImageDecoder.getInstance().readNextDigit();
		double[] grayTones = digit.toGrayDoubleArray();
		this.inputLayer.setActivationValues(grayTones);

		for (HiddenLayer hiddenLayer : this.hiddenLayerList) {
			hiddenLayer.setActivationValues();
		}
		
		this.outputLayer.setActivationValues();

		// NEW Add rate of certainty to history by round
		// Larger if the network is uncertain of the prediction
		double cost = this.outputLayer.getCost(digit);
		
		this.numOfPredictions++;
		if(digit.getPrediction() != digit.getLabel()) {
			this.numOfErrors++;
		}
		
		
		
		// NEW Backpropagation
		// Minimize cost over all ran predictions: Calculate slope to reduce cost
		// If slope is negative, reduce weight/bias; if it's positive, increase
		// weight/bias

		mainScene.showResult(digit);
		
	}

	public int getPrediction() {
		Neuron neuron = this.outputLayer.getMostActiveNeuron();
		return this.outputLayer.getNeuronList().indexOf(neuron);
	}

	public double getSuccessRate() {
		return ((this.numOfPredictions - this.numOfErrors) / this.numOfPredictions) * 100;
	}
}
