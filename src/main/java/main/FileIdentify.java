package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class FileIdentify {

	public static void main(String[] args) {
		if (args.length < 2) {
			printHelp();
			System.exit(1);
		}

		String filePath = args[0];
		File file = new File(filePath);

		if (!file.exists()) {
			System.err.println("Error: File or directory does not exist - " + filePath);
			System.exit(1);
		}

		boolean reportToFile = false;
		File reportDirectory = new File("reports");
		if (!reportDirectory.exists()) {
			reportDirectory.mkdir(); // Create the directory if it doesn't exist
		}

		// Check for report flag before processing operations
		for (String arg : args) {
			if (arg.equals("--report") || arg.equals("-r")) {
				reportToFile = true;
				break; // Only set up report file if processing individual files
			}
		}

		if (file.isFile()) {
			processFile(file, args, reportToFile, reportDirectory);
		} else if (file.isDirectory()) {
			for (File subFile : file.listFiles()) {
				if (subFile.isFile()) {
					processFile(subFile, args, reportToFile, reportDirectory);
				}
			}
		}
	}

	private static void processFile(File file, String[] args, boolean reportToFile, File reportDirectory) {
		String reportFilename = reportDirectory.getPath() + File.separator + file.getName().replace('.', '_')
				+ "_report.txt";

		if (reportToFile) {
			setupReportFile(reportFilename);
		}

		for (int i = 1; i < args.length; i++) {
			String arg = args[i];
			if (!arg.equals("--report") && !arg.equals("-r")) {
				executeCommand(arg, file);
			}
		}

		if (reportToFile) {
			System.out.close(); // Close the file stream
		}
	}

	private static void executeCommand(String command, File file) {
		switch (command) {
		case "--check-signature":
		case "-c":
			checkSignature(file);
			break;
		case "--upload-virustotal":
		case "-u":
			uploadToVirusTotal(file);
			break;
		case "--analyze-tika":
		case "-a":
			analyzeWithTika(file);
			break;
		case "--help":
		case "-h":
			printHelp();
			break;
		default:
			System.err.println("Invalid operation: " + command);
			printHelp();
			System.exit(1);
		}
	}

	private static void setupReportFile(String reportFilename) {
		try {
			PrintStream fileOut = new PrintStream(new FileOutputStream(reportFilename, true));
			System.setOut(fileOut);
		} catch (FileNotFoundException e) {
			System.err.println("Error: Failed to create output file - " + e.getMessage());
			System.exit(1);
		}
	}

	private static void checkSignature(File file) {
		String result = FileSignatureChecker.checkFileSignature(file);
		System.out.println(result);
	}

	private static String uploadToVirusTotal(File file) {
		String fileId = VirusTotalUploader.uploadFileToVirusTotal(file);
		if (fileId == null) {
			System.out.println("Failed to upload file to VirusTotal.");
			return null;
		}

		VirusTotalReportAnalyzer a = new VirusTotalReportAnalyzer(VirusTotalUploader.getReportFromVirusTotal(fileId));
		a.printAnalysis();
		return fileId;
	}

	private static void analyzeWithTika(File file) {
		String report = new TikaStringExtractor().extractStringsFromFile(file);
		System.out.println(report);
	}

	private static void printHelp() {
		System.out.println(
				"Usage: java -jar FileIdentify-0.0.1-SNAPSHOT.jar <file_path> <operation> [--report] [<operation>...]");
		System.out.println("Operations:");
		System.out.println("  --check-signature, -c  : Check the file signature and suggest file extension.");
		System.out.println("  --upload-virustotal, -u: Upload the file and get a detailed report from VirusTotal.");
		System.out
				.println("  --analyze-tika, -a     : Analyze the file using Apache Tika to extract text and metadata.");
		System.out.println("  --help, -h             : Display help information.");
		System.out.println("Flags:");
		System.out.println("  --report, -r           : Write output to a report file named based on the input file.");
	}
}
