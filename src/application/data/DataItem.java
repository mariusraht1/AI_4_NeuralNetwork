package application.data;

import java.util.ArrayList;

public class DataItem {
	private static ArrayList<Integer> PossibleTargetValues = new ArrayList<Integer>();

	public static ArrayList<Integer> getPossibleTargetValues() {
		return PossibleTargetValues;
	}

	protected void setPossibleTargetValues(ArrayList<Integer> possibleTargetValues) {
		PossibleTargetValues = possibleTargetValues;
	}

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

	public static int getNumOfInputNeurons() {
		return 0;
	}
}
