import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PostgrestClient {
    private GoTrue auth;
    private String apiUrl;
    private String apiKey;
    private String method = "GET";
    private JSONObject body = new JSONObject();
    private JSONArray bodyArray = new JSONArray();
    private Boolean csv = false;
    private Boolean isSingle = false;
    private Boolean upsert = false;

    private String selectQuery = "*";
    private ArrayList<String[]> filters = new ArrayList<String[]>();
    private String orderQuery = "";

    public PostgrestClient(String apiUrl, String apiKey, GoTrue auth){
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.auth = auth;
    }


    public JSONObject exec() {
        try {
            // Apply select
            String builtUrl = this.apiUrl + "?select=" + this.selectQuery;

            // Apply filters
            for (String[] filter : this.filters) {
                builtUrl += "&" + filter[0] + "=" + filter[1];
            }

            // Apply order
            if (!orderQuery.equals("")) {
                builtUrl += "&order=" + orderQuery;
            }
   

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(builtUrl))
                    .header("apikey", auth.genHeaders().get("apikey").toString())
                    .header("Content-Type", auth.genHeaders().get("Content-Type").toString());


            if (auth.genHeaders().get("Authorization") != null && auth.genHeaders().get("Authorization") != "") {
                requestBuilder.header("Authorization", auth.genHeaders().get("Authorization").toString());
            }

            if (this.csv) {
                requestBuilder.header("Accept", "text/csv");
            }

            if(this.isSingle){
                requestBuilder.header("Accept", "application/vnd.pgrst.object+json");
            }
            
            if (this.method != "GET") {
                String jsonToSend = (this.body.size() > 0) ? this.body.toJSONString() : this.bodyArray.toJSONString();
                requestBuilder.header("Prefer", "return=representation");
                if (this.upsert) {
                    requestBuilder.header("Prefer", "resolution=merge-duplicates, return=representation");
                }
                
               requestBuilder.method(this.method, HttpRequest.BodyPublishers.ofString(jsonToSend));
            }

            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            return new PostgrestResponse(response, isSingle,csv).getResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PostgrestClient select(String selectQuery){
        this.selectQuery = selectQuery;
        return this;
    }

    private <T>String joinArray(T[] array, String start , String end){
        String value = "";
        for(T val : array){
            value += val + ",";
        }

        //Remove last comma and add brackets
        value = start + value.substring(0, value.length() - 1) + end;

        return value;
    }

    private void addFilter(String query, String value){
        String[] filter = {query,value};
        this.filters.add(filter);
    }

    public PostgrestClient eq(String column, String value){
        addFilter(column, "eq." + value);
        return this;
    }

    public PostgrestClient neq(String column, String value){
        addFilter(column, "neq." + value);
        return this;
    }

    public PostgrestClient gt(String column, String value){
        addFilter(column, "gt." + value);
        return this;
    }

    public PostgrestClient gte(String column, String value){
        addFilter(column, "gte." + value);
        return this;
    }

    public PostgrestClient lt(String column, String value){
        addFilter(column, "lt." + value);
        return this;
    }

    public PostgrestClient lte(String column, String value){
        addFilter(column, "lte." + value);
        return this;
    }

    public PostgrestClient like(String column, String value){
        addFilter(column, "like." + value);
        return this;
    }

    public PostgrestClient ilike(String column, String value){
        addFilter(column, "ilike." + value);
        return this;
    }

    public PostgrestClient is(String column, String value){
        addFilter(column, "is." + value);
        return this;
    }

    public <T>PostgrestClient in(String column, T[] values){
        addFilter(column, "in." + joinArray(values, "(", ")"));
        return this;
    }

    public <T>PostgrestClient contains(String column, T[] values){
        addFilter(column, "cs." + joinArray(values, "{", "}"));
        return this;
    }

    public <T>PostgrestClient containedBy(String column, T[] values){
        addFilter(column, "cd." + joinArray(values, "{", "}"));
        return this;
    }

    public <T>PostgrestClient rangeGt(String column, T[] range){
        addFilter(column, "sr." + joinArray(range, "(", ")"));
        return this;
    }

    public PostgrestClient rangeGt(String column, String range){
        addFilter(column, "sr." + range);
        return this;
    }

    public <T>PostgrestClient rangeGte(String column, T[] range){
        addFilter(column, "nxl." + joinArray(range, "(", ")"));
        return this;
    }

    public PostgrestClient rangeGte(String column, String range){
        addFilter(column, "nxl." + range);
        return this;
    }

    public <T>PostgrestClient rangeLt(String column, T[] range){
        addFilter(column, "sl." + joinArray(range, "(", ")"));
        return this;
    }

    public PostgrestClient rangeLt(String column, String range){
        addFilter(column, "sl." + range);
        return this;
    }

    public <T>PostgrestClient rangeLte(String column, T[] range){
        addFilter(column, "nxr." + joinArray(range, "(", ")"));
        return this;
    }

    public PostgrestClient rangeLte(String column, String range){
        addFilter(column, "nxr." + range);
        return this;
    }

    public <T>PostgrestClient rangeAdjacent(String column, T[] range){
        addFilter(column, "adj." + joinArray(range, "(", ")"));
        return this;
    }

    public PostgrestClient rangeAdjacent(String column, String range){
        addFilter(column, "adj." + range);
        return this;
    }

    public <T>PostgrestClient overlaps(String column, T[] range){
        addFilter(column, "ov." + joinArray(range, "(", ")"));
        return this;
    }

    public PostgrestClient overlaps(String column, String range){
        addFilter(column, "ov." + range);
        return this;
    }

    public PostgrestClient textSearch(String column, String query,PostgrestFullTextSearchOptions options){
        addFilter(column, options.getRenderedType() + "fts"  + options.getRenderedConfig() + "." + query);
        return this;
    }

    public <T>PostgrestClient match(Map<String,T> columnValueMap){
        for (Map.Entry<String, T> entry : columnValueMap.entrySet()) {
            addFilter(entry.getKey(), "eq." + entry.getValue());
        }
        return this;
    }

    public PostgrestClient not(String column, String operator, String value){
        addFilter(column, "not." + operator + "." + value);
        return this;
    }

    public PostgrestClient or(String orFilters){
        addFilter("or", "(" + orFilters + ")");
        return this;
    }

    public PostgrestClient or(String orFilters, String foreignTable){
         addFilter(foreignTable+ ".or", "(" + orFilters + ")");
        return this;
    }

    public PostgrestClient filter(String column, String operator, String value){
        addFilter(column, operator + "." + value);
        return this;
    }

    public PostgrestClient order(String column, PostgrestOrderOptions options){
        String newOrderQuery = column + "." + options.getRenderedAscending() + options.getRenderedNullsFirst();

        if(!this.orderQuery.equals("")){
            this.orderQuery += "," + newOrderQuery;
        }else{
            this.orderQuery = newOrderQuery;
        }

        return this;
    }

    public PostgrestClient order(String column){
        String newOrderQuery = column;

        if(!this.orderQuery.equals("")){
            this.orderQuery += "," + newOrderQuery;
        }else{
            this.orderQuery = newOrderQuery;
        }

        return this;
    }

    public PostgrestClient limit(int limit){
        addFilter("limit", limit + "");
        return this;
    }

    public PostgrestClient limit(int limit, String foreignTable){
        addFilter(foreignTable + ".limit", limit + "");
        return this;
    }

    public PostgrestClient range(int from, int to){
        addFilter("offset", from + "");
        addFilter("limit", (to - from  + 1) + "");

        return this;
    }

    public PostgrestClient range(int from, int to, String foreignTable){
        addFilter(foreignTable + ".offset", from + "");
        addFilter(foreignTable + ".limit", (to - from  + 1) + "");

        return this;
    }

    public PostgrestClient single(){
        this.isSingle = true;
        addFilter("limit", "1");

        return this;
    }

    public PostgrestClient csv(){
        this.csv = true;

        return this;
    }

    public PostgrestClient insert(JSONObject body){
        this.method = "POST";
        this.body = body;

        return this;
    }

    public PostgrestClient insert(JSONArray body){
        this.method = "POST";
        this.bodyArray = body;

        return this;
    }

    public PostgrestClient upsert(JSONObject body){
        this.method = "POST";
        this.body = body;

        this.upsert = true;
        return this;
    }

    public PostgrestClient upsert(JSONArray body){
        this.method = "POST";
        this.bodyArray = body;

        this.upsert = true;
        return this;
    }

    public PostgrestClient update(JSONObject body){
        this.method = "PATCH";
        this.body = body;

        return this;
    }

    public PostgrestClient delete(){
        this.method = "DELETE";

        return this;
    }
}
