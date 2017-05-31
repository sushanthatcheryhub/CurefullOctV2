package fragment.healthapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import ElasticVIews.ElasticAction;
import curefull.healthapp.BaseBackHandlerFragment;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import stepcounter.MessengerService;
import utils.AppPreference;
import utils.MyConstants;
import utils.SeekArc;
import utils.Utils;

/**
 * Created by Sourav on 26-05-2017.
 */

public class Fragment_Health_Todays extends BaseBackHandlerFragment implements View.OnClickListener {
    private View rootView;
    SharedPreferences preferences;
    LinearLayout liner_btn_goal;
    TextView btn_set_goal_target, txt_total_steps, txt_steps, txt_running, txt_cycling, txt_calories, txt_water_level_current, txt_water_level_total;
    private ImageView img_minus_icon, img_plus_icon, img_left_arrow, img_right_arrow;
    private SeekArc seekArcComplete;
    boolean mIsBound;
    Messenger mService = null;
    boolean isToStop = false, isWaterTakeCall = false;
    private int waterLevel = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_health_today,
                container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(CureFull.getInstanse().getActivityIsntanse());
        CureFull.getInstanse().getActivityIsntanse().isbackButtonVisible(false, "Health App");
        CureFull.getInstanse().getActivityIsntanse().isTobBarButtonVisible(true, "");
        CureFull.getInstanse().getActivityIsntanse().activateDrawer();

        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);

        liner_btn_goal = (LinearLayout) rootView.findViewById(R.id.liner_btn_goal);

        btn_set_goal_target = (TextView) rootView.findViewById(R.id.btn_set_goal_target);
        txt_total_steps = (TextView) rootView.findViewById(R.id.txt_total_steps);
        txt_steps = (TextView) rootView.findViewById(R.id.txt_steps);
        txt_running = (TextView) rootView.findViewById(R.id.txt_running);
        txt_cycling = (TextView) rootView.findViewById(R.id.txt_cycling);
        txt_calories = (TextView) rootView.findViewById(R.id.txt_calories);
        txt_water_level_current = (TextView) rootView.findViewById(R.id.txt_water_level_current);
        txt_water_level_total = (TextView) rootView.findViewById(R.id.txt_water_level_total);
        img_minus_icon = (ImageView) rootView.findViewById(R.id.img_minus_icon);
        img_plus_icon = (ImageView) rootView.findViewById(R.id.img_plus_icon);

        img_left_arrow = (ImageView) rootView.findViewById(R.id.img_left_arrow);
        img_right_arrow = (ImageView) rootView.findViewById(R.id.img_right_arrow);
        seekArcComplete = (SeekArc) rootView.findViewById(R.id.seekArcComplete);


        seekArcComplete.setProgress(preferences.getInt("percentage", 0));
        btn_set_goal_target.setText("Goals - " + AppPreference.getInstance().getStepsCountTarget() + " steps");
        txt_total_steps.setText("" + preferences.getInt("stepsIn", 0));
        txt_steps.setText("" + preferences.getInt("stepsIn", 0));
        // txt_water_level_current.setText("" + new DecimalFormat("###.##").format(Utils.getMlToLiter(Integer.parseInt(preferences.getString("waterTake", "0")))) + " L Drinked");
        txt_calories.setText("" + preferences.getString("CaloriesCount", "0") + " kcal");

        /*if (AppPreference.getInstance().getWaterInTakeLeft().equalsIgnoreCase("0")) {
            txt_water_level_total.setText("0 L Left");

        } else {
            txt_water_level_total.setText("" + new DecimalFormat("###.##").format(Utils.getMlToLiter(Integer.parseInt(AppPreference.getInstance().getWaterInTakeLeft()))) + " L Left");

        }*/
        liner_btn_goal.setOnClickListener(this);
        img_plus_icon.setOnClickListener(this);
        img_minus_icon.setOnClickListener(this);
        img_left_arrow.setOnClickListener(this);
        img_right_arrow.setOnClickListener(this);

        Intent intent = new Intent(CureFull.getInstanse().getActivityIsntanse(), MessengerService.class);
        CureFull.getInstanse().getActivityIsntanse().startService(intent);
        doBindService();
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.liner_btn_goal:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);

                CureFull.getInstanse().getFlowInstanse().clearBackStack();
                CureFull.getInstanse().getFlowInstanse()
                        .replaceLoss(new FragmentEditGoal(), true);

                break;

            case R.id.img_plus_icon:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(img_plus_icon, 400, 0.9f, 0.9f);
                if (AppPreference.getInstance().isEditGoal()) {
                    if (isWaterTakeCall) {

                    } else {
                        getIncreseWaterInTake("true");
                    }

                } else {
                    CureFull.getInstanse().cancel();
                    CureFull.getInstanse().getFlowInstanse()
                            .replace(new FragmentEditGoal(), true);

                }

                break;
            case R.id.img_minus_icon:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(img_minus_icon, 400, 0.9f, 0.9f);
                if (AppPreference.getInstance().isEditGoal()) {
                    if (isWaterTakeCall) {

                    } else {
                        getIncreseWaterInTake("false");
                    }

                } else {
                    CureFull.getInstanse().cancel();
                    CureFull.getInstanse().getFlowInstanse()
                            .replace(new FragmentEditGoal(), true);
                }
                break;
            case R.id.img_left_arrow:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);

                break;

            case R.id.img_right_arrow:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);

                break;
        }
    }

    public void doBindService() {
        CureFull.getInstanse().getActivityIsntanse().bindService(new Intent(CureFull.getInstanse().getActivityIsntanse(),
                MessengerService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;

        txt_steps.setText("" + preferences.getInt("stepsIn", 0));

    }

    final Messenger mMessenger = new Messenger(new Fragment_Health_Todays.IncomingHandler());

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            mService = new Messenger(service);
            txt_steps.setText("" + preferences.getInt("stepsIn", 0));

            try {
                Message msg = Message.obtain(null,
                        MessengerService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);
                msg = Message.obtain(null,
                        MessengerService.MSG_SET_VALUE, this.hashCode(), 0);
                mService.send(msg);
            } catch (RemoteException e) {
            }

            if (isToStop) {
                stop();
            }

        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;

            txt_steps.setText("" + preferences.getInt("stepsIn", 0));

        }
    };


    private void stop() {
        Message msg = Message.obtain(null,
                MessengerService.STOP_FOREGROUND);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        doUnbindService();
    }

    void doUnbindService() {
        if (mIsBound) {
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null,
                            MessengerService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                }
            }

            // Detach our existing connection.
            CureFull.getInstanse().getActivityIsntanse().unbindService(mConnection);
            mIsBound = false;

            txt_steps.setText("" + preferences.getInt("stepsIn", 0));

        }
    }

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerService.MSG_SET_VALUE:
                    txt_steps.setText("" + msg.arg1);
                    preferences.edit().putInt("stepsIn", msg.arg1).commit();

                    float percentage = Utils.getPercentage(msg.arg1, AppPreference.getInstance().getStepsCountTarget());
                    int b = (int) percentage;
                    seekArcComplete.setProgress(b);

                    //ticker1.setText(b + "%");
                    preferences.edit().putInt("percentage", b).commit();
                    txt_total_steps.setText("" + msg.arg1);

                    double wirght = 0.0;

                    double i2 = Utils.getCaloriesBurnt(wirght, msg.arg1);
                    txt_calories.setText("" + new DecimalFormat("###.#").format(i2) + " kcal");
                    preferences.edit().putString("CaloriesCount", "" + new DecimalFormat("###.#").format(i2)).commit();
//                    text_calories_count.setText("" + Utils.getCaloriesBurnt((int) (kg * 2.20462), msg.arg1)+"kcal");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private void getIncreseWaterInTake(String isture) {
        isWaterTakeCall = true;
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
//        if (requestQueue == null) {
//            requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
//        }
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.INCRESE_WATER_INTAKE + isture,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("response", " " + response);
                        isWaterTakeCall = false;
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                if (!json.getString("payload").equals(null)) {
                                    JSONObject json1 = new JSONObject(json.getString("payload"));
                                    String waterIntakeDone1 = json1.getString("waterIntakeDone");
                                    String waterIntakeLeft1 = "0";
                                    if (json1.getString("waterIntakeLeft").equals(null) || json1.getString("waterIntakeLeft").equalsIgnoreCase(null) || json1.getString("waterIntakeLeft").equalsIgnoreCase("null")) {
                                        waterIntakeLeft1 = "0";
                                    } else {
                                        waterIntakeLeft1 = json1.getString("waterIntakeLeft");
                                    }
                                    preferences.edit().putString("waterTake", waterIntakeDone1).commit();
                                    preferences.edit().putString("waterin", "" + waterIntakeDone1).commit();
                                    AppPreference.getInstance().setWaterInTakeLeft("" + waterIntakeLeft1);
                                    waterLevel = Integer.parseInt(preferences.getString("waterTake", "0"));
                                    if (waterLevel == 0) {
                                        img_minus_icon.setVisibility(View.INVISIBLE);
                                    } else {
                                        img_minus_icon.setVisibility(View.VISIBLE);
                                    }
                                    txt_water_level_current.setText("" + new DecimalFormat("###.##").format(Utils.getMlToLiter(Integer.parseInt(preferences.getString("waterTake", "0")))) + " L");

                                }

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
//                        Log.e("error", " " + error.getMessage());
                        isWaterTakeCall = false;
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
                headers.put("user_id", AppPreference.getInstance().getUserIDProfile());
                return headers;
            }
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }

}
