package application.network;

import java.util.ArrayList;

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
	
	public void init(int numOfNeurons) {
		this.numOfNeurons = numOfNeurons;
		generate();
	}
	
	public void generate() {
		Layer inputLayer = new Layer(1);
		
		for(int i = 0; i < numOfNeurons; i++) {
			inputLayer.getNeuronList().add(new Neuron(i));
		}
	}
}
