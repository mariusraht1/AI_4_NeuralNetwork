package application.data;

public class DataItem {
	private double[] initialValues;

	public double[] getInitialValues() {
		return initialValues;
	}

	public void setInitialValues(double[] initialValues) {
		this.initialValues = initialValues;
	}

	private int label;

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	private int prediction;

	public int getPrediction() {
		return prediction;
	}

	public void setPrediction(int prediction) {
		this.prediction = prediction;
	}

	public DataItem(int label) {
		this.label = label;
	}

	public DataItem(int label, double[] initialValues) {
		this.label = label;
		this.initialValues = initialValues;
	}
}
