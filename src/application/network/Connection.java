package application.network;

import java.util.ArrayList;

import application.layer.ConnectableLayer;
import application.layer.Layer;
import application.neuron.ConnectableNeuron;
import application.neuron.Neuron;

public class Connection {
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private Neuron sourceNeuron;

	public Neuron getSourceNeuron() {
		return sourceNeuron;
	}

	public void setSourceNeuron(Neuron sourceNeuron) {
		this.sourceNeuron = sourceNeuron;
	}

	private double weight = 0.0;

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public Connection(String id, Neuron sourceNeuron) {
		this.id = id;
		this.sourceNeuron = sourceNeuron;
	}

	public static Connection getById(String id) {
		Connection result = null;
		String layerID = id.substring(0, 2);
		int lastOccIdDivider = id.lastIndexOf("_");
		int targetNeuronID = Integer.parseInt(id.substring(3, lastOccIdDivider));
		int sourceNeuronID = Integer.parseInt(id.substring(lastOccIdDivider + 1, id.length()));

		ArrayList<ConnectableLayer> layerList = new ArrayList<ConnectableLayer>();
		layerList.addAll(Network.getInstance().getHiddenLayerList());
		layerList.add(Network.getInstance().getOutputLayer());

		for (Layer layer : layerList) {
			if (layer.getId().equals(layerID)) {
				for (Neuron neuron : layer.getNeuronList()) {
					if (neuron instanceof ConnectableNeuron) {
						ConnectableNeuron connectableNeuron = (ConnectableNeuron) neuron;
						if (connectableNeuron.getId() == targetNeuronID) {
							for (Connection inboundConnection : connectableNeuron.getInboundConnections()) {
								if (inboundConnection.getSourceNeuron().getId() == sourceNeuronID) {
									result = inboundConnection;
									break;
								}
							}
							break;
						}
					}
				}
				break;
			}
		}

		return result;
	}
}
