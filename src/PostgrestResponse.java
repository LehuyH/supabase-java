import java.net.http.HttpResponse;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

public class PostgrestResponse {
    
    private JSONObject result = new JSONObject();

    public PostgrestResponse(HttpResponse<String> response, boolean isSingle) {
        result.put("data",null);
        result.put("error",null);

        try {
            // Get response code
            Integer responseCode = response.statusCode();

            // If response code is 2xx, get response body
            if(responseCode.toString().startsWith("2")){
                if(isSingle){
                    this.applySingle(response.body(),"data");
                } else {
                    this.applyArray(response.body(),"data");
                }
            } else {
                this.applySingle(response.body(),"error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void applySingle(String body, String key){
        try {
            // Parse response body
            JSONParser parser = new JSONParser();
            try{
                JSONObject json = (JSONObject) parser.parse(body);
                result.put(key,json);
            } catch (Exception e){
                result.put(key, body);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void applyArray(String body,String key){
        try {
            // Parse response body
            JSONParser parser = new JSONParser();
            JSONArray json = (JSONArray) parser.parse(body);

            result.put(key,json);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public JSONObject getResult() {
        return result;
    }
}
