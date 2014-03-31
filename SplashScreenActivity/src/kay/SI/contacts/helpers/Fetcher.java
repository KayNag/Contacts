package kay.SI.contacts.helpers;

import java.util.ArrayList;




import org.json.JSONArray;
import org.json.JSONException;

public class Fetcher extends URLFetcher<ArrayList<Contact>> {
	@Override
	protected ArrayList<Contact> postProcess(byte[] response)
	
	
	 {
		String jsonResponse = new String(response);
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		
		try {
			JSONArray arr = new JSONArray(jsonResponse);
			
			for (int i = 0; i < arr.length(); i++) {
				contacts.add(new Contact(arr.getJSONObject(i)));
			}
		} catch (JSONException e) {
			this.retry();
		}
		
		return contacts;
	}
}
