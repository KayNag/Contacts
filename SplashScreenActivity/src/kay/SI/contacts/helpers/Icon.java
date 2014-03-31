package kay.SI.contacts.helpers;

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
		String s_url;
		try {
			s_url = arg0[0];
			InputStream istr;
			URL url = new URL(s_url);
			
			istr = url.openStream();
		
			this.result = BitmapFactory.decodeStream(istr);
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
