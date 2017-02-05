package fragment.healthapp;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import item.property.UHIDItemsCheck;
import operations.DbOperations;
import utils.CheckNetworkState;
import utils.HandlePermission;
import utils.MyConstants;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentSignUp extends Fragment implements View.OnClickListener {


    private View rootView;
    private TextView btn_signup;
    private AutoCompleteTextView edtInputEmail;
    private EditText edtInput_name, edt_phone;
    private RequestQueue requestQueue;
    private boolean showPwd = false;
    private TextInputLayout input_layout_name, inputLayoutEmail;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
    private ArrayList<UHIDItemsCheck> uhidItemsChecks;
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
        edtInputEmail = (AutoCompleteTextView) rootView.findViewById(R.id.input_email);
        btn_signup = (TextView) rootView.findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(this);


        edtInputEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btn_signup.setEnabled(false);
                    submitForm();
                }
                return false;
            }
        });

        if (HandlePermission.checkPermissionSMS(CureFull.getInstanse().getActivityIsntanse())) {
            Cursor c = getActivity().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
            c.moveToFirst();
            TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            edtInput_name.setText("" + c.getString(c.getColumnIndex("display_name")));
            if (tMgr.getLine1Number() != null) {
                edt_phone.setText("" + tMgr.getLine1Number().replace("+91", ""));
            }
            addAdapterToViews();
//        edtInputEmail.setText("" + getAllAccount());
            c.close();
        }

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
            return false;
        } else {
        }
        return true;
    }


    private boolean validateEmail() {
        String email = edtInputEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            return false;
        }
        return true;
    }


    private boolean validateMobileNo() {
        String email = edt_phone.getText().toString().trim();

        if (email.isEmpty() || email.length() != 10) {
            if (email.length() < 10 && email.length() > 1) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Mobile Number cannot be less than 10 numbers.");
            } else {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Mobile Number cannot be left blank.");
            }
            requestFocus(edt_phone);
            return false;

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
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Name cannot be left blank.");
            return;
        }

        if (!validateEmail()) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Email Id cannot be left blank.");
            return;
        }


        if (!validateMobileNo()) {
            return;
        }

        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            jsonUHIDCheck();
//            sendOTPService();
        } else {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);

        }


    }

    private void sendOTPService() {
        Random rnd = new Random();
        final int n = 100000 + rnd.nextInt(900000);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
//        String firstName = "";
//        String LastName = "";
//        String[] name = edtInput_name.getText().toString().split(" ");
//        if (name.length > 1) {
//            firstName = name[0];
//            LastName = name[1];
//        } else {
//            firstName = name[0];
//        }
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.OTP_WEB_SERVICE + edt_phone.getText().toString().trim() + MyConstants.WebUrls.OTP_MESSAGE + "Dear%20User%20,%0AYour%20verification%20code%20is%20" + String.valueOf(n) + "%0AThanx%20for%20using%20Curefull.%20Stay%20Relief." + MyConstants.WebUrls.OTP_LAST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        btn_signup.setEnabled(true);
                        Log.e("getSymptomsList, URL 1.", response);
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Bundle bundle = new Bundle();
                        bundle.putString("NAME", edtInput_name.getText().toString().trim());
                        bundle.putString("EMAIL", edtInputEmail.getText().toString().trim());
                        bundle.putString("MOBILE", edt_phone.getText().toString().trim());
                        bundle.putInt("otp", n);
                        bundle.putString("UHID", "");
                        CureFull.getInstanse().getFlowInstanse()
                                .addWithBottomTopAnimation(new FragmentOTPCheck(), bundle, true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        btn_signup.setEnabled(true);
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        error.printStackTrace();
                    }
                }
        ) {
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }


    private void addAdapterToViews() {

        Account[] accounts = AccountManager.get(CureFull.getInstanse().getActivityIsntanse()).getAccounts();
        Set<String> emailSet = new HashSet<String>();
        for (Account account : accounts) {
            if (EMAIL_PATTERN.matcher(account.name).matches()) {
                emailSet.add(account.name);
            }
        }

        if (emailSet.size() == 1) {
            edtInputEmail.setText(new ArrayList<String>(emailSet).get(0).toString());
            edtInputEmail.setAdapter(new ArrayAdapter<String>(CureFull.getInstanse().getActivityIsntanse(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>(emailSet)));

        } else {
            List<String> emailSet1 = DbOperations.getEmailList(CureFull.getInstanse().getActivityIsntanse());
            edtInputEmail.setAdapter(new ArrayAdapter<String>(CureFull.getInstanse().getActivityIsntanse(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>(emailSet1)));
        }
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


    public void jsonUHIDCheck() {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
//        JSONObject data = JsonUtilsObject.toLogin("user.doctor1.fortise@hatcheryhub.com", "ashwani");
        JSONObject data = JsonUtilsObject.toUHID(edtInput_name.getText().toString().trim(), edt_phone.getText().toString().trim(), edtInputEmail.getText().toString().trim());
        Log.e("data", ":- " + data.toString() + ":- " + MyConstants.WebUrls.UHID_SIGN_UP);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.UHID_SIGN_UP, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        btn_signup.setEnabled(true);
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Log.e("FragmentLogin, URL 3.", response.toString());
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                String payload = json.getString("payload");
                                if (payload.equalsIgnoreCase("null")) {
                                    sendOTPService();
                                } else {
                                    Log.e("payload", "payload");
                                    uhidItemsChecks = ParseJsonData.getInstance().getUHIDCheck(response.toString());

                                    Bundle bundle = new Bundle();
                                    bundle.putString("NAME", edtInput_name.getText().toString().trim());
                                    bundle.putString("EMAIL", edtInputEmail.getText().toString().trim());
                                    bundle.putString("MOBILE", edt_phone.getText().toString().trim());
                                    bundle.putParcelableArrayList("UHID", uhidItemsChecks);
                                    CureFull.getInstanse().getFlowInstanse()
                                            .addWithBottomTopAnimation(new FragmentUHIDSignUp(), bundle, true);
                                }
                            } else {
                                btn_signup.setEnabled(true);
                                try {
                                    JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                    JSONObject json12 = new JSONObject(json1.getString("errorDetails"));
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + json12.getString("message"));
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
                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
                VolleyLog.e("FragmentLogin, URL 3.", "Error: " + error.getMessage());
            }
        }) {


        };
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e("signup", "signup");
        switch (requestCode) {
            case HandlePermission.MY_PERMISSIONS_REQUEST_READ_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Cursor c = getActivity().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
                    c.moveToFirst();
                    TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                    edtInput_name.setText("" + c.getString(c.getColumnIndex("display_name")));
                    if (tMgr.getLine1Number() != null) {
                        edt_phone.setText("" + tMgr.getLine1Number().replace("+91", ""));
                    }
                    addAdapterToViews();
//        edtInputEmail.setText("" + getAllAccount());
                    c.close();
                } else {
                    //code for deny
                }
                break;
        }
    }


}