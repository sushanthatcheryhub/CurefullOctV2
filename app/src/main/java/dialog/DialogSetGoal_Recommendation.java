package dialog;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import ElasticVIews.ElasticAction;
import asyns.JsonUtilsObject;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import fragment.healthapp.Activity_Today_Trends_Home;
import fragment.healthapp.FragmentEditGoal;
import operations.DbOperations;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;
import utils.Utils;


public class DialogSetGoal_Recommendation extends Dialog implements View.OnClickListener{

    private View v = null;
    Context context;
    private EditText edt_water, edt_steps, edt_calories;
    private ImageView dialog_cancel;
    private TextView btn_lets_start,btn_edit_goal;

    public DialogSetGoal_Recommendation(Context _activiyt, String targetStepCount, String targetCaloriesToBurn, String targetWaterInTake) {
        super(_activiyt, R.style.MyTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = _activiyt;
        setCancelable(true);
        setContentView(R.layout.dialog_set_goal_recommended_exercise);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        edt_water = (EditText)findViewById(R.id.edt_water);
        edt_steps = (EditText)findViewById(R.id.edt_steps);
        edt_calories = (EditText)findViewById(R.id.edt_calories);
        dialog_cancel=(ImageView) findViewById(R.id.dialog_cancel);
        btn_edit_goal=(TextView) findViewById(R.id.btn_edit_goal);
        btn_lets_start=(TextView) findViewById(R.id.btn_lets_start);
        dialog_cancel.setOnClickListener(this);
        btn_edit_goal.setOnClickListener(this);
        btn_lets_start.setOnClickListener(this);
        edt_water.setEnabled(false);
        edt_water.setFocusable(false);
        edt_steps.setEnabled(false);
        edt_steps.setFocusable(false);
        edt_calories.setEnabled(false);
        edt_calories.setFocusable(false);
        edt_water.setText(new DecimalFormat("###.#").format(Utils.getMlToLiter(Integer.parseInt(targetWaterInTake))) + " L");
        edt_steps.setText(targetStepCount);
        edt_calories.setText(targetCaloriesToBurn);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_cancel:
                dismiss();
                break;

            case R.id.btn_lets_start:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                }
               /* Intent i=new Intent(context,Activity_Today_Trends_Home.class);
                context.startActivity(i);*/
                CureFull.getInstanse().getFlowInstanse().clearBackStack();
                CureFull.getInstanse().getFlowInstanse().replace(new Activity_Today_Trends_Home(), false);
                /*CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentEditGoal(), true);*/
                dismiss();

                break;

            case R.id.btn_edit_goal:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                }

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
                        if (!edt_water.getText().toString().trim().equalsIgnoreCase("") && !edt_steps.getText().toString().trim().equalsIgnoreCase("") && !edt_calories.getText().toString().trim().equalsIgnoreCase("")) {
                            double water = Double.parseDouble(edt_water.getText().toString().trim().replace("L", ""));
                            jsonUploadTarget(steps, calories, Utils.getLiterToMl(water));
                        } else {
                            CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "Please fill all recommended daily exercise");

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
                        if (!edt_water.getText().toString().trim().equalsIgnoreCase("") && !edt_steps.getText().toString().trim().equalsIgnoreCase("") && !edt_calories.getText().toString().trim().equalsIgnoreCase("")) {
                            double water = Double.parseDouble(edt_water.getText().toString().trim().replace("L", ""));


                            ContentValues values = new ContentValues();
                            values.put("targetStepCount", steps);
                            values.put("targetCaloriesToBurn", calories);
                            values.put("targetWaterInTake", Utils.getLiterToMl(water));
                            values.put("isUploaded", "1");

                            DbOperations.insertEditGoalList(CureFull.getInstanse().getActivityIsntanse(), values, AppPreference.getInstance().getcf_uuhid());

                            edt_water.setEnabled(false);
                            edt_water.setFocusable(false);
                            edt_steps.setEnabled(false);
                            edt_steps.setFocusable(false);
                            edt_calories.setEnabled(false);
                            edt_calories.setFocusable(false);
                            AppPreference.getInstance().setStepsCountTarget(Integer.parseInt(steps));
                            AppPreference.getInstance().setWaterInTakeTarget(String.valueOf(Utils.getLiterToMl(water)));
                            //edt_water.setText(new DecimalFormat("###.##").format(Utils.getMlToLiter(Integer.parseInt(targetWaterInTake))) + " L");
                            btn_edit_goal.setText("Edit Goal");

                        } else {
                            CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "Please fill all recommended daily exercise");

                        }

                    }
                    if (btn_edit_goal.getText().toString().trim().equalsIgnoreCase("Edit Goal")) {
                        btn_edit_goal.setText("Done");
                        if (!edt_water.getText().toString().trim().equalsIgnoreCase("")) {
                            double change = Double.parseDouble(edt_water.getText().toString().trim().replace("L", ""));
                            edt_water.setText("" + change);
                        }

                    }

                    //CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);
                }


                break;

        }
    }



    public void jsonUploadTarget(final String targetSteps, String targetCaloriesToBurn, double targetWaterInTake) {
        JSONObject data = JsonUtilsObject.toSetGoals(targetSteps, targetCaloriesToBurn, targetWaterInTake);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.SET_GOALS, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                return headers;
            }
        };
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }

}