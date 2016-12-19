package fragment.healthapp;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.BaseBackHandlerFragment;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import item.property.GoalInfo;
import item.property.PrescriptionImageList;
import item.property.UserInfo;
import utils.AppPreference;
import utils.MyConstants;
import utils.Utils;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentEditGoal extends BaseBackHandlerFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    private View rootView;
    private ImageView img_select_height, img_200ml, img_100ml, img_300ml, img_500ml;
    private ListPopupWindow listPopupWindow;
    private TextView txt_ideal_weight, txt_height, txt_weight, txt_BMR, txt_BMI, btn_done, btn_edit_goal, edt_years;
    private EditText edt_feet, edt_inchs, edt_cm, edt_kgs, edt_grams, edt_pounds;

    private EditText edt_water, edt_steps, edt_calories;
    private RadioGroup radioGender;
    private RadioButton radioMale, radioFemale;
    private boolean isCm = false;
    private boolean isPounds = false;
    private boolean isMale = false;
    private boolean isFemale = false;
    private int heightfeet, heightInch;
    private double weightkg;
    double fweight, mweight;
    private LinearLayout liner_100ml, liner_200ml, liner_300ml, liner_500ml;
    private RequestQueue requestQueue;
    int glassInTake = 0;

    @Override
    public boolean onBackPressed() {
        if (AppPreference.getInstance().getMale() == false && AppPreference.getInstance().getFeMale() == false) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Select Gender");
            return false;
        }

        if (AppPreference.getInstance().getGoalAge().equalsIgnoreCase("0")) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Select Age");
            return false;
        }

        Log.e("Select Age", "" + AppPreference.getInstance().getGoalAge());

        return super.onBackPressed();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_set_goal,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);
        edt_water = (EditText) rootView.findViewById(R.id.edt_water);
        edt_steps = (EditText) rootView.findViewById(R.id.edt_steps);
        edt_calories = (EditText) rootView.findViewById(R.id.edt_calories);
        txt_ideal_weight = (TextView) rootView.findViewById(R.id.txt_ideal_weight);
        edt_water.setEnabled(false);
        edt_water.setFocusable(false);
        edt_steps.setEnabled(false);
        edt_steps.setFocusable(false);
        edt_calories.setEnabled(false);
        edt_calories.setFocusable(false);

        edt_years = (TextView) rootView.findViewById(R.id.edt_years);
        edt_feet = (EditText) rootView.findViewById(R.id.edt_feet);
        edt_inchs = (EditText) rootView.findViewById(R.id.edt_inchs);
        edt_cm = (EditText) rootView.findViewById(R.id.edt_cm);
        edt_kgs = (EditText) rootView.findViewById(R.id.edt_kgs);
        edt_grams = (EditText) rootView.findViewById(R.id.edt_grams);
        edt_pounds = (EditText) rootView.findViewById(R.id.edt_pounds);
        txt_BMI = (TextView) rootView.findViewById(R.id.txt_BMI);
        txt_BMR = (TextView) rootView.findViewById(R.id.txt_BMR);
        txt_weight = (TextView) rootView.findViewById(R.id.txt_weight);
        txt_height = (TextView) rootView.findViewById(R.id.txt_height);
        btn_done = (TextView) rootView.findViewById(R.id.btn_done);
        img_select_height = (ImageView) rootView.findViewById(R.id.img_select_height);
        btn_edit_goal = (TextView) rootView.findViewById(R.id.btn_edit_goal);
        radioGender = (RadioGroup) rootView.findViewById(R.id.radioGender);
        radioMale = (RadioButton) rootView.findViewById(R.id.radioMale);
        radioFemale = (RadioButton) rootView.findViewById(R.id.radioFemale);
        liner_100ml = (LinearLayout) rootView.findViewById(R.id.liner_100ml);
        liner_200ml = (LinearLayout) rootView.findViewById(R.id.liner_200ml);
        liner_300ml = (LinearLayout) rootView.findViewById(R.id.liner_300ml);
        liner_500ml = (LinearLayout) rootView.findViewById(R.id.liner_500ml);
        img_100ml = (ImageView) rootView.findViewById(R.id.img_100ml);
        img_200ml = (ImageView) rootView.findViewById(R.id.img_200ml);
        img_300ml = (ImageView) rootView.findViewById(R.id.img_300ml);
        img_500ml = (ImageView) rootView.findViewById(R.id.img_500ml);
        liner_100ml.setOnClickListener(this);
        liner_200ml.setOnClickListener(this);
        liner_300ml.setOnClickListener(this);
        liner_500ml.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        btn_edit_goal.setOnClickListener(this);

        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioMale) {
                    AppPreference.getInstance().setMale(true);
                    AppPreference.getInstance().setFeMale(false);
                    isMale = true;
                    isFemale = false;
                    getBmrForMale();
                    if (heightfeet > 4) {
                        if (!validateHeightFeetBMI()) {
                            return;
                        }
                        if (!validateHeightinchBMI()) {
                            return;
                        }
                        if (heightfeet == 5) {
                            txt_ideal_weight.setText("" + Utils.getIdealWeightMen(heightInch) + " kg");
                        } else {
                            int checkheight = ((heightfeet * 12) - 60) + heightInch;
                            txt_ideal_weight.setText("" + Utils.getIdealWeightMen(checkheight) + " kg");
                        }
                    } else {
                        if (!validateHeightFeetBMI()) {
                            return;
                        }
                        if (!validateHeightinchBMI()) {
                            return;
                        }
                        txt_ideal_weight.setText("" + "50 kg");
                    }
                } else if (checkedId == R.id.radioFemale) {
                    AppPreference.getInstance().setMale(false);
                    AppPreference.getInstance().setFeMale(true);
                    isMale = false;
                    isFemale = true;
                    getBmrForFeMale();
                    if (heightfeet > 4) {
                        if (!validateHeightFeetBMI()) {
                            return;
                        }
                        if (!validateHeightinchBMI()) {
                            return;
                        }
                        if (heightfeet == 5) {
                            txt_ideal_weight.setText("" + Utils.getIdealWeightWomen(heightInch) + " kg");
                        } else {
                            int checkheight = ((heightfeet * 12) - 60) + heightInch;
                            txt_ideal_weight.setText("" + Utils.getIdealWeightWomen(checkheight) + " kg");
                        }
                    } else {
                        if (!validateHeightFeetBMI()) {
                            return;
                        }
                        if (!validateHeightinchBMI()) {
                            return;
                        }
                        txt_ideal_weight.setText("" + "45 kg");
                    }
                }


            }
        });


        edt_years.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int year, day;
                int month1;
                final Calendar c1 = Calendar.getInstance();
                Log.e("Age ", ":- " + AppPreference.getInstance().getGoalAge());
                if (AppPreference.getInstance().getGoalAge().equalsIgnoreCase("0")) {
                    year = c1.get(Calendar.YEAR);
                    month1 = c1.get(Calendar.MONTH);
                    day = c1.get(Calendar.DAY_OF_MONTH) + 1;
                } else {
                    Log.e("ye wala ", " ye wlal");
                    String[] dateFormat = AppPreference.getInstance().getGoalAge().split("-");
                    year = Integer.parseInt(dateFormat[0]);
                    month1 = Integer.parseInt(dateFormat[1]);
                    day = Integer.parseInt(dateFormat[2]);
                    month1 = (month1 - 1);
                }
                DatePickerDialog newDateDialog = new DatePickerDialog(CureFull.getInstanse().getActivityIsntanse(), AlertDialog.THEME_HOLO_LIGHT, FragmentEditGoal.this, year, month1, day);
                newDateDialog.getDatePicker().setSpinnersShown(true);
//                c.add(Calendar.DATE, 1);
                Date newDate = c1.getTime();
                newDateDialog.getDatePicker().setMaxDate(newDate.getTime());
                newDateDialog.show();
            }
        });

        (rootView.findViewById(R.id.img_select_height)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                listPopupWindow.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                        R.layout.adapter_list_doctor_data, MyConstants.IArrayData.listPopUpHeight));
                listPopupWindow.setAnchorView(rootView.findViewById(R.id.txt_height));
                listPopupWindow.setWidth((int) getResources().getDimension(R.dimen._80dp));
//                listPopupWindow.setHeight(400);
                listPopupWindow.setModal(true);
                listPopupWindow.setOnItemClickListener(popUpItemClick);
                listPopupWindow.show();
            }
        });

        (rootView.findViewById(R.id.img_select_weight)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                listPopupWindow.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                        R.layout.adapter_list_doctor_data, MyConstants.IArrayData.listPopUpWeight));
                listPopupWindow.setAnchorView(rootView.findViewById(R.id.txt_weight));
                listPopupWindow.setWidth((int) getResources().getDimension(R.dimen._80dp));
//                listPopupWindow.setHeight(400);
                listPopupWindow.setModal(true);
                listPopupWindow.setOnItemClickListener(popUpItemClick1);
                listPopupWindow.show();
            }
        });

        edt_feet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("onTextChanged", ":- " + "onTextChanged" + count + ":- " + s.length());
                AppPreference.getInstance().setGoalHeightFeet("" + s.toString());
                if (s.length() > 0) {

                    bmiCalculator();
                    if (isFemale) {
                        getBmrForFeMale();

                        if (heightfeet > 4) {
                            if (!validateHeightinchBMI()) {
                                return;
                            }

                            if (heightfeet == 5) {
                                txt_ideal_weight.setText("" + Utils.getIdealWeightWomen(heightInch) + " kg");
                            } else {
                                int checkheight = ((heightfeet * 12) - 60) + heightInch;
                                txt_ideal_weight.setText("" + Utils.getIdealWeightWomen(checkheight) + " kg");
                            }
                        } else {
                            if (!validateHeightinchBMI()) {
                                return;
                            }
                            txt_ideal_weight.setText("" + "45 kg");
                        }
                    }
                    if (isMale) {
                        getBmrForMale();
                        if (heightfeet > 4) {
                            if (!validateHeightinchBMI()) {
                                return;
                            }
                            if (heightfeet == 5) {
                                txt_ideal_weight.setText("" + Utils.getIdealWeightMen(heightInch) + " kg");
                            } else {
                                int checkheight = ((heightfeet * 12) - 60) + heightInch;
                                txt_ideal_weight.setText("" + Utils.getIdealWeightMen(checkheight) + " kg");
                            }
                        } else {
                            if (!validateHeightinchBMI()) {
                                return;
                            }
                            txt_ideal_weight.setText("" + "50 kg");
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_cm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("onTextChanged", ":- " + "onTextChanged" + count + ":- " + s.length());
                AppPreference.getInstance().setGoalHeightCm(s.toString());
                if (s.length() > 0) {
                    if (!s.toString().equalsIgnoreCase("0.0")) {
                        c2f(Integer.parseInt(s.toString()));
                        bmiCalculator();
                        if (isFemale) {
                            getBmrForFeMale();
                        }
                        if (isMale) {
                            getBmrForMale();
                        }
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_pounds.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("onTextChanged", ":- " + "onTextChanged" + count + ":- " + s.length());
                AppPreference.getInstance().setGoalWeightPound("" + s.toString());
                if (s.length() > 0) {
                    poundToKgs(Double.parseDouble(s.toString()));
                    bmiCalculator();
                    if (isFemale) {
                        getBmrForFeMale();
                    }
                    if (isMale) {
                        getBmrForMale();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        edt_inchs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("onTextChanged", ":- " + "onTextChanged" + count + ":- " + s.length() + " " + s.toString());

                if (s.length() > 0) {
                    if (Integer.parseInt(s.toString()) < 11) {
                        AppPreference.getInstance().setGoalHeightInch("" + s.toString());
                        Log.e("less", ":- " + " " + s.toString());
                        bmiCalculator();
                        if (isFemale) {
                            getBmrForFeMale();
                            if (heightfeet > 4) {
                                if (!validateHeightFeetBMI()) {
                                    return;
                                }
                                if (heightfeet == 5) {
                                    txt_ideal_weight.setText("" + Utils.getIdealWeightWomen(heightInch) + " kg");
                                } else {
                                    int checkheight = ((heightfeet * 12) - 60) + heightInch;
                                    txt_ideal_weight.setText("" + Utils.getIdealWeightWomen(checkheight) + " kg");
                                }

                            } else {
                                if (!validateHeightFeetBMI()) {
                                    return;
                                }
                                txt_ideal_weight.setText("" + "45 kg");
                            }
                        }
                        if (isMale) {
                            getBmrForMale();
                            if (heightfeet > 4) {
                                if (!validateHeightFeetBMI()) {
                                    return;
                                }
                                if (heightfeet == 5) {
                                    txt_ideal_weight.setText("" + Utils.getIdealWeightMen(heightInch) + " kg");
                                } else {
                                    int checkheight = ((heightfeet * 12) - 60) + heightInch;
                                    txt_ideal_weight.setText("" + Utils.getIdealWeightMen(checkheight) + " kg");
                                }

                            } else {
                                if (!validateHeightFeetBMI()) {
                                    return;
                                }
                                txt_ideal_weight.setText("" + "50 kg");
                            }

                        }
                    } else {
                        edt_inchs.setText("11");
                        Log.e("greater", ":- " + s.toString());
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_kgs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("onTextChanged", ":- " + "onTextChanged" + count + ":- " + s.length());
                AppPreference.getInstance().setGoalWeightKg("" + s.toString());
                if (s.length() > 0) {
                    bmiCalculator();
                    if (isFemale) {
                        getBmrForFeMale();
                    }
                    if (isMale) {
                        getBmrForMale();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        edt_grams.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("edt_grams", ":- " + "edt_grams" + count + ":- " + s.length());
                AppPreference.getInstance().setGoalWeightGrams("" + s.toString());
                if (s.length() > 0) {
                    bmiCalculator();
                    if (isFemale) {
                        Log.e("isFemale", ":- " + "isFemale");
                        getBmrForFeMale();
                    }
                    if (isMale) {
                        Log.e("Male", ":- " + "Male");
                        getBmrForMale();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        getAllDetails();


        if (AppPreference.getInstance().getKgs()) {
            txt_weight.setText("Kgs");
            edt_kgs.setVisibility(View.VISIBLE);
            edt_grams.setVisibility(View.VISIBLE);
            edt_pounds.setVisibility(View.GONE);
            isPounds = false;
        }

        if (AppPreference.getInstance().getPound()) {
            txt_weight.setText("Pounds");
            edt_kgs.setVisibility(View.GONE);
            edt_grams.setVisibility(View.GONE);
            edt_pounds.setVisibility(View.VISIBLE);
            isPounds = true;
        }

        if (AppPreference.getInstance().getFtIN()) {
            txt_height.setText("Ft & In");
            edt_feet.setVisibility(View.VISIBLE);
            edt_inchs.setVisibility(View.VISIBLE);
            edt_cm.setVisibility(View.GONE);
            isCm = false;
        }
        if (AppPreference.getInstance().getCM()) {
            txt_height.setText("Cm");
            edt_feet.setVisibility(View.GONE);
            edt_inchs.setVisibility(View.GONE);
            edt_cm.setVisibility(View.VISIBLE);
            isCm = true;
        }

//        if (AppPreference.getInstance().getMale()) {
//            radioMale.setChecked(true);
//            getBmrForFeMale();
//        }
//        if (AppPreference.getInstance().getFeMale()) {
//            radioFemale.setChecked(true);
//            getBmrForMale();
//        }


//        if (AppPreference.getInstance().getGlass().equalsIgnoreCase("100")) {
//            img_100ml.setVisibility(View.VISIBLE);
//            img_200ml.setVisibility(View.INVISIBLE);
//            img_500ml.setVisibility(View.INVISIBLE);
//            img_300ml.setVisibility(View.INVISIBLE);
//        } else if (AppPreference.getInstance().getGlass().equalsIgnoreCase("200")) {
//            img_100ml.setVisibility(View.INVISIBLE);
//            img_200ml.setVisibility(View.VISIBLE);
//            img_500ml.setVisibility(View.INVISIBLE);
//            img_300ml.setVisibility(View.INVISIBLE);
//        } else if (AppPreference.getInstance().getGlass().equalsIgnoreCase("300")) {
//            img_100ml.setVisibility(View.INVISIBLE);
//            img_200ml.setVisibility(View.INVISIBLE);
//            img_300ml.setVisibility(View.VISIBLE);
//            img_500ml.setVisibility(View.INVISIBLE);
//        } else if (AppPreference.getInstance().getGlass().equalsIgnoreCase("500")) {
//            img_100ml.setVisibility(View.INVISIBLE);
//            img_200ml.setVisibility(View.INVISIBLE);
//            img_300ml.setVisibility(View.INVISIBLE);
//            img_500ml.setVisibility(View.VISIBLE);
//        }

        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);
        return rootView;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            CureFull.getInstanse().getActivityIsntanse().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    AdapterView.OnItemClickListener popUpItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listPopupWindow.dismiss();
            if (position == 0) {
                txt_height.setText("Ft & In");
                edt_feet.setVisibility(View.VISIBLE);
                edt_inchs.setVisibility(View.VISIBLE);
                edt_cm.setVisibility(View.GONE);
                isCm = false;
                AppPreference.getInstance().setFtIN(true);
                AppPreference.getInstance().setCM(false);
            } else {
                txt_height.setText("Cm");
                edt_feet.setVisibility(View.GONE);
                edt_inchs.setVisibility(View.GONE);
                edt_cm.setVisibility(View.VISIBLE);
                isCm = true;
                AppPreference.getInstance().setCM(true);
                AppPreference.getInstance().setFtIN(false);
            }
        }
    };

    AdapterView.OnItemClickListener popUpItemClick1 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listPopupWindow.dismiss();
            if (position == 0) {
                txt_weight.setText("Kgs");
                edt_kgs.setVisibility(View.VISIBLE);
                edt_grams.setVisibility(View.VISIBLE);
                edt_pounds.setVisibility(View.GONE);
                isPounds = false;
                AppPreference.getInstance().setKgs(true);
                AppPreference.getInstance().setpound(false);
            } else {
                txt_weight.setText("Pounds");
                edt_kgs.setVisibility(View.GONE);
                edt_grams.setVisibility(View.GONE);
                edt_pounds.setVisibility(View.VISIBLE);
                isPounds = true;
                AppPreference.getInstance().setpound(true);
                AppPreference.getInstance().setKgs(false);
            }
        }
    };

    //BMI calculator
    private void bmiCalculator() {

        if (isCm) {
            if (!validateHeightCM()) {
                return;
            }
        } else {
            if (!validateHeightFeetBMI()) {
                return;
            }

        }


        if (isPounds) {
            if (!validateWeightPounds()) {
                return;
            }
        } else {
            if (!validateWeightKgsBMI()) {
                Log.e("failed", "failed");
                return;
            }
        }


        Log.e("pass", "pass");
        if (!isCm) {
            heightfeet = Integer.parseInt(edt_feet.getText().toString().trim());
            if (!edt_inchs.getText().toString().trim().equalsIgnoreCase("")) {
                heightInch = Integer.parseInt(edt_inchs.getText().toString().trim());
            }

        }
        if (!isPounds) {
//            String weight;
//            if (edt_kgs.getText().toString().trim().equalsIgnoreCase("0.0")) {
//                weight = "0";
//            } else {
//                weight = edt_kgs.getText().toString().trim();
//            }
            weightkg = Double.parseDouble(edt_kgs.getText().toString().trim());
        }

        int weightGrm = 0;
        if (!edt_grams.getText().toString().trim().equalsIgnoreCase("")) {
            weightGrm = Integer.parseInt(edt_grams.getText().toString().trim());
        }

        double h = 0;
        if (heightInch != 0) {
            float inches = (heightfeet * 12) + heightInch;
            h = inches * 0.0254;
        } else {
            float inches = heightfeet * 12;
            h = inches * 0.0254;
        }
        double w = weightkg + (weightGrm / 1000);
        double bmi = w / (h * h);
        txt_BMI.setText("" + new DecimalFormat("##.##").format(bmi) + " " + interpretBMI(bmi));
    }

    private String interpretBMI(double bmiValue) {

        if (bmiValue < 16) {
            return "Severely underweight";
        } else if (bmiValue < 18.5) {
            return "Underweight";
        } else if (bmiValue < 25) {

            return "Normal";
        } else if (bmiValue < 30) {

            return "Overweight";
        } else {
            return "Obese";
        }
    }

    private boolean validateHeightFeetBMI() {
        String email = edt_feet.getText().toString().trim();

        if (email.equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }

    private boolean validateHeightinchBMI() {
        String email = edt_inchs.getText().toString().trim();
        if (email.equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }


    private boolean validateWeightKgsBMI() {
        String email = edt_kgs.getText().toString().trim();
        if (email.equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }


    private boolean validateAge() {
        String email = edt_years.getText().toString().trim();
        if (email.equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }


    private boolean validateHeightCM() {
        String email = edt_cm.getText().toString().trim();
        if (email.equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }

    private boolean validateWeightPounds() {
        String email = edt_pounds.getText().toString().trim();
        if (email.equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }

    public void c2f(int cm) {
        double feet = cm / 30.48;
        double inches = (cm / 2.54) - ((int) feet * 12);
        heightfeet = (int) feet;
        heightInch = (int) inches;
        Log.e("There are ", (int) feet + " feet and " + (int) inches + " inches in " + cm + "cm");
    }

    public void poundToKgs(double pounds) {
        double kilograms = pounds * 0.454;
        weightkg = (int) kilograms;
        Log.e("hello ", pounds + " pounds is " + kilograms + " killograms");
    }

    public void getBmrForMale() {

        if (!validateAge()) {
            return;
        }

        if (isCm) {
            if (!validateHeightCM()) {
                return;
            }
        } else {
            if (!validateHeightFeetBMI()) {
                return;
            }

        }


        if (isPounds) {
            if (!validateWeightPounds()) {
                return;
            }
        } else {
            if (!validateWeightKgsBMI()) {
                Log.e("failed", "failed");
                return;
            }
        }


        String feet = String.valueOf(heightfeet);
        String inches = String.valueOf(heightInch);
        int bmr = (int) ((10 * weightkg) + (6.25 * Utils.convertFeetandInchesToCentimeter(feet, inches)) - (5 * Integer.parseInt(AppPreference.getInstance().getGoalAgeNew()) + 5));
        Log.e("BMR", ":- " + bmr);
        txt_BMR.setText("" + bmr + " Calories/day");
    }

    public void getBmrForFeMale() {

        if (!validateAge()) {
            return;
        }
        if (isCm) {
            if (!validateHeightCM()) {
                return;
            }
        } else {
            if (!validateHeightFeetBMI()) {
                return;
            }

        }


        if (isPounds) {
            if (!validateWeightPounds()) {
                return;
            }
        } else {
            if (!validateWeightKgsBMI()) {
                Log.e("failed", "failed");
                return;
            }
        }
        String feet = String.valueOf(heightfeet);
        String inches = String.valueOf(heightInch);


        Log.e("total:- ", "weight :- " + weightkg + " :- feeet" + Utils.convertFeetandInchesToCentimeter(feet, inches) + " year" + AppPreference.getInstance().getGoalAgeNew());
        int bmr = (int) ((10 * weightkg) + (6.25 * Utils.convertFeetandInchesToCentimeter(feet, inches)) - (5 * Integer.parseInt(AppPreference.getInstance().getGoalAgeNew())) - 161);
        Log.e("BMR", ":- " + bmr);
        txt_BMR.setText("" + bmr + " Calories/day");
    }


    public void jsonUploadTarget(final String targetSteps, String targetCaloriesToBurn, String targetWaterInTake) {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
        JSONObject data = JsonUtilsObject.toSetGoals(targetSteps, targetCaloriesToBurn, targetWaterInTake);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.SET_GOALS, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("jsonUpload, URL 3.", response.toString());
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == 100) {
                            edt_water.setEnabled(false);
                            edt_water.setFocusable(false);
                            edt_steps.setEnabled(false);
                            edt_steps.setFocusable(false);
                            edt_calories.setEnabled(false);
                            edt_calories.setFocusable(false);
                            AppPreference.getInstance().setStepsCountTarget(Integer.parseInt(targetSteps));
                            AppPreference.getInstance().setWaterInTakeTarget(edt_water.getText().toString().trim());
                            edt_water.setText(edt_water.getText().toString().trim() + " Ltr");
                            btn_edit_goal.setText("Edit Goal");
//                            UserInfo userInfo = ParseJsonData.getInstance().getLoginData(response.toString());
//                            if (ParseJsonData.getInstance().getHttp_code().equalsIgnoreCase(MyConstants.JsonUtils.OK)) {
//                            }
                        } else {
                            try {
                                JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                JSONObject json12 = new JSONObject(json1.getString("errorDetails"));
                                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + json12.getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
                VolleyLog.e("FragmentLogin, URL 3.", "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("a_t", AppPreference.getInstance().getAt());
                headers.put("r_t", AppPreference.getInstance().getRt());
                headers.put("user_name", AppPreference.getInstance().getUserName());
                headers.put("email_id", AppPreference.getInstance().getUserID());
                headers.put("cf_uuhid", AppPreference.getInstance().getcf_uuhid());
                return headers;
            }
        };
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }


    public void jsonGlassTarget(int targetWaterInTake) {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.SELECT_GLASS + targetWaterInTake, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("GlassTarget, URL 3.", response.toString());
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
//                            UserInfo userInfo = ParseJsonData.getInstance().getLoginData(response.toString());
//                            if (ParseJsonData.getInstance().getHttp_code().equalsIgnoreCase(MyConstants.JsonUtils.OK)) {
//                            }
                        } else {
                            try {
                                JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                JSONObject json12 = new JSONObject(json1.getString("errorDetails"));
                                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + json12.getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
                VolleyLog.e("FragmentLogin, URL 3.", "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("a_t", AppPreference.getInstance().getAt());
                headers.put("r_t", AppPreference.getInstance().getRt());
                headers.put("user_name", AppPreference.getInstance().getUserName());
                headers.put("email_id", AppPreference.getInstance().getUserID());
                headers.put("cf_uuhid", AppPreference.getInstance().getcf_uuhid());
                return headers;
            }
        };
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.liner_100ml:
                glassInTake = 100;
                img_100ml.setVisibility(View.VISIBLE);
                img_200ml.setVisibility(View.INVISIBLE);
                img_500ml.setVisibility(View.INVISIBLE);
                img_300ml.setVisibility(View.INVISIBLE);
                break;
            case R.id.liner_200ml:
                glassInTake = 200;
                img_100ml.setVisibility(View.INVISIBLE);
                img_200ml.setVisibility(View.VISIBLE);
                img_500ml.setVisibility(View.INVISIBLE);
                img_300ml.setVisibility(View.INVISIBLE);
                break;
            case R.id.liner_300ml:
                glassInTake = 300;
                img_100ml.setVisibility(View.INVISIBLE);
                img_200ml.setVisibility(View.INVISIBLE);
                img_300ml.setVisibility(View.VISIBLE);
                img_500ml.setVisibility(View.INVISIBLE);
                break;
            case R.id.liner_500ml:
                glassInTake = 500;
                img_100ml.setVisibility(View.INVISIBLE);
                img_200ml.setVisibility(View.INVISIBLE);
                img_300ml.setVisibility(View.INVISIBLE);
                img_500ml.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_done:

                String gender = "";
                if (AppPreference.getInstance().getMale() == true) {
                    gender = "MALE";
                } else {
                    gender = "FEMALE";
                }
                jsonGlassTarget(glassInTake);
                AppPreference.getInstance().setGlass("" + glassInTake);
                AppPreference.getInstance().setIsLoginFirst(false);
                AppPreference.getInstance().setStepStarts(true);
                CureFull.getInstanse().getActivityIsntanse().onBackPressed();
                break;

            case R.id.btn_edit_goal:
                edt_water.setEnabled(true);
                edt_water.setFocusable(true);
                edt_water.setFocusableInTouchMode(true);
                edt_steps.setEnabled(true);
                edt_steps.setFocusableInTouchMode(true);
                edt_steps.setFocusable(true);
                edt_calories.setEnabled(true);
                edt_calories.setFocusable(true);
                edt_calories.setFocusableInTouchMode(true);
                if (btn_edit_goal.getText().toString().trim().equalsIgnoreCase("Done")) {
                    String steps = edt_steps.getText().toString().trim();
                    String calories = edt_calories.getText().toString().trim();
                    String water = edt_water.getText().toString().trim().replace(" Ltr", "");
                    jsonUploadTarget(steps, calories, water);
                }
                if (btn_edit_goal.getText().toString().trim().equalsIgnoreCase("Edit Goal")) {
                    btn_edit_goal.setText("Done");
                }
                break;

        }

    }


    private void getAllDetails() {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.GET_SET_GOALS_DEATILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Log.e("getSymptomsList, URL 1.", response);
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            GoalInfo userInfo = ParseJsonData.getInstance().getGoalDeatils(response.toString());

//                            if (!userInfo.getDateOfBirth().equalsIgnoreCase("")) {
//                                edt_years.setText("" + AppPreference.getInstance().getGoalAgeNew());
//                            }
//                            if (!userInfo.getHeight().equalsIgnoreCase("")) {
//                                if (userInfo.getHeight().equalsIgnoreCase("0.0"))

//                                edt_cm.setText("" + userInfo.getHeight());
//                            }
//
//                            if (!userInfo.getWeight().equalsIgnoreCase("")) {
//                                edt_kgs.setText("" + userInfo.getWeight());
//                                edt_grams.setText("00");
//
//                            }

                            String age = userInfo.getDateOfBirth();
                            if (!age.equalsIgnoreCase("null")) {
                                String[] dateFormat = age.split("-");
                                int mYear = Integer.parseInt(dateFormat[0]);
                                int mMonth = Integer.parseInt(dateFormat[1]);
                                int mDay = Integer.parseInt(dateFormat[2]);
                                edt_years.setText("" + (mDay < 10 ? "0" + mDay : mDay) + "-" + (mMonth < 10 ? "0" + mMonth : mMonth) + "-" + mYear);
                                AppPreference.getInstance().setGoalAgeNew("" + Utils.getAge(mYear, mMonth, mDay));
                                AppPreference.getInstance().setGoalAge(userInfo.getDateOfBirth());
                            }

                            Log.e("getHeight cm", ": -" + userInfo.getHeight());
                            if (!"null".equals(userInfo.getHeight())) {
                                c2f(Integer.parseInt(userInfo.getHeight()));
                                AppPreference.getInstance().setGoalHeightFeet("" + heightfeet);
                                AppPreference.getInstance().setGoalHeightInch("" + heightInch);
                                Log.e("heightfeet", ": -" + heightfeet);
                                Log.e("heightInch", ": -" + heightInch);
                            }
                            Log.e("getWeight", ": -" + userInfo.getWeight());
                            if (!userInfo.getWeight().equalsIgnoreCase("null")) {
                                AppPreference.getInstance().setGoalWeightKg("" + userInfo.getWeight());
                            }


                            if (!userInfo.getTargetStepCount().equalsIgnoreCase("null")) {
                                edt_steps.setText("" + userInfo.getTargetStepCount());
                                AppPreference.getInstance().setStepsCountTarget(Integer.parseInt(userInfo.getTargetStepCount()));
                            }
                            if (!userInfo.getTargetStepCount().equalsIgnoreCase("null")) {
                                edt_calories.setText("" + userInfo.getTargetCaloriesToBurn());
                            }

                            if (!userInfo.getTargetWaterInTake().equalsIgnoreCase("null")) {
                                edt_water.setText("" + userInfo.getTargetWaterInTake() + " Ltr");
                                AppPreference.getInstance().setWaterInTakeTarget(userInfo.getTargetWaterInTake());
                            }

//                            if (!AppPreference.getInstance().getGoalAge().equalsIgnoreCase("") && !AppPreference.getInstance().getGoalAge().equalsIgnoreCase("0")) {
//                                edt_years.setText("" + AppPreference.getInstance().getGoalAgeNew());
//                            }
                            if (!AppPreference.getInstance().getGoalHeightFeet().equalsIgnoreCase("") && !AppPreference.getInstance().getGoalHeightFeet().equalsIgnoreCase("0")) {
                                edt_feet.setText("" + AppPreference.getInstance().getGoalHeightFeet());
                            }
                            if (!AppPreference.getInstance().getGoalHeightInch().equalsIgnoreCase("") && !AppPreference.getInstance().getGoalHeightInch().equalsIgnoreCase("0")) {
                                edt_inchs.setText("" + AppPreference.getInstance().getGoalHeightInch());
                            }
                            if (!AppPreference.getInstance().getGoalWeightKg().equalsIgnoreCase("") && !AppPreference.getInstance().getGoalWeightKg().equalsIgnoreCase("0")) {
                                edt_kgs.setText("" + AppPreference.getInstance().getGoalWeightKg());
                            }
                            if (!AppPreference.getInstance().getGoalWeightGrams().equalsIgnoreCase("") && !AppPreference.getInstance().getGoalWeightGrams().equalsIgnoreCase("0")) {
                                edt_grams.setText("" + AppPreference.getInstance().getGoalWeightGrams());
                            }

                            if (!userInfo.getGender().equalsIgnoreCase("null")) {
                                if (userInfo.getGender().equalsIgnoreCase("MALE")) {
                                    AppPreference.getInstance().setMale(true);
                                    AppPreference.getInstance().setFeMale(false);
                                    isMale = true;
                                    isFemale = false;
                                    radioMale.setChecked(true);
                                    getBmrForFeMale();
                                    if (heightfeet > 4) {
                                        if (!validateHeightFeetBMI()) {
                                            return;
                                        }
                                        if (!validateHeightinchBMI()) {
                                            return;
                                        }
                                        if (heightfeet == 5) {
                                            txt_ideal_weight.setText("" + Utils.getIdealWeightMen(heightInch) + " kg");
                                        } else {
                                            int checkheight = ((heightfeet * 12) - 60) + heightInch;
                                            txt_ideal_weight.setText("" + Utils.getIdealWeightMen(checkheight) + " kg");
                                        }
                                    } else {
                                        if (!validateHeightFeetBMI()) {
                                            return;
                                        }
                                        if (!validateHeightinchBMI()) {
                                            return;
                                        }
                                        txt_ideal_weight.setText("" + "50 kg");
                                    }
                                } else {
                                    AppPreference.getInstance().setMale(false);
                                    AppPreference.getInstance().setFeMale(true);
                                    radioFemale.setChecked(true);
                                    isMale = false;
                                    isFemale = true;
                                    getBmrForMale();
                                    if (heightfeet > 4) {
                                        if (!validateHeightFeetBMI()) {
                                            return;
                                        }
                                        if (!validateHeightinchBMI()) {
                                            return;
                                        }
                                        if (heightfeet == 5) {
                                            txt_ideal_weight.setText("" + Utils.getIdealWeightWomen(heightInch) + " kg");
                                        } else {
                                            int checkheight = ((heightfeet * 12) - 60) + heightInch;
                                            txt_ideal_weight.setText("" + Utils.getIdealWeightWomen(checkheight) + " kg");
                                        }
                                    } else {
                                        if (!validateHeightFeetBMI()) {
                                            return;
                                        }
                                        if (!validateHeightinchBMI()) {
                                            return;
                                        }
                                        txt_ideal_weight.setText("" + "45 kg");
                                    }
                                }
//
                            }

//
                            if (userInfo.getGlassSize().equalsIgnoreCase("100")) {
                                glassInTake = 100;
                                AppPreference.getInstance().setGlass("" + glassInTake);
                                img_100ml.setVisibility(View.VISIBLE);
                                img_200ml.setVisibility(View.INVISIBLE);
                                img_500ml.setVisibility(View.INVISIBLE);
                                img_300ml.setVisibility(View.INVISIBLE);
                            } else if (userInfo.getGlassSize().equalsIgnoreCase("200")) {
                                glassInTake = 200;
                                AppPreference.getInstance().setGlass("" + glassInTake);
                                img_100ml.setVisibility(View.INVISIBLE);
                                img_200ml.setVisibility(View.VISIBLE);
                                img_500ml.setVisibility(View.INVISIBLE);
                                img_300ml.setVisibility(View.INVISIBLE);
                            } else if (userInfo.getGlassSize().equalsIgnoreCase("300")) {
                                glassInTake = 300;
                                AppPreference.getInstance().setGlass("" + glassInTake);
                                img_100ml.setVisibility(View.INVISIBLE);
                                img_200ml.setVisibility(View.INVISIBLE);
                                img_300ml.setVisibility(View.VISIBLE);
                                img_500ml.setVisibility(View.INVISIBLE);
                            } else if (userInfo.getGlassSize().equalsIgnoreCase("500")) {
                                glassInTake = 500;
                                AppPreference.getInstance().setGlass("" + glassInTake);
                                img_100ml.setVisibility(View.INVISIBLE);
                                img_200ml.setVisibility(View.INVISIBLE);
                                img_300ml.setVisibility(View.INVISIBLE);
                                img_500ml.setVisibility(View.VISIBLE);
                            }

                        } else {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        error.printStackTrace();
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("a_t", AppPreference.getInstance().getAt());
                headers.put("r_t", AppPreference.getInstance().getRt());
                headers.put("user_name", AppPreference.getInstance().getUserName());
                headers.put("email_id", AppPreference.getInstance().getUserID());
                headers.put("cf_uuhid", AppPreference.getInstance().getcf_uuhid());
                return headers;
            }
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        int mnt = (monthOfYear + 1);
        bmiCalculator();
        if (isFemale) {
            getBmrForFeMale();
        }
        if (isMale) {
            getBmrForMale();
        }

        AppPreference.getInstance().setGoalAge("" + year + "-" + (mnt < 10 ? "0" + mnt : mnt) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth));
        edt_years.setText("" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + (mnt < 10 ? "0" + mnt : mnt) + "-" + year);
        AppPreference.getInstance().setGoalAgeNew("" + Utils.getAge(year, mnt, dayOfMonth));
    }


}