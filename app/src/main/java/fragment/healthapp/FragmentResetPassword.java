package fragment.healthapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import utils.MyConstants;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentResetPassword extends Fragment {


    private View rootView;
    private EditText input_mobile_number;
    private RequestQueue requestQueue;
    private TextView btn_reset_password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_reset_password,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
        CureFull.getInstanse().getActivityIsntanse().disableDrawer();
        CureFull.getInstanse().getActivityIsntanse().showLogo(true);
        input_mobile_number = (EditText) rootView.findViewById(R.id.input_mobile_number);
        btn_reset_password = (TextView) rootView.findViewById(R.id.btn_reset_password);


        btn_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!isValidEmail(input_mobile_number.getText().toString().trim()) || !isValidPhoneNumber(input_mobile_number.getText().toString().trim())) {
//                    return;
//                }

                if (isValidEmail(input_mobile_number.getText().toString().trim())) {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Email Address Check");
                } else if (isValidPhoneNumber(input_mobile_number.getText().toString().trim())) {
                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
                    checkMoileNumber();

                } else {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Invalid Enter");
                }


            }
        });


        return rootView;
    }





    private void checkMoileNumber() {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.CHECK_MOBILE_VALID + input_mobile_number.getText().toString().trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getSymptomsList, URL 1.", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("responseStatus").equalsIgnoreCase("100")){
                                if (jsonObject.getBoolean("payload")) {
                                    sendOTPService();
                                } else {
                                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Mobile number does not exist");
                                }
                            }else{
                                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Mobile number does not exist");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
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
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }


    private void sendOTPService() {
        Random rnd = new Random();
        final int n = 100000 + rnd.nextInt(900000);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.OTP_WEB_SERVICE + input_mobile_number.getText().toString().trim() + MyConstants.WebUrls.OTP_MESSAGE + "CureFull" + n + MyConstants.WebUrls.OTP_LAST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getSymptomsList, URL 1.", response);
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Bundle bundle = new Bundle();
                        bundle.putString("mobile", input_mobile_number.getText().toString().trim());
                        CureFull.getInstanse().getFlowInstanse()
                                .addWithBottomTopAnimation(new FragmentResetPasswordResend(), bundle, true);
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
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }


    private boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }

}