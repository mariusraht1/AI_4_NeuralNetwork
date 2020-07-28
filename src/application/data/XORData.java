package application.data;

import java.util.ArrayList;
import java.util.Arrays;

import application.utilities.MathManager;

public class XORData extends DataItem {
	private static final ArrayList<XORData> DefaultXORData = new ArrayList<XORData>(
			Arrays.asList(new XORData(1, new double[] { 1, 1 }), new XORData(1, new double[] { 0, 0 }),
					new XORData(0, new double[] { 1, 0 }), new XORData(0, new double[] { 0, 1 })));

	public XORData(int label, double[] initialValues) {
		super(label, initialValues);
	}

	public static ArrayList<DataItem> getList(int size) {
		ArrayList<DataItem> dataItems = new ArrayList<DataItem>();

		for (int i = 0; i < size; i++) {
			int randomIndex = MathManager.getInstance().getRandom(0, DefaultXORData.size());
			dataItems.add(DefaultXORData.get(randomIndex));
		}

		return dataItems;
	}
}
