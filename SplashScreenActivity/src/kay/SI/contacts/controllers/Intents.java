package kay.SI.contacts.controllers;

import kay.SI.contacts.models.Contact;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;

public class Intents {
	public static Intent addToContacts(Contact contact) {
		Intent i = new Intent(Intent.ACTION_INSERT,
				ContactsContract.Contacts.CONTENT_URI);
		i.setType(ContactsContract.Contacts.CONTENT_TYPE);
		i.putExtra(ContactsContract.Intents.EXTRA_FORCE_CREATE, true);
		i.putExtra(ContactsContract.Intents.Insert.EMAIL, contact.getEmail());
		i.putExtra(ContactsContract.Intents.Insert.NAME, contact.getName());
		i.putExtra(ContactsContract.Intents.Insert.PHONE, contact.getPhone());
		i.putExtra(ContactsContract.Intents.Insert.COMPANY, "Screen Interaction");
		i.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, contact.getJobTitle());

		return i;
	}
	
	public static Intent sendEmail(Contact contact) {
		String email = contact.getEmail();
        String subject = "";
        String message = "";

        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, message);
        i.setType("message/rfc822");
        
        return i;
	}

	public static Intent callPhone(Contact contact) {
		String uri = "tel:" + contact.getPhone();
		Intent i = new Intent(Intent.ACTION_CALL);
		i.setData(Uri.parse(uri));
		
		return i;
	}
	
	public static Intent openWebsite(Contact contact) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(contact.getWebpage()));
		
		return i;
	}
}
