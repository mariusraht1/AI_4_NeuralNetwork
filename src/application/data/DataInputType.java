package application.data;

import java.util.ArrayList;

import application.network.Network;

public enum DataInputType {
	DIGIT, XOR;
	
	public static ArrayList<DataItem> getList(int size) {
		ArrayList<DataItem> dataItems = null;

		switch (Network.getInstance().getDataInputType()) {
		case DIGIT:
			dataItems = DigitImage.getList(size);
			break;
		case XOR:
			dataItems = XORData.getList(size);
			break;
		default:
			break;
		}

		return dataItems;
	}
}
