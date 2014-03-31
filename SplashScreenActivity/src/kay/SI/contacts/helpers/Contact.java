package kay.SI.contacts.helpers;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class Contact {
	private int id;
	private String name;
	private String jobTitle;
	private String thumbnailURL;
	private String phone;
	private String pictureURL;
	private String email;
	private String webpage;
	private Bitmap thumbnail;
	private Bitmap picture;
	private JSONObject json;
	private boolean favorited;
	private final int MAX_NAMES = 2;
	
	public Contact() {
		name = "UNDEFINED";
		jobTitle = "UNDEFINED";
	}
	
	public Contact(JSONObject json) {
		try {
			this.json = json;
			
			removeExtraSurnames();
			
			name = json.getString("name");
			jobTitle = json.getString("job_title");
			thumbnailURL = json.getString("thumbnail-url");
			pictureURL = json.getString("picture-url");
			email = json.getString("email");
			webpage = json.getString("webpage");
			phone = json.getString("phone");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void removeExtraSurnames() {
		try {
			String name = this.json.getString("name");
			String[] names = name.split(" ");
			int maxNames = Math.min(names.length, MAX_NAMES);
			
			name = "";
			for (int i = 0; i < maxNames; ++i) {
				name += names[i] + " ";
			}
			
			this.json.put("name", name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}
	
	public String getJobTitle() {
		return jobTitle;
	}
	
	public String getWebpage() {
		return webpage;
	}
	
	public String getThumbnailURL() {
		return thumbnailURL;
	}
	
	public Bitmap getThumbnail() {
		return thumbnail;
	}
	
	public void setThumbnail(Bitmap image) {
		thumbnail = image;
	}
	
	public String getPictureURL() {
		return pictureURL;
	}
	
	public Bitmap getPicture() {
		return picture;
	}
	
	public void setPicture(Bitmap image) {
		picture = image;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public int getID() {
		return id;
	}
	
	public Boolean getFavorited() {
		return favorited;
	}
	
	public void setFavorited(Boolean favorited) {
		try {
			json.put("favorited", favorited);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.favorited = favorited;
	}
	
	@Override
	public String toString() {
		return json.toString();
	}
}
