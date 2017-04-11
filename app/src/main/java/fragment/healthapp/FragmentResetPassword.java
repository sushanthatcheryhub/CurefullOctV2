package fragment.healthapp;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import ElasticVIews.ElasticAction;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import utils.HandlePermission;
import utils.MyConstants;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentResetPassword extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    //private View rootView;
    private EditText input_mobile_number;
    private TextView btn_reset_password;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reset_password);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
        CureFull.getInstanse().getActivityIsntanse().disableDrawer();
        //CureFull.getInstanse().getActivityIsntanse().showLogo(true);  by sourav
        input_mobile_number = (EditText) findViewById(R.id.input_mobile_number);
        btn_reset_password = (TextView) findViewById(R.id.btn_reset_password);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        btn_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
//                if (!isValidEmail(input_mobile_number.getText().toString().trim()) || !isValidPhoneNumber(input_mobile_number.getText().toString().trim())) {
//                    return;
//                }

//                if (isValidEmail(input_mobile_number.getText().toString().trim())) {
//                    checkEmailId();
//
//                } else

                if (!validateMobileNo()) {
                    return;
                }
//                    if (isValidPhoneNumber(input_mobile_number.getText().toString().trim())) {
//                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
//
//                } else {
//                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Invalid Enter");
//                }
                CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
                checkMoileNumber();

            }
        });
        if (HandlePermission.checkPermissionSMS(this)) {

        }
        input_mobile_number.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!validateMobileNo()) {
                        return false;
                    } else {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
                        btn_reset_password.setEnabled(false);
                        checkMoileNumber();
                    }

                }
                return false;
            }
        });

    }


    private void checkEmailId() {
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.CHECK_EMAIL_VALID + input_mobile_number.getText().toString().trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("getSymptomsList, URL 1.", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("responseStatus").equalsIgnoreCase("100")) {
                                if (jsonObject.getBoolean("payload")) {
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, "Email sent please your email id.");
                                    Bundle bundle = new Bundle();
                                    bundle.putString("email", "yes");
                                    CureFull.getInstanse().getFlowInstanse()
                                            .addWithBottomTopAnimation(new FragmentResetNewPassword(), bundle, true);
                                } else {
                                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, "Email Id does not exist");
                                }
                            } else {
                                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, "Email Id does not exist");
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


    private void checkMoileNumber() {
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.CHECK_MOBILE_VALID + input_mobile_number.getText().toString().trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("getSymptomsList, URL 1.", response);
                        btn_reset_password.setEnabled(true);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("responseStatus").equalsIgnoreCase("100")) {
                                if (jsonObject.getBoolean("payload")) {
                                    sendOTPService();
                                } else {
                                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, "Mobile number does not exist");
                                }
                            } else {
                                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, "Mobile number does not exist");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        btn_reset_password.setEnabled(true);
                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
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
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.OTP_WEB_SERVICE + input_mobile_number.getText().toString().trim() + MyConstants.WebUrls.OTP_MESSAGE + "Dear%20User%20,%0AYour%20verification%20code%20is%20" + String.valueOf(n) + "%0AThanx%20for%20using%20Curefull.%20Stay%20Relief." + MyConstants.WebUrls.OTP_LAST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("getSymptomsList, URL 1.", response);
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        /*Bundle bundle = new Bundle();
                        bundle.putInt("otp", n);
                        bundle.putString("MOBILE", input_mobile_number.getText().toString().trim());
                        CureFull.getInstanse().getFlowInstanse()
                                .addWithBottomTopAnimation(new FragmentOTPCheckForgot(), bundle, true);*/
                        Intent intent_otpchkforgot = new Intent(FragmentResetPassword.this, FragmentOTPCheckForgot.class);
                        intent_otpchkforgot.putExtra("otp", n);
                        intent_otpchkforgot.putExtra("MOBILE", input_mobile_number.getText().toString().trim());
                        startActivity(intent_otpchkforgot);
                        finish();
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

    private boolean validateMobileNo() {
        String email = input_mobile_number.getText().toString().trim();

        if (email.isEmpty() || email.length() != 10) {
            if (email.length() < 10 && email.length() > 1) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, "Mobile Number cannot be less than 10 numbers.");
            } else {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, "Mobile Number cannot be left blank.");
            }
            return false;

        }
        return true;
    }

}