package application.filemanager;

import java.io.File;

public class MNISTImageDecoder {
	private static MNISTImageDecoder instance;

	public static MNISTImageDecoder getInstance() {
		if (instance == null) {
			instance = new MNISTImageDecoder();
		}

		return instance;
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
		byte[] imageFileContent = Filemanager.getInstance().read(imageFile);
		this.imageFileContent = Filemanager.getInstance().unzip(imageFile, imageFileContent);
		
		byte[] labelFileContent = Filemanager.getInstance().read(labelFile);
		this.labelFileContent = Filemanager.getInstance().unzip(labelFile, labelFileContent);
	}
}
