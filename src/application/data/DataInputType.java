package application.data;

import java.util.ArrayList;

import application.network.Network;
import application.utilities.ImageDecoder;

public enum DataInputType {
	HANDWRITTEN_DIGIT, MNIST_DIGIT, MNIST_SAME_DIGIT, XOR;

	public ArrayList<DataItem> getList(int size) {
		ArrayList<DataItem> dataItems = null;

		switch (Network.getInstance().getDataInputType()) {
		case HANDWRITTEN_DIGIT:
			dataItems = HandwrittenDigit.getList(size);
			break;
		case MNIST_DIGIT:
			dataItems = MNISTDigit.getList(size);
			break;
		case MNIST_SAME_DIGIT:
			dataItems = MNISTDigit.getListSameDigit(size);
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
		case MNIST_DIGIT:
		case MNIST_SAME_DIGIT:
			numOfInputNeurons = ImageDecoder.getInstance().size();
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
		case HANDWRITTEN_DIGIT:
		case MNIST_DIGIT:
		case MNIST_SAME_DIGIT:
			possibleTargetValues = Digit.getPossibleTargetValues();
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
