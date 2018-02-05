package com.github.piyush1594.statsGenerator;

public class QueryField {

    private String Name;

    private String URL;

    public QueryField(String Name){
        this.setName(Name);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
