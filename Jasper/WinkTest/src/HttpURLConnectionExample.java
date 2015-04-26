
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
	 
import javax.net.ssl.HttpsURLConnection;
	 
	public class HttpURLConnectionExample {
	 
	 
		public static void main(String[] args) throws Exception {
	 
			HttpURLConnectionExample http = new HttpURLConnectionExample();
	 
			System.out.println("Testing 1 - Send Http GET request");
			http.sendGet();
	 
			System.out.println("\nTesting 2 - Send Http PUT request");
			http.sendPut();
			
			System.out.println("\nTesting 3 - Send Http GET request");
			http.sendGet();
	 
		}
	 
		// HTTP GET request
		private void sendGet() throws Exception {
	 
			String url = "https://private-anon-7dcbba2c6-wink.apiary-mock.com";
			String urlParameters = "/powerstrips/qs1ga9_1234deadbeef";
//			String urlParameters = "/outlets/1tq1-654fed_18y5";
//			String urlParameters = "/outlets/u59h-654fee_ih17afg";
			URL obj = new URL(url + urlParameters);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	 
			// optional default is GET
			con.setRequestMethod("GET");
	 
			//con.setRequestProperty("Content-Type" ,"application/json");
			con.setRequestProperty("Authorization", "Bearer example_access_token_like_135fhn80w35hynainrsg0q824hyn");
	 
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			//print result
			System.out.println(response.toString());
	 
		}
	 
		// HTTP PUT request
		private void sendPut() throws Exception {
	 
			String url = "https://private-anon-7dcbba2c6-wink.apiary-mock.com";
			String urlParameters = "/powerstrips/qs1ga9_1234deadbeef";
//			String urlParameters = "/outlets/1tq1-654fed_18y5";
//			String urlParameters = "/outlets/u59h-654fee_ih17afg";
			
			URL obj = new URL(url + urlParameters);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	 
			//add request header
			con.setRequestMethod("PUT");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Content-Type" ,"application/json");
			con.setRequestProperty("Authorization", "Bearer example_access_token_like_135fhn80w35hynainrsg0q824hyn");
	 
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//			wr.writeBytes("{\n" +"\"name\": \"Living room\"\n" +"}");
//			wr.writeBytes("{\n    \"powered\": false\n}");
			wr.flush();
			wr.close();
	 
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'PUT' request to URL : " + url);
			System.out.println("Put parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			//print result
			System.out.println(response.toString());
	 
		}
	 
	}
