package dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import curefull.healthapp.R;


public class DialogHintScreenaNote extends Dialog {

    private View v = null;
    Context context;

    public DialogHintScreenaNote(Context _activiyt) {
        super(_activiyt, R.style.MyTheme);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = _activiyt;
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_hint_screen_note);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Tap anywhere to close dialog.
        this.dismiss();
        return true;
    }
}

