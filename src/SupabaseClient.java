public class SupabaseClient {
    private String supabaseUrl;
    private String supabaseKey;
    private PostgrestClient client;
    public GoTrue auth;
    public FunctionsClient functions;
    private SupabaseTokenManager tokenManager = new SupabaseTokenManager();

    public SupabaseClient(String supabaseUrl, String supabaseKey){
        this.supabaseUrl = supabaseUrl;
        this.supabaseKey = supabaseKey;
        this.auth = new GoTrue(this.supabaseUrl, this.supabaseKey,tokenManager);
        this.functions = new FunctionsClient(supabaseUrl, supabaseKey, auth);
    }

    public PostgrestClient from(String table){
        this.client = new PostgrestClient(this.supabaseUrl + "/rest/v1/" + table, this.supabaseKey, this.auth);
        return this.client;
    }
}
