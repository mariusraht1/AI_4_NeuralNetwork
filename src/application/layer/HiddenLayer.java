package application.layer;

public class HiddenLayer extends Layer {	
	public HiddenLayer(int id) {
		super(id);
	}
	
	public void calcActivationValues() {
		this.activationFunction.execute(this);
	}
	
	public void initializeWeights() {
		activationFunction.initWeight(this);
	}
}
