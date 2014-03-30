package kay.SI.contacts.controllers;

import java.util.ArrayList;

import kay.SI.contacts.models.Contact;


import org.json.JSONArray;
import org.json.JSONException;

public class ScreenInteractionContactFetcher extends URLFetcher<ArrayList<Contact>> {
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
