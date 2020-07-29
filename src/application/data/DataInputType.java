package application.data;

import java.util.ArrayList;

import application.network.Network;

public enum DataInputType {
	DIGIT, SAME_DIGIT, XOR;

	public ArrayList<DataItem> getList(int size) {
		ArrayList<DataItem> dataItems = null;

		switch (Network.getInstance().getDataInputType()) {
		case DIGIT:
			dataItems = DigitImage.getList(size);
			break;
		case SAME_DIGIT:
			dataItems = DigitImage.getListSameDigit(size);
			break;
		case XOR:
			dataItems = XORData.getList(size);
			break;
		default:
			break;
		}

		return dataItems;
	}

	public int getNumOfInputNeurons() {
		int numOfInputNeurons = 0;

		switch (Network.getInstance().getDataInputType()) {
		case DIGIT:
		case SAME_DIGIT:
			numOfInputNeurons = DigitImage.getNumOfInputNeurons();
			break;
		case XOR:
			numOfInputNeurons = XORData.getNumOfInputNeurons();
			break;
		default:
			break;
		}

		return numOfInputNeurons;
	}

	public ArrayList<Integer> getPossibleTargetValues() {
		ArrayList<Integer> possibleTargetValues = null;

		switch (Network.getInstance().getDataInputType()) {
		case DIGIT:
		case SAME_DIGIT:
			possibleTargetValues = DigitImage.getPossibleTargetValues();
			break;
		case XOR:
			possibleTargetValues = XORData.getPossibleTargetValues();
			break;
		default:
			break;
		}

		return possibleTargetValues;
	}
}
