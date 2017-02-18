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
import android.widget.RelativeLayout;
import android.widget.TextView;

import curefull.healthapp.R;


public class DialogHintScreenaNote extends Dialog {

    private View v = null;
    Context context;
    private RelativeLayout realtive_hint;
    private TextView txt_health_text;

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
        realtive_hint = (RelativeLayout) findViewById(R.id.realtive_hint);


        txt_health_text = (TextView) findViewById(R.id.txt_health_text);

        String you = "CureNotes";
        String termCondtiions = " to store signs of any health issue.";

        String meassgeNew = you + termCondtiions;

        Spannable sb1 = new SpannableString(meassgeNew);
        sb1.setSpan(new ForegroundColorSpan(context.getResources()
                        .getColor(R.color.health_yellow)), meassgeNew.indexOf(you),
                meassgeNew.indexOf(you) + you.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                meassgeNew.indexOf(you), meassgeNew.indexOf(you) + you.length(), 0);
        sb1.setSpan(new ForegroundColorSpan(context.getResources()
                        .getColor(R.color.health_yellow)), meassgeNew.indexOf(termCondtiions),
                meassgeNew.indexOf(termCondtiions) + termCondtiions.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb1.setSpan(new RelativeSizeSpan(1.4f), 0, 9, 0);
        txt_health_text.setText(sb1);

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

