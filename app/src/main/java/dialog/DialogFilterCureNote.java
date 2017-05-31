package dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
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


public class DialogFilterCureNote extends Dialog implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private View v = null;
    Context context;
    private TextView txt_from_date, txt_to_date, txt_reset, txt_apply;
    private ImageView dialog_cancel;
    SharedPreferences preferences;
    String tag = "from";

    public DialogFilterCureNote(Context _activiyt) {
        super(_activiyt, R.style.MyTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = _activiyt;
        setCancelable(true);
        setContentView(R.layout.dialog_filter_curenote);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        viewInit();
        preferences = PreferenceManager.getDefaultSharedPreferences(CureFull.getInstanse().getActivityIsntanse());
        txt_reset.setPaintFlags(txt_reset.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_apply.setPaintFlags(txt_reset.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_from_date.setOnClickListener(this);
        txt_to_date.setOnClickListener(this);
        dialog_cancel.setOnClickListener(this);
        txt_reset.setOnClickListener(this);
        txt_apply.setOnClickListener(this);

    }

    private void viewInit() {
        txt_from_date = (TextView) findViewById(R.id.txt_from_date);
        txt_to_date = (TextView) findViewById(R.id.txt_to_date);
        dialog_cancel = (ImageView) findViewById(R.id.dialog_cancel);
        txt_reset = (TextView) findViewById(R.id.txt_reset);
        txt_apply = (TextView) findViewById(R.id.txt_apply);
    }


    private boolean validateFromDate() {
        String email = txt_from_date.getText().toString().trim();
        if (email.equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }

    private boolean validateToDate() {
        String email = txt_to_date.getText().toString().trim();
        if (email.equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_cancel:
                dismiss();
                break;

            case R.id.txt_from_date:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                }

                final int year, day;
                int month1;
                final Calendar c1 = Calendar.getInstance();
                year = c1.get(Calendar.YEAR);
                month1 = c1.get(Calendar.MONTH);
                day = c1.get(Calendar.DAY_OF_MONTH);
                month1 = (month1 - 1);
                tag = "from";
                DatePickerDialog newDateDialog = new DatePickerDialog(CureFull.getInstanse().getActivityIsntanse(), AlertDialog.THEME_HOLO_LIGHT, DialogFilterCureNote.this, year, month1, day);
                newDateDialog.getDatePicker().setSpinnersShown(true);
//                c.add(Calendar.DATE, 1);
                Date newDate = c1.getTime();
                newDateDialog.getDatePicker().setMaxDate(newDate.getTime());
                newDateDialog.show();

                break;

            case R.id.txt_to_date:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                }

                final int year1, day1;
                int month11;
                final Calendar c11 = Calendar.getInstance();
                year1 = c11.get(Calendar.YEAR);
                month11 = c11.get(Calendar.MONTH);
                day1 = c11.get(Calendar.DAY_OF_MONTH);
                month11 = (month11 - 1);
                tag = "to";
                DatePickerDialog newDateDialog1 = new DatePickerDialog(CureFull.getInstanse().getActivityIsntanse(), AlertDialog.THEME_HOLO_LIGHT, DialogFilterCureNote.this, year1, month11, day1);
                newDateDialog1.getDatePicker().setSpinnersShown(true);
//                c.add(Calendar.DATE, 1);
                Date newDate1 = c11.getTime();
                newDateDialog1.getDatePicker().setMaxDate(newDate1.getTime());
                newDateDialog1.show();

                break;

            case R.id.txt_reset:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                }


                break;

            case R.id.txt_apply:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                }
                if(!validateFromDate()){
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "From Date can not be blank");
                    return;
                }
                if(!validateToDate()){
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "To Date can not be blank");
                    return;
                }


                break;
        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        int mnt = (monthOfYear + 1);
        if (tag.equalsIgnoreCase("from")) {
            txt_from_date.setText("" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + (mnt < 10 ? "0" + mnt : mnt) + "-" + year);
        } else {
            txt_to_date.setText("" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + (mnt < 10 ? "0" + mnt : mnt) + "-" + year);
        }

    }


  /*  private void requestFocus(View view) {
        if (view.requestFocus()) {
            CureFull.getInstanse().getActivityIsntanse().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
*/

}