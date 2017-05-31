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
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;

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
import adpter.HorizontalAdapterNewProgressVersionNew;
import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.BaseBackHandlerFragment;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import item.property.GraphYearMonthDeatilsList;
import operations.DbOperations;
import stepcounter.MessengerService;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;
import utils.RecyclerItemClickListener;
import utils.SeekArc;
import utils.Utils;
import widgets.HorizontalRecyclerView;

/**
 * Created by Sourav on 26-05-2017.
 */

public class Fragment_Health_Trends extends BaseBackHandlerFragment implements View.OnClickListener {
    private View rootView;
    SharedPreferences preferences;
    private ImageView img_right_arrow, img_left_arrow;
    private TextView txt_date, txt_total_steps, txt_details, txt_total_target, txt_walking, txt_running, txt_cycling, txt_calories_burnt, txt_water_level_current, txt_water_level_total, btn_daily, btn_weekly, btn_monthy;
    private HorizontalRecyclerView horizontal_recycler_view;
    private HorizontalAdapterNewProgressVersionNew horizontalAdapterNew;
    private String fromdate, date, frequency, type;
    private List<GraphYearMonthDeatilsList> graphViewsList;
    private boolean isFirstTime = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_curehealth_trends,
                container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(CureFull.getInstanse().getActivityIsntanse());
        CureFull.getInstanse().getActivityIsntanse().isbackButtonVisible(false, "Health App");
        CureFull.getInstanse().getActivityIsntanse().isTobBarButtonVisible(true, "");
        CureFull.getInstanse().getActivityIsntanse().activateDrawer();

        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        txt_details = (TextView) rootView.findViewById(R.id.txt_details);
        txt_date = (TextView) rootView.findViewById(R.id.txt_date);
        img_right_arrow = (ImageView) rootView.findViewById(R.id.img_right_arrow);
        img_left_arrow = (ImageView) rootView.findViewById(R.id.img_left_arrow);
        txt_total_steps = (TextView) rootView.findViewById(R.id.txt_total_steps);
        txt_total_target = (TextView) rootView.findViewById(R.id.txt_total_target);
        txt_walking = (TextView) rootView.findViewById(R.id.txt_walking);
        txt_running = (TextView) rootView.findViewById(R.id.txt_running);
        txt_cycling = (TextView) rootView.findViewById(R.id.txt_cycling);
        txt_calories_burnt = (TextView) rootView.findViewById(R.id.txt_calories_burnt);
        txt_water_level_current = (TextView) rootView.findViewById(R.id.txt_water_level_current);
        txt_water_level_total = (TextView) rootView.findViewById(R.id.txt_water_level_total);
        btn_daily = (TextView) rootView.findViewById(R.id.btn_daily);
        btn_weekly = (TextView) rootView.findViewById(R.id.btn_weekly);
        btn_monthy = (TextView) rootView.findViewById(R.id.btn_monthy);
        horizontal_recycler_view = (HorizontalRecyclerView) rootView.findViewById(R.id.horizontal_recycler_view);
        btn_daily.setOnClickListener(this);
        btn_weekly.setOnClickListener(this);
        btn_monthy.setOnClickListener(this);
        img_right_arrow.setOnClickListener(this);
        img_left_arrow.setOnClickListener(this);

       /* date = getTodayDate();
        frequency = "daily";
        type = "steps";
        fromdate = "";
        jsonGetGraphDeatils(fromdate, date, frequency, type);*/

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            date = getTodayDate();
            frequency = "daily";
            type = "steps";
            fromdate = "";
            jsonGetGraphDeatils(fromdate, date, frequency, type);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_daily:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                isFirstTime = false;
                AppPreference.getInstance().setGraphDate("");
                //  liner_avg.setVisibility(View.GONE);
                btn_daily.setTextColor(Color.parseColor("#23A5F0"));
                btn_monthy.setTextColor(Color.parseColor("#151515"));
                btn_weekly.setTextColor(Color.parseColor("#151515"));
                /*btn_daily.setBackgroundResource(R.drawable.today_edit_rounded);
                btn_monthy.setBackgroundResource(R.drawable.today_edit_rounded_trans);
                btn_weekly.setBackgroundResource(R.drawable.today_edit_rounded_trans);*/
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
                btn_weekly.setTextColor(Color.parseColor("#23A5F0"));
                /*btn_daily.setBackgroundResource(R.drawable.today_edit_rounded_trans);
                btn_monthy.setBackgroundResource(R.drawable.today_edit_rounded_trans);
                btn_weekly.setBackgroundResource(R.drawable.today_edit_rounded);*/
                date = getTodayDate();
                fromdate = "";
                frequency = "weekly";
                jsonGetGraphDeatils(fromdate, date, frequency, type);

                break;
            case R.id.btn_monthy:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                isFirstTime = true;
                AppPreference.getInstance().setGraphDate("");
                btn_daily.setTextColor(Color.parseColor("#151515"));
                btn_monthy.setTextColor(Color.parseColor("#23A5F0"));
                btn_weekly.setTextColor(Color.parseColor("#151515"));
              /*  btn_daily.setBackgroundResource(R.drawable.today_edit_rounded_trans);
                btn_monthy.setBackgroundResource(R.drawable.today_edit_rounded);
                btn_weekly.setBackgroundResource(R.drawable.today_edit_rounded_trans);*/
                date = getTodayDate();
                frequency = "monthly";
                fromdate = "";
                jsonGetGraphDeatils(fromdate, date, frequency, type);

                break;

            case R.id.img_left_arrow:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                img_right_arrow.setImageResource(R.drawable.right_arrow_blue_small);
                date=Utils.getPreviousDatebyOne(date);

                txt_date.setText("" + getDate(date));
                break;

            case R.id.img_right_arrow:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);

                if(!date.equalsIgnoreCase(Utils.getTodayDate())){
                    date=Utils.getNextDate(date);
                    txt_date.setText("" + getDate(date));
                }else{
                    img_right_arrow.setImageResource(R.drawable.right_arrow_grey_small);

                }
                break;
        }
    }

    private String getDate(String date){
        String[] dateFormat = date.split("-");
        int mYear = Integer.parseInt(dateFormat[0]);
        int mMonth = Integer.parseInt(dateFormat[1]);
        int mDay = Integer.parseInt(dateFormat[2]);
        try {
            date = mDay + "," + Utils.formatMonth(String.valueOf(mMonth))+ " " +mYear;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public void valueFromGrpah(String date, String stepsValue, String waterTake, String calories, String frequencys) {
        //liner_avg.setVisibility(View.VISIBLE);
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
                txt_date.setText("" + date + " " + mYear);
                txt_details.setText("Detail");
            }
        } else if (frequencys.equalsIgnoreCase("monthly")) {
            txt_date.setText(MyConstants.IArrayData.mMonths[Integer.parseInt(date)] + " " + graphViewsList.get(0).getYear() + " (Average)");
            txt_details.setText("Average detail of the Month");
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
                txt_date.setText("" + days1.trim() + " " + Utils.formatMonth(months) + "-" + (days2.trim()) + " " + Utils.formatMonth(months) + " " + years1 + " " + "(Average)");
                txt_details.setText("Average detail of the Week");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        txt_total_steps.setText("" + stepsValue + " " + "/ ");// ye top wala hai
        txt_total_target.setText("" + AppPreference.getInstance().getStepsCountTarget());// ye top wala hai  steps target
        txt_walking.setText("" + stepsValue);
        txt_water_level_current.setText("" + new DecimalFormat("###.##").format(Utils.getMlToLiter(Integer.parseInt(waterTake))) + " ltr");
        txt_calories_burnt.setText("" + calories + " Kcl");
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

    public void setChartGraph(String frequencys) {
        horizontalAdapterNew = new HorizontalAdapterNewProgressVersionNew(graphViewsList.get(0).getGraphViewDetailses(), CureFull.getInstanse().getActivityIsntanse(), Fragment_Health_Trends.this, frequencys, isFirstTime);
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

}
