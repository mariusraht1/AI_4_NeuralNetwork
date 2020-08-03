package application.utilities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import application.data.Digit;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import library.MathManager;

public class ImageDecoder {
	private static ImageDecoder instance;

	public static ImageDecoder getInstance() {
		if (instance == null) {
			instance = new ImageDecoder();
		}

		return instance;
	}

	private final int labelOffset = 8;

	public int getLabelOffset() {
		return labelOffset;
	}

	private int labelIndex = 0;

	public int getLabelIndex() {
		return labelIndex;
	}

	public void setLabelIndex(int labelIndex) {
		this.labelIndex = labelIndex;
	}

	private final int imageWidth = 28;

	public int getImageWidth() {
		return imageWidth;
	}

	private final int imageHeight = 28;

	public int getImageHeight() {
		return imageHeight;
	}

	private final int imageOffset = 16;

	public int getImageOffset() {
		return imageOffset;
	}

	private int imageIndex = 0;

	public int getImageIndex() {
		return imageIndex;
	}

	public void setImageIndex(int imageIndex) {
		this.imageIndex = imageIndex;
	}

	private byte[] imageFileContent = null;

	public byte[] getImageFileContent() {
		return imageFileContent;
	}

	public void setImageFileContent(byte[] imageFileContent) {
		this.imageFileContent = imageFileContent;
	}

	private byte[] labelFileContent = null;

	public byte[] getLabelFileContent() {
		return labelFileContent;
	}

	public void setLabelFileContent(byte[] labelFileContent) {
		this.labelFileContent = labelFileContent;
	}

	public void readFiles(File imageFile, File labelFile) {
		this.imageIndex = 0;
		this.labelIndex = 0;

		byte[] imageFileContent = FileManager.getInstance().read(imageFile);
		this.imageFileContent = FileManager.getInstance().unzip(imageFile, imageFileContent);

		byte[] labelFileContent = FileManager.getInstance().read(labelFile);
		this.labelFileContent = FileManager.getInstance().unzip(labelFile, labelFileContent);

		this.labelIndex = labelOffset;
		this.imageIndex = imageOffset;
	}

	public Digit readNextDigit() {
		if (labelIndex > labelFileContent.length || imageIndex > imageFileContent.length) {
			this.labelIndex = labelOffset;
			this.imageIndex = imageOffset;
		}

		int label = (labelFileContent[labelIndex] & 0xFF);
		byte[] image = new byte[imageWidth * imageHeight];

		int index = 0;
		for (int i = imageIndex; i < (imageIndex + (imageWidth * imageHeight)); i++) {
			image[index] = imageFileContent[i];
			index++;
		}

		labelIndex++;
		imageIndex += (imageWidth * imageHeight);

		return new Digit(label, image);
	}

	public Digit readRandomDigit() {
		int numOfImages = labelFileContent.length - labelOffset;
		int randomIndex = MathManager.getInstance().getRandom(0, numOfImages - 1);

		int label = (labelFileContent[labelOffset + randomIndex] & 0xFF);
		byte[] image = new byte[imageWidth * imageHeight];

		int index = 0;
		int startIndex = imageOffset + randomIndex * (imageWidth * imageHeight);
		for (int i = startIndex; i < (startIndex + (imageWidth * imageHeight)); i++) {
			image[index] = imageFileContent[i];
			index++;
		}

		return new Digit(label, image);
	}

	public byte[][] to2DByteArray(byte[] byte1DArray, int offset, int rows, int cols) {
		int index = 0;
		byte[][] byteArray = new byte[rows][cols];

		for (int i = 0; i < rows - 1; i++) {
			for (int j = (offset + (i * cols)); j < (offset + ((i + 1) * cols)); j++) {
				byteArray[i][j] = byte1DArray[index];
				index++;
			}
		}

		return byteArray;
	}

	public int size() {
		return imageWidth * imageHeight;
	}

	public BufferedImage resize(Image image) {
		BufferedImage bufferedImage = toBufferedImage(image);
		BufferedImage scaledBufferedImage = new BufferedImage(28, 28, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = scaledBufferedImage.createGraphics();
		graphics2D.drawImage(bufferedImage, 0, 0, 28, 28, null);
		graphics2D.dispose();

		return scaledBufferedImage;
	}

	public Image getImageFromCanvas(Canvas cv_canvas) {
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(javafx.scene.paint.Color.BLACK);
		return cv_canvas.snapshot(params, null);
	}

	public BufferedImage toBufferedImage(Image image) {
		return SwingFXUtils.fromFXImage(image, null);
	}
	
	public Image toImage(BufferedImage bufferedImage) {
		return SwingFXUtils.toFXImage(bufferedImage, null);
	}

	public byte[] toBytes(BufferedImage scaledImage) {
		byte[] imageBytes = null;

		try {
			ByteArrayOutputStream s = new ByteArrayOutputStream();
			ImageIO.write(scaledImage, "png", s);
			imageBytes = s.toByteArray();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return imageBytes;
	}

	public byte[] toMNIST(byte[] scaledImageBytes) {
		byte[] mnistImageBytes = new byte[ImageDecoder.getInstance().size()];
		if (mnistImageBytes.length != scaledImageBytes.length) {
			for (int i = 0; i < scaledImageBytes.length; i++) {
				mnistImageBytes[i] = scaledImageBytes[i];
			}
		} else {
			mnistImageBytes = scaledImageBytes;
		}
		return mnistImageBytes;
	}

	public boolean isNull() {
		return ImageDecoder.getInstance().getImageFileContent() == null
				|| ImageDecoder.getInstance().getLabelFileContent() == null;
	}
}
