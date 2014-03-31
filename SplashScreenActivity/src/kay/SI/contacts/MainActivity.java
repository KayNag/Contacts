package kay.SI.contacts;



import kay.SI.contacts.helpers.Adapter;
import kay.SI.contacts.helpers.Contact;
import kay.SI.contacts.helpers.Mapper;





import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainActivity extends Activity implements OnTouchListener {
	final private Mapper contacts = Mapper.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		setupContactList();
	    
	    setupActionbar();
	}
	
	public void setupContactList() {
	    ListView list = (ListView)findViewById(R.id.list);
	    Adapter dataAdapter = new Adapter(this, R.layout.rowdetail, contacts);
	    
	    list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LoadContactFullView(contacts.getContact(arg2));
			}
	    	
	    });
	    list.setEmptyView(findViewById(R.id.contacts_loading_view));
	    list.setAdapter(dataAdapter);
	}
	
	public void setupActionbar() {
		ImageButton orderingBtn = (ImageButton)findViewById(R.id.contact_ordering);
	    orderingBtn.setOnTouchListener(this);
	}
	
	public void openSortingDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(R.string.select_sorting)
	           .setItems(R.array.ordering_options, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   
	            	   contacts.setOrdering(which);
	           }
	    });
	           
	    builder.create().show();
	}
	
	public void LoadContactFullView(Contact contact) {
		Intent intent = new Intent(this, ViewActivity.class);
		intent.putExtra("contact", contact.toString());
		startActivity(intent);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			switch (v.getId()) {
	        	case R.id.contact_ordering:
            		openSortingDialog();
            		return true;
			}
		}
        
		return false;
	}
}
