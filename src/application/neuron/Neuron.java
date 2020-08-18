package application.neuron;

public abstract class Neuron {
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private ActivationValue activationValue = new ActivationValue(0.0);

	public ActivationValue getActivationValue() {
		return activationValue;
	}

	public void setActivationValue(double activationValue) {
		this.activationValue.set(activationValue);
	}

	protected Neuron(int id) {
		this.id = id;
	}

	protected Neuron(int id, ActivationValue activationValue) {
		this.id = id;
		this.activationValue = activationValue;
	}
}