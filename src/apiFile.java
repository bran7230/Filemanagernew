import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class apiFile extends guiFile {
    private static final String API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        try {
            String response = callApi("");
            String definition = extractDefinition(response);
            System.out.println("Definition: " + definition);
        } catch (Exception e) {
            e.printStackTrace();
        }

        scanner.close();
    }

    private static String callApi(String word) throws Exception {
        String urlString = API_URL + word;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // Success
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } else {
            throw new Exception("Failed to call API: " + responseCode);
        }
    }

    private static String extractDefinition(String jsonResponse) {
        JSONArray jsonArray = new JSONArray(jsonResponse);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        JSONArray meaningsArray = jsonObject.getJSONArray("meanings");
        JSONObject meaningObject = meaningsArray.getJSONObject(0);
        JSONArray definitionsArray = meaningObject.getJSONArray("definitions");
        JSONObject definitionObject = definitionsArray.getJSONObject(0);
        return definitionObject.getString("definition");
    }
}
