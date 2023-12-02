import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Fetch {
    public static JSONObject post(String url, JSONObject body, JSONObject headers) {
        try {
            // Create connection
            URL endpoint = new URL(url);
            HttpURLConnection con = (HttpURLConnection) endpoint.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            // Set headers
            if (headers != null) {
                for (Object key : headers.keySet()) {
                    con.setRequestProperty(key.toString(), headers.get(key).toString());
                }
            }

            // Set body
            con.setDoOutput(true);
            con.getOutputStream().write(body.toString().getBytes("utf-8"));
            con.getOutputStream().flush();
            con.getOutputStream().close();

            // Get response code
            int responseCode = con.getResponseCode();

            // If response code is 200, get response body
            if (responseCode == 200) {
                // Read response body
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null){
                    content.append(inputLine);
                }
                in.close();

                // Parse response body
                JSONParser parser = new JSONParser();
                try{
                    JSONObject json = (JSONObject) parser.parse(content.toString());
                    return json;
                } catch (Exception e){
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject get(String url, JSONObject headers) {
        try {
            // Create connection
            URL endpoint = new URL(url);
            HttpURLConnection con = (HttpURLConnection) endpoint.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            // Set headers
            if (headers != null) {
                for (Object key : headers.keySet()) {
                    con.setRequestProperty(key.toString(), headers.get(key).toString());
                }
            }

            // Get response code
            int responseCode = con.getResponseCode();

            // If response code is 200, get response body
            if (responseCode == 200) {
                // Read response body
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null){
                    content.append(inputLine);
                }
                in.close();

                // Parse response body
                JSONParser parser = new JSONParser();
                try{
                    JSONObject json = (JSONObject) parser.parse(content.toString());
                    return json;
                } catch (Exception e){
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
