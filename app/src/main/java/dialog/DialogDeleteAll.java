package dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import curefull.healthapp.R;
import interfaces.IOnOtpDoneDelete;


public class DialogDeleteAll extends Dialog implements View.OnClickListener {

    private View v = null;
    private IOnOtpDoneDelete iOnOtpDoneDelete;
    private TextView txt_message;
    private String dialogName;
    private int pos;

    public DialogDeleteAll(Context _activiyt, String message, String dialogName, int position) {
        super(_activiyt, R.style.MyTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.dialog_delete_message);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        txt_message = (TextView) findViewById(R.id.txt_message);
        txt_message.setText(message);
        this.dialogName = dialogName;
        findViewById(R.id.btn_done).setOnClickListener(this);
        findViewById(R.id.btn_no).setOnClickListener(this);
        this.pos = position;
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_done) {
            if (iOnOtpDoneDelete != null) {
                iOnOtpDoneDelete.optDoneDelete("OK", dialogName, pos);
                dismiss();

            }
        }
        if (v.getId() == R.id.btn_no) {
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
