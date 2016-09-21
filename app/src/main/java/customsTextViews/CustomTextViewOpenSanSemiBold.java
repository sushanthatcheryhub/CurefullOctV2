package customsTextViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import curefull.healthapp.CureFull;


public class CustomTextViewOpenSanSemiBold extends TextView {

    public CustomTextViewOpenSanSemiBold(Context context) {
        super(context);
        inIt(context);
    }

    public CustomTextViewOpenSanSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        inIt(context);

    }

    public CustomTextViewOpenSanSemiBold(Context context, AttributeSet attrs,
                                         int defStyle) {
        super(context, attrs, defStyle);
        inIt(context);

    }

    public void inIt(Context context) {
        if (isInEditMode())
            return;
        this.setTypeface(CureFull.getInstanse().getOpenSansSemiBold(
                getContext()));
    }

}
