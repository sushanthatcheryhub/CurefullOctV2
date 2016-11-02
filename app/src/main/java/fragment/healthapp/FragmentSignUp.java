package fragment.healthapp;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.database.Cursor;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.Random;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import utils.CheckNetworkState;
import utils.MyConstants;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentSignUp extends Fragment implements View.OnClickListener {


    private View rootView;
    private TextView btn_signup;
    private EditText edtInput_name, edtInputEmail, edt_phone;
    private RequestQueue requestQueue;
    private boolean showPwd = false;
    private TextInputLayout input_layout_name, inputLayoutEmail;

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
        edt_phone = (EditText) rootView.findViewById(R.id.edt_phone);
        edtInput_name = (EditText) rootView.findViewById(R.id.input_name);
        edtInputEmail = (EditText) rootView.findViewById(R.id.input_email);

        btn_signup = (TextView) rootView.findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(this);
        Cursor c = getActivity().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
        c.moveToFirst();
        TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        edtInput_name.setText("" + c.getString(c.getColumnIndex("display_name")));
        edt_phone.setText("" + tMgr.getLine1Number().replace("+91",""));
        edtInputEmail.setText("" + getAllAccount());
        c.close();
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
            input_layout_name.setError("Name cannot be left blank.");
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


        if (!validateMobileNo()) {
            return;
        }

        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            sendOTPService();
        } else {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);

        }


    }

    private void sendOTPService() {
        Random rnd = new Random();
        final int n = 100000 + rnd.nextInt(900000);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.OTP_WEB_SERVICE + edt_phone.getText().toString().trim() + MyConstants.WebUrls.OTP_MESSAGE + "CureFull" + n + MyConstants.WebUrls.OTP_LAST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getSymptomsList, URL 1.", response);
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Bundle bundle = new Bundle();
                        bundle.putString("NAME", edtInput_name.getText().toString().trim());
                        bundle.putString("EMAIL", edtInputEmail.getText().toString().trim());
                        bundle.putString("MOBILE", edt_phone.getText().toString().trim());
                        bundle.putInt("otp", n);
                        CureFull.getInstanse().getFlowInstanse()
                                .addWithBottomTopAnimation(new FragmentOTPCheck(), bundle, true);
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


//    public void jsonLoginCheck() {
//        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
////        JSONObject data = JsonUtilsObject.toLogin("user.doctor1.fortise@hatcheryhub.com", "ashwani");
//        JSONObject data = JsonUtilsObject.toSignUp(edtInput_name.getText().toString().trim(), edtInputEmail.getText().toString().trim(), edtInputPassword.getText().toString().trim(), edt_phone.getText().toString().trim());
//        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.SIGN_UP, data,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.e("FragmentLogin, URL 3.", response.toString());
//                        int responseStatus = 0;
//                        JSONObject json = null;
//                        try {
//                            json = new JSONObject(response.toString());
//                            responseStatus = json.getInt("responseStatus");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        if (responseStatus == 100) {
//                            CureFull.getInstanse().getFlowInstanse().clearBackStack();
//                            CureFull.getInstanse().getFlowInstanse()
//                                    .replace(new FragmentHomeScreenAll(), false);
//
////                            SignUpInfo userInfo = ParseJsonData.getInstance().getSignUpData(response.toString());
////                            if (ParseJsonData.getInstance().getHttp_code().equalsIgnoreCase(MyConstants.JsonUtils.OK)) {
////                                AppPreference.getInstance().setUserName(userInfo.getUser_name());
////                                AppPreference.getInstance().setUserID(userInfo.getUser_id());
////
////                                Log.e("name", " " + userInfo.getUser_name());
////                            }
//                        } else {
//                            Toast.makeText(CureFull.getInstanse().getActivityIsntanse(), "Invalid Details", Toast.LENGTH_SHORT).show();
//                        }
//
//
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
//                VolleyLog.e("FragmentLogin, URL 3.", "Error: " + error.getMessage());
//            }
//        }) {
//
//
//            @Override
//            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//                try {
//                    String jsonString = new String(response.data,
//                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
////                    Log.e("headers", "" +  response.headers.get("a_t"));
//                    JSONObject jsonResponse = new JSONObject(jsonString);
//                    jsonResponse.put(MyConstants.PrefrenceKeys.HEADERS, new JSONObject(response.headers));
//                    return Response.success(jsonResponse,
//                            HttpHeaderParser.parseCacheHeaders(response));
//                } catch (UnsupportedEncodingException e) {
//                    return Response.error(new ParseError(e));
//                } catch (JSONException je) {
//                    return Response.error(new ParseError(je));
//                }
//            }
//
//
//        };
//        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
//    }

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