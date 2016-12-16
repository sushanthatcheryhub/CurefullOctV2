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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import curefull.healthapp.R;
import utils.AppPreference;


public class DialogHintScreenaPrescriptions extends Dialog {

    private View v = null;
    Context context;
    private LinearLayout liner_share;
    private RelativeLayout realtive_hint;
    public DialogHintScreenaPrescriptions(Context _activiyt) {
        super(_activiyt, R.style.MyTheme);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = _activiyt;
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_hint_screen_prescrition);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        liner_share = (LinearLayout) findViewById(R.id.liner_share);
        if (AppPreference.getInstance().getPrescriptionSize() == 0) {
            liner_share.setVisibility(View.GONE);
        } else {
            liner_share.setVisibility(View.VISIBLE);
        }
        realtive_hint=(RelativeLayout)findViewById(R.id.realtive_hint);

        realtive_hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Tap anywhere to close dialog.
        this.dismiss();
        return true;
    }
}

