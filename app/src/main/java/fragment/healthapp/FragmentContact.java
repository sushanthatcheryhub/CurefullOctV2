package fragment.healthapp;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import ElasticVIews.ElasticAction;
import asyns.JsonUtilsObject;
import curefull.healthapp.BaseBackHandlerFragment;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentContact extends BaseBackHandlerFragment implements View.OnClickListener {


    private View rootView;
    private TextView btn_done;
    private EditText edt_deatils, edt_subject;
    private RequestQueue requestQueue;

//    @Override
//    public boolean onBackPressed() {
//        CureFull.getInstanse().cancel();
//        CureFull.getInstanse().getFlowInstanse()
//                .replace(new FragmentLandingPage(), false);
//        return super.onBackPressed();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_contact,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().setshareVisibilty(false);
        CureFull.getInstanse().getActivityIsntanse().selectedNav(4);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);
        CureFull.getInstanse().getActivityIsntanse().isbackButtonVisible(true, "");
        CureFull.getInstanse().getActivityIsntanse().isTobBarButtonVisible(true, "");
        edt_deatils = (EditText) rootView.findViewById(R.id.edt_deatils);
        edt_subject = (EditText) rootView.findViewById(R.id.edt_subject);
        btn_done = (TextView) rootView.findViewById(R.id.btn_done);
        btn_done.setOnClickListener(this);




        edt_subject.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edt_deatils.requestFocus();
                }
                return false;
            }
        });
        return rootView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_done:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                if (!validateSubject()) {
                    return;
                }
                if (!validateDeatils()) {
                    return;
                }
                CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
                btn_done.setEnabled(false);
                jsonCotactCheck();

                break;
        }
    }


    private boolean validateSubject() {
        String email = edt_subject.getText().toString().trim();
        if (email.isEmpty()) {
            edt_subject.setError("Subject cannot be left blank.");
            requestFocus(edt_subject);
            return false;
        } else {
            edt_subject.setError(null);
        }
        return true;
    }

    private boolean validateDeatils() {
        String email = edt_deatils.getText().toString().trim();
        if (email.isEmpty()) {
            edt_deatils.setError("Deatils cannot be left blank.");
            requestFocus(edt_deatils);
            return false;
        } else {
            edt_deatils.setError(null);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            CureFull.getInstanse().getActivityIsntanse().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public void jsonCotactCheck() {
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
            }
            JSONObject data = JsonUtilsObject.toCotact(edt_subject.getText().toString().trim(), edt_deatils.getText().toString().trim());


            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.GET_CONTACT, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            btn_done.setEnabled(true);
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
                                edt_subject.setText("");
                                edt_deatils.setText("");
                                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "We love questions! will reach you shortly.");
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
                    btn_done.setEnabled(true);
                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
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
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);
        }

    }
}