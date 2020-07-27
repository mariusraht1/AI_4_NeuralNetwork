package application.neuron;

public class ConnectableNeuron extends Neuron {
	private double bias = 0.0;

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

	private double targetValue = 0.0;

	public double getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(double targetValue) {
		this.targetValue = targetValue;
	}

	private double error = 0.0;

	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}
}
