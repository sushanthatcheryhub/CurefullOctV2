package customsTextViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import curefull.healthapp.CureFull;


public class CustomTextViewOpenSanBold extends TextView {

    public CustomTextViewOpenSanBold(Context context) {
        super(context);
        inIt(context);
    }

    public CustomTextViewOpenSanBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        inIt(context);

    }

    public CustomTextViewOpenSanBold(Context context, AttributeSet attrs,
                                     int defStyle) {
        super(context, attrs, defStyle);
        inIt(context);

    }

    public void inIt(Context context) {
        if (isInEditMode())
            return;
        this.setTypeface(CureFull.getInstanse().getOpenSansBold(
                getContext()));
    }

}
