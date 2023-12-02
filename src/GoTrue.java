import org.json.simple.JSONObject;

public class GoTrue {
    private String supabaseUrl;
    private String supabaseKey;
    private JSONObject user = null;
    private SupabaseTokenManager tokenManager;

    private String genAuthAPIUrl(String path){
        return this.supabaseUrl + "/auth/v1" + path;
    }

    public JSONObject genHeaders(){
        JSONObject headers = new JSONObject();
        headers.put("apikey", this.supabaseKey);
        headers.put("Content-Type", "application/json");

        if(this.tokenManager.getAccessToken() != ""){
            //Check if expired, if so refresh
            if(this.tokenManager.isExpired()){
                this.refreshSession();
            }
            headers.put("Authorization", "Bearer " + this.tokenManager.getAccessToken());
        }

        return headers;
    }

    public GoTrue(String supabaseUrl, String supabaseKey,SupabaseTokenManager tokenManager){
        this.supabaseUrl = supabaseUrl;
        this.supabaseKey = supabaseKey;
        this.tokenManager = tokenManager;
    }

    private void updateTokens(JSONObject session){
        this.tokenManager.setAccessToken(session.get("access_token").toString());
        this.tokenManager.setRefreshToken(session.get("refresh_token").toString());
        this.tokenManager.setExpiresIn(Integer.parseInt(session.get("expires_in").toString()));
        this.tokenManager.setTokenType(session.get("token_type").toString());
    }

    public JSONObject getSession(){
        JSONObject session = new JSONObject();

        session.put("access_token", this.tokenManager.getAccessToken());
        session.put("refresh_token", this.tokenManager.getRefreshToken());
        session.put("expires_in", this.tokenManager.getExpiresIn());
        session.put("token_type", this.tokenManager.getTokenType());

        return session;
    }

    private void fetchUser(){
        JSONObject headers = this.genHeaders();

        JSONObject user = Fetch.get(genAuthAPIUrl("/user"), headers);

        if(user != null){
            this.user = user;
        }
    }

    public void refreshSession(){
        JSONObject headers = this.genHeaders();
        JSONObject body = new JSONObject();

        body.put("refresh_token", this.tokenManager.getRefreshToken());

        JSONObject session = Fetch.post(genAuthAPIUrl("/token?grant_type=refresh_token"), body, headers);

        if(session != null){
            this.updateTokens(session);
        }
    }

    public void signUp(String email, String password){
        JSONObject headers = this.genHeaders();
        JSONObject body = new JSONObject();

        body.put("email", email);
        body.put("password", password);

        JSONObject newUser = Fetch.post(genAuthAPIUrl("/signup"), body, headers);

        if(newUser != null){
            this.user = (JSONObject) newUser.get("user");
        }
    }

    public void signInWithPassword(String email, String password){
        JSONObject headers = this.genHeaders();
        JSONObject body = new JSONObject();

        body.put("email", email);
        body.put("password", password);

        JSONObject session = Fetch.post(genAuthAPIUrl("/token?grant_type=password"), body, headers);

        if(session != null){
            this.updateTokens(session);
            this.fetchUser();
        }
    }

    public JSONObject getUser() {
        return user;
    }
}
