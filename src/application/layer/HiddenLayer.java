package application.layer;

public class HiddenLayer extends Layer {	
	public HiddenLayer() {
		super();
	}
	
	public void calcActivationValues() {
		this.activationFunction.execute(this);
	}
	
	public void initializeWeights() {
		activationFunction.initWeight(this);
	}
}
