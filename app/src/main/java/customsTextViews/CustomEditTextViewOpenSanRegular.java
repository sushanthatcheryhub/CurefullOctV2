package customsTextViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import curefull.healthapp.CureFull;


public class CustomEditTextViewOpenSanRegular extends EditText {

    public CustomEditTextViewOpenSanRegular(Context context) {
        super(context);
        inIt(context);
    }

    public CustomEditTextViewOpenSanRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        inIt(context);

    }

    public CustomEditTextViewOpenSanRegular(Context context, AttributeSet attrs,
                                            int defStyle) {
        super(context, attrs, defStyle);
        inIt(context);

    }

    public void inIt(Context context) {
        if (isInEditMode())
            return;
        this.setTypeface(CureFull.getInstanse().getOpenSansRegular(
                getContext()));
    }

}
