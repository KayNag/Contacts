package kay.SI.contacts.helpers;

import java.util.ArrayList;



import org.json.JSONException;
import org.json.JSONObject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Databaseactions extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "contacts.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String COLUMN_ID = "_id";
	public static final String TABLE_CONTACTS = "contacts";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_JOB_TITLE = "job_title";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_PHONE = "phone";
	public static final String COLUMN_WEBPAGE = "webpage";
	public static final String COLUMN_PICTURE_URL = "picture-url";
	public static final String COLUMN_THUMBNAIL_URL = "thumbnail-url";
	public static final String COLUMN_FAVORITED = "favorited";
	public static final String COLUMN_PICTURE = "picture";
	public static final String COLUMN_THUMBNAIL = "thumbnail";
	
	private static final String DATABASE_CREATE = "create table " +
			TABLE_CONTACTS + "(" + 
			COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_NAME + " text, " + 
			COLUMN_PHONE + " text," +
			COLUMN_WEBPAGE + " text," +
			COLUMN_EMAIL + " text," +
			COLUMN_JOB_TITLE + " text," +
			COLUMN_PICTURE_URL + " text," +
			COLUMN_THUMBNAIL_URL + " text," +
			COLUMN_FAVORITED + " text," +
			COLUMN_THUMBNAIL + " blob," +
			COLUMN_PICTURE + " blob," +
			");";
	
	private static final String DATABASE_ALL = "select " + COLUMN_ID + " as _id," +
			COLUMN_NAME + " as " + COLUMN_NAME + "," +
			COLUMN_PHONE + " as " + COLUMN_PHONE + "," +
			COLUMN_WEBPAGE + " as " + COLUMN_WEBPAGE + "," +
			COLUMN_EMAIL + " as " + COLUMN_EMAIL + "," +
			COLUMN_JOB_TITLE + " as " + COLUMN_JOB_TITLE + "," +
			COLUMN_PICTURE_URL + " as " + COLUMN_PICTURE_URL + "," +
			COLUMN_THUMBNAIL_URL + " as " + COLUMN_THUMBNAIL_URL + "," +
			COLUMN_FAVORITED + " as " + COLUMN_FAVORITED + "" +
			" from " + TABLE_CONTACTS;

	public Databaseactions(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		onCreate(db);
	}
  
	public int insertContact(Contact contact)  {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = contactToContentValues(contact);
	  
		int contactID = (int)db.insert(TABLE_CONTACTS, COLUMN_ID, cv);
		db.close();
	  
		return contactID;
	}
  
	public ArrayList<Contact> getAllContacts() {
		SQLiteDatabase db = this.getReadableDatabase();
	  
		Cursor cur = db.rawQuery(DATABASE_ALL, new String [] {});
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		JSONObject contact = new JSONObject();
	  
		while (cur.moveToNext()) {
			try {
				contact.put(COLUMN_ID, cur.getInt(cur.getColumnIndex(COLUMN_ID)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
	  
			addColumnToJSON(cur, COLUMN_NAME, contact);
			addColumnToJSON(cur, COLUMN_PHONE, contact);
			addColumnToJSON(cur, COLUMN_JOB_TITLE, contact);
			addColumnToJSON(cur, COLUMN_EMAIL, contact);
			addColumnToJSON(cur, COLUMN_PICTURE_URL, contact);
			addColumnToJSON(cur, COLUMN_THUMBNAIL_URL, contact);
			addColumnToJSON(cur, COLUMN_WEBPAGE, contact);
	  
			contacts.add(new Contact(contact));
		}
	  
		return contacts;
	}
  
	public int updateContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = contactToContentValues(contact);
	  
		int contactID = db.update(TABLE_CONTACTS, cv, COLUMN_ID + "=?", 
				new String []{ String.valueOf(contact.getID())});
	  
		return contactID;
	}
  
	private ContentValues contactToContentValues(Contact contact) {
		ContentValues cv = new ContentValues();
		
		cv.put(COLUMN_NAME, contact.getName());
		cv.put(COLUMN_JOB_TITLE, contact.getJobTitle());
		cv.put(COLUMN_EMAIL, contact.getEmail());
		cv.put(COLUMN_PHONE, contact.getPhone());
		cv.put(COLUMN_WEBPAGE, contact.getWebpage());
		cv.put(COLUMN_PICTURE_URL, contact.getPictureURL());
		cv.put(COLUMN_THUMBNAIL_URL, contact.getThumbnailURL());
		cv.put(COLUMN_FAVORITED, contact.getFavorited());
	  
		return cv;
	}
	
	private void addColumnToJSON(Cursor cur, String column, JSONObject contact) {
		try {
				contact.put(column, cur.getString(cur.getColumnIndex(column)));
		} catch (JSONException e) {
				e.printStackTrace();
		}
	}
}
