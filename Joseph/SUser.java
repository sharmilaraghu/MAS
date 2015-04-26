import org.json.*;
import java.io.*;
import java.util.*;

public class SUser {
  public String deviceID;
  public String salarmID;
  public int trip_time; // in seconds 
  public double src_lat,src_lon,dst_lat,dst_lon;
  private int alarm;
  private Traffic w;
  private JSONObject json;
  private String key;

  public SUser(String deviceID, String salarmID, int alarm) {
          this.deviceID = deviceID;
          this.salarmID = salarmID;
          this.alarm = alarm;
  }

  public void setSrc(double lat, double lon) {
          this.src_lat = lat;
          this.src_lon = lon;
  }

  public void setDst(double lat, double lon) {
          this.dst_lat = lat;
          this.dst_lon = lon;
  }

  public void setTrip() throws IOException{
          this.trip_time = getCurrTime();
  }

  public void setJson(JSONObject json) {
    this.json = json;
  }

  public void setKey(String key) { 
    this.key = key;
  }

  public String getSid() {return salarmID; }

  public String getDid() {return deviceID; }

  public int getTripTime() {return trip_time;}

  public String getKey() { return key; }
  
  public JSONObject getJson() {
    return json;
  }

  public int getCurrTime() throws IOException{
    if (w == null) 
      w = new Traffic (src_lat, src_lon, dst_lat, dst_lon);
    return w.get();
  }

  public int getAlarm() { return alarm; }

  public String toString(){
    return deviceID + " " + trip_time;
  }
}
