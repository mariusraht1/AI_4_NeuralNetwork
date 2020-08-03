package application.data;

import java.util.ArrayList;

import application.utilities.ImageDecoder;

public class MNISTDigit extends Digit {
	public MNISTDigit(int label, byte[] image) {
		super(label, image);
	}

	public static ArrayList<DataItem> getList(int size) {
		ArrayList<DataItem> dataItems = new ArrayList<DataItem>();

		for (int i = 0; i < size; i++) {
			dataItems.add(ImageDecoder.getInstance().readRandomDigit());
		}

		return dataItems;
	}

	private static DataItem RandomDigit = null;

	public static ArrayList<DataItem> getListSameDigit(int size) {
		ArrayList<DataItem> dataItems = new ArrayList<DataItem>();
		if (RandomDigit == null) {
			RandomDigit = ImageDecoder.getInstance().readRandomDigit();
		}

		for (int i = 0; i < size; i++) {
			dataItems.add(RandomDigit);
		}

		return dataItems;
	}
}
