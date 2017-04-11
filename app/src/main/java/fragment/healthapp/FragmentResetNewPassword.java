package fragment.healthapp;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ElasticVIews.ElasticAction;
import asyns.JsonUtilsObject;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import utils.MyConstants;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentResetNewPassword extends Fragment {


    private View rootView;
    private TextInputLayout input_layout_mobile, input_layout_email;
    private EditText input_mobile_number, input_confirm_password, input_email;
    private TextView btn_reset_password;
    private String emailCheck = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_reset_new_password,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
        CureFull.getInstanse().getActivityIsntanse().disableDrawer();
       // CureFull.getInstanse().getActivityIsntanse().showLogo(true);
        input_layout_mobile = (TextInputLayout) rootView.findViewById(R.id.input_layout_mobile);
        input_layout_email = (TextInputLayout) rootView.findViewById(R.id.input_layout_email);
        input_email = (EditText) rootView.findViewById(R.id.input_email);
        input_confirm_password = (EditText) rootView.findViewById(R.id.input_confirm_password);
        input_mobile_number = (EditText) rootView.findViewById(R.id.input_mobile_number);
        btn_reset_password = (TextView) rootView.findViewById(R.id.btn_reset_password);

        Bundle bundle = getArguments();
        if (bundle != null) {
            emailCheck = bundle.getString("email");
        }
        btn_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                if (emailCheck.equalsIgnoreCase("yes")) {
                    if (isValidEmail(input_email.getText().toString().trim())) {
                        if (!validatePassword()) {
                            return;
                        }
                        jsonForgotCheck(input_email.getText().toString().trim());

                    } else {
                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Email Address not valid");
                    }
                } else {
                    if (!validateMobileNo()) {
                        return;
                    }
                    if (!validatePassword()) {
                        return;
                    }
                    jsonForgotCheck(input_confirm_password.getText().toString().trim());
                }


            }
        });


        return rootView;
    }

    private boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }


    private boolean validateMobileNo() {
        String email = input_mobile_number.getText().toString().trim();

        if (email.isEmpty() || email.length() != 10) {
            if (email.length() < 10 && email.length() > 1) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Mobile Number cannot be less than 10 numbers.");
            } else {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Mobile Number cannot be left blank.");
            }
            return false;

        }
        return true;
    }

    private boolean validatePassword() {
        if (input_confirm_password.getText().toString().trim().isEmpty()) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Enter Password");
            return false;
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            CureFull.getInstanse().getActivityIsntanse().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void jsonForgotCheck(String userId) {
        JSONObject data = null;
        if (emailCheck.equalsIgnoreCase("yes")) {
            data = JsonUtilsObject.toForgotPasswordEmail(userId, input_confirm_password.getText().toString().trim());
        } else {
            data = JsonUtilsObject.toForgotPassword(userId, input_confirm_password.getText().toString().trim());
        }
//        Log.e("data", ":- " + data.toString());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.FORGOT_SEND, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
//                        Log.e("FragmentLogin, URL 3.", response.toString());
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
//                VolleyLog.e("FragmentLogin, URL 3.", "Error: " + error.getMessage());
            }
        }) {


        };
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }


}