package fragment.healthapp;


import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ElasticVIews.ElasticAction;
import adpter.HorizontalAdapterNewProgress;
import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import item.property.GraphYearMonthDeatilsList;
import operations.DbOperations;
import stepcounter.MessengerService;
import ticker.TickerUtils;
import ticker.TickerView;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;
import utils.RecyclerItemClickListener;
import utils.SeekArc;
import utils.Utils;
import widgets.HorizontalRecyclerView;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentHealthAppNewProgress extends Fragment implements View.OnClickListener {


    private View rootView;
    private TextView txt_water_intake_left, txt_water_intake_done, btn_daily, btn_weekly, btn_monthy, txt_steps_counter, txt_steps_txt, tickerTotal, text_calories_count;
    Messenger mService = null;
    boolean mIsBound;
    boolean isToStop = false;
    private TickerView ticker1;
    private SeekArc seekArcComplete;
    private static final char[] NUMBER_LIST = TickerUtils.getDefaultNumberList();
    private ListPopupWindow listPopupWindow;
    private String fromdate, date, frequency, type;
    private List<GraphYearMonthDeatilsList> graphViewsList;
    private TextView btn_set_goal_target;
    private LinearLayout liner_steps, liner_btn_goal;
    private HorizontalRecyclerView horizontal_recycler_view;
    private HorizontalAdapterNewProgress horizontalAdapterNew;
    private TextView txt_graph_steps, txt_water_inatke_real, txt_change, txt_change_aveg, txt_calories;
    private LinearLayout liner_avg;
    private boolean isFirstTime = false;
    SharedPreferences preferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_health_app,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        CureFull.getInstanse().getActivityIsntanse().isbackButtonVisible(false, "Health App");
        CureFull.getInstanse().getActivityIsntanse().isTobBarButtonVisible(true, "");
        preferences = PreferenceManager.getDefaultSharedPreferences(CureFull.getInstanse().getActivityIsntanse());
        AppPreference.getInstance().setFragmentHealthApp(true);
        AppPreference.getInstance().setFragmentHealthNote(false);
        AppPreference.getInstance().setFragmentHealthpre(false);
        AppPreference.getInstance().setFragmentHealthReprts(false);

        CureFull.getInstanse().getActivityIsntanse().selectedNav(0);
        liner_avg = (LinearLayout) rootView.findViewById(R.id.liner_avg);
        txt_calories = (TextView) rootView.findViewById(R.id.txt_calories);
        txt_change_aveg = (TextView) rootView.findViewById(R.id.txt_change_aveg);
        txt_change = (TextView) rootView.findViewById(R.id.txt_change);
        txt_water_inatke_real = (TextView) rootView.findViewById(R.id.txt_water_inatke_real);
        txt_graph_steps = (TextView) rootView.findViewById(R.id.txt_graph_steps);
        txt_water_intake_left = (TextView) rootView.findViewById(R.id.txt_water_intake_left);
        txt_water_intake_done = (TextView) rootView.findViewById(R.id.txt_water_intake_done);
        liner_btn_goal = (LinearLayout) rootView.findViewById(R.id.liner_btn_goal);
        liner_steps = (LinearLayout) rootView.findViewById(R.id.liner_steps);
        btn_set_goal_target = (TextView) rootView.findViewById(R.id.btn_set_goal_target);
        txt_steps_txt = (TextView) rootView.findViewById(R.id.txt_steps_txt);
//        txt_no_data = (TextView) rootView.findViewById(R.id.txt_no_data);
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
        AppPreference.getInstance().setGraphDate("");
//        if (AppPreference.getInstance().getStepStarts()) {
        Intent intent = new Intent(CureFull.getInstanse().getActivityIsntanse(), MessengerService.class);
        CureFull.getInstanse().getActivityIsntanse().startService(intent);
        doBindService();


//        }

        btn_set_goal_target.setText("Goals - " + AppPreference.getInstance().getStepsCountTarget() + " steps");
        txt_water_intake_done.setText("" + new DecimalFormat("###.##").format(Utils.getMlToLiter(Integer.parseInt(preferences.getString("waterTake", "0")))) + " L Drinked");

        if (AppPreference.getInstance().getWaterInTakeLeft().equalsIgnoreCase("0")) {
            txt_water_intake_left.setText("0 L Left");

        } else {
            txt_water_intake_left.setText("" + new DecimalFormat("###.##").format(Utils.getMlToLiter(Integer.parseInt(AppPreference.getInstance().getWaterInTakeLeft()))) + " L Left");

        }


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
        });//not in use


        txt_steps_counter.setText("" + preferences.getInt("stepsIn", 0));
//        txt_steps_counter.setText("" + AppPreference.getInstance().getStepsCount());
        tickerTotal.setText("" + preferences.getInt("stepsIn", 0));
        text_calories_count.setText("" + preferences.getString("CaloriesCount", "0") + " kcal");
        ticker1.setText("" + preferences.getInt("percentage", 0) + "%");
        seekArcComplete.setProgress(preferences.getInt("percentage", 0));
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);

        horizontal_recycler_view = (HorizontalRecyclerView) rootView.findViewById(R.id.horizontal_recycler_view);

//        horizontalAdapter = new HorizontalAdapter(data, CureFull.getInstanse().getActivityIsntanse());
//
////        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
////        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
//        horizontal_recycler_view.setAdapter(horizontalAdapter);
//        horizontal_recycler_view.smoothScrollToPosition(data.size() - 1);
//        horizontal_recycler_view.setOnLoadMoreListener(new HorizontalRecyclerView.IOnLoadMoreListener() {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                // data.addAll(createSampleYear());
//                //horizontalAdapter.notifyDataSetChanged();
//            }
//        });

        date = getTodayDate();
        frequency = "daily";
        type = "steps";
        fromdate = "";
        jsonGetGraphDeatils(fromdate, date, frequency, type);


        return rootView;
    }


//    private ArrayList<Month> createSampleYear() {
//        ArrayList<Month> months = new ArrayList<>();
//        for (int day : MyConstants.IArrayData.YEAR_2016) {
//            months.add(new Month(day));
//        }
//        return months;
//    }

    public static String getTodayDate() {
        String formattedDate = null;
        try {
            SimpleDateFormat initialformatter = new SimpleDateFormat(
                    "yyyy-MM-dd", Locale.getDefault());
            java.util.Date today = Calendar.getInstance().getTime();
            formattedDate = initialformatter.format(today);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    AdapterView.OnItemClickListener popUpItemClickDoctor = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
            listPopupWindow.dismiss();
            if (position == 0) {
                txt_change.setText("Walking Steps");
                txt_steps_txt.setText("Steps");
                type = "steps";
                jsonGetGraphDeatils(fromdate, date, frequency, type);

            } else if (position == 1) {
                txt_change.setText("Running");
                txt_steps_txt.setText("Running");
                type = "running";
                jsonGetGraphDeatils(fromdate, date, frequency, type);
//                txt_no_data.setVisibility(View.VISIBLE);
            } else {
                txt_change.setText("Cycling");
                txt_steps_txt.setText("Cycling");
                type = "cycling";
                jsonGetGraphDeatils(fromdate, date, frequency, type);
//                txt_no_data.setVisibility(View.VISIBLE);
            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.liner_btn_goal:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentEditGoal(), true);
                break;

            case R.id.btn_daily:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                isFirstTime = false;
                AppPreference.getInstance().setGraphDate("");
                liner_avg.setVisibility(View.GONE);
                btn_daily.setTextColor(Color.parseColor("#ffffff"));
                btn_monthy.setTextColor(Color.parseColor("#151515"));
                btn_weekly.setTextColor(Color.parseColor("#151515"));
                btn_daily.setBackgroundResource(R.drawable.today_edit_rounded);
                btn_monthy.setBackgroundResource(R.drawable.today_edit_rounded_trans);
                btn_weekly.setBackgroundResource(R.drawable.today_edit_rounded_trans);
                date = getTodayDate();
                frequency = "daily";
                fromdate = "";
                jsonGetGraphDeatils(fromdate, date, frequency, type);
                break;
            case R.id.btn_weekly:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                isFirstTime = true;
                AppPreference.getInstance().setGraphDate("");
                btn_daily.setTextColor(Color.parseColor("#151515"));
                btn_monthy.setTextColor(Color.parseColor("#151515"));
                btn_weekly.setTextColor(Color.parseColor("#ffffff"));
                btn_daily.setBackgroundResource(R.drawable.today_edit_rounded_trans);
                btn_monthy.setBackgroundResource(R.drawable.today_edit_rounded_trans);
                btn_weekly.setBackgroundResource(R.drawable.today_edit_rounded);
                date = getTodayDate();
                fromdate = "";
                frequency = "weekly";
                jsonGetGraphDeatils(fromdate, date, frequency, type);

//                jsonGetGraphDeatils(date, frequency, type, offset);
//                setData(10, 19);
//                mChart.animateXY(3000, 3000);
                break;
            case R.id.btn_monthy:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                isFirstTime = true;
                AppPreference.getInstance().setGraphDate("");
                btn_daily.setTextColor(Color.parseColor("#151515"));
                btn_monthy.setTextColor(Color.parseColor("#ffffff"));
                btn_weekly.setTextColor(Color.parseColor("#151515"));
                btn_daily.setBackgroundResource(R.drawable.today_edit_rounded_trans);
                btn_monthy.setBackgroundResource(R.drawable.today_edit_rounded);
                btn_weekly.setBackgroundResource(R.drawable.today_edit_rounded_trans);
                date = getTodayDate();
                frequency = "monthly";
                fromdate = "";
                jsonGetGraphDeatils(fromdate, date, frequency, type);
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
//        preferences.edit().putInt("stepsIn", Integer.parseInt(steps)).commit();
        txt_steps_counter.setText("" + preferences.getInt("stepsIn", 0));
//        txt_steps_counter.setText("" + AppPreference.getInstance().getStepsCount());
    }

    final Messenger mMessenger = new Messenger(new FragmentHealthAppNewProgress.IncomingHandler());

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            mService = new Messenger(service);
//            preferences.edit().putInt("stepsIn", Integer.parseInt(steps)).commit();
            txt_steps_counter.setText("" + preferences.getInt("stepsIn", 0));
//            txt_steps_counter.setText("" + AppPreference.getInstance().getStepsCount());

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
//            preferences.edit().putInt("stepsIn", Integer.parseInt(steps)).commit();
            txt_steps_counter.setText("" + preferences.getInt("stepsIn", 0));
//            txt_steps_counter.setText("" + AppPreference.getInstance().getStepsCount());

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
//            preferences.edit().putInt("stepsIn", Integer.parseInt(steps)).commit();
            txt_steps_counter.setText("" + preferences.getInt("stepsIn", 0));
//            txt_steps_counter.setText("" + AppPreference.getInstance().getStepsCount());
        }
    }


    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerService.MSG_SET_VALUE:
                    txt_steps_counter.setText("" + msg.arg1);
                    preferences.edit().putInt("stepsIn", msg.arg1).commit();
//                    txt_steps_counter.setText("" + preferences.getInt("stepsIn", 0));
//                    AppPreference.getInstance().setStepsCount("" + msg.arg1);
                    float percentage = Utils.getPercentage(msg.arg1, AppPreference.getInstance().getStepsCountTarget());
                    int b = (int) percentage;
                    seekArcComplete.setProgress(b);
////                    setProgressUpdateAnimation(b);
                    ticker1.setText(b + "%");
                    preferences.edit().putInt("percentage", b).commit();
                    tickerTotal.setText("" + msg.arg1);

                    double wirght = 0.0;
////                    if (AppPreference.getInstance().getGoalWeightKg().equalsIgnoreCase("0.0") || AppPreference.getInstance().getGoalWeightKg().equalsIgnoreCase("")) {
////                        wirght = 40;
////                    } else {
////                        wirght = Double.parseDouble(AppPreference.getInstance().getGoalWeightKg());
////                    }
//                    if (AppPreference.getInstance().getGoalWeightGrams().equalsIgnoreCase("")) {
//                        wirght = Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg()));
//                    } else {
//                        if (AppPreference.getInstance().getGoalWeightKg().equalsIgnoreCase("")) {
//                            wirght = 40;
//                        } else {
//                            wirght = Utils.getConvertingKilogramsIntoPounds(Double.parseDouble(AppPreference.getInstance().getGoalWeightKg() + "." + AppPreference.getInstance().getGoalWeightGrams()));
//
//                        }
//
//                    }
                    double i2 = Utils.getCaloriesBurnt(wirght, msg.arg1);
                    text_calories_count.setText("" + new DecimalFormat("###.#").format(i2) + " kcal");
                    preferences.edit().putString("CaloriesCount", "" + new DecimalFormat("###.#").format(i2)).commit();
//                    text_calories_count.setText("" + Utils.getCaloriesBurnt((int) (kg * 2.20462), msg.arg1)+"kcal");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    public void jsonGetGraphDeatils(String fromDate, String date, final String frequency, final String type) {
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            JSONObject data1 = JsonUtilsObject.getGraphDeatils(fromDate, date, frequency, type);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.GET_GRAPH, data1,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
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
                                if (graphViewsList != null && graphViewsList.size() > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("cf_uuhid", AppPreference.getInstance().getcf_uuhid());
                                    cv.put("graph_data", response.toString());
                                    cv.put("graph_type", type);
                                    cv.put("graph_frequecy", frequency);
                                    DbOperations.insertGraphList(CureFull.getInstanse().getActivityIsntanse(), cv, AppPreference.getInstance().getcf_uuhid());
                                    setChartGraph(frequency);
                                }
                            } else {
//                                try {
//                                    JSONObject json1 = new JSONObject(json.getString("errorInfo"));
//                                    JSONObject json12 = new JSONObject(json1.getString("errorDetails"));
//                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + json12.getString("message"));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
                            }


                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    String response = DbOperations.getGraphList(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhid(), type, frequency);
                    if (!response.equalsIgnoreCase("")) {
                        graphViewsList = ParseJsonData.getInstance().getGraphViewList(response.toString());
                        if (graphViewsList != null)
                            setChartGraph(frequency);
                    }
                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
//                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
//                    VolleyLog.e("FragmentLogin, URL 3.", "Error: " + error.getMessage());
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
        } else {
            String response = DbOperations.getGraphList(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhid(), type, frequency);
            if (!response.equalsIgnoreCase("")) {
                graphViewsList = ParseJsonData.getInstance().getGraphViewList(response.toString());
                if (graphViewsList != null)
                    setChartGraph(frequency);
            }

        }

    }


    public void valueFromGrpah(String date, String stepsValue, String waterTake, String calories, String frequencys) {
        liner_avg.setVisibility(View.VISIBLE);
        if (frequencys.equalsIgnoreCase("daily")) {
            if (!date.equalsIgnoreCase("")) {
                String[] dateFormat = date.split("-");
                int mYear = Integer.parseInt(dateFormat[0]);
                int mMonth = Integer.parseInt(dateFormat[1]);
                int mDay = Integer.parseInt(dateFormat[2]);
                try {
                    date = mDay + "," + Utils.formatMonth(String.valueOf(mMonth));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                txt_change_aveg.setText("" + date + " " + mYear);
            }
        } else if (frequencys.equalsIgnoreCase("monthly")) {
            txt_change_aveg.setText("Average of " + MyConstants.IArrayData.mMonths[Integer.parseInt(date)] + " " + graphViewsList.get(0).getYear());
        } else {
            String dateTime = date;
            String[] dateParts = dateTime.split("to");
            String day1 = dateParts[0];
            String day2 = dateParts[1];

            String[] real1 = day1.split("-");
            String years = real1[0];
            String months = real1[1];
            String days1 = real1[2];

            String[] real2 = day2.split("-");
            String years1 = real2[0];
            String months1 = real2[1];
            String days2 = real2[2];
            try {
                txt_change_aveg.setText("Average of " + "" + days1.trim() + "-" + (days2.trim()) + " " + Utils.formatMonth(months));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        txt_graph_steps.setText("" + stepsValue);
        txt_water_inatke_real.setText("" + new DecimalFormat("###.##").format(Utils.getMlToLiter(Integer.parseInt(waterTake))) + " ltr");
        txt_calories.setText("" + calories + " Kcl");
    }


    public void setChartGraph(String frequencys) {
        horizontalAdapterNew = new HorizontalAdapterNewProgress(graphViewsList.get(0).getGraphViewDetailses(), CureFull.getInstanse().getActivityIsntanse(), FragmentHealthAppNewProgress.this, frequencys, isFirstTime);
        horizontal_recycler_view.setAdapter(horizontalAdapterNew);
//                                horizontal_recycler_view.scrollToPosition(graphViewsList.get(0).getGraphViewDetailses().size() - 1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                horizontal_recycler_view.smoothScrollToPosition(graphViewsList.get(0).getGraphViewDetailses().size());
            }
        }, 100);


        horizontal_recycler_view.setOnLoadMoreListener(new HorizontalRecyclerView.IOnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // data.addAll(createSampleYear());
                //horizontalAdapter.notifyDataSetChanged();
            }
        });
        horizontalAdapterNew.notifyDataSetChanged();


        horizontal_recycler_view.addOnItemTouchListener(
                new RecyclerItemClickListener(CureFull.getInstanse().getActivityIsntanse(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                    }
                })
        );
    }


}