package fragment.healthapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adpter.UHID_ListAdpter;
import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import item.property.UHIDItems;
import utils.AppPreference;
import utils.MyConstants;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentUHID extends Fragment {


    private View rootView;
    private RecyclerView recyclerView_notes;
    private List<UHIDItems> uhidItemses;
    private UHID_ListAdpter uhid_listAdpter;
    private RequestQueue requestQueue;
    private LinearLayout liner_add_new, login_liner;
    private EditText input_name, edt_phone;
    private TextView btn_add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_uhid,
                container, false);
        if (CureFull.getInstanse().getiGlobalIsbackButtonVisible() != null) {
            CureFull.getInstanse().getiGlobalIsbackButtonVisible().isbackButtonVisible(true,"");
        }
        if (CureFull.getInstanse().getiGlobalTopBarButtonVisible() != null) {
            CureFull.getInstanse().getiGlobalTopBarButtonVisible().isTobBarButtonVisible(true);
        }
        CureFull.getInstanse().getActivityIsntanse().selectedNav(2);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);
        btn_add = (TextView) rootView.findViewById(R.id.btn_add);
        input_name = (EditText) rootView.findViewById(R.id.input_name);
        edt_phone = (EditText) rootView.findViewById(R.id.edt_phone);

        liner_add_new = (LinearLayout) rootView.findViewById(R.id.liner_add_new);
        login_liner = (LinearLayout) rootView.findViewById(R.id.login_liner);
        recyclerView_notes = (RecyclerView) rootView.findViewById(R.id.recyclerView_notes);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_notes.setLayoutManager(mLayoutManager);
        getAllUserList();

        liner_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_name.setText("");
                edt_phone.setText("");
                login_liner.setVisibility(View.VISIBLE);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateName()) {
                    return;
                }
                if (!validateMobileNo()) {
                    return;
                }
                CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
                jsonUploadUHID(input_name.getText().toString().trim(), edt_phone.getText().toString().trim());
            }
        });
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);
        return rootView;
    }

    public void getcheck() {
        getAllUserList();
    }


    private boolean validateName() {
        if (input_name.getText().toString().trim().isEmpty()) {
            input_name.setError("Please Enter Name");
            requestFocus(input_name);
            return false;
        } else {
            input_name.setError(null);
        }

        return true;
    }


    private boolean validateMobileNo() {
        String email = edt_phone.getText().toString().trim();

        if (email.isEmpty() || email.length() != 10) {
            if (email.length() < 10 && email.length() > 1) {
                edt_phone.setError("Mobile Number cannot be less than 10 numbers.");
            } else {
                edt_phone.setError("Mobile Number cannot be left blank.");
            }
            requestFocus(edt_phone);
            return false;

        } else {
            edt_phone.setError(null);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            CureFull.getInstanse().getActivityIsntanse().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public void showAdpter() {
        if (uhidItemses != null && uhidItemses.size() > 0) {
            recyclerView_notes.setVisibility(View.VISIBLE);
            uhid_listAdpter = new UHID_ListAdpter(FragmentUHID.this, CureFull.getInstanse().getActivityIsntanse(), uhidItemses);
            recyclerView_notes.setAdapter(uhid_listAdpter);
            uhid_listAdpter.notifyDataSetChanged();
        }
    }


    private void getAllUserList() {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.CfUuhidList,
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            uhidItemses = ParseJsonData.getInstance().getUHID(response);
                            showAdpter();
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


    public void jsonUploadUHID(String name, String mobileNumber) {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
        JSONObject data = JsonUtilsObject.toUHIDADD(name, mobileNumber);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.CfUuhidUpload, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            login_liner.setVisibility(View.GONE);
                            CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
                            getAllUserList();
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
