// { ascending?: boolean; nullsFirst?: boolean; foreignTable?: string }
public class PostgrestOrderOptions {

    private boolean ascending = true;
    private boolean nullsFirst = false;
    private String foreignTable = "";

    public PostgrestOrderOptions(boolean ascending, boolean nullsFirst, String foreignTable){
        this.ascending = ascending;
        this.nullsFirst = nullsFirst;
        this.foreignTable = foreignTable;
    }

    public PostgrestOrderOptions(boolean ascending, boolean nullsFirst){
        this.ascending = ascending;
        this.nullsFirst = nullsFirst;
    }

    public PostgrestOrderOptions(boolean ascending){
        this.ascending = ascending;
    }

    public PostgrestOrderOptions(){
    }

    public boolean getAscending(){
        return this.ascending;
    }

    public boolean getNullsFirst(){
        return this.nullsFirst;
    }

    public String getForeignTable(){
        return this.foreignTable;
    }

    public void setAscending(boolean ascending){
        this.ascending = ascending;
        
    }

    public void setNullsFirst(boolean nullsFirst){
        this.nullsFirst = nullsFirst;
    }

    public void setForeignTable(String foreignTable){
        this.foreignTable = foreignTable;
    }

    public String getRenderedAscending(){
        if(this.ascending){
            return "asc";
        }else{
            return "desc";
        }
    }

    public String getRenderedNullsFirst(){
        if(this.nullsFirst){
            return ".nullsfirst";
        }else{
            return ".nullslast";
        }
    }

    public String getRenderedForeignTable(){
        if(this.foreignTable == ""){
            return "";
        }else{
            return this.foreignTable + ".";
        }
    }

    public String getRendered(){
        return this.getRenderedForeignTable() + this.getRenderedAscending() + "." + this.getRenderedNullsFirst();
    }
    
}
