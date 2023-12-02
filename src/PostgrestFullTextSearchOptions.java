public class PostgrestFullTextSearchOptions {
    private String config = "";
    private String type = "plain";

    public PostgrestFullTextSearchOptions(String type){
        this.type = type;
    }

    public PostgrestFullTextSearchOptions(String type, String config){
        this.type = type;
        this.config = config;
    }

    private boolean validateType(String type){
        String[] validTypes = {"plain","websearch","phrase"};
        boolean isValid = false;

        for (String validType : validTypes) {
            if (validType.equals(type)) {
                isValid = true;
                break;
            }
        }

        return isValid;
    }

    public String getType(){
        return this.type;
    }

    public String getRenderedType(){
        if(this.type == "plain"){
            return "pl";
        }else if(this.type == "websearch"){
            return "w";
        }else if(this.type == "phrase"){
            return "ph";
        }else{
            return "";
        }
    }

    public String getConfig(){
        return this.config;
    }

    public String getRenderedConfig(){
        if(this.config == ""){
            return "";
        }else{
            return  "(" + this.config + ")";
        }
    }

    public void setType(String type){
        if(validateType(type)){
            this.type = type;
        } else {
            throw new RuntimeException("Invalid type");
        }
    }

    public void setConfig(String config){
        this.config = config;
    }
}
