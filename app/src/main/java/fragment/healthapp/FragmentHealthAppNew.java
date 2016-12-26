package fragment.healthapp;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import adpter.HorizontalAdapter;
import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.BaseBackHandlerFragment;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import curefull.healthapp.models.Month;
import item.property.GraphView;
import stepcounter.MessengerService;
import ticker.TickerUtils;
import ticker.TickerView;
import utils.AppPreference;
import utils.MyConstants;
import utils.SeekArc;
import utils.Utils;
import widgets.HorizontalRecyclerView;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentHealthAppNew extends BaseBackHandlerFragment implements View.OnClickListener {


    private View rootView;
    private TextView txt_water_intake_left, txt_water_intake_done, btn_daily, btn_weekly, btn_monthy, txt_steps_counter, txt_steps_txt, tickerTotal, text_calories_count;
    Messenger mService = null;
    boolean mIsBound;
    boolean isToStop = false;
    private TickerView ticker1;
    private SeekArc seekArcComplete;
    private static final char[] NUMBER_LIST = TickerUtils.getDefaultNumberList();
    private LinearLayout txt_prescription, txt_heath_note, txt_lab_reports;
    private ListPopupWindow listPopupWindow;
    private RequestQueue requestQueue;
    private String fromdate, date, frequency, type;
    private List<GraphView> graphViewsList;
    private TextView btn_set_goal_target;
    private LinearLayout liner_steps, liner_btn_goal;
    HorizontalRecyclerView horizontal_recycler_view;
    HorizontalAdapter horizontalAdapter;

    ArrayList<Month> data;

    @Override
    public boolean onBackPressed() {

        return super.onBackPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_health_app,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        txt_water_intake_left = (TextView) rootView.findViewById(R.id.txt_water_intake_left);
        txt_water_intake_done = (TextView) rootView.findViewById(R.id.txt_water_intake_done);
        liner_btn_goal = (LinearLayout) rootView.findViewById(R.id.liner_btn_goal);
        liner_steps = (LinearLayout) rootView.findViewById(R.id.liner_steps);
        btn_set_goal_target = (TextView) rootView.findViewById(R.id.btn_set_goal_target);
        txt_steps_txt = (TextView) rootView.findViewById(R.id.txt_steps_txt);
//        txt_no_data = (TextView) rootView.findViewById(R.id.txt_no_data);
        txt_prescription = (LinearLayout) rootView.findViewById(R.id.txt_prescription);
        txt_heath_note = (LinearLayout) rootView.findViewById(R.id.txt_heath_note);
        txt_lab_reports = (LinearLayout) rootView.findViewById(R.id.txt_lab_reports);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);
        ticker1 = (TickerView) rootView.findViewById(R.id.ticker1);
        tickerTotal = (TextView) rootView.findViewById(R.id.tickerTotal);
        text_calories_count = (TextView) rootView.findViewById(R.id.text_calories_count);
        txt_steps_counter = (TextView) rootView.findViewById(R.id.txt_steps_counter);
        btn_daily = (TextView) rootView.findViewById(R.id.btn_daily);
        btn_weekly = (TextView) rootView.findViewById(R.id.btn_weekly);
        btn_monthy = (TextView) rootView.findViewById(R.id.btn_monthy);
        seekArcComplete = (SeekArc) rootView.findViewById(R.id.seekArcComplete);
        ticker1.setCharacterList(NUMBER_LIST);
//        tickerTotal.setCharacterList(NUMBER_LIST);
//        text_calories_count.setCharacterList(NUMBER_LIST);
        btn_daily.setOnClickListener(this);
        btn_weekly.setOnClickListener(this);
        btn_monthy.setOnClickListener(this);
        liner_btn_goal.setOnClickListener(this);
//        if (AppPreference.getInstance().getStepStarts()) {
        Intent intent = new Intent(CureFull.getInstanse().getActivityIsntanse(), MessengerService.class);
        CureFull.getInstanse().getActivityIsntanse().startService(intent);
        doBindService();


//        }

        btn_set_goal_target.setText("Goals - " + AppPreference.getInstance().getStepsCountTarget() + " steps");
        txt_water_intake_done.setText("" + AppPreference.getInstance().getWaterInTake() + " ml Drinked");
        txt_water_intake_left.setText("" + AppPreference.getInstance().getWaterInTakeLeft() + " ml Left");

        date = getTodayDate();
        frequency = "monthly";
        type = "steps";
        fromdate = "";
        jsonGetGraphDeatils(fromdate, date, frequency, type);


        txt_lab_reports.setOnClickListener(this);
        txt_heath_note.setOnClickListener(this);
        txt_prescription.setOnClickListener(this);


        (rootView.findViewById(R.id.liner_steps)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                listPopupWindow.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                        R.layout.adapter_list_doctor_data, MyConstants.IArrayData.listStepsName));
                listPopupWindow.setAnchorView(rootView.findViewById(R.id.txt_steps_txt));
//                listPopupWindow.setWidth(190);
                listPopupWindow.setWidth((int) getResources().getDimension(R.dimen._110dp));
//                listPopupWindow.setHeight(400);
                listPopupWindow.setModal(true);
                listPopupWindow.setOnItemClickListener(popUpItemClickDoctor);
                listPopupWindow.show();
            }
        });


        txt_steps_counter.setText("" + AppPreference.getInstance().getStepsCount());
        tickerTotal.setText("" + AppPreference.getInstance().getStepsCount());
        text_calories_count.setText("" + AppPreference.getInstance().getCaloriesCount() + " kcal");
        ticker1.setText("" + AppPreference.getInstance().getPercentage() + "%");
        seekArcComplete.setProgress(AppPreference.getInstance().getPercentage());
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);


        horizontal_recycler_view = (HorizontalRecyclerView) rootView.findViewById(R.id.horizontal_recycler_view);
        data = createSampleYear();

        horizontalAdapter = new HorizontalAdapter(data, CureFull.getInstanse().getActivityIsntanse());

//        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
//        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setAdapter(horizontalAdapter);
        horizontal_recycler_view.setOnLoadMoreListener(new HorizontalRecyclerView.IOnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // data.addAll(createSampleYear());
                //horizontalAdapter.notifyDataSetChanged();
            }
        });
        return rootView;
    }


    private ArrayList<Month> createSampleYear() {
        ArrayList<Month> months = new ArrayList<>();
        for (int day : MyConstants.IArrayData.YEAR_2016) {
            months.add(new Month(day));
        }
        return months;
    }

    public static String getTodayDate() {
        String formattedDate = null;
        try {
            SimpleDateFormat initialformatter = new SimpleDateFormat(
                    "yyyy-MM-dd", Locale.getDefault());
            java.util.Date today = Calendar.getInstance().getTime();
            formattedDate = initialformatter.format(today);
            Log.e("", "formattedDate" + formattedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    AdapterView.OnItemClickListener popUpItemClickDoctor = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listPopupWindow.dismiss();
            if (position == 0) {
                txt_steps_txt.setText("Steps");
//                txt_no_data.setVisibility(View.GONE);
                date = getTodayDate();
                type = "steps";
//                setData(10, 9);
//                jsonGetGraphDeatils(date, frequency, type, offset);

            } else if (position == 1) {
                txt_steps_txt.setText("Running");
//                txt_no_data.setVisibility(View.VISIBLE);
            } else {
                txt_steps_txt.setText("Cycling");
//                txt_no_data.setVisibility(View.VISIBLE);
            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.txt_heath_note:
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentHealthNote(), true);
                break;

            case R.id.liner_btn_goal:
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentEditGoal(), true);
                break;
            case R.id.txt_prescription:
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentPrescriptionCheck(), true);
                break;
            case R.id.txt_lab_reports:
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentLabTestReport(), true);
                break;
            case R.id.btn_daily:
                btn_daily.setTextColor(Color.parseColor("#EB4748"));
                btn_monthy.setTextColor(Color.parseColor("#ffffff"));
                btn_weekly.setTextColor(Color.parseColor("#ffffff"));
                btn_daily.setBackgroundResource(R.drawable.today_edit_rounded);
                btn_monthy.setBackgroundResource(R.drawable.today_edit_rounded_trans);
                btn_weekly.setBackgroundResource(R.drawable.today_edit_rounded_trans);
                date = getTodayDate();
                frequency = "daily";
//                jsonGetGraphDeatils(date, frequency, type, offset);
//                setData(10, 12);
                break;
            case R.id.btn_weekly:
                btn_daily.setTextColor(Color.parseColor("#ffffff"));
                btn_monthy.setTextColor(Color.parseColor("#ffffff"));
                btn_weekly.setTextColor(Color.parseColor("#EB4748"));
                btn_daily.setBackgroundResource(R.drawable.today_edit_rounded_trans);
                btn_monthy.setBackgroundResource(R.drawable.today_edit_rounded_trans);
                btn_weekly.setBackgroundResource(R.drawable.today_edit_rounded);
                date = getTodayDate();
                frequency = "weekly";
//                jsonGetGraphDeatils(date, frequency, type, offset);
//                setData(10, 19);
//                mChart.animateXY(3000, 3000);
                break;
            case R.id.btn_monthy:
                btn_daily.setTextColor(Color.parseColor("#ffffff"));
                btn_monthy.setTextColor(Color.parseColor("#EB4748"));
                btn_weekly.setTextColor(Color.parseColor("#ffffff"));
                btn_daily.setBackgroundResource(R.drawable.today_edit_rounded_trans);
                btn_monthy.setBackgroundResource(R.drawable.today_edit_rounded);
                btn_weekly.setBackgroundResource(R.drawable.today_edit_rounded_trans);
                date = getTodayDate();
                frequency = "monthly";
//                jsonGetGraphDeatils(date, frequency, type, offset);
//                setData(10, 29);
//                mChart.animateXY(3000, 3000);
                break;
        }
    }


    public void doBindService() {
        CureFull.getInstanse().getActivityIsntanse().bindService(new Intent(CureFull.getInstanse().getActivityIsntanse(),
                MessengerService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
        txt_steps_counter.setText("" + AppPreference.getInstance().getStepsCount());
    }

    final Messenger mMessenger = new Messenger(new FragmentHealthAppNew.IncomingHandler());

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            mService = new Messenger(service);
            txt_steps_counter.setText("" + AppPreference.getInstance().getStepsCount());

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

//                Toast.makeText(CureFull.getInstanse().getActivityIsntanse(), R.string.remote_service_connected,
//                        Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            txt_steps_counter.setText("" + AppPreference.getInstance().getStepsCount());

//            Toast.makeText(CureFull.getInstanse().getActivityIsntanse(), R.string.remote_service_disconnected,
//                    Toast.LENGTH_SHORT).show();
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
            txt_steps_counter.setText("" + AppPreference.getInstance().getStepsCount());
        }
    }


    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerService.MSG_SET_VALUE:
                    Log.e("", "steps received in activity:" + msg.what);
                    Log.e("", "steps arg in activity:" + msg.arg1);
//                    txt_steps_counter.setText("Step Count : " + msg.arg1);
                    txt_steps_counter.setText("" + msg.arg1);
                    AppPreference.getInstance().setStepsCount("" + msg.arg1);
                    float percentage = Utils.getPercentage(msg.arg1, AppPreference.getInstance().getStepsCountTarget());
                    Log.e("percentage", ":- " + percentage);
                    int b = (int) percentage;
                    Log.e("b", ":- " + b);
                    seekArcComplete.setProgress(b);
////                    setProgressUpdateAnimation(b);
                    ticker1.setText(b + "%");
                    AppPreference.getInstance().setPercentage(b);
                    tickerTotal.setText("" + msg.arg1);

                    double wirght;
                    if (AppPreference.getInstance().getGoalWeightKg().equalsIgnoreCase("0.0")) {
                        wirght = 0;
                    } else {
                        wirght = Double.parseDouble(AppPreference.getInstance().getGoalWeightKg());
                    }
                    double i2 = Utils.getCaloriesBurnt((wirght * 2.2), msg.arg1);
                    text_calories_count.setText("" + new DecimalFormat("##.##").format(i2) + " kcal");
//                    text_calories_count.setText("" + Utils.getCaloriesBurnt((int) (kg * 2.20462), msg.arg1)+"kcal");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    public void jsonGetGraphDeatils(String fromDate, String date, String frequency, String type) {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
        JSONObject data = JsonUtilsObject.getGraphDeatils(fromDate, date, frequency, type);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.GET_GRAPH, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("GraphDeatils , URL 3.", response.toString());
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {

                            graphViewsList = ParseJsonData.getInstance().getGraphViewList(response.toString());

//                            if (graphViewsList.size() > 0) {
////                                setData(10, 9);
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

    public void jsonUploadGenderDetails(String height, String weight, String dateOfBirth, String gender) {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
        JSONObject data = JsonUtilsObject.toSetGoalsDetails(height, weight, dateOfBirth, gender);
        Log.e("jsonUploadPrescription", ":- " + data.toString());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.SET_GOALS_DEATILS, data,
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


}