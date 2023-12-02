public class SupabaseTokenManager {
    private String accessToken = "";
    private String refreshToken = "";
    private String tokenType = "";
    private long expiresIn = -1;
    private long lastRefreshed = -1;

    public SupabaseTokenManager(String accessToken, String refreshToken, String tokenType, int expiresIn){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.lastRefreshed = System.currentTimeMillis();
    }

    public SupabaseTokenManager(){
    }

    public String getAccessToken(){
        return this.accessToken;
    }

    public String getRefreshToken(){
        return this.refreshToken;
    }

    public String getTokenType(){
        return this.tokenType;
    }

    public long getExpiresIn(){
        return this.expiresIn;
    }

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
        this.lastRefreshed = System.currentTimeMillis();
    }

    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void setTokenType(String tokenType){
        this.tokenType = tokenType;
    }

    public void setExpiresIn(int expiresIn){
        this.expiresIn = expiresIn;
    }

    public boolean isExpired(){
        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - this.lastRefreshed;
        return timeElapsed > this.expiresIn * 1000;
    }
}
