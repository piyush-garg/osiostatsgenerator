package com.github.piyush1594.statsGenerator;

import java.net.URL;

public class TableField {

    private String Name;

    private URL URL = null;

    private Integer count;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public URL getURL() {
        return URL;
    }

    public void setURL(URL URL) {
        this.URL = URL;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
