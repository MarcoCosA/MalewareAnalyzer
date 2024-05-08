package main;

import org.json.JSONObject;

public class VirusTotalReportAnalyzer {

	private JSONObject jsonResponse;

	public VirusTotalReportAnalyzer(JSONObject jsonResponse) {
		this.jsonResponse = jsonResponse;
	}

	public int countMaliciousDetections() {
		JSONObject data = jsonResponse.getJSONObject("data");
		JSONObject attributes = data.getJSONObject("attributes");
		JSONObject results = attributes.getJSONObject("results");

		int maliciousCount = 0;

		// Iterate over each vendor result
		for (String key : results.keySet()) {
			JSONObject result = results.getJSONObject(key);
			String category = result.getString("category");
			if ("malicious".equals(category)) {
				maliciousCount++;
			}
		}

		return maliciousCount;
	}

	public void printAnalysis() {
		System.out.println("Number of vendors that detected the file as malicious: " + countMaliciousDetections());
	}
}
