package fragment.healthapp;


import android.animation.Animator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.BaseBackHandlerFragment;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import item.property.GoalInfo;
import operations.DbOperations;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;
import utils.Utils;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentEditGoal extends BaseBackHandlerFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    private View rootView;
    private ListPopupWindow listPopupWindow;
    private ImageView img_select_height, img_200ml, img_100ml, img_300ml, img_500ml;
    private LinearLayout liner_100ml, liner_200ml, liner_300ml, liner_500ml, linearView;
    private EditText water_100, water_200, water_300, water_500;
    private TextView txt_ideal_weight, txt_height, txt_weight, txt_BMR, txt_BMI, btn_edit_goal, btn_done, edt_years;
    private EditText edt_feet, edt_inchs, edt_cm, edt_kgs, edt_grams, edt_pounds;
    private LinearLayout revealView, layoutButtons, liner_upload_new, liner_animation_upload;
    private EditText edt_water, edt_steps, edt_calories;
    private RadioGroup radioGender;
    private RadioButton radioMale, radioFemale;
    private boolean isCm = false;
    private boolean isPounds = false;
    private boolean isMale = false;
    private boolean isFemale = false;
    private int heightfeet;
    private double heightInch;
    private double weightkg;
    double fweight, mweight;
    private RequestQueue requestQueue;
    private GoalInfo userInfo;
    private TextView txt_btn_set_glass, btn_edit_done;
    private RelativeLayout realtive_notes;
    private float pixelDensity;
    boolean flag = true;
    private Animation alphaAnimation;
    private boolean isUploadClick = false, doubleback = false;
    private int glassInTake = 0, glassNumber = 0;
    private int pos;
    SharedPreferences preferences;

    @Override
    public boolean onBackPressed() {
        if (AppPreference.getInstance().isEditGoal()) {
            if (doubleback) {
                AppPreference.getInstance().setIsEditGoalPage(false);
                return true;
            } else {
                if (isUploadClick) {
                    Log.e("isclick", " " + isUploadClick);
                    isUploadClick = false;
                    btn_edit_goal.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitter(rootView);
                        }
                    });
                    return false;
                } else {
                    Log.e("isclick", " else");
                    if (AppPreference.getInstance().getMale() == false && AppPreference.getInstance().getFeMale() == false) {
                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Select Gender");
                        return false;
                    } else if (AppPreference.getInstance().getGoalAge().equalsIgnoreCase("0")) {
                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Select Age");
                        return false;
                    } else {
                        AppPreference.getInstance().setIsEditGoalPage(false);
                        return true;
                    }
                }
            }
        } else {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Fill Details");
            return false;
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_set_goal,
                container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(CureFull.getInstanse().getActivityIsntanse());
        CureFull.getInstanse().getActivityIsntanse().isbackButtonVisible(false, "");
        if (AppPreference.getInstance().isEditGoal()) {
            CureFull.getInstanse().getActivityIsntanse().activateDrawer();
        } else {
            CureFull.getInstanse().getActivityIsntanse().disableDrawer();
        }
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);
        AppPreference.getInstance().setIsEditGoalPage(true);
        realtive_notes = (RelativeLayout) rootView.findViewById(R.id.realtive_notes);
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

        btn_done = (TextView) rootView.findViewById(R.id.btn_done);
        water_100 = (EditText) rootView.findViewById(R.id.water_100);
        water_200 = (EditText) rootView.findViewById(R.id.water_200);
        water_300 = (EditText) rootView.findViewById(R.id.water_300);
        water_500 = (EditText) rootView.findViewById(R.id.water_500);

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


        revealView = (LinearLayout) rootView.findViewById(R.id.linearView);
        layoutButtons = (LinearLayout) rootView.findViewById(R.id.layoutButtons);

        txt_btn_set_glass = (TextView) rootView.findViewById(R.id.txt_btn_set_glass);
        btn_edit_done = (TextView) rootView.findViewById(R.id.btn_edit_done);
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
        img_select_height = (ImageView) rootView.findViewById(R.id.img_select_height);
        btn_edit_goal = (TextView) rootView.findViewById(R.id.btn_edit_goal);
        radioGender = (RadioGroup) rootView.findViewById(R.id.radioGender);
        radioMale = (RadioButton) rootView.findViewById(R.id.radioMale);
        radioFemale = (RadioButton) rootView.findViewById(R.id.radioFemale);
        btn_edit_goal.setOnClickListener(this);
        btn_edit_done.setOnClickListener(this);
        txt_BMI.setSelected(true);
        revealView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        glassNumber = 2;
        img_200ml.setVisibility(View.VISIBLE);
        getAllDetails();


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
                            txt_ideal_weight.setText("" + new DecimalFormat("###.###").format(Utils.getIdealWeightMen(heightInch)) + " kg");
                        } else {
                            double checkheight = ((heightfeet * 12) - 60) + heightInch;
                            txt_ideal_weight.setText("" + new DecimalFormat("###.###").format(Utils.getIdealWeightMen(checkheight)) + " kg");
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
                            txt_ideal_weight.setText("" + new DecimalFormat("###.###").format(Utils.getIdealWeightWomen(heightInch)) + " kg");
                        } else {
                            double checkheight = ((heightfeet * 12) - 60) + heightInch;
                            txt_ideal_weight.setText("" + new DecimalFormat("###.###").format(Utils.getIdealWeightWomen(checkheight)) + " kg");
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
                    day = c1.get(Calendar.DAY_OF_MONTH);
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

                if (s.length() > 0) {
                    int value = Integer.parseInt(s.toString());
                    if (value > 9) {
                        value = 9;
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
                Log.e("onTextChanged", ":- " + "onTextChanged" + count + ":- " + s.length());
                if (s.length() > 0) {
                    double value = Double.parseDouble(s.toString());
                    if (value > 274.32) {
                        value = 274.32;
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
                Log.e("onTextChanged", ":- " + "onTextChanged" + count + ":- " + s.length());
                if (s.length() > 0) {

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
                Log.e("onTextChanged", ":- " + "onTextChanged" + count + ":- " + s.length() + " " + s.toString());
//                if (s.toString().equalsIgnoreCase("0")) {
//                    edt_inchs.setText("");
//                }
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
                    Log.e("less", ":- " + " " + s.toString());

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
                    int value = Integer.parseInt(s.toString());
                    if (value > 500) {
                        value = 500;
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
                Log.e("gram", "gram");
                edt_pounds.setText("" + Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg() + "." + AppPreference.getInstance().getGoalWeightGrams())));
            } else {
                Log.e("kg", "kg");
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


        String you = "Click here";
        String termCondtiions = " to set glass size for water";

        String meassgeNew = you + termCondtiions;

        Spannable sb1 = new SpannableString(meassgeNew);
        sb1.setSpan(new ForegroundColorSpan(CureFull.getInstanse().getActivityIsntanse().getResources()
                        .getColor(R.color.health_yellow)), meassgeNew.indexOf(you),
                meassgeNew.indexOf(you) + you.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb1.setSpan(new UnderlineSpan(), meassgeNew.indexOf(you),
                meassgeNew.indexOf(you) + you.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb1.setSpan(new ForegroundColorSpan(CureFull.getInstanse().getActivityIsntanse().getResources()
                        .getColor(R.color.health_yellow)), meassgeNew.indexOf(termCondtiions),
                meassgeNew.indexOf(termCondtiions) + termCondtiions.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txt_btn_set_glass.setText(sb1);
        txt_btn_set_glass.setOnClickListener(this);
        alphaAnimation = AnimationUtils.loadAnimation(CureFull.getInstanse().getActivityIsntanse(), R.anim.alpha_anim);

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
                        AppPreference.getInstance().setGoalWeightGrams("" + graam + "00");
                    } else {
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
                    Log.e("gram", "gram");
                    edt_pounds.setText("" + Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg() + "." + AppPreference.getInstance().getGoalWeightGrams())));
                } else {
                    Log.e("kg", "kg");
                    edt_pounds.setText("" + Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg())));
                }
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

        if (!isPounds) {
            weightkg = Double.parseDouble(edt_kgs.getText().toString().trim());
        }

        int weightGrm = 0;
        if (!edt_grams.getText().toString().trim().equalsIgnoreCase("")) {
            weightGrm = Integer.parseInt(edt_grams.getText().toString().trim());
        }


        double h = 0;
        if (heightInch != 0) {
            double inches = (heightfeet * 12) + heightInch;
            h = inches * 0.0254;
        } else {
            float inches = heightfeet * 12;
            h = inches * 0.0254;
        }

        Log.e("real hero", " " + heightfeet + " " + heightInch);


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

    public void c2f(double cm, String plus) {
        double feet = cm / 30.48;
        double inches = (cm / 2.54) - ((int) feet * 12);
        heightfeet = (int) feet;
        String valueInches = new DecimalFormat("###.#").format(inches);
        heightInch = Double.parseDouble(valueInches);
        AppPreference.getInstance().setGoalHeightFeet("" + heightfeet);
        AppPreference.getInstance().setGoalHeightInch("" + heightInch);
        Log.e("There are ", (int) feet + " feet and " + new DecimalFormat("###.#").format(inches) + " inches in " + cm + "cm");
    }

    public void poundToKgs(double pounds) {
        double kilograms = pounds * 0.454;
        weightkg = kilograms;
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
            totalWeight = Double.parseDouble(edt_pounds.getText().toString().trim());
        } else {
            if (!edt_grams.getText().toString().trim().equalsIgnoreCase("")) {
                Log.e("gram", "gram");
                totalWeight = Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg() + "." + AppPreference.getInstance().getGoalWeightGrams()));
            } else {
                Log.e("kg", "kg");
                totalWeight = Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg()));
            }
        }

        int bmr = (int) ((10 * totalWeight) + (6.25 * totalHeight) - (5 * Integer.parseInt(AppPreference.getInstance().getGoalAgeNew()) + 5));
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
            totalWeight = Double.parseDouble(edt_pounds.getText().toString().trim());
        } else {
            if (!edt_grams.getText().toString().trim().equalsIgnoreCase("")) {
                Log.e("gram", "gram");
                totalWeight = Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg() + "." + AppPreference.getInstance().getGoalWeightGrams()));
            } else {
                Log.e("kg", "kg");
                totalWeight = Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg()));
            }
        }


        int bmr = (int) ((10 * totalWeight) + (6.25 * totalHeight) - (5 * Integer.parseInt(AppPreference.getInstance().getGoalAgeNew())) - 161);
        txt_BMR.setText("" + bmr + " Calories/day");
    }


    public void jsonUploadTarget(final String targetSteps, String targetCaloriesToBurn, double targetWaterInTake) {
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

                            if (responseStatus == 100) {
                                edt_water.setEnabled(false);
                                edt_water.setFocusable(false);
                                edt_steps.setEnabled(false);
                                edt_steps.setFocusable(false);
                                edt_calories.setEnabled(false);
                                edt_calories.setFocusable(false);

                                JSONObject json1 = new JSONObject(json.getString("payload"));
                                String targetStepCount = json1.getString("targetStepCount");
                                String targetCaloriesToBurn = json1.getString("targetCaloriesToBurn");
                                String targetWaterInTake = json1.getString("targetWaterInTake");
                                AppPreference.getInstance().setStepsCountTarget(Integer.parseInt(targetStepCount));
                                AppPreference.getInstance().setWaterInTakeTarget(targetWaterInTake);
                                edt_water.setText(new DecimalFormat("###.##").format(Utils.getMlToLiter(Integer.parseInt(targetWaterInTake))) + " L");
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
                        } catch (JSONException e) {
                            e.printStackTrace();
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


    public void jsonGlassTarget(int glassInTake, int glassNumber) {

        if (glassNumber == 1) {
            if (!water_100.getText().toString().trim().equalsIgnoreCase("")) {
                glassInTake = Integer.parseInt(water_100.getText().toString().trim());
            } else {
                glassInTake = 0;
            }
        } else if (glassNumber == 2) {
            if (!water_200.getText().toString().trim().equalsIgnoreCase("")) {
                glassInTake = Integer.parseInt(water_200.getText().toString().trim());
            } else {
                glassInTake = 0;
            }

        } else if (glassNumber == 3) {
            if (!water_300.getText().toString().trim().equalsIgnoreCase("")) {
                glassInTake = Integer.parseInt(water_300.getText().toString().trim());
            } else {
                glassInTake = 0;
            }

        } else if (glassNumber == 4) {
            if (!water_500.getText().toString().trim().equalsIgnoreCase("")) {
                glassInTake = Integer.parseInt(water_500.getText().toString().trim());
            } else {
                glassInTake = 0;
            }

        }
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.SELECT_GLASS + glassInTake + "&glassNumber=" + glassNumber,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Log.e("getUserList, URL 1.", response);
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                JSONObject json1 = new JSONObject(json.getString("payload"));
                                String galssSize = json1.getString("glassSize");
                                String glassNumber = json1.getString("glassNumber");
                                AppPreference.getInstance().setGlass("" + galssSize);
                                AppPreference.getInstance().setIsLoginFirst(false);
                                AppPreference.getInstance().setStepStarts(true);
                                btn_edit_goal.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        launchTwitter(rootView);
                                    }
                                });
//                                CureFull.getInstanse().getActivityIsntanse().onBackPressed();
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.liner_100ml:
                glassNumber = 1;
                img_100ml.setVisibility(View.VISIBLE);
                img_200ml.setVisibility(View.INVISIBLE);
                img_500ml.setVisibility(View.INVISIBLE);
                img_300ml.setVisibility(View.INVISIBLE);
                break;
            case R.id.liner_200ml:
                glassNumber = 2;
                img_100ml.setVisibility(View.INVISIBLE);
                img_200ml.setVisibility(View.VISIBLE);
                img_500ml.setVisibility(View.INVISIBLE);
                img_300ml.setVisibility(View.INVISIBLE);
                break;
            case R.id.liner_300ml:
                glassNumber = 3;
                img_100ml.setVisibility(View.INVISIBLE);
                img_200ml.setVisibility(View.INVISIBLE);
                img_300ml.setVisibility(View.VISIBLE);
                img_500ml.setVisibility(View.INVISIBLE);
                break;
            case R.id.liner_500ml:
                glassNumber = 4;
                img_100ml.setVisibility(View.INVISIBLE);
                img_200ml.setVisibility(View.INVISIBLE);
                img_300ml.setVisibility(View.INVISIBLE);
                img_500ml.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_done:
                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    String gender = "";
                    if (AppPreference.getInstance().getMale() == true) {
                        gender = "MALE";
                    } else {
                        gender = "FEMALE";
                    }
                    if (glassNumber != 0) {
                        doubleback = true;
                        jsonGlassTarget(glassInTake, glassNumber);
                    } else {
                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please select glass for water Intake");
                    }
                } else {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);

                }


                break;

            case R.id.btn_edit_goal:
                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    edt_water.setEnabled(true);
                    edt_water.setFocusable(true);
                    edt_water.setFocusableInTouchMode(true);
                    edt_steps.setEnabled(true);

                    edt_steps.setFocusableInTouchMode(true);
                    edt_steps.setFocusable(true);
                    edt_steps.setActivated(true);
                    edt_calories.setEnabled(true);
                    edt_calories.setFocusable(true);
                    edt_calories.setFocusableInTouchMode(true);
                    if (btn_edit_goal.getText().toString().trim().equalsIgnoreCase("Done")) {
                        String steps = edt_steps.getText().toString().trim();
                        String calories = edt_calories.getText().toString().trim();
                        if (!edt_water.getText().toString().trim().equalsIgnoreCase("")) {
                            double water = Double.parseDouble(edt_water.getText().toString().trim().replace("L", ""));
                            jsonUploadTarget(steps, calories, Utils.getLiterToMl(water));
                        }

                    }
                    if (btn_edit_goal.getText().toString().trim().equalsIgnoreCase("Edit Goal")) {
                        btn_edit_goal.setText("Done");
                        if (!edt_water.getText().toString().trim().equalsIgnoreCase("")) {
                            double change = Double.parseDouble(edt_water.getText().toString().trim().replace("L", ""));
                            edt_water.setText("" + change);
                        }

                    }
                } else {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);
                }

                break;

            case R.id.btn_edit_done:

                bmiCalculator();
                if (isFemale) {
                    getBmrForFeMale();
                    if (heightfeet > 4) {
                        if (!validateHeightFeetBMI()) {
                            return;
                        }
                        if (heightfeet == 5) {
                            txt_ideal_weight.setText("" + new DecimalFormat("###.###").format(Utils.getIdealWeightWomen(heightInch)) + " kg");
                        } else {
                            double checkheight = ((heightfeet * 12) - 60) + heightInch;
                            txt_ideal_weight.setText("" + new DecimalFormat("###.###").format(Utils.getIdealWeightWomen(checkheight)) + " kg");
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
                            txt_ideal_weight.setText("" + new DecimalFormat("###.###").format(Utils.getIdealWeightMen(heightInch)) + " kg");
                        } else {
                            double checkheight = ((heightfeet * 12) - 60) + heightInch;
                            txt_ideal_weight.setText("" + new DecimalFormat("###.###").format(Utils.getIdealWeightMen(checkheight)) + " kg");
                        }

                    } else {
                        if (!validateHeightFeetBMI()) {
                            return;
                        }
                        txt_ideal_weight.setText("" + "50 kg");
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
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);
                }

                break;
            case R.id.txt_btn_set_glass:
                isUploadClick = true;
                launchTwitter(rootView);

                break;

        }
    }


    private void getAllDetails() {
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
            StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.GET_SET_GOALS_DEATILS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                            Log.e("getAllDetails, URL 1.", response);
                            int responseStatus = 0;
                            JSONObject json = null;
                            try {
                                json = new JSONObject(response.toString());
                                responseStatus = json.getInt("responseStatus");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                ParseJsonData.getInstance().getGoalDeatils(response.toString());
                                userInfo = DbOperations.getGoalList(CureFull.getInstanse().getActivityIsntanse());
                                if (userInfo == null)
                                    return;
                                setGoals(userInfo);
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


                            } else {
                                userInfo = DbOperations.getGoalList(CureFull.getInstanse().getActivityIsntanse());
                                if (userInfo == null)
                                    return;
                                setGoals(userInfo);
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
        } else {
            userInfo = DbOperations.getGoalList(CureFull.getInstanse().getActivityIsntanse());
            if (userInfo == null)
                return;
            setGoals(userInfo);
        }

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


    public void setRecommededDetails() {

        if (!validateAge()) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "DOB can not be blank");
            return;
        }

        if (isCm) {
            if (!validateHeightCM()) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Height CM can not be blank");
                return;
            }
        } else {
            if (!validateHeightFeetBMI()) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Height Feet can not be blank");
                return;
            }

        }


        if (isPounds) {
            if (!validateWeightPounds()) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Pound can not be blank");
                return;
            }
        } else {
            if (!validateWeightKgsBMI()) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Weight kg can not be blank");
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
                Log.e("gram", "gram");
                totalWeight = Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg() + "." + AppPreference.getInstance().getGoalWeightGrams()));
            } else {
                Log.e("kg", "kg");
                totalWeight = Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg()));
            }
        }
        String gender = "MALE";
        if (!isMale && !isFemale) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Select Gender can not be blank");
            return;
        }
        if (isMale) {
            gender = "MALE";
        } else {
            gender = "FEMALE";
        }
        String dateOfBirth = AppPreference.getInstance().getGoalAge();
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
        JSONObject data = JsonUtilsObject.toSetGoalsDetails(String.valueOf(new DecimalFormat("###.###").format(totalHeight)), String.valueOf(new DecimalFormat("###.###").format(totalWeight)), dateOfBirth, gender);
        Log.e("jsonUploadPrescription", ":- " + data.toString());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.SET_GOALS_DEATILS, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("GenderDetails, URL 3.", response.toString());
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
                                    edt_steps.setText("" + targetStepCount);
                                    AppPreference.getInstance().setStepsCountTarget(Integer.parseInt(targetStepCount));
                                }
                                if (!targetCaloriesToBurn.equalsIgnoreCase("null")) {
                                    edt_calories.setText("" + targetCaloriesToBurn);
                                }
                                if (!targetWaterInTake.equalsIgnoreCase("null")) {
                                    edt_water.setText("" + new DecimalFormat("###.#").format(Utils.getMlToLiter(Integer.parseInt(targetWaterInTake))) + " L");
                                    AppPreference.getInstance().setWaterInTakeTarget(targetWaterInTake);
                                }

                            } else {
                                try {
                                    JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                    JSONObject json12 = new JSONObject(json1.getString("errorDetails"));
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + json12.getString("message"));
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


    public void setGoals(GoalInfo userInfo) {
        AppPreference.getInstance().setIsEditGoal(true);

        Log.e("getHeight cm", ": -" + userInfo.getHeight());
        if (!"null".equals(userInfo.getHeight())) {
            c2f(Double.parseDouble(userInfo.getHeight()), "minus");
            AppPreference.getInstance().setGoalHeightFeet("" + heightfeet);
            AppPreference.getInstance().setGoalHeightInch("" + heightInch);
            Log.e("heightfeet", ": -" + heightfeet);
            Log.e("heightInch", ": -" + heightInch);
        }
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
        Log.e("getWeight", ": -" + userInfo.getWeight());
        if (!userInfo.getWeight().equalsIgnoreCase("null")) {
            AppPreference.getInstance().setGoalWeightPound("" + userInfo.getWeight());
            edt_pounds.setText("" + userInfo.getWeight());
            String kilogram = "" + new DecimalFormat("###.###").format(Utils.getConvertingPoundsIntoKilograms(Double.parseDouble(userInfo.getWeight())));
            Log.e("kilogram", " " + kilogram + " " + Utils.getConvertingPoundsIntoKilograms(Double.parseDouble(userInfo.getWeight())));
            String[] dateParts = kilogram.split("\\.");
            Log.e("lenth", "lenth " + dateParts.length);
            if (dateParts.length == 2) {
                String kg = dateParts[0];
                String graam = dateParts[1];
                preferences.edit().putString("kg", "" + kg).commit();
                AppPreference.getInstance().setGoalWeightKg("" + kg);
                if (graam.length() == 1) {
                    AppPreference.getInstance().setGoalWeightGrams("" + graam + "00");
                } else {
                    AppPreference.getInstance().setGoalWeightGrams("" + graam);
                }

            } else {
                String kg = dateParts[0];
                preferences.edit().putString("kg", "" + kg).commit();
                AppPreference.getInstance().setGoalWeightKg("" + kg);
                AppPreference.getInstance().setGoalWeightGrams("");
            }
        }

        if (!AppPreference.getInstance().getGoalHeightFeet().equalsIgnoreCase("") && !AppPreference.getInstance().getGoalHeightFeet().equalsIgnoreCase("0")) {
            edt_feet.setText("" + AppPreference.getInstance().getGoalHeightFeet());
        }
        if (!AppPreference.getInstance().getGoalHeightInch().equalsIgnoreCase("") && !AppPreference.getInstance().getGoalHeightInch().equalsIgnoreCase("0")) {
            edt_inchs.setText("" + AppPreference.getInstance().getGoalHeightInch());
        }
        if (!userInfo.getTargetStepCount().equalsIgnoreCase("null")) {
            edt_steps.setText("" + userInfo.getTargetStepCount());
            AppPreference.getInstance().setStepsCountTarget(Integer.parseInt(userInfo.getTargetStepCount()));
        }
        if (!userInfo.getTargetStepCount().equalsIgnoreCase("null")) {
            edt_calories.setText("" + userInfo.getTargetCaloriesToBurn());
        }

        if (!userInfo.getTargetWaterInTake().equalsIgnoreCase("null")) {
            edt_water.setText("" + new DecimalFormat("###.#").format(Utils.getMlToLiter(Integer.parseInt(userInfo.getTargetWaterInTake()))) + " L");
            AppPreference.getInstance().setWaterInTakeTarget(userInfo.getTargetWaterInTake());
        }

//                            if (!AppPreference.getInstance().getGoalAge().equalsIgnoreCase("") && !AppPreference.getInstance().getGoalAge().equalsIgnoreCase("0")) {
//                                edt_years.setText("" + AppPreference.getInstance().getGoalAgeNew());
//                            }

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
                        txt_ideal_weight.setText("" + new DecimalFormat("###.###").format(Utils.getIdealWeightMen(heightInch)) + " kg");
                    } else {
                        double checkheight = ((heightfeet * 12) - 60) + heightInch;
                        txt_ideal_weight.setText("" + new DecimalFormat("###.###").format(Utils.getIdealWeightMen(checkheight)) + " kg");
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
                        txt_ideal_weight.setText("" + new DecimalFormat("###.###").format(Utils.getIdealWeightWomen(heightInch)) + " kg");
                    } else {
                        double checkheight = ((heightfeet * 12) - 60) + heightInch;
                        txt_ideal_weight.setText("" + new DecimalFormat("###.###").format(Utils.getIdealWeightWomen(checkheight)) + " kg");
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
        if (!"null".equals(userInfo.getGlassNumber())) {
            if (userInfo.getGlassNumber().equalsIgnoreCase("1")) {
                glassNumber = 1;
                glassInTake = Integer.parseInt(userInfo.getGlassSize());
                water_100.setText("" + userInfo.getGlassSize());
                AppPreference.getInstance().setGlass("" + glassInTake);
                img_100ml.setVisibility(View.VISIBLE);
                img_200ml.setVisibility(View.INVISIBLE);
                img_500ml.setVisibility(View.INVISIBLE);
                img_300ml.setVisibility(View.INVISIBLE);
            } else if (userInfo.getGlassNumber().equalsIgnoreCase("2")) {
                glassNumber = 2;
                water_200.setText("" + userInfo.getGlassSize());
                glassInTake = Integer.parseInt(userInfo.getGlassSize());
                AppPreference.getInstance().setGlass("" + glassInTake);
                img_100ml.setVisibility(View.INVISIBLE);
                img_200ml.setVisibility(View.VISIBLE);
                img_500ml.setVisibility(View.INVISIBLE);
                img_300ml.setVisibility(View.INVISIBLE);
            } else if (userInfo.getGlassNumber().equalsIgnoreCase("3")) {
                glassNumber = 3;
                water_300.setText("" + userInfo.getGlassSize());
                glassInTake = Integer.parseInt(userInfo.getGlassSize());
                AppPreference.getInstance().setGlass("" + glassInTake);
                img_100ml.setVisibility(View.INVISIBLE);
                img_200ml.setVisibility(View.INVISIBLE);
                img_300ml.setVisibility(View.VISIBLE);
                img_500ml.setVisibility(View.INVISIBLE);
            } else if (userInfo.getGlassNumber().equalsIgnoreCase("4")) {
                glassNumber = 4;
                water_500.setText("" + userInfo.getGlassSize());
                glassInTake = Integer.parseInt(userInfo.getGlassSize());
                AppPreference.getInstance().setGlass("" + glassInTake);
                img_100ml.setVisibility(View.INVISIBLE);
                img_200ml.setVisibility(View.INVISIBLE);
                img_300ml.setVisibility(View.INVISIBLE);
                img_500ml.setVisibility(View.VISIBLE);
            }
        }
        bmiCalculator();
    }

    public void launchTwitter(View view) {
        /*
         MARGIN_RIGHT = 16;
         FAB_BUTTON_RADIUS = 28;
         */
        int x = realtive_notes.getLeft();
        int y = realtive_notes.getBottom();
        x -= ((28 * pixelDensity) + (16 * pixelDensity));
        int hypotenuse = (int) Math.hypot(realtive_notes.getWidth(), realtive_notes.getHeight());
        try {
            if (flag) {
//            imageButton.setBackgroundResource(R.drawable.rounded_cancel_button);
//            imageButton.setImageResource(R.drawable.image_cancel);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams)
                            revealView.getLayoutParams();
                    parameters.height = realtive_notes.getHeight();
                    revealView.setLayoutParams(parameters);

                    Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, 0, hypotenuse);
                    anim.setDuration(700);

                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            layoutButtons.setVisibility(View.VISIBLE);
                            layoutButtons.startAnimation(alphaAnimation);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });

                    revealView.setVisibility(View.VISIBLE);
                    anim.start();
                } else {
                    revealView.setVisibility(View.VISIBLE);
                    layoutButtons.setVisibility(View.VISIBLE);
                }


                flag = false;
            } else {

                isUploadClick = false;
//            imageButton.setBackgroundResource(R.drawable.rounded_button);
//            imageButton.setImageResource(R.drawable.twitter_logo);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, hypotenuse, 0);
                    anim.setDuration(400);
                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            revealView.setVisibility(View.GONE);
                            layoutButtons.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });

                    anim.start();
                } else {
                    revealView.setVisibility(View.GONE);
                    layoutButtons.setVisibility(View.GONE);
                }
                flag = true;
            }
        } catch (Exception e) {

        }


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


}