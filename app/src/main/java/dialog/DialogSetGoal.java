package dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import android.preference.PreferenceManager;

import android.support.v7.widget.ListPopupWindow;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ElasticVIews.ElasticAction;
import adpter.AddImageDoneAdpter;
import asyns.JsonUtilsObject;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import fragment.healthapp.FragmentEditGoal;
import interfaces.IOnCheckCheckbox;
import interfaces.IOnDoneMoreImage;
import item.property.PrescriptionImageList;
import operations.DbOperations;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;
import utils.SpacesItemDecoration;
import utils.Utils;


public class DialogSetGoal extends Dialog implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private View v = null;
    Context context;

   // private IOnDoneMoreImage iOnDoneMoreImage;

    private TextView txt_height, txt_weight, btn_edit_next, edt_years;
    private RadioGroup radioGender;
    private RadioButton radioMale, radioFemale;
    private EditText edt_feet, edt_inchs, edt_cm, edt_kgs, edt_grams, edt_pounds;
    private ImageView img_select_height, img_select_weight, dialog_cancel;
    private boolean isCm = false;
    private boolean isPounds = false;
    private boolean isMale = false;
    private boolean isFemale = false;
    private ListPopupWindow listPopupWindow;
    private int pos;
    private int heightfeet;
    private double heightInch;
    private double weightkg;
    SharedPreferences preferences;
    int bmr;
    String str_ideal_weight = "";
    String txt_BMI = "";

    public DialogSetGoal(Context _activiyt) {
        super(_activiyt, R.style.MyTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = _activiyt;
        setCancelable(true);
        setContentView(R.layout.dialog_set_goal);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        preferences = PreferenceManager.getDefaultSharedPreferences(CureFull.getInstanse().getActivityIsntanse());
        txt_weight = (TextView) findViewById(R.id.txt_weight);
        txt_height = (TextView) findViewById(R.id.txt_height);
        img_select_height = (ImageView) findViewById(R.id.img_select_height);
        img_select_weight = (ImageView) findViewById(R.id.img_select_weight);
        radioGender = (RadioGroup) findViewById(R.id.radioGender);
        radioMale = (RadioButton) findViewById(R.id.radioMale);
        radioFemale = (RadioButton) findViewById(R.id.radioFemale);
        edt_years = (TextView) findViewById(R.id.edt_years);//dob


        edt_feet = (EditText) findViewById(R.id.edt_feet);
        edt_inchs = (EditText) findViewById(R.id.edt_inchs);
        edt_cm = (EditText) findViewById(R.id.edt_cm);

        edt_kgs = (EditText) findViewById(R.id.edt_kgs);
        edt_grams = (EditText) findViewById(R.id.edt_grams);
        edt_pounds = (EditText) findViewById(R.id.edt_pounds);

        dialog_cancel = (ImageView) findViewById(R.id.dialog_cancel);
        btn_edit_next = (TextView) findViewById(R.id.btn_edit_next);
        dialog_cancel.setOnClickListener(this);
        btn_edit_next.setOnClickListener(this);
        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioMale) {
                    AppPreference.getInstance().setMale(true);
                    AppPreference.getInstance().setFeMale(false);
                    isMale = true;
                    isFemale = false;

                } else if (checkedId == R.id.radioFemale) {
                    AppPreference.getInstance().setMale(false);
                    AppPreference.getInstance().setFeMale(true);
                    isMale = false;
                    isFemale = true;

                }

            }
        });
        edt_years.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int year, day;
                int month1;
                final Calendar c1 = Calendar.getInstance();

                if (AppPreference.getInstance().getGoalAge().equalsIgnoreCase("0")) {
                    year = c1.get(Calendar.YEAR);
                    month1 = c1.get(Calendar.MONTH);
                    day = c1.get(Calendar.DAY_OF_MONTH);
                } else {
                    String[] dateFormat = AppPreference.getInstance().getGoalAge().split("-");
                    year = Integer.parseInt(dateFormat[0]);
                    month1 = Integer.parseInt(dateFormat[1]);
                    day = Integer.parseInt(dateFormat[2]);
                    month1 = (month1 - 1);
                }
                DatePickerDialog newDateDialog = new DatePickerDialog(CureFull.getInstanse().getActivityIsntanse(), AlertDialog.THEME_HOLO_LIGHT, DialogSetGoal.this, year, month1, day);
                newDateDialog.getDatePicker().setSpinnersShown(true);
//                c.add(Calendar.DATE, 1);
                Date newDate = c1.getTime();
                newDateDialog.getDatePicker().setMaxDate(newDate.getTime());
                newDateDialog.show();
            }
        });

        (findViewById(R.id.img_select_height)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                listPopupWindow.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                        R.layout.adapter_list_doctor_data, MyConstants.IArrayData.listPopUpHeight));
                listPopupWindow.setAnchorView(findViewById(R.id.txt_height));
                listPopupWindow.setWidth((int) context.getResources().getDimension(R.dimen._80dp));
//                listPopupWindow.setHeight(400);
                listPopupWindow.setModal(true);
                listPopupWindow.setOnItemClickListener(popUpItemClick);
                listPopupWindow.show();
            }
        });

        (findViewById(R.id.img_select_weight)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                listPopupWindow.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                        R.layout.adapter_list_doctor_data, MyConstants.IArrayData.listPopUpWeight));
                listPopupWindow.setAnchorView(findViewById(R.id.txt_weight));
                listPopupWindow.setWidth((int) context.getResources().getDimension(R.dimen._80dp));
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

                if (s.length() > 0) {
                    isCm = false;
                    int value = Integer.parseInt(s.toString());
                    if (value > 10) {
                        value = 10;
                        edt_feet.setText("" + value);
                        AppPreference.getInstance().setGoalHeightFeet("" + value);
                    } else {
                        AppPreference.getInstance().setGoalHeightFeet("" + value);
                    }

                    getEditTextLength(edt_feet);

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
                if (s.length() > 0) {
                    isCm = true;
                    double value = Double.parseDouble(s.toString());
                    if (value > 332.74) {
                        value = 332.74;
                        edt_cm.setText("" + value);
                        AppPreference.getInstance().setGoalHeightCm("" + value);
                    } else {
                        AppPreference.getInstance().setGoalHeightCm("" + value);
                    }
                    getEditTextLength(edt_cm);
                    if (!s.toString().equalsIgnoreCase("0.0")) {
                        c2f(Double.parseDouble(s.toString()), "plus");
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
                if (s.length() > 0) {
                    isPounds = true;
                    double value = Double.parseDouble(s.toString());
                    if (value > 1102.31) {
                        value = 1102.31;
                        edt_pounds.setText("" + value);
                        AppPreference.getInstance().setGoalWeightPound("" + value);
                    } else {
                        AppPreference.getInstance().setGoalWeightPound("" + value);
                    }
                    getEditTextLength(edt_pounds);
                    poundToKgs(Double.parseDouble(s.toString()));

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
                if (s.toString().equalsIgnoreCase("")) {
                    AppPreference.getInstance().setGoalHeightInch("");
                    heightInch = 0;
                }
                if (s.length() > 0) {
                    double value = Double.parseDouble(s.toString());
                    if (value > 11) {
                        value = 11;
                        edt_inchs.setText("" + (int) value);
                        AppPreference.getInstance().setGoalHeightInch("" + (int) value);
                    } else {
                        if (value == 0.0) {
                            value = 0;
                        }
                        AppPreference.getInstance().setGoalHeightInch("" + value);
                    }
                    getEditTextLength(edt_inchs);

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
                if (s.length() > 0) {
                    isPounds = false;
                    int value = Integer.parseInt(s.toString());
                    if (value > 800) {
                        value = 800;
                        edt_kgs.setText("" + (int) value);
                        preferences.edit().putString("kg", "" + (int) value).commit();
                        AppPreference.getInstance().setGoalWeightKg("" + (int) value);
                    } else {
                        preferences.edit().putString("kg", "" + value).commit();
                        AppPreference.getInstance().setGoalWeightKg("" + value);
                    }
                    getEditTextLength(edt_kgs);
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
                if (s.length() > 0) {
                    AppPreference.getInstance().setGoalWeightGrams("" + s.toString());
                    preferences.edit().putString("gram", "" + s.toString()).commit();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (AppPreference.getInstance().getKgs()) {
            txt_weight.setText("Kgs");
            edt_kgs.setVisibility(View.VISIBLE);
            edt_grams.setVisibility(View.VISIBLE);
            edt_pounds.setVisibility(View.GONE);
            isPounds = false;
            AppPreference.getInstance().setKgs(true);
            AppPreference.getInstance().setpound(false);
            edt_kgs.setText("" + AppPreference.getInstance().getGoalWeightKg());
            edt_grams.setText("" + AppPreference.getInstance().getGoalWeightGrams());
        }

        if (AppPreference.getInstance().getPound()) {
            txt_weight.setText("Pounds");
            edt_kgs.setVisibility(View.GONE);
            edt_grams.setVisibility(View.GONE);
            edt_pounds.setVisibility(View.VISIBLE);
            AppPreference.getInstance().setKgs(false);
            AppPreference.getInstance().setpound(true);
            isPounds = true;
            if (!edt_grams.getText().toString().trim().equalsIgnoreCase("")) {
                edt_pounds.setText("" + Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg() + "." + AppPreference.getInstance().getGoalWeightGrams())));
            } else {
                edt_pounds.setText("" + Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg())));
            }
        }

        if (AppPreference.getInstance().getFtIN()) {
            txt_height.setText("Ft & In");
            edt_feet.setVisibility(View.VISIBLE);
            edt_inchs.setVisibility(View.VISIBLE);
            edt_cm.setVisibility(View.GONE);
            isCm = false;
            AppPreference.getInstance().setFtIN(true);
            AppPreference.getInstance().setCM(false);
            edt_feet.setText("" + AppPreference.getInstance().getGoalHeightFeet());
            edt_inchs.setText("" + AppPreference.getInstance().getGoalHeightInch());
        }
        if (AppPreference.getInstance().getCM()) {
            txt_height.setText("Cm");
            edt_feet.setVisibility(View.GONE);
            edt_inchs.setVisibility(View.GONE);
            edt_cm.setVisibility(View.VISIBLE);
            AppPreference.getInstance().setFtIN(false);
            AppPreference.getInstance().setCM(true);
            isCm = true;
            edt_cm.setText("" + AppPreference.getInstance().getGoalHeightCm());
        }
        edt_feet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edt_inchs.requestFocus();
                }
                return false;
            }
        });


        edt_kgs.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edt_grams.requestFocus();
                }
                return false;
            }
        });


        edt_grams.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    setRecommededDetails();
                }
                return false;
            }
        });
        edt_pounds.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    setRecommededDetails();
                }
                return false;
            }
        });

        CureFull.getInstanse().getActivityIsntanse().clickImage(v);
    }


    public void setRecommededDetails() {

        if (!validateAge()) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "DOB can not be blank");
            return;
        }

        if (isCm) {
            if (!validateHeightCM()) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "Height CM can not be blank");
                return;
            }
        } else {
            if (!validateHeightFeetBMI()) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "Height Feet can not be blank");
                return;
            }

        }


        if (isPounds) {
            if (!validateWeightPounds()) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "Pound can not be blank");
                return;
            }
        } else {
            if (!validateWeightKgsBMI()) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "Weight kg can not be blank");
                return;
            }
        }
        String feet = String.valueOf(heightfeet);
        String inches = String.valueOf(heightInch);
        double totalHeight;
        if (isCm) {
            totalHeight = Double.parseDouble(edt_cm.getText().toString().trim());
        } else {
            totalHeight = Utils.convertFeetandInchesToCentimeter(feet, inches);
        }

        double totalWeight;

        if (isPounds) {
            totalWeight = Double.parseDouble(edt_pounds.getText().toString().trim());
        } else {
            if (!edt_grams.getText().toString().trim().equalsIgnoreCase("")) {
                totalWeight = Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg() + "." + AppPreference.getInstance().getGoalWeightGrams()));
            } else {
                totalWeight = Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg()));
            }
        }
        String gender = "MALE";
        if (!isMale && !isFemale) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "Select Gender can not be blank");
            return;
        }
        if (isMale) {
            gender = "MALE";
        } else {
            gender = "FEMALE";
        }
        String dateOfBirth = AppPreference.getInstance().getGoalAge();
        JSONObject data = JsonUtilsObject.toSetGoalsDetails(String.valueOf(new DecimalFormat("###.###").format(totalHeight)), String.valueOf(new DecimalFormat("###.###").format(totalWeight)), dateOfBirth, gender);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.SET_GOALS_DEATILS, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                AppPreference.getInstance().setIsEditGoal(true);
                                JSONObject json1 = new JSONObject(json.getString("payload"));
                                String targetStepCount = json1.getString("targetStepCount");
                                String targetCaloriesToBurn = json1.getString("targetCaloriesToBurn");
                                String targetWaterInTake = json1.getString("targetWaterInTake");
                                if (!targetStepCount.equalsIgnoreCase("null")) {
                                    //edt_steps.setText("" + targetStepCount);
                                    AppPreference.getInstance().setStepsCountTarget(Integer.parseInt(targetStepCount));
                                }
                                if (!targetCaloriesToBurn.equalsIgnoreCase("null")) {
                                    //edt_calories.setText("" + targetCaloriesToBurn);
                                }
                                if (!targetWaterInTake.equalsIgnoreCase("null")) {
                                    //edt_water.setText("" + new DecimalFormat("###.#").format(Utils.getMlToLiter(Integer.parseInt(targetWaterInTake))) + " L");
                                    AppPreference.getInstance().setWaterInTakeTarget(targetWaterInTake);
                                }
                                DialogSetGoal_BMI dialogSetGoal_bmi = new DialogSetGoal_BMI(CureFull.getInstanse().getActivityIsntanse(), txt_BMI, bmr, str_ideal_weight, targetStepCount, targetCaloriesToBurn, targetWaterInTake);
                                dialogSetGoal_bmi.show();
                                dismiss();

                               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    ElasticAction.doAction(txt_BMI, 5000, 1.2f, 1.2f);
                                    ElasticAction.doAction(txt_BMR, 5000, 1.2f, 1.2f);
                                    ElasticAction.doAction(txt_ideal_weight, 5000, 1.2f, 1.2f);
                                    ElasticAction.doAction(edt_steps, 5000, 1.1f, 1.1f);
                                    ElasticAction.doAction(edt_calories, 5000, 1.1f, 1.1f);
                                    ElasticAction.doAction(edt_water, 5000, 1.1f, 1.1f);
                                    handler = new Handler();
                                    handler.removeCallbacksAndMessages(null);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            DialogEditGoal dialogEditGoal = new DialogEditGoal(CureFull.getInstanse().getActivityIsntanse(), "", "Set Glass", 0);
                                            dialogEditGoal.setiOnOtpDoneDelete(FragmentEditGoal.this);
                                            dialogEditGoal.show();
                                        }
                                    }, 2000);

                                }
*/
                            } else {
                                try {
                                    JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                    JSONObject json12 = new JSONObject(json1.getString("errorDetails"));
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "" + json12.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

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
                headers.put("user_id", AppPreference.getInstance().getUserIDProfile());
                return headers;
            }
        };
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }

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
                return;
            }
        }
        if (!isCm) {
            heightfeet = Integer.parseInt(edt_feet.getText().toString().trim());
            if (!edt_inchs.getText().toString().trim().equalsIgnoreCase("")) {
                heightInch = Double.parseDouble(edt_inchs.getText().toString().trim());
            }
        } else {
            heightInch = Double.parseDouble(new DecimalFormat("###.#").format(heightInch));
        }
        double totalWeight;
        if (isPounds) {
            totalWeight = Double.parseDouble(new DecimalFormat("###.#").format(Utils.getConvertingPoundsIntoKilograms(Double.parseDouble(edt_pounds.getText().toString().trim()))));
        } else {
            if (!edt_grams.getText().toString().trim().equalsIgnoreCase("")) {
                totalWeight = Double.parseDouble(AppPreference.getInstance().getGoalWeightKg() + "." + AppPreference.getInstance().getGoalWeightGrams());
            } else {
                totalWeight = Double.parseDouble(AppPreference.getInstance().getGoalWeightKg());
            }
        }
//        if (!isPounds) {
//            weightkg = Double.parseDouble(edt_kgs.getText().toString().trim());
//        }
//
//        int weightGrm = 0;
//        if (!edt_grams.getText().toString().trim().equalsIgnoreCase("")) {
//            weightGrm = Integer.parseInt(edt_grams.getText().toString().trim());
//        }


        double h = 0;
        if (heightInch != 0) {
            double inches = (heightfeet * 12) + heightInch;
            h = inches * 0.0254;
        } else {
            float inches = heightfeet * 12;
            h = inches * 0.0254;
        }

        double w = totalWeight;
        double bmi = w / (h * h);
        txt_BMI = String.valueOf(new DecimalFormat("##.##").format(bmi) + " " + interpretBMI(bmi));

        //txt_BMI.setText("" + new DecimalFormat("##.##").format(bmi) + " " + interpretBMI(bmi));//move to next UI
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
                return;
            }
        }


        String feet = String.valueOf(heightfeet);
        String inches = String.valueOf(heightInch);

        double totalHeight;
        if (isCm) {
            totalHeight = Double.parseDouble(edt_cm.getText().toString().trim());
        } else {
            totalHeight = Utils.convertFeetandInchesToCentimeter(feet, inches);
        }

//        double totalWeight;
//
//        if (isPounds) {
//            totalWeight = Double.parseDouble(edt_pounds.getText().toString().trim());
//        } else {
//            totalWeight = weightkg;
//        }

        double totalWeight;

        if (isPounds) {
            totalWeight = Double.parseDouble(new DecimalFormat("###.#").format(Utils.getConvertingPoundsIntoKilograms(Double.parseDouble(edt_pounds.getText().toString().trim()))));
        } else {
            if (!edt_grams.getText().toString().trim().equalsIgnoreCase("")) {
                totalWeight = Double.parseDouble(AppPreference.getInstance().getGoalWeightKg() + "." + AppPreference.getInstance().getGoalWeightGrams());
            } else {
                totalWeight = Double.parseDouble(AppPreference.getInstance().getGoalWeightKg());
            }
        }

        bmr = (int) ((10 * totalWeight) + (6.25 * totalHeight) - (5 * Integer.parseInt(AppPreference.getInstance().getGoalAgeNew())) + 5);

//        Log.e("bmr ", "we "+(10 * totalWeight)+"height "+(6.25 * totalHeight)+" age"+(5 * Integer.parseInt(AppPreference.getInstance().getGoalAgeNew()) + 5)+""+" "+((10 * totalWeight) + (6.25 * totalHeight) - (5 * Integer.parseInt(AppPreference.getInstance().getGoalAgeNew()) + 5) + " totalWeight- " + totalWeight + "totalHeight- " + totalHeight + " age- " + Integer.parseInt(AppPreference.getInstance().getGoalAgeNew())));

        //txt_BMR.setText("" + bmr + " Calories/day");
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
                return;
            }
        }
        String feet = String.valueOf(heightfeet);
        String inches = String.valueOf(heightInch);

        double totalHeight;
        if (isCm) {
            totalHeight = Double.parseDouble(edt_cm.getText().toString().trim());
        } else {
            totalHeight = Utils.convertFeetandInchesToCentimeter(feet, inches);
        }

//        double totalWeight;
//
//        if (isPounds) {
//            totalWeight = Double.parseDouble(edt_pounds.getText().toString().trim());
//        } else {
//            totalWeight = weightkg;
//        }

        double totalWeight;

        if (isPounds) {
            totalWeight = Double.parseDouble(new DecimalFormat("###.#").format(Utils.getConvertingPoundsIntoKilograms(Double.parseDouble(edt_pounds.getText().toString().trim()))));
        } else {
            if (!edt_grams.getText().toString().trim().equalsIgnoreCase("")) {
                totalWeight = Double.parseDouble(AppPreference.getInstance().getGoalWeightKg() + "." + AppPreference.getInstance().getGoalWeightGrams());
            } else {
                totalWeight = Double.parseDouble(AppPreference.getInstance().getGoalWeightKg());
            }
        }


        bmr = (int) ((10 * totalWeight) + (6.25 * totalHeight) - (5 * Integer.parseInt(AppPreference.getInstance().getGoalAgeNew())) - 161);

//        Log.e("bmr ", " " + "totalWeight- " + totalWeight + "totalHeight- " + totalHeight + " age- " + Integer.parseInt(AppPreference.getInstance().getGoalAgeNew()));

        // txt_BMR.setText("" + bmr + " Calories/day");
    }


    private boolean validateHeightFeetBMI() {
        String email = edt_feet.getText().toString().trim();

        if (email.equalsIgnoreCase("") || email.equalsIgnoreCase("0")) {
            return false;
        }
        return true;
    }

    private boolean validateWeightKgsBMI() {
        String email = edt_kgs.getText().toString().trim();
        if (email.equalsIgnoreCase("") || email.equalsIgnoreCase("0")) {
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
        if (email.equalsIgnoreCase("") || email.equalsIgnoreCase("0")) {
            return false;
        }
        return true;
    }

    private boolean validateWeightPounds() {
        String email = edt_pounds.getText().toString().trim();
        if (email.equalsIgnoreCase("") || email.equalsIgnoreCase("0")) {
            return false;
        }
        return true;
    }


    public void c2f(double cm, String plus) {
        double feet = cm / 30.48;
        double inches = (cm / 2.54) - ((int) feet * 12);
        heightfeet = (int) feet;
        String valueInches = new DecimalFormat("###.#").format(inches);
        heightInch = Double.parseDouble(valueInches);
        AppPreference.getInstance().setGoalHeightFeet("" + heightfeet);
        AppPreference.getInstance().setGoalHeightInch("" + heightInch);
    }

    public void poundToKgs(double pounds) {
        double kilograms = pounds * 0.454;
        weightkg = kilograms;
    }

    AdapterView.OnItemClickListener popUpItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listPopupWindow.dismiss();
            if (position == 0) {
                pos = position;
                txt_height.setText("Ft & In");
                edt_feet.setVisibility(View.VISIBLE);
                edt_inchs.setVisibility(View.VISIBLE);
                edt_cm.setVisibility(View.GONE);
                isCm = false;
                AppPreference.getInstance().setFtIN(true);
                AppPreference.getInstance().setCM(false);
                if (edt_inchs.getText().toString().trim().equalsIgnoreCase("")) {
                    edt_inchs.setText("" + 0);
                } else {
                    edt_inchs.setText("" + AppPreference.getInstance().getGoalHeightInch());
                }
                edt_feet.setText("" + AppPreference.getInstance().getGoalHeightFeet());


            } else {
                if (pos == position)
                    return;
                pos = position;
                isCm = true;
                txt_height.setText("Cm");
                edt_feet.setVisibility(View.GONE);
                edt_inchs.setVisibility(View.GONE);
                edt_cm.setVisibility(View.VISIBLE);
                edt_cm.setText("" + new DecimalFormat("###.#").format(Utils.convertFeetandInchesToCentimeter(AppPreference.getInstance().getGoalHeightFeet(), AppPreference.getInstance().getGoalHeightInch())));
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

                String kilogram = "" + new DecimalFormat("###.###").format(Utils.getConvertingPoundsIntoKilograms(Double.parseDouble(AppPreference.getInstance().getGoalWeightPound())));
                String[] dateParts = kilogram.split("\\.");
                if (dateParts.length == 2) {
                    String kg = dateParts[0];
                    String graam = dateParts[1];
                    AppPreference.getInstance().setGoalWeightKg("" + kg);
                    preferences.edit().putString("kg", kg).commit();
                    if (graam.length() == 1) {
                        preferences.edit().putString("gram", "" + graam + "00").commit();
                        AppPreference.getInstance().setGoalWeightGrams("" + graam + "00");
                    } else {
                        preferences.edit().putString("gram", graam).commit();
                        AppPreference.getInstance().setGoalWeightGrams("" + graam);
                    }

                } else {
                    String kg = dateParts[0];
                    preferences.edit().putString("kg", kg).commit();
                    AppPreference.getInstance().setGoalWeightKg("" + kg);
                    AppPreference.getInstance().setGoalWeightGrams("");
                }

                edt_kgs.setText("" + AppPreference.getInstance().getGoalWeightKg());
                edt_grams.setText("" + AppPreference.getInstance().getGoalWeightGrams());


            } else {
                txt_weight.setText("Pounds");
                edt_kgs.setVisibility(View.GONE);
                edt_grams.setVisibility(View.GONE);
                edt_pounds.setVisibility(View.VISIBLE);

                if (!edt_grams.getText().toString().trim().equalsIgnoreCase("")) {
                    edt_pounds.setText("" + Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg() + "." + AppPreference.getInstance().getGoalWeightGrams())));
                } else {
                    edt_pounds.setText("" + Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg())));
                }
                isPounds = true;
                AppPreference.getInstance().setpound(true);
                AppPreference.getInstance().setKgs(false);
            }
        }
    };

    /*public IOnDoneMoreImage getiOnDoneMoreImage() {
        return iOnDoneMoreImage;
    }

    public void setiOnDoneMoreImage(IOnDoneMoreImage iOnDoneMoreImage) {
        this.iOnDoneMoreImage = iOnDoneMoreImage;
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_cancel:
                dismiss();
                break;

            case R.id.btn_edit_next:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                }

                if (!isFemale && !isMale) {
                    Toast.makeText(CureFull.getInstanse().getActivityIsntanse(), "Please Select Gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!validateAge()) {
                    Toast.makeText(CureFull.getInstanse().getActivityIsntanse(), "Please Select Date of Birth", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (isCm) {
                    if (!validateHeightCM()) {
                        Toast.makeText(CureFull.getInstanse().getActivityIsntanse(), "Please Enter Height CM", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if (!validateHeightFeetBMI()) {
                        Toast.makeText(CureFull.getInstanse().getActivityIsntanse(), "Please Enter Height", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                if (isPounds) {
                    if (!validateWeightPounds()) {
                        Toast.makeText(CureFull.getInstanse().getActivityIsntanse(), "Please Enter Weight", Toast.LENGTH_SHORT).show();

                        return;
                    }
                } else {
                    if (!validateWeightKgsBMI()) {
                        Toast.makeText(CureFull.getInstanse().getActivityIsntanse(), "Please Enter Weight", Toast.LENGTH_SHORT).show();

                        return;
                    }
                }

                bmiCalculator();
                if (isFemale) {
                    getBmrForFeMale();
                    if (heightfeet > 4) {
                        double checkheight = ((heightfeet * 12) - 60) + heightInch;
                        str_ideal_weight = String.valueOf(new DecimalFormat("###.###").format(Utils.getIdealWeightWomen(checkheight)) + " kg");

                    } else {
                        double checkheight = ((heightfeet * 12) - 60) + heightInch;
                        str_ideal_weight = String.valueOf(new DecimalFormat("###.###").format(Utils.getIdealWeightWomenBelow(checkheight)) + " kg");
                    }
                }
                if (isMale) {
                    getBmrForMale();
                    if (heightfeet > 4) {


                        double checkheight = ((heightfeet * 12) - 60) + heightInch;
                        str_ideal_weight = String.valueOf(new DecimalFormat("###.###").format(Utils.getIdealWeightMen(checkheight)) + " kg");

                    } else {

                        double checkheight = ((heightfeet * 12) - 60) + heightInch;
                        str_ideal_weight = String.valueOf(new DecimalFormat("###.###").format(Utils.getIdealWeightMenBelow(checkheight)) + " kg");
                    }

                }

                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    edt_cm.clearFocus();
                    edt_feet.clearFocus();
                    edt_pounds.clearFocus();
                    edt_inchs.clearFocus();
                    edt_kgs.clearFocus();
                    edt_grams.clearFocus();
                    CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
                    setRecommededDetails();
                } else {
                    edt_cm.clearFocus();
                    edt_feet.clearFocus();
                    edt_pounds.clearFocus();
                    edt_inchs.clearFocus();
                    edt_kgs.clearFocus();
                    edt_grams.clearFocus();
                    CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
                    setRecommededDetailsLocal();

                    //CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);
                }

                break;

        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        int mnt = (monthOfYear + 1);

        preferences.edit().putString("chk_Birthday_notification", "0").commit();
        AppPreference.getInstance().setGoalAge("" + year + "-" + (mnt < 10 ? "0" + mnt : mnt) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth));
        edt_years.setText("" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + (mnt < 10 ? "0" + mnt : mnt) + "-" + year);
        String age = dayOfMonth + "/" + mnt + "/" + year;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date birthDate = sdf.parse(age); //Yeh !! It's my date of birth :-)
            AppPreference.getInstance().setGoalAgeNew("" + Utils.getAge(birthDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void setRecommededDetailsLocal() {

        if (!validateAge()) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "DOB can not be blank");
            return;
        }

        if (isCm) {
            if (!validateHeightCM()) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "Height CM can not be blank");
                return;
            }
        } else {
            if (!validateHeightFeetBMI()) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "Height Feet can not be blank");
                return;
            }

        }


        if (isPounds) {
            if (!validateWeightPounds()) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "Pound can not be blank");
                return;
            }
        } else {
            if (!validateWeightKgsBMI()) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "Weight kg can not be blank");
                return;
            }
        }
        String feet = String.valueOf(heightfeet);
        String inches = String.valueOf(heightInch);
        double totalHeight;
        if (isCm) {
            totalHeight = Double.parseDouble(edt_cm.getText().toString().trim());
        } else {
            totalHeight = Utils.convertFeetandInchesToCentimeter(feet, inches);
        }

        double totalWeight;

        if (isPounds) {
            totalWeight = Double.parseDouble(edt_pounds.getText().toString().trim());
        } else {
            if (!edt_grams.getText().toString().trim().equalsIgnoreCase("")) {
                totalWeight = Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg() + "." + AppPreference.getInstance().getGoalWeightGrams()));
            } else {
                totalWeight = Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg()));
            }
        }
        String gender = "MALE";
        if (!isMale && !isFemale) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "Select Gender can not be blank");
            return;
        }
        if (isMale) {
            gender = "MALE";
        } else {
            gender = "FEMALE";
        }
        String dateOfBirth = AppPreference.getInstance().getGoalAge();

//java code from pavitra
        String genderStr = gender;
        String dateOfBirthStr = dateOfBirth;
        String heightInCmStr = String.valueOf(new DecimalFormat("###.###").format(totalHeight));
        String weightStr = String.valueOf(new DecimalFormat("###.###").format(totalWeight));

        // GenderEnum gender = GenderEnum.valueOf(genderStr);
        // LocalDate dateOfBirth = BasicUtils.convertStringToLocalDateObj(dateOfBirthStr, BasicConstants.DATE_FORMAT_yyyy_MM_dd);
        Double heightInCm = Double.valueOf(heightInCmStr);
        Double weightInPounds = Double.valueOf(weightStr);

        double heightInFeet = heightInCm / 30.48;
        double heightInInches = (heightInCm / 2.54) - ((int) heightInFeet * 12);


        double weightInKg = Math.round((weightInPounds * 0.453592) * 100) / 100.0d;

        Double idealWeight = null;
        Long targetStepCount = null;
        Double targetCaloriesToBurn = null;
        Long targetWaterIntake = null;

        //calculate ideal weight
        if (genderStr.equalsIgnoreCase("FEMALE")) {

            if (heightInFeet == 5) {

                idealWeight = 45.50;

            } else if (heightInFeet < 5) {

                double heightInInchesBeforeTheshold = (5 - Math.floor(heightInFeet)) * 12;
                idealWeight = 45.50 - (heightInInchesBeforeTheshold + (Math.round(heightInInches * 10) / 10.0d) * 2.3);

            } else if (heightInFeet > 5) {

                //double heightInInchesAfterTheshold = (Math.floor(heightInFeet) - 5) * 12;
                idealWeight = 45.50 + ((Math.round(heightInInches * 10) / 10.0d) * 2.3);

            }

            //target steps and calories to burn
            if (weightInKg <= idealWeight) {

                targetStepCount = (long) 6000;
                targetCaloriesToBurn = (double) 1750;

            } else if (weightInKg > idealWeight) {

                double overWeight = (weightInKg - idealWeight) > 10 ? 10 : weightInKg - idealWeight;

                targetStepCount = (long) (6000 + (overWeight) * 500);
                targetCaloriesToBurn = (double) (1750 + (overWeight) * 25);

            }

            //target water intake
            if (weightInKg <= 45) {
                targetWaterIntake = (long) (2.25 * 1000);
            } else if (weightInKg > 45) {
                targetWaterIntake = (long) ((2.25 + ((weightInKg - 45) / 20)) * 1000);
            }

        } else if (genderStr.equalsIgnoreCase("MALE")) {
            if (heightInFeet == 5) {

                idealWeight = 50.0;

            } else if (heightInFeet < 5) {

                double heightInInchesBeforeTheshold = (5 - Math.floor(heightInFeet)) * 12;
                idealWeight = 50.0 - (heightInInchesBeforeTheshold + (Math.round(heightInInches * 10) / 10.0d) * 2.3);

            } else if (heightInFeet > 5) {

                //double heightInInchesAfterTheshold = (Math.floor(heightInFeet) - 5) * 12;
                idealWeight = 50.0 + ((Math.round(heightInInches * 10) / 10.0d) * 2.3);

            }

            //target steps and calories to burn
            if (weightInKg <= idealWeight) {

                targetStepCount = (long) 6000;
                targetCaloriesToBurn = 2250.0;

            } else if (weightInKg > idealWeight) {

                double overWeight = (weightInKg - idealWeight) > 10 ? 10 : weightInKg - idealWeight;

                targetStepCount = (long) (6000 + (overWeight) * 500);
                targetCaloriesToBurn = (2250.0 + (overWeight) * 25);


            }

            //target water intake
            if (weightInKg <= 65) {

                targetWaterIntake = (long) (3.25 * 1000);

            } else if (weightInKg > 65) {

                targetWaterIntake = (long) ((3.25 + ((weightInKg - 65) / 20)) * 1000);

            }

        }

        //format calories toburn to 2 decimal digits
        targetCaloriesToBurn = Math.round(targetCaloriesToBurn * 100) / 100.0d;
        AppPreference.getInstance().setStepsCountTarget(Integer.parseInt(String.valueOf(targetStepCount)));
        AppPreference.getInstance().setWaterInTakeTarget(String.valueOf(targetWaterIntake));

        //working on local
        ContentValues values = new ContentValues();

        values.put("edit_id", AppPreference.getInstance().getcf_uuhid());
        values.put("dateOfBirth", dateOfBirth);
        values.put("gender", gender);
        values.put("height", String.valueOf(new DecimalFormat("###.###").format(totalHeight)));
        values.put("weight", String.valueOf(new DecimalFormat("###.###").format(totalWeight)));
        values.put("targetStepCount", targetStepCount);
        values.put("targetCaloriesToBurn", targetCaloriesToBurn);
        values.put("targetWaterInTake", targetWaterIntake);
        values.put("glassSize", "0");
        values.put("glassNumber", "0");
        values.put("isUploaded", "1");

        DbOperations.insertEditGoalList(CureFull.getInstanse().getActivityIsntanse(), values, AppPreference.getInstance().getcf_uuhid());

        DialogSetGoal_BMI dialogSetGoal_bmi = new DialogSetGoal_BMI(CureFull.getInstanse().getActivityIsntanse(), txt_BMI, bmr, str_ideal_weight, String.valueOf(targetStepCount), String.valueOf(targetCaloriesToBurn), String.valueOf(targetWaterIntake) + " L");
        dialogSetGoal_bmi.show();
        dismiss();

    }

    public void getEditTextLength(EditText txt_view_remaining_pills) {
        InputMethodManager imm = (InputMethodManager) CureFull.getInstanse().getActivityIsntanse().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(txt_view_remaining_pills,
                InputMethodManager.SHOW_IMPLICIT);
        try {
            txt_view_remaining_pills.setSelection(txt_view_remaining_pills.getText()
                    .length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            CureFull.getInstanse().getActivityIsntanse().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

/*
    @Override
    public void optCheckBox(String messsage) {
        if (messsage.equalsIgnoreCase("true")) {
            btn_done.setText("Delete");
        } else {
            btn_done.setText("Done");
        }
    }*/


}