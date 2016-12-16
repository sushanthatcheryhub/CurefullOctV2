package customsTextViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import curefull.healthapp.CureFull;


public class CustomAutoComplteViewOpenSanRegular extends AutoCompleteTextView {

    public CustomAutoComplteViewOpenSanRegular(Context context) {
        super(context);
        inIt(context);
    }

    public CustomAutoComplteViewOpenSanRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        inIt(context);

    }

    public CustomAutoComplteViewOpenSanRegular(Context context, AttributeSet attrs,
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
