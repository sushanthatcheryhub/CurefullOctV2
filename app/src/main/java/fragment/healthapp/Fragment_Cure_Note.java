package fragment.healthapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.ParseError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adpter.Cure_Note_ListAdpter;

import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.BaseBackHandlerFragment;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import item.property.HealthNoteItems;
import operations.DbOperations;
import sticky.header.ExpandableStickyListHeadersListView;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;

/**
 * Created by Sourav on 31-05-2017.
 */

public class Fragment_Cure_Note extends BaseBackHandlerFragment implements View.OnClickListener, MyConstants.JsonUtils{
    private View rootView;
    private ExpandableStickyListHeadersListView mListView;
    private List<HealthNoteItems> healthNoteItemsesDummy;
    private int offset = 0;
    private boolean isloadMore = false, isCallAgain = false;
    private List<HealthNoteItems> healthNoteItemses = new ArrayList<HealthNoteItems>();
    private Cure_Note_ListAdpter adapterRecentNew;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_curenote_main,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        CureFull.getInstanse().getActivityIsntanse().isbackButtonVisible(false, "Note");
        CureFull.getInstanse().getActivityIsntanse().isTobBarButtonVisible(true, "");
        AppPreference.getInstance().setIsEditGoalPage(false);
        AppPreference.getInstance().setFragmentHealthApp(false);
        AppPreference.getInstance().setFragmentHealthNote(true);
        AppPreference.getInstance().setFragmentHealthpre(false);
        AppPreference.getInstance().setFragmentHealthReprts(false);

        CureFull.getInstanse().getActivityIsntanse().selectedNav(0);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);
        mListView = (ExpandableStickyListHeadersListView) rootView.findViewById(R.id.list);

        mListView.setAnimationCacheEnabled(false);
        mListView.setFastScrollEnabled(true);

        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            healthNoteItemsesDummy = DbOperations.getOfflineNoteList(CureFull.getInstanse().getActivityIsntanse());
            if (healthNoteItemsesDummy != null && healthNoteItemsesDummy.size() > 0) {

                offlineDataSent(healthNoteItemsesDummy);
            } else {
                getAllHealthList();
            }
        } else {
            getAllHealthList();
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {

    }

    public void offlineDataSent(List<HealthNoteItems> healthNoteItemsesDummy) {
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            JSONObject data = JsonUtilsObject.toAddOfflineHealthNote(healthNoteItemsesDummy);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.ADD_LIST_OF_HEALTH_NOTE, data,
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
                                CureFull.setIdss(0);
                                DbOperations.clearOfflineNoteDB();
                                DbOperations.clearNoteDB();
                                getAllHealthList();
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
                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
//                    VolleyLog.e("health, URL 3.", "Error: " + error.getMessage());
                }

            }) {


                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
//                    Log.e("headers", "" +  response.headers.get("a_t"));
                        JSONObject jsonResponse = new JSONObject(jsonString);
                        jsonResponse.put(MyConstants.JsonUtils.HEADERS, new JSONObject(response.headers));
                        return Response.success(jsonResponse,
                                HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    } catch (JSONException je) {
                        return Response.error(new ParseError(je));
                    }
                }

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
        }

    }

    private void getAllHealthList() {
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.HEALTH_LIST_NOTE + "limit=15&offset=" + offset,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
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
                                healthNoteItemsesDummy = null;
                                healthNoteItemsesDummy = ParseJsonData.getInstance().getHealthNoteListItem(response);
                                if (healthNoteItemsesDummy != null && healthNoteItemsesDummy.size() > 0) {
                                    if (healthNoteItemsesDummy.size() < 15) {
                                        isloadMore = true;
                                    }

                                    healthNoteItemses.addAll(healthNoteItemsesDummy);
                                    showAdpter();
                                } else {
                                    if (healthNoteItemsesDummy == null) {
                                        isloadMore = true;
                                    }
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
        } else {
            healthNoteItemsesDummy = DbOperations.getNoteList(CureFull.getInstanse().getActivityIsntanse());
            isloadMore = false;
            offset = 0;
            healthNoteItemses = null;
            healthNoteItemses = new ArrayList<HealthNoteItems>();
            if (healthNoteItemsesDummy != null && healthNoteItemsesDummy.size() > 0) ;
            healthNoteItemses.addAll(healthNoteItemsesDummy);
            showAdpter();
        }

    }

    public void showAdpter() {
        isCallAgain = false;
        if (healthNoteItemses != null && healthNoteItemses.size() > 0) {
            //realtive_no_health.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            adapterRecentNew = new Cure_Note_ListAdpter(CureFull.getInstanse().getActivityIsntanse(),
                    healthNoteItemses, Fragment_Cure_Note.this);
            mListView.setAdapter(adapterRecentNew);

        } else {
            //realtive_no_health.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
    }
    public void checkSize() {

        if (healthNoteItemses.size() == 0) {
            //realtive_no_health.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
    }

    public void callWebServiceAgain(int offsets) {
        if (!isCallAgain) {
            if (!isloadMore) {
                offset = +offsets;
                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
                    StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.HEALTH_LIST_NOTE + "limit=15&offset=" + offset,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
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
                                        healthNoteItemsesDummy = ParseJsonData.getInstance().getHealthNoteListItem(response);
                                        if (healthNoteItemsesDummy != null && healthNoteItemsesDummy.size() > 0) {
                                            if (healthNoteItemsesDummy.size() < 15) {
                                                isloadMore = true;
                                            }
                                            healthNoteItemses.addAll(healthNoteItemsesDummy);
                                            adapterRecentNew.notifyDataSetChanged();
//                                            showAdpternew();
                                        } else {
                                            if (healthNoteItemsesDummy == null) {
                                                isloadMore = true;
                                            }
                                        }
                                    } else {
                                        if (healthNoteItemsesDummy == null) {
                                            isloadMore = true;
                                        }
                                        //realtive_no_health.setVisibility(View.VISIBLE);
                                        mListView.setVisibility(View.GONE);
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

            }
        }

    }
}
