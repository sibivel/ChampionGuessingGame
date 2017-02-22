package util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

public class Util {
	
	private static boolean showRequest = false;	
	
	// HTTP GET request
	public static JSONObject sendGet(String url) throws Exception {
			
			

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			//add request header
			//con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();
			if (showRequest){
				System.out.println("\nSending 'GET' request to URL : " + url);
				System.out.println("Response Code : " + responseCode);
			}

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			if (showRequest)
				System.out.println(response);
			return new JSONObject(response.toString());

		}
		
		public static JSONObject sendGet(String url, JSONObject data) throws Exception {

			StringBuilder parameters = new StringBuilder();
			Iterator iter = data.keys();
			while(iter.hasNext()){
				parameters.append("&");
				String key = (String) iter.next();
				String value = data.getString(key);
				parameters.append(key + "=" + value);
				
			}
			String urlParameters = parameters.toString();
			
			url = url + urlParameters;
			
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");
			
			
			
			//add request header
			//con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();
			if (showRequest){
				System.out.println("\nSending 'GET' request to URL : " + url);
				System.out.println("Response Code : " + responseCode);
			}

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			if (showRequest)
				System.out.println(response);
			return new JSONObject(response.toString());

		}
		
		// HTTP POST request
		public static JSONObject sendPost(String url, JSONObject data, boolean showRequest) throws Exception {

			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

			//add reuqest header
			con.setRequestMethod("POST");

			StringBuilder parameters = new StringBuilder();
			Iterator iter = data.keys();
			while(iter.hasNext()){
				String key = (String) iter.next();
				String value = data.getString(key);
				parameters.append(key + "=" + value);
				if(iter.hasNext())
					parameters.append("&");
			}
			String urlParameters = parameters.toString();
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			if (showRequest){
				System.out.println("\nSending 'POST' request to URL : " + url);
				System.out.println("Post parameters : " + urlParameters);
				System.out.println("Response Code : " + responseCode);
			}

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			if (showRequest)
				System.out.println(response);
			return new JSONObject(response.toString());

		}

}
