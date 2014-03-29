package kay.SI.contacts.views;

import kay.SI.contacts.R;
import kay.SI.contacts.R.styleable;

import android.widget.TextView;
import android.util.AttributeSet;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.content.Context;


// OBS: class added to obtain color selector in TextView
// for selection of rows in contact list
public class CustomTextView extends TextView
{
    private static String TAG = "CustomTextView";

    private ColorStateList mShadowColors;
    private float mShadowDx;
    private float mShadowDy;
    private float mShadowRadius;


    public CustomTextView(Context context)
    {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyle)
    {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView, defStyle, 0);

        final int attributeCount = a.getIndexCount();
        for (int i = 0; i < attributeCount; i++) {
            int curAttr = a.getIndex(i);

            switch (curAttr) {                  
                case R.styleable.CustomTextView_shadowColor:
                    mShadowColors = a.getColorStateList(curAttr);
                    break;

                case R.styleable.CustomTextView_android_shadowDx:
                    mShadowDx = a.getFloat(curAttr, 0);
                    break;

                case R.styleable.CustomTextView_android_shadowDy:
                    mShadowDy = a.getFloat(curAttr, 0);
                    break;

                case R.styleable.CustomTextView_android_shadowRadius:
                    mShadowRadius = a.getFloat(curAttr, 0);
                    break;  

                default:
                break;
        }
    }

        a.recycle();

        updateShadowColor();
    }

    private void updateShadowColor()
    {
        if (mShadowColors != null) {
            setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mShadowColors.getColorForState(getDrawableState(), 0));
            invalidate();
        }
    }

    @Override
    protected void drawableStateChanged()
    {
        super.drawableStateChanged();
        updateShadowColor();
    }
}