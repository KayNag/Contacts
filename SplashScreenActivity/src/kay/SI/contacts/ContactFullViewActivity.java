package kay.SI.contacts;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import kay.SI.contacts.controllers.AccelerometerManager;
import kay.SI.contacts.controllers.ContactAndroidIntents;
import kay.SI.contacts.controllers.ImageFetcher;
import kay.SI.contacts.controllers.JSONFetcher;
import kay.SI.contacts.controllers.URLFetcher;
import kay.SI.contacts.controllers.AccelerometerManager.AccelerometerListener;
import kay.SI.contacts.controllers.URLFetcher.OnFetched;
import kay.SI.contacts.models.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactFullViewActivity extends Activity implements OnTouchListener, AccelerometerListener {
	private Contact contact;
	private String GOOGLE_SEARCH_API = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
	float FORCE_THRESHOLD = 10;
	private ImageView pictureView;
	private URLFetcher<JSONObject> jsonFetcher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_full_view);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			try {
				contact = new Contact(new JSONObject(extras.getString("contact")));
				LoadContactData(contact);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		pictureView = (ImageView)findViewById(R.id.full_view_picture);
	}
	
	public void LoadContactData(final Contact contact) {
		TextView name = (TextView)findViewById(R.id.full_view_name);
		TextView jobTitle = (TextView)findViewById(R.id.full_view_job_title);
		TextView email = (TextView)findViewById(R.id.full_view_email);
		TextView telephone = (TextView)findViewById(R.id.full_view_telephone);
		TextView website = (TextView)findViewById(R.id.full_view_website);
		ImageView addContact = (ImageView)findViewById(R.id.full_view_add_contact);
		ImageView favoriteContact = (ImageView)findViewById(R.id.full_view_favorite_contact);
		
		email.setOnTouchListener(this);
		telephone.setOnTouchListener(this);
		website.setOnTouchListener(this);
		favoriteContact.setOnTouchListener(this);
		addContact.setOnTouchListener(this);
		
		name.setText(contact.getName());
		jobTitle.setText(contact.getJobTitle());
		email.setText(contact.getEmail());
		telephone.setText(contact.getPhone());
		website.setText(contact.getWebpage());
		
		loadPicture();
	}
	
	// TO-DO do a view for lazy loading of images
	private void loadPicture() {
		String pictureURL = contact.getPictureURL();
		if (!pictureURL.equals("")) {
			final ImageView pictureView = (ImageView)findViewById(R.id.full_view_picture);
			Bitmap picture = contact.getPicture();
			
			if (picture == null) {
				new ImageFetcher()
		        	.setOnFetched(new OnFetched() {
						@Override
						public <T> void onFetched(T response) {
							if (response != null) {
								final Bitmap image = (Bitmap)response;
								contact.setPicture(image);
								runOnUiThread(new Runnable() {
								    @Override
								    public void run() {
								    	pictureView.setImageBitmap(image);
								    	pictureView.invalidate();
								    }
								});
							}
						}
					})
		        	.execute(pictureURL);
			}
		}
	}

	public void setFavoritedImage(ImageView favoritedView) {
		int favoriteImageId;
		if (contact.getFavorited()) {
			favoriteImageId = R.drawable.favorite_contact;
		} else {
			favoriteImageId = R.drawable.favorite_contact_active;
		}
		Drawable favoriteImage = getResources().getDrawable(favoriteImageId);
		favoritedView.setImageDrawable(favoriteImage);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			switch (v.getId()) {
		    	case R.id.full_view_telephone:
            		call();
            		return true;
		    	case R.id.full_view_email:
            		sendEmail();
            		return true;
		    	case R.id.full_view_website:
            		openWebpage();
            		return true;
				case R.id.full_view_favorite_contact:
	        		favoriteContact();
	        		return false;
				case R.id.full_view_add_contact:
		    		addContact();
		    		return true;
			}
		}
		
		return false;
	}
	
	public void openWebpage() {
		Intent i = ContactAndroidIntents.openWebsite(contact);
		
		startActivity(i);
	}
	
	public void call() {
		Intent i = ContactAndroidIntents.callPhone(contact);
		
		startActivity(i);
	}
	
	public void sendEmail() {
		Intent i = ContactAndroidIntents.sendEmail(contact);

        startActivity(Intent.createChooser(i, getText(R.string.choose_email_client)));
	}
	
	public void addContact() {
		Intent i = ContactAndroidIntents.addToContacts(contact);
		
		startActivity(i);
		
	}
	
	public void favoriteContact() {
		
	}

	@Override
	public void onAccelerationChanged(float x, float y, float z) {
		
		
	}

	@Override
	public void onShake(float force) {
		if (force > FORCE_THRESHOLD && jsonFetcher == null) {
			searchGoogleImages();
		}
	}
	
	private void searchGoogleImages() {
		try {
			String query = URLEncoder.encode(contact.getName(), "utf-8");
		
			jsonFetcher = new JSONFetcher()
				.setOnFetched(new OnFetched() {
					@Override
					public <T> void onFetched(T googlSearch) {
						JSONObject response = (JSONObject)googlSearch;
						try {
							JSONObject responseData = response.getJSONObject("responseData");
							JSONArray results = responseData.getJSONArray("results");
							
							JSONObject imageResult = (JSONObject)results.get((int)(Math.random()*results.length()));
							
							String url = imageResult.getString("tbUrl");
							loadImage(url);
						} catch (JSONException e) {
							jsonFetcher = null;
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
			jsonFetcher.execute(GOOGLE_SEARCH_API + query);
		} catch (UnsupportedEncodingException e) {
			jsonFetcher = null;
			e.printStackTrace();
		}
	}
	
	private void loadImage(String url) {
		new ImageFetcher().setOnFetched(new OnFetched() {
			@Override
			public <T> void onFetched(T response) {
				final Bitmap image = (Bitmap)response;
				jsonFetcher = null;
				pictureView.setImageBitmap(image);
			}
		}).execute(url);
	}
	
	@Override
    public void onResume() {
        super.onResume();
         
        if (AccelerometerManager.isSupported(this)) {
            AccelerometerManager.startListening(this);
        }
    }
     
    @Override
    public void onStop() {
		super.onStop();
		 
		if (AccelerometerManager.isListening()) {
		    AccelerometerManager.stopListening();
		}  
    }
     
    @Override
    public void onDestroy() {
		super.onDestroy();
		
		if (AccelerometerManager.isListening()) {
		    AccelerometerManager.stopListening();
		}      
    }

}
