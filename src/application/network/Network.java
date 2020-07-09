package application.network;

import java.util.ArrayList;

import application.image.ImageDecoder;

public class Network {
	private int numOfNeurons = 0;

	public int getNumOfNeurons() {
		return numOfNeurons;
	}

	public void setNumOfNeurons(int numOfNeurons) {
		this.numOfNeurons = numOfNeurons;
	}

	private ArrayList<Layer> layerList = new ArrayList<Layer>();

	public ArrayList<Layer> getLayerList() {
		return layerList;
	}

	public void setLayerList(ArrayList<Layer> layerList) {
		this.layerList = layerList;
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
		this.numOfNeurons = getNumOfInputNeurons();
		generateLayers();
	}

	public void generateLayers() {
		Layer inputLayer = new Layer(1);
		for (int i = 0; i < this.numOfNeurons; i++) {
			inputLayer.getNeuronList().add(new Neuron(i));
		}
		this.layerList.add(inputLayer);

		int numOfNeuronsHiddenLayer = 32;

		Layer hiddenLayer1 = new Layer(2);
		for (int i = 0; i < numOfNeuronsHiddenLayer; i++) {
			hiddenLayer1.getNeuronList().add(new Neuron(i));
		}
		hiddenLayer1.connectWith(inputLayer);
		this.layerList.add(hiddenLayer1);
		

		Layer hiddenLayer2 = new Layer(3);
		for (int i = 0; i < numOfNeuronsHiddenLayer; i++) {
			hiddenLayer2.getNeuronList().add(new Neuron(i));
		}
		hiddenLayer2.connectWith(hiddenLayer1);
		this.layerList.add(hiddenLayer2);

		Layer outputLayer = new Layer(4);
		for (int i = 0; i < 10; i++) {
			outputLayer.getNeuronList().add(new Neuron(i));
		}
		outputLayer.connectWith(hiddenLayer2);
		this.layerList.add(outputLayer);
	}

	public void setInputValues(double[] values) {
		Layer inputLayer = this.layerList.get(0);

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
		Network.getInstance().setInputValues(grayTones);
	}
}
