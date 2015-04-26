import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
import org.json.JSONException;
import org.json.JSONObject;

// socket, listen, accept
// JSON object, use API, get duration
// send duration
/*
{
	"trip" : {
		"start_lat" : 33.33333
		"start_lon" : -80.20202
		"end_lat" : 33.333333
		"end_lon" : -80.220202
	}
}
// what getting from Python frontend 
{
	"dest_lon" : 333.333
	"dest_lat" :-222.2
	"src_lon" : 333.3
	"src_lat" : -222.2
	"alarm_time" : "15:03"
	"from" : "Device ID"
	"salarm_id" : "SAlarm id"
}

// being sent to device
{
	"salarm_id" : "SAlarm id"
	"update_alarm" : "7:30"
}
*/
// Listening: 39999 
// Sending: 40000

public class Wrapper {

	public void send_rpc(JSONObject obj) throws IOException { 
		Socket socket = new Socket("localhost", 40000);
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		out.writeBytes(obj.toString() + '\n');
		socket.close();
	}

	public SUser listen_rpc() throws IOException {
		ServerSocket soc = new ServerSocket(39999);
		String rcv;
		Socket conn = soc.accept();
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		rcv = in.readLine();
		soc.close();

		JSONObject obj = new JSONObject(rcv);
		System.out.println(obj.getString("from"));
		System.out.println(obj.getDouble("dest_lon"));
		System.out.println(obj.getDouble("dest_lat"));
		System.out.println(obj.getDouble("src_lon"));
		System.out.println(obj.getDouble("src_lat"));

		String did = obj.getString("from");
		String sid = obj.getString("salarm_id");
		int alarm = obj.getInt("alarm_time");
		String key = did + "/" + sid;

		double dest_lon = obj.getDouble("dest_lon");
		double dest_lat = obj.getDouble("dest_lat");
		double src_lon = obj.getDouble("src_lon");
		double src_lat = obj.getDouble("src_lat");

		SUser user = null;

		//if (!map.containsKey(key)) {
			//System.out.println("Got new one :) " + key);
			user = new SUser(did, sid, alarm);
		//} else {
			//user = map.get(key);
		//}

		user.setDst(dest_lat, dest_lon);
		user.setSrc(src_lat, src_lon);
    	user.setJson(obj);
    	user.setKey(key);

		System.out.println("Trip time is " + user.getCurrTime());
		user.setTrip();
		//map.put(key,user);
	    return user;
	}

/*    public static void main(String[] args) throws IOException {
        Wrapper w = new Wrapper();
        for (int i = 0; i < 3; i++) {
            w.listen_rpc();
        }
        for (Map.Entry<String, SUser> entry : map.entrySet())
        {
              System.out.println(entry.getKey() + "/" + entry.getValue());
        }
    }*/
}
