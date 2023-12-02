import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.simple.JSONObject;

public class FunctionsClient {
    private String supabaseUrl;
    private String supabaseKey;
    private GoTrue auth;

    public FunctionsClient(String supabaseUrl, String supabaseKey, GoTrue auth){
        this.supabaseUrl = supabaseUrl;
        this.supabaseKey = supabaseKey;
        this.auth = auth;
    }

    public String invoke(String name){
        try{
            String functionURL = this.supabaseUrl + "/functions/v1/" + name;
            //Default POST request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest.Builder request = HttpRequest.newBuilder()
            .uri(URI.create(functionURL))
            .header("apikey", this.supabaseKey)
            .header("Content-Type", "application/json");

            if (auth.genHeaders().get("Authorization") != null && auth.genHeaders().get("Authorization") != "") {
                request.header("Authorization", auth.genHeaders().get("Authorization").toString());
            }

            HttpResponse<String> res = client.send(request.build(), HttpResponse.BodyHandlers.ofString());
            
            return res.body();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public String invoke(String name, JSONObject body){
        try{
            String functionURL = this.supabaseUrl + "/functions/v1/" + name;
            //Default POST request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest.Builder request = HttpRequest.newBuilder()
            .uri(URI.create(functionURL))
            .header("apikey", this.supabaseKey)
            .header("Content-Type", "application/json");

            if (auth.genHeaders().get("Authorization") != null && auth.genHeaders().get("Authorization") != "") {
                request.header("Authorization", auth.genHeaders().get("Authorization").toString());
            }

            HttpResponse<String> res = client.send(request.POST(HttpRequest.BodyPublishers.ofString(body.toJSONString())).build(), HttpResponse.BodyHandlers.ofString());    
            return res.body();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public String invoke(String name, JSONObject body, JSONObject headers){
        try{
            String functionURL = this.supabaseUrl + "/functions/v1/" + name;
            //Default POST request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest.Builder request = HttpRequest.newBuilder()
            .uri(URI.create(functionURL))
            .header("apikey", this.supabaseKey)
            .header("Content-Type", "application/json");

            if (auth.genHeaders().get("Authorization") != null && auth.genHeaders().get("Authorization") != "") {
                request.header("Authorization", auth.genHeaders().get("Authorization").toString());
            }

            for (Object key : headers.keySet()) {
                request.header(key.toString(), headers.get(key).toString());
            }

            HttpResponse<String> res = client.send(request.POST(HttpRequest.BodyPublishers.ofString(body.toJSONString())).build(), HttpResponse.BodyHandlers.ofString());
            
            return res.body();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public String invoke(String name, JSONObject body, JSONObject headers, String method){
        try{
            String functionURL = this.supabaseUrl + "/functions/v1/" + name;
            //Default POST request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest.Builder request = HttpRequest.newBuilder()
            .uri(URI.create(functionURL))
            .header("apikey", this.supabaseKey)
            .header("Content-Type", "application/json");

            if (auth.genHeaders().get("Authorization") != null && auth.genHeaders().get("Authorization") != "") {
                request.header("Authorization", auth.genHeaders().get("Authorization").toString());
            }

            for (Object key : headers.keySet()) {
                request.header(key.toString(), headers.get(key).toString());
            }

            request.method(method, HttpRequest.BodyPublishers.ofString(body.toJSONString()));

            HttpResponse<String> res = client.send(request.build(), HttpResponse.BodyHandlers.ofString());
            
            return res.body();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }


}
