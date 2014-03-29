package kay.SI.contacts;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class SplashScreenActivity extends Activity {
	private Thread waitingThread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		WaitAndFinish(3000);
	}
	
	private void WaitAndFinish(final long milliseconds) {
		waitingThread = new Thread() {
			@Override
			public void run() {
				synchronized(this) {
					try {
						wait(milliseconds);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				FinishActivity();
			}
		};
		
		waitingThread.start();
	}
	
	private void FinishActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

}
