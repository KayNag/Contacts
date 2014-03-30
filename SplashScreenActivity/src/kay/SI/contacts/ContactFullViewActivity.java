package kay.SI.contacts;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import kay.SI.contacts.controllers.Icon;
import kay.SI.contacts.controllers.Intents;
import kay.SI.contacts.controllers.URLFetcher;
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

public class ContactFullViewActivity extends Activity implements
		OnTouchListener {
	private Contact contact;

	float FORCE_THRESHOLD = 10;
	private ImageView pictureView;
	private URLFetcher<JSONObject> jsonFetcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailview);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			try {
				contact = new Contact(new JSONObject(
						extras.getString("contact")));
				LoadContactData(contact);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		pictureView = (ImageView) findViewById(R.id.full_view_picture);
	}

	public void LoadContactData(final Contact contact) {
		TextView name = (TextView) findViewById(R.id.full_view_name);
		TextView jobTitle = (TextView) findViewById(R.id.full_view_job_title);
		TextView email = (TextView) findViewById(R.id.full_view_email);
		TextView telephone = (TextView) findViewById(R.id.full_view_telephone);
		TextView website = (TextView) findViewById(R.id.full_view_website);
		ImageView addContact = (ImageView) findViewById(R.id.full_view_add_contact);
		ImageView favoriteContact = (ImageView) findViewById(R.id.full_view_favorite_contact);

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
			final ImageView pictureView = (ImageView) findViewById(R.id.full_view_picture);
			Bitmap picture = contact.getPicture();

			if (picture == null) {
				new Icon().setOnFetched(new OnFetched() {
					@Override
					public <T> void onFetched(T response) {
						if (response != null) {
							final Bitmap image = (Bitmap) response;
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
				}).execute(pictureURL);
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
		Intent i = Intents.openWebsite(contact);

		startActivity(i);
	}

	public void call() {
		Intent i = Intents.callPhone(contact);

		startActivity(i);
	}

	public void sendEmail() {
		Intent i = Intents.sendEmail(contact);

		startActivity(Intent.createChooser(i,
				getText(R.string.choose_email_client)));
	}

	public void addContact() {
		Intent i = Intents.addToContacts(contact);

		startActivity(i);

	}

	public void favoriteContact() {

	}

}
