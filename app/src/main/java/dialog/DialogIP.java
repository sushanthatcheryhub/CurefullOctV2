package dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import curefull.healthapp.R;
import interfaces.IOnOtpDoneDelete;
import utils.AppPreference;


public class DialogIP extends Dialog implements View.OnClickListener {

    private View v = null;
    private EditText edt_warings;

    public DialogIP(Context _activiyt) {
        super(_activiyt, R.style.MyTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.dialog_ip);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        edt_warings = (EditText) findViewById(R.id.edt_warings);
        findViewById(R.id.btn_done).setOnClickListener(this);
        findViewById(R.id.btn_no).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_done) {
            Log.e("dd",":- "+edt_warings.getText().toString().trim());
            AppPreference.getInstance().set_ip("" + edt_warings.getText().toString().trim());
            dismiss();

        }
        if (v.getId() == R.id.btn_no) {
            dismiss();
        }
    }


}
