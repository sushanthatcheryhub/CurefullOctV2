package fragment.healthapp;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import item.property.SignUpInfo;
import item.property.UserInfo;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentSignUp extends Fragment implements View.OnClickListener {


    private View rootView;
    private TextView btn_signup;
    private EditText edtInput_name, edtInputEmail, edtInputPassword;
    private RequestQueue requestQueue;
    private boolean showPwd = false;
    private TextInputLayout input_layout_name, inputLayoutEmail, inputLayoutPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_signup,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
        CureFull.getInstanse().getActivityIsntanse().disableDrawer();
        input_layout_name = (TextInputLayout) rootView.findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) rootView.findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) rootView.findViewById(R.id.input_layout_password);
        edtInput_name = (EditText) rootView.findViewById(R.id.input_name);
        edtInputEmail = (EditText) rootView.findViewById(R.id.input_email);
        edtInputPassword = (EditText) rootView.findViewById(R.id.input_password);
        btn_signup = (TextView) rootView.findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(this);


        return rootView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                submitForm();
//                CureFull.getInstanse().getFlowInstanse()
//                        .replaceWithleftrightAnimation(new FragmentAuthorizationInformation(),null, true);
                break;
//            case R.id.btn_birthday:
//                input_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                final Calendar c1 = Calendar.getInstance();
//                final int year = c1.get(Calendar.YEAR);
//                final int month = c1.get(Calendar.MONTH);
//                final int day = c1.get(Calendar.DAY_OF_MONTH) + 1;
//
//                DatePickerDialog newDateDialog = new DatePickerDialog(CureFull.getInstanse().getActivityIsntanse(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, FragmentSignUp.this, year, month, day);
//                newDateDialog.getDatePicker().setCalendarViewShown(false);
////                c.add(Calendar.DATE, 1);
//                Date newDate = c1.getTime();
//                newDateDialog.getDatePicker().setMaxDate(newDate.getTime());
//                newDateDialog.show();
//                break;
        }
    }


//    @Override
//    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        int mnt = (monthOfYear + 1);
//        getActivity().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//        );
//        //txt_dob.setText("" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "/" + (mnt < 10 ? "0" + mnt : mnt) + "/" + year);
//        btn_birthday.setText("" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + (mnt < 10 ? "0" + mnt : mnt) + "-" + year + " (" + getAge(year, mnt, dayOfMonth) + " y" + ")");
//    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();
        if (ageS.equalsIgnoreCase("-1"))
            ageS = "0";

        return ageS;
    }

    private String getAgeMonth(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, day);
        int mnt = (today.get(Calendar.MONTH) - dob.get(Calendar.MONTH));

        if (today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
            mnt--;
        }
        Integer ageMnt = new Integer(mnt);
        String mthS = ageMnt.toString();
        int m;
        if (mthS.length() == 2) {
            Log.e("length", ":- " + mthS.length());
            String[] dateParts = mthS.split("-");
            m = (Integer.parseInt(dateParts[1]) + 1);

        } else {
            m = Integer.parseInt(mthS);
        }
        Log.e("mth", ":- " + m);

        return String.valueOf(m);
    }

    private boolean validateName() {
        String email = edtInput_name.getText().toString().trim();
        if (email.isEmpty()) {
            input_layout_name.setError("Email Id cannot be left blank.");
            requestFocus(edtInput_name);
            return false;
        } else {
            input_layout_name.setErrorEnabled(false);
        }
        return true;
    }


    private boolean validateEmail() {
        String email = edtInputEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError("Email Id cannot be left blank.");
            requestFocus(edtInputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }
        return true;
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

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            CureFull.getInstanse().getActivityIsntanse().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private void submitForm() {

        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            jsonLoginCheck();
        } else {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);

        }


    }


    public void jsonLoginCheck() {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
//        JSONObject data = JsonUtilsObject.toLogin("user.doctor1.fortise@hatcheryhub.com", "ashwani");
        JSONObject data = JsonUtilsObject.toSignUp(edtInput_name.getText().toString().trim(), edtInputEmail.getText().toString().trim(), edtInputPassword.getText().toString().trim());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.SIGN_UP, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                            }
                        } else {
                            Toast.makeText(CureFull.getInstanse().getActivityIsntanse(), "Invalid Details", Toast.LENGTH_SHORT).show();
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
}