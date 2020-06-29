package application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Random;

public class Utilities {
	private static Utilities instance;

	public static Utilities getInstance() {
		if (instance == null) {
			instance = new Utilities();
		}

		return instance;
	}

	private Random random = new Random();

	protected Utilities() {
	}

	public int getRandom(int min, int max) {
		return random.nextInt((max - min) + 1) + min;
	}

	public double getRandom(double min, double max) {
		double range = max - min;
		double scaled = random.nextDouble() * range;
		double shifted = scaled + min;

		return shifted;
	}

	public enum OSType {
		Windows, MacOS, Unix, Other
	};

	protected static OSType osType;

	public OSType getOperatingSystemType() {
		if (osType == null) {
			String osName = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);

			if (osName.contains("mac") || osName.contains("darwin")) {
				osType = OSType.MacOS;
			} else if (osName.contains("win")) {
				osType = OSType.Windows;
			} else if (osName.contains("nux")) {
				osType = OSType.Unix;
			} else {
				osType = OSType.Other;
			}
		}

		return osType;
	}

	public double divide(double dividend, double divisor) {
		if (divisor == 0) {
			return 0;
		} else {
			return dividend / divisor;
		}
	}

	public int parseInt(String s) {
		int result = 0;

		try {
			result = Integer.parseInt(s);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public double parseDouble(String s) {
		double result = 0;
		
		try {
			result = Double.parseDouble(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public double formatDouble(String format, double number) {
		DecimalFormat decimalFormat = new DecimalFormat(format);
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
		number = Double.valueOf(decimalFormat.format(number));

		return number;
	}

	@SuppressWarnings("unchecked")
	public <T extends Serializable> T deepCopy(T object) {
		if (object == null) {
			return null;
		}

		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			new ObjectOutputStream(outputStream).writeObject(object);

			ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			Object copiedObject = new ObjectInputStream(inputStream).readObject();

			return (T) copiedObject;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
