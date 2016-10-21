package customsTextViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import curefull.healthapp.CureFull;


public class CustomEditTextViewMontserratHairLine extends EditText {

    public CustomEditTextViewMontserratHairLine(Context context) {
        super(context);
        inIt(context);
    }

    public CustomEditTextViewMontserratHairLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        inIt(context);

    }

    public CustomEditTextViewMontserratHairLine(Context context, AttributeSet attrs,
                                                int defStyle) {
        super(context, attrs, defStyle);
        inIt(context);

    }

    public void inIt(Context context) {
        if (isInEditMode())
            return;
        this.setTypeface(CureFull.getInstanse().getMontserratHairLine(
                getContext()));
    }

}
