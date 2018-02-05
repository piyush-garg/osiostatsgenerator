package com.github.piyush1594.statsGenerator;

import org.json.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpURLCon {

    private static final String USER_AGENT = "Mozilla/5.0";

    private StringBuffer sendGet(URL api, String token) throws Exception {


        StringBuffer response;

            HttpURLConnection con = (HttpURLConnection) api.openConnection();

            con.setRequestProperty("User-Agent", USER_AGENT);

            if(!token.isEmpty()) {
                con.setRequestProperty("Authorization", "token " + token);
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response;

    }

    public List<String> getLabels(String request, String token) throws Exception {

        int page = 1;
        List<String> labels = new ArrayList<String>();
        labels.clear();

        while(true) {
            URL obj = new URL(request+ "?page=" + page);
            StringBuffer responses = sendGet(obj, token);

            JSONArray objects = new JSONArray(responses.toString());
            for (int i = 0; i < objects.length(); i++) {
                labels.add(objects.getJSONObject(i).getString("name"));
            }
            if(objects.length()==0){
                break;
            }
            else{
                page++;
            }
        }

        return labels;
    }

    public int getCount(String request, String token) throws Exception {

        URL obj = new URL(request);
        StringBuffer responses = sendGet(obj, token);

        JSONObject objects = new JSONObject(responses.toString());

        return objects.getInt("total_count");
    }
}
