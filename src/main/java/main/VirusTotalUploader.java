package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class VirusTotalUploader {
	private static final String API_KEY = "864b8d79af5f331ae01e2bf48a93a888478fde783ba377e46ca7d85e34712136";

	public static String uploadFileToVirusTotal(File file) {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPost post = new HttpPost("https://www.virustotal.com/api/v3/files");

			// Prepare the file and the API key header
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addTextBody("x-apikey", API_KEY, ContentType.TEXT_PLAIN);
			builder.addBinaryBody("file", new FileInputStream(file), ContentType.APPLICATION_OCTET_STREAM,
					file.getName());

			// Set the complete entity
			post.setEntity(builder.build());

			// Ensure headers are set correctly
			post.setHeader("X-Apikey", API_KEY);
			post.setHeader("Accept", "application/json");

			org.apache.http.HttpResponse response = httpClient.execute(post);
			String result = EntityUtils.toString(response.getEntity());

			// Print and check the response
			System.out.println("Response from VirusTotal: " + result);

			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.has("data")) {
				System.out.println("The JSON response does not contain 'data'.");
				return null;
			}

			return jsonObject.getJSONObject("data").getString("id");
		} catch (IOException e) {
			System.out.println("Failed to upload file to VirusTotal: " + e.getMessage());
			return null;
		}
	}

	public static JSONObject getReportFromVirusTotal(String fileId) {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpGet get = new HttpGet("https://www.virustotal.com/api/v3/analyses/" + fileId);
			get.setHeader("x-apikey", API_KEY);
			get.setHeader("accept", "application/json");

			boolean analysisComplete = false;
			while (!analysisComplete) {
				org.apache.http.HttpResponse response = httpClient.execute(get);
				String result = EntityUtils.toString(response.getEntity());
				JSONObject report = new JSONObject(result);

				String status = report.getJSONObject("data").getJSONObject("attributes").getString("status");
				if ("completed".equals(status)) {
					analysisComplete = true;
					System.out.println("Analysis complete. Report: ");
					System.out.println(result); // or further parse to display specific info
					return report;
				} else {
					System.out.println("Analysis not complete. Waiting...");
					Thread.sleep(10000); // wait for 10 seconds before polling again
				}
			}
		} catch (IOException | InterruptedException e) {
			System.out.println("Failed to retrieve report from VirusTotal: " + e.getMessage());
		}
		return null;
	}
}
