package application.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;

public class GeneralUtilities {
	private static GeneralUtilities instance;

	public static GeneralUtilities getInstance() {
		if (instance == null) {
			instance = new GeneralUtilities();
		}

		return instance;
	}

	protected GeneralUtilities() {
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
