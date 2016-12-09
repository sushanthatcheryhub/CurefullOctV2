package fragment.healthapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import item.property.UserInfo;
import utils.AppPreference;
import utils.MyConstants;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentResetNewPassword extends Fragment {


    private View rootView;
    private EditText input_mobile_number, input_confirm_password;
    private RequestQueue requestQueue;
    private TextView btn_reset_password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_reset_new_password,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
        CureFull.getInstanse().getActivityIsntanse().disableDrawer();
        CureFull.getInstanse().getActivityIsntanse().showLogo(true);
        input_confirm_password = (EditText) rootView.findViewById(R.id.input_confirm_password);
        input_mobile_number = (EditText) rootView.findViewById(R.id.input_mobile_number);
        btn_reset_password = (TextView) rootView.findViewById(R.id.btn_reset_password);


        btn_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateMobileNo()) {
                    return;
                }
                if (!validatePassword()) {
                    return;
                }
                jsonForgotCheck();
            }
        });


        return rootView;
    }

    private boolean validateMobileNo() {
        String email = input_mobile_number.getText().toString().trim();

        if (email.isEmpty() || email.length() != 10) {
            if (email.length() < 10 && email.length() > 1) {
                input_mobile_number.setError("Mobile Number cannot be less than 10 numbers.");
            } else {
                input_mobile_number.setError("Mobile Number cannot be left blank.");
            }
            requestFocus(input_mobile_number);
            return false;

        } else {
            input_mobile_number.setError(null);
        }
        return true;
    }

    private boolean validatePassword() {
        if (input_confirm_password.getText().toString().trim().isEmpty()) {
            input_confirm_password.setError("Please Enter Password");
            requestFocus(input_confirm_password);
            return false;
        } else {
            input_confirm_password.setError(null);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            CureFull.getInstanse().getActivityIsntanse().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void jsonForgotCheck() {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
        JSONObject data = JsonUtilsObject.toForgotPassword(input_mobile_number.getText().toString().trim(), input_confirm_password.getText().toString().trim());
        Log.e("data", ":- " + data.toString());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.FORGOT_NEW, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Log.e("FragmentLogin, URL 3.", response.toString());
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            CureFull.getInstanse().getFlowInstanse()
                                    .replace(new FragmentResetNewPassword(), false);
                        } else {
                            Toast.makeText(CureFull.getInstanse().getActivityIsntanse(), "Invalid Details", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
                VolleyLog.e("FragmentLogin, URL 3.", "Error: " + error.getMessage());
            }
        }) {


        };
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }


}