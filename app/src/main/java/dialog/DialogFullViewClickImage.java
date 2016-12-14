package dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adpter.AddImageDoneAdpter;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import interfaces.IOnCheckCheckbox;
import interfaces.IOnDoneMoreImage;
import item.property.PrescriptionImageList;
import utils.SpacesItemDecoration;


public class DialogFullViewClickImage extends Dialog implements View.OnClickListener, DatePickerDialog.OnDateSetListener, IOnCheckCheckbox {

    private boolean isdelete = false;
    private View v = null;
    Context context;
    private List<PrescriptionImageList> prescriptionImageListss;
    private RecyclerView recyclerViewAddImage;
    private GridLayoutManager lLayout;
    private AddImageDoneAdpter addImageAdpter;
    private IOnDoneMoreImage iOnDoneMoreImage;
    private TextView btn_done, txt_date, txt_health_text, txt_pre_date;
    private EditText input_doctor_name, input_disease;
    private String date = "";
    private LinearLayout liner_date_select;
    private ImageView btn_delete;
    private String type;
    private boolean isclick = false;

    public DialogFullViewClickImage(Context _activiyt, List<PrescriptionImageList> prescriptionImageLists, String type) {
        super(_activiyt, R.style.MyTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = _activiyt;
        setCancelable(true);
        setContentView(R.layout.dialog_add_more_image);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        txt_pre_date = (TextView) findViewById(R.id.txt_pre_date);
        txt_health_text = (TextView) findViewById(R.id.txt_health_text);
        btn_delete = (ImageView) findViewById(R.id.btn_delete);
        btn_done = (TextView) findViewById(R.id.btn_done);
        txt_date = (TextView) findViewById(R.id.txt_date);
        input_doctor_name = (EditText) findViewById(R.id.input_doctor_name);
        input_disease = (EditText) findViewById(R.id.input_disease);
        liner_date_select = (LinearLayout) findViewById(R.id.liner_date_select);
        liner_date_select.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        prescriptionImageListss = prescriptionImageLists;
        if (prescriptionImageListss.size() > 0) {
            recyclerViewAddImage = (RecyclerView) findViewById(R.id.grid_list_symptom);
            int spacingInPixels = 10;
            recyclerViewAddImage.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
            lLayout = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
            recyclerViewAddImage.setLayoutManager(lLayout);
            recyclerViewAddImage.setHasFixedSize(true);
            addImageAdpter = new AddImageDoneAdpter(CureFull.getInstanse().getActivityIsntanse(),
                    prescriptionImageListss, isdelete,DialogFullViewClickImage.this);
            addImageAdpter.setiOnCheckCheckbox(this);
            recyclerViewAddImage.setAdapter(addImageAdpter);
            addImageAdpter.notifyDataSetChanged();
        }

        if (type.equalsIgnoreCase("lab")) {
            txt_pre_date.setText("Report Date");
            input_disease.setHint("Test Name");
            txt_health_text.setText("You added " + (prescriptionImageListss.size() - 1) + " file in this Lab test please add detail for better experince");
        } else {
            input_disease.setHint("Disease Name");
            txt_health_text.setText("You added " + (prescriptionImageListss.size() - 1) + " file in this prescription please add detail for better experince");

        }
    }

    public IOnDoneMoreImage getiOnDoneMoreImage() {
        return iOnDoneMoreImage;
    }

    public void setiOnDoneMoreImage(IOnDoneMoreImage iOnDoneMoreImage) {
        this.iOnDoneMoreImage = iOnDoneMoreImage;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_done:
                CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
                if (btn_done.getText().toString().equalsIgnoreCase("Done")) {

                    if (!validateDoctorName()) {
                        return;
                    }
                    if (!validateDisease()) {
                        return;
                    }
                    if (!validateDate()) {
                        return;
                    }

                    if (iOnDoneMoreImage != null) {
                        iOnDoneMoreImage.optDoneMoreImage(input_doctor_name.getText().toString(), input_disease.getText().toString(), date, prescriptionImageListss,"no");
                        dismiss();
                    }
                } else {
                    for (int i = 0; i < prescriptionImageListss.size(); i++) {
                        Log.e("check ", ":- " + prescriptionImageListss.get(i).isChecked());
                        if (prescriptionImageListss.get(i).isChecked() == true) {
                            prescriptionImageListss.remove(i);
                        }
                    }
                    isdelete = false;
                    addImageAdpter = new AddImageDoneAdpter(CureFull.getInstanse().getActivityIsntanse(),
                            prescriptionImageListss, isdelete, DialogFullViewClickImage.this);
                    recyclerViewAddImage.setAdapter(addImageAdpter);
                    addImageAdpter.notifyDataSetChanged();
                    txt_health_text.setText("You added " + prescriptionImageListss.size() + " file in this prescription please add detail for better experince");
                    btn_done.setText("Done");
                }

                break;

            case R.id.liner_date_select:
                final Calendar c1 = Calendar.getInstance();
                final int year = c1.get(Calendar.YEAR);
                final int month = c1.get(Calendar.MONTH);
                final int day = c1.get(Calendar.DAY_OF_MONTH) + 1;

                DatePickerDialog newDateDialog = new DatePickerDialog(CureFull.getInstanse().getActivityIsntanse(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, year, month, day);
                newDateDialog.getDatePicker().setCalendarViewShown(false);
//                c.add(Calendar.DATE, 1);
                Date newDate = c1.getTime();
                newDateDialog.getDatePicker().setMaxDate(newDate.getTime());
                newDateDialog.show();
                break;

            case R.id.btn_delete:
                if (isclick) {
                    isclick = false;
                    btn_delete.setImageResource(R.drawable.delete_red);
                    isdelete = false;
                    addImageAdpter = new AddImageDoneAdpter(CureFull.getInstanse().getActivityIsntanse(),
                            prescriptionImageListss, isdelete, DialogFullViewClickImage.this);
                    recyclerViewAddImage.setAdapter(addImageAdpter);
                    addImageAdpter.notifyDataSetChanged();
                    btn_done.setText("Done");
                } else {
                    isclick = true;
                    btn_delete.setImageResource(R.drawable.cancel_red);
                    isdelete = true;
                    addImageAdpter = new AddImageDoneAdpter(CureFull.getInstanse().getActivityIsntanse(),
                            prescriptionImageListss, isdelete, DialogFullViewClickImage.this);
                    recyclerViewAddImage.setAdapter(addImageAdpter);
                    addImageAdpter.notifyDataSetChanged();
                    btn_done.setText("Delete");
                }

                break;

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        int mnt = (monthOfYear + 1);
        txt_date.setText("" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "/" + (mnt < 10 ? "0" + mnt : mnt) + "/" + year);
        date = "" + year + "-" + (mnt < 10 ? "0" + mnt : mnt) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
    }


    private boolean validateDate() {
        String email = txt_date.getText().toString().trim();

        if (email.equalsIgnoreCase("Select Date")) {
            txt_date.setError("Please Select Date");
            requestFocus(txt_date);
            return false;
        } else {
            txt_date.setError(null);
        }
        return true;
    }


    private boolean validateDoctorName() {
        String email = input_doctor_name.getText().toString().trim();

        if (email.equalsIgnoreCase("")) {
            input_doctor_name.setError("Please Type Doctor Name");
            requestFocus(input_doctor_name);
            return false;
        } else {
            input_doctor_name.setError(null);
        }
        return true;
    }


    private boolean validateDisease() {
        String email = input_disease.getText().toString().trim();

        if (email.equalsIgnoreCase("")) {
            input_disease.setError("Please Type Disease");
            requestFocus(input_disease);
            return false;
        } else {
            input_disease.setError(null);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            CureFull.getInstanse().getActivityIsntanse().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    public void optCheckBox(String messsage) {
        if (messsage.equalsIgnoreCase("true")) {
            btn_done.setText("Delete");
        } else {
            btn_done.setText("Done");
        }
    }

    public void isCheck(){
        if (iOnDoneMoreImage != null) {
            iOnDoneMoreImage.optDoneMoreImage(input_doctor_name.getText().toString(), input_disease.getText().toString(), date, prescriptionImageListss,"yes");
            dismiss();
        }
    }

}