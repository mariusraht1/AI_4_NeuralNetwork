package application.utilities;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Random;

public class MathManager {
	private static MathManager instance;

	public static MathManager getInstance() {
		if (instance == null) {
			instance = new MathManager();
		}

		return instance;
	}

	private Random random = new Random();

	public int getRandom(int min, int max) {
		return random.nextInt((max - min) + 1) + min;
	}

	public double getRandom(double min, double max) {
		double range = max - min;
		double scaled = random.nextDouble() * range;
		double shifted = scaled + min;

		return shifted;
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

	public double sigmoid(double value) {
		return 1 / (1 + Math.exp(-value));
	}

	public double tanh(double value) {
		return Math.tanh(value);
	}

	public double relu(double value) {
		double result = value;

		if (result < 0) {
			result = 0.0;
		}

		return result;
	}

	public double leaky_relu(double value) {
		double a = 0.01;
		double result = value;

		if (result < 0) {
			result *= a;
		}

		return result;
	}
}
