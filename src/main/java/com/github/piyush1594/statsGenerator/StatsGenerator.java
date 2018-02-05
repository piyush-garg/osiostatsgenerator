package com.github.piyush1594.statsGenerator;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.*;

public class StatsGenerator {

    private static List<QueryField> queries = new ArrayList<QueryField>();

    private static String orgName = "openshiftio";
    private static String repoName = "openshift.io";
    private static String baseUrl = "https://api.github.com/";
    private static String extendCountUrl = "search/issues?q=repo:" + orgName +"/" + repoName + "+is:issue+is:open";
    private static String extendLabelUrl = "repos/" + orgName + "/" + repoName + "/" + "labels";

    private static List<TableField> Stats = new ArrayList<TableField>();

    private static HttpURLCon client = new HttpURLCon();

    private static String token = "";

    public static void main(String[] args) throws Exception {

        if (args.length == 0){
            token = System.getenv("GITHUB_TOKEN");

            if (token == null) {
                token = "";
            }
        }
        else if (args.length == 1){
            token = args[0];
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/stats", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    private static void initialise() throws Exception {
        queries.clear();

        QueryField sev1 = new QueryField("SEV1 - urgent issues");
        sev1.setURL(baseUrl + extendCountUrl + "+label:SEV1-urgent");
        queries.add(0,sev1);

        QueryField sev2 = new QueryField("SEV2 - high issues");
        sev2.setURL(baseUrl + extendCountUrl + "+label:SEV2-high");
        queries.add(1,sev2);

        QueryField noLabels = new QueryField("Needs Triaging - No labels have been assigned");
        noLabels.setURL(baseUrl + extendCountUrl + "+no:label");
        queries.add(2,noLabels);

        QueryField noTypeLabels = new QueryField("Needs Triaging - Mandatory 'Type' label missing");
        List<String> labels = client.getLabels(baseUrl + extendLabelUrl, token);

        String queryParams = "";
        for (int i=0; i< labels.size();i++){
            if(labels.get(i).startsWith("type/")){
                queryParams = queryParams.concat("+-label:" + labels.get(i));
            }
        }
        noTypeLabels.setURL(baseUrl + extendCountUrl + queryParams);
        queries.add(3,noTypeLabels);

        QueryField needInput = new QueryField("Bugs that need PMs input on severity level");
        queryParams = "+label:type/bug";
        for (int i=0; i< labels.size();i++){
            if(labels.get(i).startsWith("SEV")){
                queryParams = queryParams.concat("+-label:" + labels.get(i));
            }
        }
        needInput.setURL(baseUrl + extendCountUrl + queryParams);
        queries.add(4,needInput);

        QueryField noAssignee = new QueryField("Issues that have not been assigned");
        noAssignee.setURL(baseUrl + extendCountUrl + "+no:assignee");
        queries.add(5,noAssignee);
    }

    private static void generateStats() throws Exception{

        Stats.clear();

        for(int i=0; i< queries.size(); i++) {
            Stats.add(i, new TableField());
            Stats.get(i).setName(queries.get(i).getName());
            Stats.get(i).setCount(client.getCount(queries.get(i).getURL(), token));
            Stats.get(i).setURL(new URL(queries.get(i).getURL().replace("https://api.github.com/search/issues?q=repo:openshiftio/openshift.io","https://github.com/openshiftio/openshift.io/issues?utf8=✓&q=")));
        }
    }

    private static String printHTMLStats() throws PebbleException, IOException {

        PebbleEngine engine = new PebbleEngine.Builder().build();
        PebbleTemplate compiledTemplate = engine.getTemplate("templates/stats_table.html");

        Writer writer = new StringWriter();
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("websiteTitle", "OpenShift.io GitHub Stats");

        context.put("stats", Stats);
        context.put("now", Calendar.getInstance().getTime());

        compiledTemplate.evaluate(writer, context);
        String output = writer.toString();
        return output;
    }

    private static class MyHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {

            try {
                initialise();

                generateStats();

                String response = printHTMLStats();

                t.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
