package application.utilities;

import java.io.File;

import application.network.Digit;

public class MNISTImageDecoder {
	private static MNISTImageDecoder instance;

	public static MNISTImageDecoder getInstance() {
		if (instance == null) {
			instance = new MNISTImageDecoder();
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
		byte[] imageFileContent = FileManager.getInstance().read(imageFile);
		this.imageFileContent = FileManager.getInstance().unzip(imageFile, imageFileContent);

		byte[] labelFileContent = FileManager.getInstance().read(labelFile);
		this.labelFileContent = FileManager.getInstance().unzip(labelFile, labelFileContent);

		this.labelIndex = labelOffset;
		this.imageIndex = imageOffset;
	}

	public Digit readNextDigit() {
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
}
