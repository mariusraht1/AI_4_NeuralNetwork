package application.data;

import java.util.ArrayList;
import java.util.Arrays;

import application.utilities.ImageDecoder;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class Digit extends DataItem {
	private byte[] image;

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Digit(int label, byte[] image) {
		super(label);
		this.image = image;

		super.setInitialValues(toGrayDoubleArray());
	}

	public WritableImage toWritableImage() {
		int imageWidth = ImageDecoder.getInstance().getImageWidth();
		int imageHeight = ImageDecoder.getInstance().getImageHeight();

		WritableImage writableImage = new WritableImage(imageWidth, imageHeight);
		PixelWriter pixelWriter = writableImage.getPixelWriter();

		int index = 0;
		for (int y = 0; y < imageHeight; y++) {
			for (int x = 0; x < imageWidth; x++, index++) {
				int gray = Byte.toUnsignedInt(image[index]);
				int argb = (0xFF << 24) | (gray << 16) | (gray << 8) | gray;
				pixelWriter.setArgb(x, y, argb);
			}
		}

		return writableImage;
	}

	public double[] toGrayDoubleArray() {
		int imageWidth = ImageDecoder.getInstance().getImageWidth();
		int imageHeight = ImageDecoder.getInstance().getImageHeight();
		double[] gray = new double[imageWidth * imageHeight];

		// 0xFF => unsigned
		// Gray as Integer divided by 255 => gray double
		// 0-1, whereas 0 is pure black and 1 is pure white
		int index = 0;
		for (int y = 0; y < imageHeight; y++) {
			for (int x = 0; x < imageWidth; x++, index++) {
				gray[index] = Byte.toUnsignedInt(image[index]) / 255.0;
			}
		}

		return gray;
	}

	public static ArrayList<Integer> getPossibleTargetValues() {
		return new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
	}
}
