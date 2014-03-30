package kay.SI.contacts.controllers;

import java.util.ArrayList;

import kay.SI.contacts.R;

import kay.SI.contacts.controllers.Icon;
import kay.SI.contacts.controllers.Mapper;
import kay.SI.contacts.controllers.URLFetcher.OnFetched;
import kay.SI.contacts.models.Contact;



import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class Adapter extends ArrayAdapter<Contact> implements Mapper.OnContactsChangeListener
{   
    private ArrayList<Contact> list;
    private Mapper mapper;
    private int contactViewId;
    private Drawable noAvatar;
    private Activity activity;

    public Adapter(Activity activity, int contactViewResourceId, Mapper mapper) 
    {
        super(activity, contactViewResourceId, mapper.get());
        mapper.setOnContactsChangeListener(this);
        
        this.list = mapper.get();
        this.mapper = mapper;
        this.contactViewId = contactViewResourceId;
        this.activity = activity;
        this.noAvatar = activity.getResources().getDrawable(R.drawable.no_thumbnail);
    }
         
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
        	LayoutInflater inflator = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);          
        	convertView = inflator.inflate(contactViewId, null);
        }
        
        TextView nameView = (TextView) convertView.findViewById(R.id.contact_name);
        TextView jobView = (TextView) convertView.findViewById(R.id.contact_job_position);
        ImageView starredView = (ImageView) convertView.findViewById(R.id.contact_starred);
        
        Contact contact = mapper.getContact(position);
        
        convertView.setTag(contact.getName());
        
        nameView.setText(contact.getName());
        jobView.setText(contact.getJobTitle());
        
        final ImageView thumbnailView = (ImageView)convertView.findViewById(R.id.contact_thumbnail);
        
        
        loadThumbnail(contact, thumbnailView);
 
        return convertView;
    }
    
    // TO-DO move thumbnail loading to Contact
    private void loadThumbnail(final Contact contact, final ImageView thumbnailView) {
    	Bitmap thumbnail = contact.getThumbnail();
        final String thumbnailURL = contact.getThumbnailURL();
		thumbnailView.setTag(thumbnailURL);
		
        if (thumbnail == null ) {
        	thumbnailView.setImageDrawable(noAvatar);
        	if (!contact.getThumbnailURL().equals("")) {
		        new Icon()
		        	.setOnFetched(new OnFetched() {
						@Override
						public <T> void onFetched(T response) {
							final Bitmap image = (Bitmap)response;
							contact.setThumbnail(image);
							
							if (((String)thumbnailView.getTag()).equals(thumbnailURL)) {
								thumbnailView.setImageBitmap(image);
								thumbnailView.invalidate();
				    		}
						}
					})
		        	.execute(thumbnailURL);
        	}
        } else {
        	thumbnailView.setImageBitmap(thumbnail);
        }
    }

	@Override
	public void onContactsChange() {
		this.list.clear();
		this.list.addAll(mapper.get());
		notifyDataSetChanged();
	}
}