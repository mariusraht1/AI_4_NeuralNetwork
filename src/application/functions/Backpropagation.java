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
			Log.getInstance().add("Backpropagation is enabled.");
			// Minimize cost over all ran predictions: Calculate slope to reduce cost
			// If slope is negative, reduce weight/bias; if it's positive, increase
			// weight/bias
			Network.getInstance().getOutputLayer().calcErrors();
			Network.getInstance().getOutputLayer().calcNewWeights();
			
			for(HiddenLayer hiddenLayer : Network.getInstance().getHiddenLayerList()) {
				hiddenLayer.calcErrors();
			}
		}
	}
}
