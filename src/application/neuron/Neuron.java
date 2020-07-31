package application.neuron;

public abstract class Neuron {
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private double activationValue = 0.0;

	public double getActivationValue() {
		return activationValue;
	}

	public void setActivationValue(double activationValue) {
		this.activationValue = activationValue;
	}

	protected Neuron(int id) {
		this.id = id;
	}

	protected Neuron(int id, double activationValue) {
		this.id = id;
		this.activationValue = activationValue;
	}
}