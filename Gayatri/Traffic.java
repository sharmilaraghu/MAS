import java.io.*;
import java.net.*;
import org.json.*;
import org.json.JSONException;
import org.json.JSONObject;

public class Traffic {
    //http://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452
    //https://maps.googleapis.com/maps/api/directions/json?origin=Chicago,IL&destination=Los+Angeles,CA&key=AIzaSyAisKYrqc3gbQ2E-9UFb-BOOP_5mJM3X1E
    private final String BASE_URL = "https://maps.googleapis.com/maps/api/";
    private final String GEOCODE = "geocode/json?latlng=";
    private final String DIRECTIONS = "directions/json?origin=";
    private final String TO     = "&destination=";
    private final String API_KEY = "&key=AIzaSyAisKYrqc3gbQ2E-9UFb-BOOP_5mJM3X1E";

    private String origin = "";
    private String destination = "";
    private double ori_lat, ori_lon, dst_lat, dst_lon;
    private String req = "";

    public Traffic (double ori_lat, double ori_lon, double dst_lat, double dst_lon) {
        this.ori_lat = ori_lat;
        this.ori_lon = ori_lon;
        this.dst_lat = dst_lat;
        this.dst_lon = dst_lon;
    }

    public int get() throws IOException {
        set_origin();
        set_destination();
        // System.out.println("From " + origin + " to " + destination);
        // System.out.println("Using \t " + BASE_URL + DIRECTIONS + origin + TO + destination + API_KEY);
        String url_made = BASE_URL + DIRECTIONS + origin + TO + destination + API_KEY;
        url_made = url_made.replaceAll(" ", "%20");
        URL url = new URL(url_made);
        URLConnection urlc = url.openConnection();
        urlc.setDoOutput(true);
        urlc.setAllowUserInteraction(false);
        BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
        String l;
        String result = "";
        int counter = 0;
        while ((l=br.readLine())!=null) {
            result += l;
        }
        br.close();
        JSONObject obj = new JSONObject(result);
        JSONArray routes = obj.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
        JSONObject dur = routes.getJSONObject(0).getJSONObject("duration");
        int txt = dur.getInt("value");
        // System.out.println("Gonna take " + dur);
        return txt;
    }

    public void set_origin() throws IOException {
        URL url = new URL(BASE_URL + GEOCODE + ori_lat + "," + ori_lon);
        URLConnection urlc = url.openConnection();
        urlc.setDoOutput(true);
        urlc.setAllowUserInteraction(false);
        BufferedReader br = new BufferedReader(new InputStreamReader(urlc
            .getInputStream()));
        String l;
        String result = null;
        while ((l=br.readLine())!=null) {
            if (l.contains("formatted_address")) {
                result = l.split(": \"")[1];
                result = result.split("\"")[0];
                origin = result;
                break;
            }
        }
        br.close();
    }

    public void set_destination() throws IOException {
        URL url = new URL(BASE_URL + GEOCODE + dst_lat + "," + dst_lon);
        URLConnection urlc = url.openConnection();
        urlc.setDoOutput(true);
        urlc.setAllowUserInteraction(false);
        BufferedReader br = new BufferedReader(new InputStreamReader(urlc
            .getInputStream()));
        String l;
        String result = null;
        while ((l=br.readLine())!=null) {
            if (l.contains("formatted_address")) {
                result = l.split(": \"")[1];
                result = result.split("\"")[0];
                destination = result;
                break;
            }
        }
        br.close();
    }
}
