package helios.wazirx.crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

class ConnectionInfo {
    private final MarketLookup.Market market;
    private final AccessPointLoopkup.AccessPoint accessPoint;

    ConnectionInfo(AccessPointLoopkup.AccessPoint accessPoint, MarketLookup.Market market) {
        this.accessPoint = accessPoint;
        this.market = market;
    }

    String getMarket() {
        return market.getMarket();
    }

    String getAccessPoint() {
        return accessPoint.getURL();
    }
}

class ResponseParser {
    private final JSONParser parser;

    ResponseParser() {
        parser = new JSONParser();
    }

    public JSONObject parse(String response) throws org.json.simple.parser.ParseException {
        return (JSONObject) parser.parse(response);
    }

    public JSONObject parse(InputStream inputStream) throws org.json.simple.parser.ParseException, IOException {
        InputStreamReader in = new InputStreamReader(inputStream);
        JSONObject jsonResponse = (JSONObject) parser.parse(in);
        in.close();
        return jsonResponse;
    }
}

class ConnectionHandler extends ConnectionInfo {

    private URL url;
    private HttpURLConnection connection;
    private ResponseParser parser;

    ConnectionHandler(AccessPointLoopkup.AccessPoint accessPoint, MarketLookup.Market market) throws IOException {
        super(accessPoint, market);
    }

    public void init() throws IOException {
        setURL();
        openConnection();
        prepareRequest();
        connect();
        parser = new ResponseParser();
    }

    private void setURL() throws MalformedURLException {
        url = new URL(getAccessPoint() + "?market=" + getMarket());
    }

    private void openConnection() throws IOException {
        connection = (HttpURLConnection) url.openConnection();
    }

    private void prepareRequest() throws ProtocolException {
        connection.setRequestMethod("GET");
    }

    private void connect() throws IOException {
        connection.connect();
    }

    public void testPrint() throws IOException {
        System.out.println(connection.getContent());
        System.out.println(connection.getContentType());
        for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
            System.out.println(header.getKey() + " :\t" + header.getValue());
        }
        System.out.println("URL = " + connection.getURL());
        System.out.println(connection.getContentType());
        System.out.println(connection.getContent().toString());

        BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        System.out.println(r.readLine());
    }

    JSONObject getResponse() throws IOException, ParseException {
        JSONObject jsonResponse = parser.parse(connection.getInputStream());
        // System.out.println(jsonResponse.entrySet());
        return jsonResponse;
    }
}

public class Price {
    private final ConnectionHandler conHandler;

    Price(AccessPointLoopkup.AccessPoint accessPoint, MarketLookup.Market market) throws IOException {
        conHandler = new ConnectionHandler(accessPoint, market);
    }

    public double getCurrAskPrice() throws IOException, ParseException {
        conHandler.init();
        JSONObject jsonResponse = conHandler.getResponse();
        // System.out.println(jsonResponse.toJSONString());
        String price = (String) jsonResponse.get("askPrice");
        return Double.parseDouble(price);
    }

}
