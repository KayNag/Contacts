package kay.SI.contacts.views;



import kay.SI.contacts.R;
import android.content.Context;
import android.widget.ImageView;

public class ActionDividerView extends ImageView {
	public ActionDividerView (Context context) {
		super(context);

		setImageDrawable(context.getResources().getDrawable(R.drawable.action_bar_divider));
	}
}