package main;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TikaStringExtractor {

	/**
	 * Extracts text and metadata from a given file.
	 * 
	 * @param file the File object to analyze
	 * @return String containing the text content and metadata of the file
	 */
	public String extractStringsFromFile(File file) {
		StringBuilder reportBuilder = new StringBuilder();
		AutoDetectParser parser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler(-1); // No write limit
		Metadata metadata = new Metadata();

		try (InputStream inputStream = new FileInputStream(file)) {
			parser.parse(inputStream, handler, metadata, new ParseContext());

			reportBuilder.append("Extracted Text Content:\n").append(handler.toString()).append("\n\n");
			reportBuilder.append("Extracted Metadata:\n");
			String[] metadataNames = metadata.names();

			for (String name : metadataNames) {
				reportBuilder.append(name).append(": ").append(metadata.get(name)).append("\n");
			}
		} catch (IOException | SAXException | TikaException e) {
			reportBuilder.append("Error processing file: ").append(e.getMessage());
		}

		return reportBuilder.toString();
	}
}
