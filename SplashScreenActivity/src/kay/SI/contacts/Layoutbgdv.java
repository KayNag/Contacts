package kay.SI.contacts;



import kay.SI.contacts.R;
import android.content.Context;
import android.widget.ImageView;

public class Layoutbgdv extends ImageView {
	public Layoutbgdv (Context context) {
		super(context);

		setImageDrawable(context.getResources().getDrawable(R.drawable.action_bar_divider));
	}
}