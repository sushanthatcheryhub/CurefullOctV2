package dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import curefull.healthapp.R;


public class DialogHintScreenaLanding extends Dialog {

    private View v = null;
    Context context;
    private TextView txt_health_text;

    public DialogHintScreenaLanding(Context _activiyt) {
        super(_activiyt, R.style.MyTheme);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = _activiyt;
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_hint_screen_landing);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        txt_health_text = (TextView) findViewById(R.id.txt_health_text);


        String you = "About Health Note ";
        String termCondtiions = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";

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
        sb1.setSpan(new RelativeSizeSpan(1.4f), 0, 17, 0);
        txt_health_text.setText(sb1);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Tap anywhere to close dialog.
        this.dismiss();
        return true;
    }
}

