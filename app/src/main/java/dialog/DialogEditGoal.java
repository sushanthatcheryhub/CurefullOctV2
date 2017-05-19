package dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


import ElasticVIews.ElasticAction;
import curefull.healthapp.R;
import interfaces.IOnOtpDoneDelete;


public class DialogEditGoal extends Dialog implements View.OnClickListener {

    private View v = null;
    private IOnOtpDoneDelete iOnOtpDoneDelete;
    private TextView txt_message, btn_done;
    private String dialogName;
    private int pos;

    public DialogEditGoal(Context _activiyt, String message, String dialogName, int position) {
        super(_activiyt, R.style.MyTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.dialog_edit_goal);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        txt_message = (TextView) findViewById(R.id.txt_message);
        txt_message.setText("If you want to continue for set water inTake click on Continue else click on Home");
        this.dialogName = dialogName;
        btn_done = (TextView) findViewById(R.id.btn_done);
        btn_done.setText("" + dialogName);
        btn_done.setOnClickListener(this);
        findViewById(R.id.btn_no).setOnClickListener(this);
        this.pos = position;
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_done) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                ElasticAction.doAction(v, 400, 0.9f, 0.9f);
            if (iOnOtpDoneDelete != null) {
                iOnOtpDoneDelete.optDoneDelete("OK", dialogName, pos);
                dismiss();

            }
        }
        if (v.getId() == R.id.btn_no) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                ElasticAction.doAction(v, 400, 0.9f, 0.9f);
            if (iOnOtpDoneDelete != null) {
                iOnOtpDoneDelete.optDoneDelete("NO", dialogName, pos);
                dismiss();
            }
        }
    }

    public IOnOtpDoneDelete getiOnOtpDoneDelete() {
        return iOnOtpDoneDelete;
    }

    public void setiOnOtpDoneDelete(IOnOtpDoneDelete iOnOtpDoneDelete) {
        this.iOnOtpDoneDelete = iOnOtpDoneDelete;
    }

}
