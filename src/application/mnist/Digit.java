package application.mnist;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class Digit {
	private int label;

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	private byte[] image;

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Digit(int label, byte[] image) {
		this.label = label;
		this.image = image;
	}
	
	public WritableImage toWritableImage() {
		int imageWidth = MNISTImageDecoder.getInstance().getImageWidth();
		int imageHeight = MNISTImageDecoder.getInstance().getImageHeight();

		WritableImage writableImage = new WritableImage(imageWidth, imageHeight);
		PixelWriter pixelWriter = writableImage.getPixelWriter();
				
		int index = 0;
		for (int y = 0; y < imageHeight; y++) {
			for (int x = 0; x < imageWidth; x++, index++) {
				int gray = 255 - (((int) image[index]) & 0xFF); // 0xFF => unsigned
				int argb = (0xFF<<24) | (gray << 16) | (gray << 8) | gray;
				pixelWriter.setArgb(x, y, argb);
			}
		}

		return writableImage;
	}
}
