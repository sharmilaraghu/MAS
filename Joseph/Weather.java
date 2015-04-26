
import java.io.*;
import java.net.*;


public class Weather {
    private final String BASE_URL = "http://api.wunderground.com/api/";
    private final String PRIVATE_KEY = "6badad19dec12124/";
    private final String BY_GEO = "geolookup/q/";
    private final String DOT_JSON = ".json";

    private String req = "";

    public Weather (double lat, double lon) {
        this.req = BASE_URL + PRIVATE_KEY + BY_GEO + lat + "," + lon + DOT_JSON;
    }

    public String get() throws IOException {
        URL url = new URL(req);
        System.out.println("REQ \t" + req);
        URLConnection urlc = url.openConnection();
        urlc.setDoOutput(true);
        urlc.setAllowUserInteraction(false);
        BufferedReader br = new BufferedReader(new InputStreamReader(urlc
            .getInputStream()));
        String l;
        String result = null;
        while ((l=br.readLine())!=null) {
            result += l;
        }
        br.close();
        return result;
    }

    /* Usage example */
    public static void main(String[] args) throws IOException {
        double lat = 33.7758;
        double lon = -84.3947;
        Weather w = new Weather(lat, lon);
        System.out.println(w.get());
    }
}