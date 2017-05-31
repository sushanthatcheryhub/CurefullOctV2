package dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import ElasticVIews.ElasticAction;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;


public class DialogSortbyCureNote extends Dialog implements View.OnClickListener {

    private View v = null;
    Context context;
    private TextView txt_newest, txt_oldest;
    private ImageView dialog_cancel;


    public DialogSortbyCureNote(Context _activiyt) {
        super(_activiyt, R.style.MyTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = _activiyt;
        setCancelable(true);
        setContentView(R.layout.dialog_sortby_curenote);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        viewInit();
        txt_newest.setOnClickListener(this);
        txt_oldest.setOnClickListener(this);
        dialog_cancel.setOnClickListener(this);


    }

    private void viewInit() {
        txt_newest = (TextView) findViewById(R.id.txt_newest);
        txt_oldest = (TextView) findViewById(R.id.txt_oldest);
        dialog_cancel = (ImageView) findViewById(R.id.dialog_cancel);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_cancel:
                dismiss();
                break;

            case R.id.txt_newest:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                }
                txt_newest.setTextColor(R.color.health_blue);
                txt_oldest.setTextColor(R.color.black);

                break;

            case R.id.txt_oldest:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                }
                txt_newest.setTextColor(R.color.black);
                txt_oldest.setTextColor(R.color.health_blue);

                break;


        }
    }

}