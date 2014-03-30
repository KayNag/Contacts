package kay.SI.contacts.controllers;

import java.io.ByteArrayOutputStream;

import java.io.InputStream;

import java.net.HttpURLConnection;

import java.net.URI;

import java.net.URL;

import java.security.KeyStore;

import org.apache.http.conn.ssl.SSLSocketFactory;

import org.apache.http.HttpEntity;

import org.apache.http.HttpResponse;

import org.apache.http.HttpVersion;

import org.apache.http.StatusLine;

import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpGet;

import org.apache.http.conn.ClientConnectionManager;

import org.apache.http.conn.scheme.PlainSocketFactory;

import org.apache.http.conn.scheme.Scheme;

import org.apache.http.conn.scheme.SchemeRegistry;

import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import org.apache.http.params.BasicHttpParams;

import org.apache.http.params.HttpParams;

import org.apache.http.params.HttpProtocolParams;

import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;

import android.util.Base64;

import android.util.Log;

public abstract class URLFetcher<T> extends AsyncTask<String, Void, byte[]> {

	protected int retries = 0;

	protected final int MAX_RETRIES = 5;

	private static final int HTTP_STATUS_OK = 200;

	private static byte[] buff = new byte[1024];

	protected String param;

	public interface OnFetched {

		public <T> void onFetched(T contacts);

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

			// this.execute(this.param);

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

			URI url = new URI(this.param);

			HttpGet request = new HttpGet(url);

			HttpClient client = getNewHttpClient();

			// execute the request

			HttpResponse response = client.execute(request);

			StatusLine status = response.getStatusLine();

			if (status.getStatusCode() != HTTP_STATUS_OK) {

			}

			// process the content.

			HttpEntity entity = response.getEntity();

			InputStream ist = entity.getContent();

			ByteArrayOutputStream content = new ByteArrayOutputStream();

			int readCount = 0;

			while ((readCount = ist.read(buff)) != -1) {

				content.write(buff, 0, readCount);

			}

			result = content.toByteArray();

			// appendLog(retval);

		} catch (Exception e) {

		}

		this.result = postProcess(result);

		return result;

	}

	public static HttpClient getNewHttpClient() {

		try {

			KeyStore trustStore = KeyStore.getInstance(KeyStore

			.getDefaultType());

			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocket(trustStore);

			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();

			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();

			registry.register(new Scheme("http", PlainSocketFactory

			.getSocketFactory(), 80));

			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(

			params, registry);

			return new DefaultHttpClient(ccm, params);

		} catch (Exception e) {

			return new DefaultHttpClient();

		}

	}

}
