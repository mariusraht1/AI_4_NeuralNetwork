package application.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

import application.Main;
import javafx.stage.FileChooser;

public class FileManager {
	private static FileManager instance;

	public static FileManager getInstance() {
		if (instance == null) {
			instance = new FileManager();
		}

		return instance;
	}
	
	public File chooseFile() {
	    FileChooser fileChooser = new FileChooser();
	    return fileChooser.showOpenDialog(Main.getPrimaryStage());
	}
	

	public byte[] read(File file) {
		byte[] fileContent = null;

		try {
			fileContent = Files.readAllBytes(Paths.get(file.toURI()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileContent;
	}

	public byte[] unzip(File file, byte[] fileContent) {
		byte[] buffer = new byte[1024];

		try {
			int i = file.getName().lastIndexOf('.');
			if (i > -1) {
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileContent;
	}
}
