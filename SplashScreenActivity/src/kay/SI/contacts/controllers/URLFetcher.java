package kay.SI.contacts.controllers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;



public abstract class URLFetcher<T> extends AsyncTask<String, Void, byte[]> {
	protected int retries = 0;
	protected final int MAX_RETRIES = 5;
	
	protected String param;
	public interface OnFetched {
		public <T>void onFetched(T contacts);
	}
	
	protected OnFetched listener;
	protected T result;
	
	public URLFetcher<T> setOnFetched(OnFetched onContactsFetched) {
		this.listener = onContactsFetched;
		
		return this;
	}
	
	protected void retry() {
		retries += 1;
		if (onRetry()) {
			//this.execute(this.param);
		}
	}
	
	protected boolean onRetry() {
		return true;
	}
	
	protected abstract T postProcess(byte[] response);
	
	@Override
	protected void onPostExecute(byte[] response) {
		if (listener != null) {
			listener.onFetched(this.result);
		}
	}
	
	@Override
	protected byte[] doInBackground(String... arg0) {
		byte[] result = new byte[0];
		
		try {
			this.param = arg0[0];
			URL url = new URL(this.param);
			
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			
			InputStream is = connection.getInputStream();
			
			byte[] b = new byte[256];
			ByteArrayOutputStream response = new ByteArrayOutputStream();
			
			while(is.read(b) != -1) {
				response.write(b);
			}
			
			result = response.toByteArray();
		} catch(Exception ex) {
			Log.d("FetcherError", ex.toString() + " " + arg0[0]);
		}
		
		// We do the post processing of the response in an asyncrhonous way
		// to avoid hicups in the main thread
		this.result = postProcess(result);
		
		return result;
	}
}
