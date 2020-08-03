package application.data;

import java.util.ArrayList;

public class HandwrittenDigit extends Digit {
	public HandwrittenDigit(int label, byte[] image) {
		super(label, image);
		HandwrittenDigit = this;
	}

	private static DataItem HandwrittenDigit = null;

	public static boolean isNull() {
		return HandwrittenDigit == null;
	}

	public static ArrayList<DataItem> getList(int size) {
		ArrayList<DataItem> dataItems = new ArrayList<DataItem>();
		if (HandwrittenDigit != null) {
			for (int i = 0; i < size; i++) {
				dataItems.add(HandwrittenDigit);
			}
		}

		return dataItems;
	}
}
