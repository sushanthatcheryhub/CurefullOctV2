package fragment.healthapp;


import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

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

import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.BaseBackHandlerFragment;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import graph.DayAxisValueFormatter;
import graph.MyAxisValueFormatter;
import graph.XYMarkerView;
import item.property.GraphView;
import stepcounter.MessengerService;
import ticker.TickerUtils;
import ticker.TickerView;
import utils.AppPreference;
import utils.MyConstants;
import utils.SeekArc;
import utils.Utils;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentHealthAppNew extends BaseBackHandlerFragment implements View.OnClickListener, OnChartValueSelectedListener {


    private View rootView;
    private TextView btn_daily, btn_weekly, btn_monthy, txt_steps_counter, txt_no_data, txt_steps_txt;
    Messenger mService = null;
    boolean mIsBound;
    boolean isToStop = false;
    private TickerView ticker1, tickerTotal, text_calories_count;
    private SeekArc seekArcComplete;
    private static final char[] NUMBER_LIST = TickerUtils.getDefaultNumberList();
    protected BarChart mChart;
    private LinearLayout txt_prescription, txt_heath_note, txt_lab_reports;
    private ListPopupWindow listPopupWindow;
    private RequestQueue requestQueue;
    private String date, frequency, type, offset;
    private List<GraphView> graphViewsList;
    private TextView btn_set_goal_target;
    private LinearLayout liner_steps;

    @Override
    public boolean onBackPressed() {

        Log.e("aaya", ":-");

        String gender = "";
        if (AppPreference.getInstance().getMale()) {
            gender = "MALE";
        } else {
            gender = "FEMALE";
        }

        jsonUploadGenderDetails(String.valueOf(convertFeetandInchesToCentimeter(String.valueOf(AppPreference.getInstance().getGoalHeightFeet()), String.valueOf(AppPreference.getInstance().getGoalHeightInch()))), String.valueOf(AppPreference.getInstance().getGoalWeightKg()), AppPreference.getInstance().getGoalAge(), gender);

        return super.onBackPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_health_app,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        liner_steps = (LinearLayout) rootView.findViewById(R.id.liner_steps);
        btn_set_goal_target = (TextView) rootView.findViewById(R.id.btn_set_goal_target);
        txt_steps_txt = (TextView) rootView.findViewById(R.id.txt_steps_txt);
        txt_no_data = (TextView) rootView.findViewById(R.id.txt_no_data);
        txt_prescription = (LinearLayout) rootView.findViewById(R.id.txt_prescription);
        txt_heath_note = (LinearLayout) rootView.findViewById(R.id.txt_heath_note);
        txt_lab_reports = (LinearLayout) rootView.findViewById(R.id.txt_lab_reports);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);
        ticker1 = (TickerView) rootView.findViewById(R.id.ticker1);
        tickerTotal = (TickerView) rootView.findViewById(R.id.tickerTotal);
        text_calories_count = (TickerView) rootView.findViewById(R.id.text_calories_count);
        txt_steps_counter = (TextView) rootView.findViewById(R.id.txt_steps_counter);
        btn_daily = (TextView) rootView.findViewById(R.id.btn_daily);
        btn_weekly = (TextView) rootView.findViewById(R.id.btn_weekly);
        btn_monthy = (TextView) rootView.findViewById(R.id.btn_monthy);
        seekArcComplete = (SeekArc) rootView.findViewById(R.id.seekArcComplete);
        ticker1.setCharacterList(NUMBER_LIST);
        tickerTotal.setCharacterList(NUMBER_LIST);
        text_calories_count.setCharacterList(NUMBER_LIST);
        btn_daily.setOnClickListener(this);
        btn_weekly.setOnClickListener(this);
        btn_monthy.setOnClickListener(this);
        btn_set_goal_target.setOnClickListener(this);
//        if (AppPreference.getInstance().getStepStarts()) {
        Intent intent = new Intent(CureFull.getInstanse().getActivityIsntanse(), MessengerService.class);
        CureFull.getInstanse().getActivityIsntanse().startService(intent);
        doBindService();
//        }

        btn_set_goal_target.setText("" + AppPreference.getInstance().getStepsCountTarget());
        //Chart
        mChart = (BarChart) rootView.findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(R.color.health_yellow);

//        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

//        YAxis rightAxis = mChart.getAxisRight();
//        rightAxis.setDrawGridLines(false);
////        rightAxis.setTypeface(mTfLight);
//        rightAxis.setLabelCount(8, false);
//        rightAxis.setValueFormatter(custom);
//        rightAxis.setSpaceTop(15f);
//        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

//        Legend l = mChart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        l.setForm(Legend.LegendForm.SQUARE);
//        l.setFormSize(9f);
//        l.setTextSize(11f);
//        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

//        XYMarkerView mv = new XYMarkerView(getActivity(), xAxisFormatter);
//        mv.setChartView(mChart); // For bounds control
//        mChart.setMarker(mv); // Set the marker to the chart


        date = getTodayDate();
        frequency = "daily";
        type = "steps";
        offset = "10";
//        jsonGetGraphDeatils(date, frequency, type, offset);


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
                listPopupWindow.setWidth((int)getResources().getDimension(R.dimen._110dp));
//                listPopupWindow.setHeight(400);
                listPopupWindow.setModal(true);
                listPopupWindow.setOnItemClickListener(popUpItemClickDoctor);
                listPopupWindow.show();
            }
        });

        setData(10, 9);
        return rootView;
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
                mChart.setVisibility(View.VISIBLE);
                txt_no_data.setVisibility(View.GONE);
                date = getTodayDate();
                type = "steps";
                offset = "10";
                setData(10, 9);
                mChart.animateXY(3000, 3000);
//                jsonGetGraphDeatils(date, frequency, type, offset);

            } else if (position == 1) {
                mChart.setVisibility(View.GONE);
                txt_steps_txt.setText("Running");
                txt_no_data.setVisibility(View.VISIBLE);
            } else {
                mChart.setVisibility(View.GONE);
                txt_steps_txt.setText("Cycling");
                txt_no_data.setVisibility(View.VISIBLE);
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

            case R.id.btn_set_goal_target:
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
                offset = "10";
//                jsonGetGraphDeatils(date, frequency, type, offset);
                setData(10, 12);
                mChart.animateXY(3000, 3000);
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
                offset = "10";
//                jsonGetGraphDeatils(date, frequency, type, offset);
                setData(10, 19);
                mChart.animateXY(3000, 3000);
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
                offset = "10";
//                jsonGetGraphDeatils(date, frequency, type, offset);
                setData(10, 29);
                mChart.animateXY(3000, 3000);
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

    private void setData(int count, float range) {

        float start = 1f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = (int) start; i < start + count + 1; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);
            yVals1.add(new BarEntry(i, val));
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
//            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);
            mChart.animateXY(3000, 3000);
            mChart.setData(data);
        }
    }

    protected RectF mOnValueSelectedRectF = new RectF();

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());
        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() {
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
                    int b = (int) Math.round(percentage);
                    Log.e("b", ":- " + b);
                    seekArcComplete.setProgress(b);
////                    setProgressUpdateAnimation(b);
                    ticker1.setText(b + "%");
                    tickerTotal.setText("" + msg.arg1);

                    String wirght;
                    if (AppPreference.getInstance().getGoalWeightKg().equalsIgnoreCase("0.0")) {
                        wirght = "0";
                    } else {
                        wirght = AppPreference.getInstance().getGoalWeightKg();
                    }
                    int kg = Integer.parseInt(wirght);
                    double i2 = Utils.getCaloriesBurnt((int) (kg * 2.20462), msg.arg1);
                    text_calories_count.setText("" + new DecimalFormat("##.##").format(i2) + " kcal");
//                    text_calories_count.setText("" + Utils.getCaloriesBurnt((int) (kg * 2.20462), msg.arg1)+"kcal");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    public void jsonGetGraphDeatils(String date, String frequency, String type, String offset) {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
        JSONObject data = JsonUtilsObject.getGraphDeatils(date, frequency, type, offset);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.GET_GRAPH, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("upload, URL 3.", response.toString());
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

                            if (graphViewsList.size() > 0) {
//                                setData(10, 9);
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

    public static double convertFeetandInchesToCentimeter(String feet, String inches) {
        double heightInFeet = 0;
        double heightInInches = 0;
        try {
            if (feet != null && feet.trim().length() != 0) {
                heightInFeet = Double.parseDouble(feet);
            }
            if (inches != null && inches.trim().length() != 0) {
                heightInInches = Double.parseDouble(inches);
            }
        } catch (NumberFormatException nfe) {

        }
        return (heightInFeet * 30.48) + (heightInInches * 2.54);
    }

}