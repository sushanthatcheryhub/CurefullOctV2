package fragment.healthapp;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.ParseError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import ElasticVIews.ElasticAction;
import asyns.JsonUtilsObject;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import interfaces.SmsListener;
import utils.CheckNetworkState;
import utils.IncomingSms;
import utils.MyConstants;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentOTPCheckForgot extends AppCompatActivity implements View.OnClickListener {


    //private View rootView;
    private TextView btn_done, btn_click_resend_otp;
    private EditText edt_otp_password, edtInputPassword, edt_confirm_password;
    private int OTP;
    private String health_mobile, health_password;
    private TextInputLayout input_layout_otp, inputLayoutPassword, input_layout_confirm_password;
    private boolean showPwd = false, isResendPassword = false, isCancel = false;
    private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_otp_check);
            CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
            CureFull.getInstanse().getActivityIsntanse().disableDrawer();
            inputLayoutPassword = (TextInputLayout)findViewById(R.id.input_layout_password);
            input_layout_confirm_password = (TextInputLayout)findViewById(R.id.input_layout_confirm_password);
            edt_otp_password = (EditText)findViewById(R.id.edt_otp_password);
            edt_confirm_password = (EditText)findViewById(R.id.edt_confirm_password);
            coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);
            btn_click_resend_otp = (TextView)findViewById(R.id.btn_click_resend_otp);
            btn_done = (TextView)findViewById(R.id.btn_done);
            btn_done.setOnClickListener(this);
            btn_click_resend_otp.setOnClickListener(this);
            edtInputPassword = (EditText)findViewById(R.id.edt_password);
            //Bundle bundle = getArguments();
            if (getIntent().getExtras() != null) {
                health_mobile = getIntent().getExtras().getString("MOBILE");
                OTP = getIntent().getExtras().getInt("otp");
//            edt_otp_password.setText("" + OTP);
            }

//        btn_click_resend_otp.setText("" + OTP);
            btn_click_resend_otp.setPaintFlags(btn_click_resend_otp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


            edtInputPassword.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (edtInputPassword.getRight() - edtInputPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            if (edtInputPassword.getText().toString().length() > 0) {
                                if (!showPwd) {
                                    showPwd = true;
                                    edtInputPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                                    edtInputPassword.setSelection(edtInputPassword.getText().length());
                                    edtInputPassword.setTextSize(14f);
                                    edtInputPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon, 0, R.drawable.password_visible, 0);
                                } else {
                                    showPwd = false;
                                    edtInputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                    edtInputPassword.setSelection(edtInputPassword.getText().length());
                                    edtInputPassword.setTextSize(14f);
                                    edtInputPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon, 0, R.drawable.password_hide, 0);

                                    //confirmPassImage.setImageResource(R.drawable.username);//change Image here
                                }
                            }
                            return true;
                        }
                    }
                    return false;
                }
            });

            IncomingSms.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String messageText) {
//                Log.e("message", ": " + messageText);
                    isCancel = true;
                    edt_otp_password.setText("");
                    String mgs = messageText.replace("Dear User ,\n" + "Your verification code is ", "");
                    String again = mgs.replace("\nThanx for using Curefull. Stay Relief.", "");
                    edt_otp_password.setText("" + again);
                }
            });

            edt_confirm_password.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (edt_confirm_password.getRight() - edt_confirm_password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            if (edt_confirm_password.getText().toString().length() > 0) {
                                if (!showPwd) {
                                    showPwd = true;
                                    edt_confirm_password.setInputType(InputType.TYPE_CLASS_TEXT);
                                    edt_confirm_password.setSelection(edt_confirm_password.getText().length());
                                    edt_confirm_password.setTextSize(14f);
                                    edt_confirm_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon, 0, R.drawable.password_visible, 0);
                                } else {
                                    showPwd = false;
                                    edt_confirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                    edt_confirm_password.setSelection(edt_confirm_password.getText().length());
                                    edt_confirm_password.setTextSize(14f);
                                    edt_confirm_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon, 0, R.drawable.password_hide, 0);

                                    //confirmPassImage.setImageResource(R.drawable.username);//change Image here
                                }
                            }
                            return true;
                        }
                    }
                    return false;
                }
            });

            edtInputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        edt_confirm_password.requestFocus();
                    }
                    return false;
                }
            });

            edt_confirm_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        btn_done.setEnabled(false);
                        submitForm();
                    }
                    return false;
                }
            });

            new CountDownTimer(30000, 1000) {

                public void onTick(long millisUntilFinished) {
                    if (isCancel) {
                        onFinish();
                        cancel();
                    } else {
                        isResendPassword = true;
                        btn_click_resend_otp.setText("00:" + millisUntilFinished / 1000);
                    }
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
//                Log.e("k", "k");
                    isResendPassword = false;
                    btn_click_resend_otp.setText("Click here to resend OTP");
                    isCancel = false;
                }

            }.start();



    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_done:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                submitForm();
                break;
            case R.id.btn_click_resend_otp:
                if (isResendPassword) {

                } else {
                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
                    sendOTPService();
                    new CountDownTimer(30000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            if (isCancel) {
                                onFinish();
                                cancel();
                            } else {
                                isResendPassword = true;
                                btn_click_resend_otp.setText("00:" + millisUntilFinished / 1000);
                            }
                            //here you can have your logic to set text to edittext
                        }

                        public void onFinish() {
                            isResendPassword = false;
                            btn_click_resend_otp.setText("Click here to resend OTP");
                            isCancel = false;
                        }

                    }.start();
                }

                break;
        }
    }

    private boolean validatePassword() {
        if (edtInputPassword.getText().toString().trim().isEmpty()) {
            edtInputPassword.setError("Please Enter Password");
            requestFocus(edtInputPassword);
            return false;
        } else {
            edtInputPassword.setError(null);
        }

        return true;
    }


    private boolean validatePasswordConfirm() {
        if (edt_confirm_password.getText().toString().trim().isEmpty()) {
            edt_confirm_password.setError("Please Enter Confirm Password");
            requestFocus(edt_confirm_password);
            return false;
        } else if (!edt_confirm_password.getText().toString().trim().equalsIgnoreCase(edtInputPassword.getText().toString().trim())) {
            edt_confirm_password.setError("Password Not Match");
            requestFocus(edt_confirm_password);
            return false;
        } else {
            edt_confirm_password.setError(null);
        }

        return true;
    }

    private boolean validateOTP() {
        String email = edt_otp_password.getText().toString().trim();
        if (email.isEmpty()) {
            edt_otp_password.setError("Otp cannot be left blank.");
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
        if (!validatePasswordConfirm()) {
            return;
        }
        if (OTP == Integer.parseInt(edt_otp_password.getText().toString().trim())) {
            if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
                jsonLoginCheck();
            } else {
                btn_done.setEnabled(true);
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, MyConstants.CustomMessages.No_INTERNET_USAGE);

            }
        } else {
            btn_done.setEnabled(true);
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, "Invalid OTP Please check again");

        }


    }

    private void sendOTPService() {
//        Log.e("mob", " " + health_mobile + " " + OTP);
        Random rnd = new Random();
        final int n = 100000 + rnd.nextInt(900000);
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.OTP_WEB_SERVICE + health_mobile + MyConstants.WebUrls.OTP_MESSAGE + "Dear%20User%20,%0AYour%20verification%20code%20is%20" + String.valueOf(n) + "%0AThanx%20for%20using%20Curefull.%20Stay%20Relief." + MyConstants.WebUrls.OTP_LAST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        OTP = n;
//                        Log.e("response", "" + response);
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
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
//        JSONObject data = JsonUtilsObject.toLogin("user.doctor1.fortise@hatcheryhub.com", "ashwani");
        JSONObject data = JsonUtilsObject.toForgotPassword(health_mobile, edtInputPassword.getText().toString().trim());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.FORGOT_SEND, data,
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

                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                if (json.getBoolean("payload")) {
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, "Password Changed Successfully");
                                    CureFull.getInstanse().getActivityIsntanse().startActivity(new Intent(CureFull.getInstanse().getActivityIsntanse(), FragmentLogin.class));
                                    finish();
                                }
                            } else if (responseStatus == 101) {
                                try {
                                    JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                    JSONObject json12 = new JSONObject(json1.getString("errorDetails"));
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, "" + json12.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                btn_done.setEnabled(true);
                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
//                VolleyLog.e("FragmentLogin, URL 3.", "Error: " + error.getMessage());
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


        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }

    private String getAllAccount() {
        AccountManager am = AccountManager.get(FragmentOTPCheckForgot.this);
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
//            Log.i("Accounts : ", "Accounts : " + acname);
        }

        return email;
    }
}