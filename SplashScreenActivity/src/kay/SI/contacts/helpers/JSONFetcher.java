package kay.SI.contacts.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;


public class JSONFetcher extends URLFetcher<JSONObject> {
	
	@Override
	protected JSONObject postProcess(byte[] response) {
	String jsonResponse = new String(response);
		JSONObject result = new JSONObject();
		try {
			result = new JSONObject(jsonResponse);
		} catch (JSONException e) {
			this.retry();
		}
		
		return result;
	}
	
	@Override
	protected byte[] doInBackground(String... arg0) {
		InputStream is = null;
		try {
			is = new URL(arg0[0]).openStream();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] response = new byte[0];
		JSONObject jsonResult = new JSONObject();
		
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      jsonResult = new JSONObject(jsonText);
	    } catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    this.result = jsonResult;
	    return response;
	}
	
	private static String readAll(BufferedReader rd) throws IOException {
		StringBuilder builder = new StringBuilder();
		String aux = "";

		while ((aux = rd.readLine()) != null) {
		    builder.append(aux);
		}

		return builder.toString();
	}
}