package application.filemanager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;

public class Filemanager {
	private static Filemanager instance;

	public static Filemanager getInstance() {
		if (instance == null) {
			instance = new Filemanager();
		}

		return instance;
	}

	public byte[] read(File file) {
		byte[] fileContent = null;
		byte[] buffer = new byte[1024];

		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			ByteArrayOutputStream fileOutputStream = new ByteArrayOutputStream();

			int bytes_read;
			while ((bytes_read = fileInputStream.read(buffer)) > 0) {
				fileOutputStream.write(buffer, 0, bytes_read);
			}
			fileInputStream.close();

			fileContent = fileOutputStream.toByteArray();
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileContent;
	}

	public byte[] unzip(File file, byte[] fileContent) {
		byte[] buffer = new byte[1024];

		try {
			int i = file.getName().lastIndexOf('.');
			String fileName = file.getName().substring(0, i);
			String fileExtension = file.getName().substring(i + 1);
			i = file.getAbsolutePath().lastIndexOf('\\');
			String fileDirectory = file.getAbsolutePath().substring(0, i);
			File decompressedFile = new File(fileDirectory + "\\" + fileName);

			if (fileExtension.equals("gz")) {
				ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileContent);
				GZIPInputStream gZIPInputStream = new GZIPInputStream(fileInputStream);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

				int bytes_read;
				while ((bytes_read = gZIPInputStream.read(buffer)) > 0) {
					byteArrayOutputStream.write(buffer, 0, bytes_read);
				}
				gZIPInputStream.close();
				fileInputStream.close();

				fileContent = byteArrayOutputStream.toByteArray();
				byteArrayOutputStream.close();

				FileOutputStream fileOutputStream = new FileOutputStream(decompressedFile);
				fileOutputStream.write(fileContent);
				fileOutputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileContent;
	}
}
