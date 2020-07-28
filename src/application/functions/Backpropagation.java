package application.functions;

import application.Log;
import application.layer.HiddenLayer;
import application.network.Network;
import application.network.OperationMode;

public class Backpropagation {
	private static Backpropagation instance;

	public static Backpropagation getInstance() {
		if (instance == null) {
			instance = new Backpropagation();
		}

		return instance;
	}

	private Backpropagation() {
	}

	// NEW Implement backpropagation
	public void execute() {
		if (Network.getInstance().getOperationMode().equals(OperationMode.Train)) {
			Log.getInstance().add("Execute Backpropagation.");

			Network.getInstance().getOutputLayer().calcErrors();
			Network.getInstance().getOutputLayer().calcNewWeights();

			for (int i = Network.getInstance().getHiddenLayerList().size() - 1; i > 0; i--) {
				HiddenLayer hiddenLayer = Network.getInstance().getHiddenLayerList().get(i);
				hiddenLayer.calcErrors();
				hiddenLayer.calcNewWeights();
			}
		}
	}
}