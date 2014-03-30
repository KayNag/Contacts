package kay.SI.contacts.controllers;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Icon extends URLFetcher<Bitmap> {
	@Override
	protected byte[] doInBackground(String... arg0) {
		byte[] result = new byte[0];
		
		try {
			URL url = new URL(arg0[0]);
			
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			
			InputStream is = connection.getInputStream();
			// TO-DO fix URLFetcher, it fails to get properly images 
			// probably due to the buffer size
			this.result = BitmapFactory.decodeStream(is);
		} catch(Exception ex) {
			Log.d("FetcherError", ex.toString());
		}
		
		return result;
	}

	@Override
	protected Bitmap postProcess(byte[] response) {
		return null;
	}
}
