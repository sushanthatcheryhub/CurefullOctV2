package fragment.healthapp;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
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
import java.util.Calendar;

import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import item.property.SignUpInfo;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentOTPCheck extends Fragment implements View.OnClickListener {


    private View rootView;
    private TextView btn_done, btn_click_resend_otp;
    private EditText edt_otp_password, edtInputPassword;
    private RequestQueue requestQueue;
    private int OTP;
    private String health_name, health_email, health_mobile, health_password;
    private TextInputLayout input_layout_otp, inputLayoutPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_otp_check,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
        CureFull.getInstanse().getActivityIsntanse().disableDrawer();
        inputLayoutPassword = (TextInputLayout) rootView.findViewById(R.id.input_layout_password);
        edt_otp_password = (EditText) rootView.findViewById(R.id.edt_otp_password);
        btn_click_resend_otp = (TextView) rootView.findViewById(R.id.btn_click_resend_otp);
        btn_done = (TextView) rootView.findViewById(R.id.btn_done);
        btn_done.setOnClickListener(this);
        btn_click_resend_otp.setOnClickListener(this);
        edtInputPassword = (EditText) rootView.findViewById(R.id.input_password);
        Bundle bundle = getArguments();
        if (bundle != null) {
            health_name = bundle.getString("NAME");
            health_email = bundle.getString("EMAIL");
            health_mobile = bundle.getString("MOBILE");
            OTP = bundle.getInt("otp");
        }
        btn_click_resend_otp.setPaintFlags(btn_click_resend_otp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        return rootView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_done:
                submitForm();
                break;
            case R.id.btn_click_resend_otp:
                sendOTPService();
                break;
        }
    }

    private boolean validatePassword() {
        if (edtInputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError("Please Enter Password");
            requestFocus(edtInputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateOTP() {
        String email = edt_otp_password.getText().toString().trim();
        if (email.isEmpty()) {
            edt_otp_password.setError("Name cannot be left blank.");
            requestFocus(edt_otp_password);
            return false;
        } else {
            edt_otp_password.setError(null);
        }
        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            CureFull.getInstanse().getActivityIsntanse().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private void submitForm() {

        if (!validateOTP()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        if (OTP == Integer.parseInt(edt_otp_password.getText().toString().trim())) {
            if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
                jsonLoginCheck();
            } else {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);

            }
        } else {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Invalid OTP Please check again");

        }


    }

    private void sendOTPService() {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.OTP_WEB_SERVICE + health_mobile + MyConstants.WebUrls.OTP_MESSAGE + "otp" + OTP + MyConstants.WebUrls.OTP_LAST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Log.e("getSymptomsList, URL 1.", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }


    public void jsonLoginCheck() {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
//        JSONObject data = JsonUtilsObject.toLogin("user.doctor1.fortise@hatcheryhub.com", "ashwani");
        JSONObject data = JsonUtilsObject.toSignUp(health_name, health_email, edt_otp_password.getText().toString().trim(), health_mobile);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.SIGN_UP, data,
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
                        if (responseStatus == 100) {

                            SignUpInfo userInfo = ParseJsonData.getInstance().getSignUpData(response.toString());
                            if (ParseJsonData.getInstance().getHttp_code().equalsIgnoreCase(MyConstants.JsonUtils.OK)) {
                                AppPreference.getInstance().setUserName(userInfo.getUser_name());
                                AppPreference.getInstance().setUserID(userInfo.getUser_id());
                                CureFull.getInstanse().getActivityIsntanse().setActionDrawerHeading(userInfo.getUser_name(), userInfo.getUser_id());
                                AppPreference.getInstance().setAt(userInfo.getA_t());
                                AppPreference.getInstance().setRt(userInfo.getR_t());
                                Log.e("name", " " + userInfo.getA_t());
                                CureFull.getInstanse().getFlowInstanse().clearBackStack();
                                CureFull.getInstanse().getFlowInstanse()
                                        .replace(new FragmentHomeScreenAll(), false);
                            }
                        } else if (responseStatus == 101) {
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
                VolleyLog.e("FragmentLogin, URL 3.", "Error: " + error.getMessage());
            }
        }) {


            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
//                    Log.e("headers", "" +  response.headers.get("a_t"));
                    JSONObject jsonResponse = new JSONObject(jsonString);
                    jsonResponse.put(MyConstants.PrefrenceKeys.HEADERS, new JSONObject(response.headers));
                    return Response.success(jsonResponse,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }


        };
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }

    private String getAllAccount() {
        AccountManager am = AccountManager.get(getActivity());
        Account[] accounts = am.getAccounts();
        String acname = "";
        String mobile_no = "";
        String email = "";

        for (Account ac : accounts) {
            acname = ac.name;

//            if (acname.startsWith("91")) {
//                mobile_no = acname;
//            } else


            if (acname.endsWith("@gmail.com") || acname.endsWith("@yahoo.com") || acname.endsWith("@hotmail.com")) {
                email = acname;
            }

            // Take your time to look at all available accounts
            Log.i("Accounts : ", "Accounts : " + acname);
        }

        return email;
    }
}