package dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import fragment.healthapp.FragmentPrescriptionCheckNew;


public class DialogHintScreenaLandingQution extends Dialog {

    private View v = null;
    Context context;
    private RelativeLayout realtive_hint;
    private LinearLayout liner_user, liner_user_red;

    public DialogHintScreenaLandingQution(Context _activiyt) {
        super(_activiyt, R.style.MyTheme);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = _activiyt;
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_hint_screen);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        liner_user = (LinearLayout) findViewById(R.id.liner_user);
        liner_user_red = (LinearLayout) findViewById(R.id.liner_user_red);
        realtive_hint = (RelativeLayout) findViewById(R.id.realtive_hint);

        realtive_hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        liner_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHintScreenaLanding dialogHintScreenaLanding = new DialogHintScreenaLanding(context);
                dialogHintScreenaLanding.show();
                dismiss();
            }
        });

        liner_user_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentPrescriptionCheckNew(), true);
                DialogHintScreenaPrescriptions dialogHintScreenaPrescriptions = new DialogHintScreenaPrescriptions(context);
                dialogHintScreenaPrescriptions.show();
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

