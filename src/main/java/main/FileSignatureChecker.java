package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileSignatureChecker {
	private static final Map<String, String> fileSignatureToExtension = new HashMap<>();

	static {
		fileSignatureToExtension.put("25504446", "pdf"); // PDF
		fileSignatureToExtension.put("89504E47", "png"); // PNG
		fileSignatureToExtension.put("47494638", "gif"); // GIF
		fileSignatureToExtension.put("FFD8FF", "jpg"); // JPEG
		fileSignatureToExtension.put("504B0304", "zip"); // ZIP
		fileSignatureToExtension.put("52617221", "rar"); // RAR
		fileSignatureToExtension.put("D0CF11E0", "doc"); // DOC or XLS or PPT
		fileSignatureToExtension.put("504B0304", "docx"); // DOCX or XLSX or PPTX
		fileSignatureToExtension.put("D0CF11E0", "msi"); // Microsoft Installer
		fileSignatureToExtension.put("25215053", "ps"); // PostScript
		fileSignatureToExtension.put("49492A00", "tif"); // TIFF
		fileSignatureToExtension.put("4D4D002A", "tif"); // TIFF
		fileSignatureToExtension.put("424D", "bmp"); // BMP
		fileSignatureToExtension.put("7B5C727466", "rtf"); // RTF
		fileSignatureToExtension.put("AC9EBD8F", "qdf"); // Quicken
		fileSignatureToExtension.put("E3828596", "pwl"); // Windows Password
		fileSignatureToExtension.put("504B0304", "jar"); // JAR
		fileSignatureToExtension.put("49443303", "mp3"); // MP3
		fileSignatureToExtension.put("000001BA", "mpg"); // MPG
		fileSignatureToExtension.put("000001B3", "mpg"); // MPG
		fileSignatureToExtension.put("3026B2758E66CF11", "wmv"); // WMV
		fileSignatureToExtension.put("4F676753", "ogg"); // OGG
		fileSignatureToExtension.put("1A45DFA3", "mkv"); // MKV
		fileSignatureToExtension.put("00014244", "mp4"); // MP4
		fileSignatureToExtension.put("00000100", "ico"); // ICO
		fileSignatureToExtension.put("52494646", "wav"); // WAV
		fileSignatureToExtension.put("664C6143", "flac"); // FLAC
		fileSignatureToExtension.put("4344303031", "iso"); // ISO
		fileSignatureToExtension.put("435753", "swf"); // SWF
		fileSignatureToExtension.put("66747970", "mp4"); // MP4
		fileSignatureToExtension.put("00002000", "mp4"); // MP4
		fileSignatureToExtension.put("377ABCAF271C", "7z"); // 7z
		fileSignatureToExtension.put("213C617263683E", "deb"); // Debian package
		fileSignatureToExtension.put("FD377A585A00", "xz"); // XZ
		fileSignatureToExtension.put("53514C6974652066", "db"); // SQLite Database
		fileSignatureToExtension.put("6674797068656963", "heic"); // HEIC, High Efficiency Image File Format
		fileSignatureToExtension.put("38425053", "psd"); // PSD, Adobe Photoshop Document
		fileSignatureToExtension.put("CAFEBABE", "class"); // Java class file
		fileSignatureToExtension.put("EFBBBF", "py"); // Python script with UTF-8 BOM
	}

	public static String checkFileSignature(File file) {
		String fileSignature = getFileSignature(file);
		if (fileSignature == null) {
			System.out.println("Could not read file signature.");
			return "Error: File signature could not be read.";
		}

		System.out.println("Detected file signature: " + fileSignature);
		String matchedExtension = matchSignature(fileSignature);
		if (matchedExtension == null) {
			return "Unknown file signature. Cannot determine file type.";
		} else {
			return "Suggested file extension: ." + matchedExtension;
		}
	}

	private static String matchSignature(String fileSignature) {
		for (String key : fileSignatureToExtension.keySet()) {
			if (fileSignature.toUpperCase().startsWith(key)) {
				return fileSignatureToExtension.get(key);
			}
		}
		return null;
	}

	private static String getFileSignature(File file) {
		try (FileInputStream fis = new FileInputStream(file)) {
			byte[] bytes = new byte[8]; // Read first 8 bytes
			if (fis.read(bytes) != 8) {
				return null;
			}
			return bytesToHex(bytes).toUpperCase();
		} catch (IOException e) {
			return null;
		}
	}

	private static String bytesToHex(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
