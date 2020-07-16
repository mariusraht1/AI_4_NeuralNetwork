package application.network;

import java.util.ArrayList;

import application.activation.ActivationFunction;
import application.activation.Distribution;
import application.image.ImageDecoder;
import application.layer.HiddenLayer;
import application.layer.InputLayer;
import application.layer.Layer;
import application.layer.OutputLayer;

public class Network {
	private int numOfNeurons = 0;

	public int getNumOfNeurons() {
		return numOfNeurons;
	}

	public void setNumOfNeurons(int numOfNeurons) {
		this.numOfNeurons = numOfNeurons;
	}
	
	private ActivationFunction activationFunction = ActivationFunction.ReLu;
	
	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}

	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}

	private Distribution distribution = Distribution.NORMAL;
	
	public Distribution getDistribution() {
		return distribution;
	}

	public void setDistribution(Distribution distribution) {
		this.distribution = distribution;
	}
	
	private InputLayer inputLayer;

	public InputLayer getInputLayer() {
		return inputLayer;
	}

	public void setInputLayer(InputLayer inputLayer) {
		this.inputLayer = inputLayer;
	}

	private ArrayList<HiddenLayer> hiddenLayerList = new ArrayList<HiddenLayer>();

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

	private final int numOfNeuronsHiddenLayer = 32;

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
		this.numOfNeurons = getNumOfInputNeurons();
		generateLayers();
	}

	public void generateLayers() {
		InputLayer inputLayer = new InputLayer(1);
		for (int i = 0; i < this.numOfNeurons; i++) {
			inputLayer.getNeuronList().add(new Neuron(i));
		}
		this.inputLayer = inputLayer;

		HiddenLayer hiddenLayer1 = new HiddenLayer(2);
		for (int i = 0; i < this.numOfNeuronsHiddenLayer; i++) {
			hiddenLayer1.getNeuronList().add(new Neuron(i));
		}
		hiddenLayer1.connectWith(inputLayer);
		this.hiddenLayerList.add(hiddenLayer1);

		HiddenLayer hiddenLayer2 = new HiddenLayer(3);
		for (int i = 0; i < this.numOfNeuronsHiddenLayer; i++) {
			hiddenLayer2.getNeuronList().add(new Neuron(i));
		}
		hiddenLayer2.connectWith(hiddenLayer1);
		this.hiddenLayerList.add(hiddenLayer2);

		OutputLayer outputLayer = new OutputLayer(4);
		for (int i = 0; i < 10; i++) {
			outputLayer.getNeuronList().add(new Neuron(i));
		}
		outputLayer.connectWith(hiddenLayer2);
		this.outputLayer = outputLayer;
	}

	public void setInputValues(double[] values) {
		Layer inputLayer = this.hiddenLayerList.get(0);

		for (int i = 0; i < inputLayer.getNeuronList().size(); i++) {
			Neuron neuron = inputLayer.getNeuronList().get(i);
			neuron.setActivationValue(values[i]);
		}
	}

	public int getNumOfInputNeurons() {
		return ImageDecoder.getInstance().getImageWidth() * ImageDecoder.getInstance().getImageHeight();
	}

	public void play(Digit digit) {
		double[] grayTones = digit.toGrayDoubleArray();
		setInputValues(grayTones);

		// NEW Set random weight at the very beginning
		for (HiddenLayer hiddenLayer : hiddenLayerList) {
			hiddenLayer.calcActivationValues();
		}

		// NEW Add rate of certainty to history by round
		// Larger if the network is uncertain of the prediction
		double cost = this.outputLayer.getCost(digit);

		// NEW Backpropagation
		// Minimize cost over all ran predictions: Calculate slope to reduce cost
		// If slope is negative, reduce weight/bias; if it's positive, increase
		// weight/bias

	}

	public int getPrediction() {
		Neuron neuron = this.outputLayer.getMostActiveNeuron();
		return this.outputLayer.getNeuronList().indexOf(neuron);
	}
}
