package fragment.healthapp;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import ElasticVIews.ElasticAction;
import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.BaseBackHandlerFragment;
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
public class FragmentSignUp extends AppCompatActivity implements View.OnClickListener {


    //private View rootView;
    private CoordinatorLayout coordinatorLayout;
    private TextView btn_signup;
    private AutoCompleteTextView edtInputEmail;
    private EditText edtInput_name, edt_phone;
    private boolean showPwd = false;
    private TextInputLayout input_layout_name, inputLayoutEmail;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
    private ArrayList<UHIDItemsCheck> uhidItemsChecks;
    private ProgressBar progress_bar;
/*
    @Override
    public boolean onBackPressed() {
        edtInputEmail.setFocusableInTouchMode(true);
        edtInputEmail.setFocusable(true);


        return super.onBackPressed();
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CureFull.getInstanse().getActivityIsntanse().startActivity(new Intent(this, FragmentLogin.class));
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signup);

            CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
            CureFull.getInstanse().getActivityIsntanse().disableDrawer();
            input_layout_name = (TextInputLayout)findViewById(R.id.input_layout_name);
            inputLayoutEmail = (TextInputLayout)findViewById(R.id.input_layout_email);
            edt_phone = (EditText)findViewById(R.id.edt_phone);
            edtInput_name = (EditText)findViewById(R.id.input_name);
            edtInputEmail = (AutoCompleteTextView)findViewById(R.id.input_email);
            coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);
            btn_signup = (TextView)findViewById(R.id.btn_signup);
            progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (HandlePermission.checkPermissionSMS(this)) {
                    try {
                        Cursor c = getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
                        if (c != null) {
                            c.moveToFirst();
                            TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                            edtInput_name.setText("" + c.getString(c.getColumnIndex("display_name")));
                            if (tMgr.getLine1Number() != null) {
                                edt_phone.setText("" + tMgr.getLine1Number().replace("+91", ""));
                            }
                            addAdapterToViews();
//        edtInputEmail.setText("" + getAllAccount());
                            c.close();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                btn_signup.setEnabled(false);
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
            String[] dateParts = mthS.split("-");
            m = (Integer.parseInt(dateParts[1]) + 1);

        } else {
            m = Integer.parseInt(mthS);
        }
//        Log.e("mth", ":- " + m);

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
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, "Mobile Number cannot be less than 10 numbers.");
            } else {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, "Mobile Number cannot be left blank.");
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
            btn_signup.setEnabled(true);
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, "Name cannot be left blank.");
            return;
        }

        if (!validateEmail()) {
            btn_signup.setEnabled(true);
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, "Email Id cannot be left blank.");
            return;
        }


        if (!validateMobileNo()) {
            btn_signup.setEnabled(true);
            return;
        }

        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            //CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            progress_bar.setVisibility(View.VISIBLE);
            jsonUHIDCheck();
//            sendOTPService();
        } else {
            btn_signup.setEnabled(true);
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, MyConstants.CustomMessages.No_INTERNET_USAGE);

        }


    }

    private void sendOTPService() {
        Random rnd = new Random();
        final int n = 100000 + rnd.nextInt(900000);
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
//                        Log.e("otp"," "+n);
                        btn_signup.setEnabled(true);
                        edtInputEmail.clearFocus();
                        edt_phone.clearFocus();
                        edtInputEmail.setFocusableInTouchMode(false);
                        edtInputEmail.setFocusable(false);
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        /*Bundle bundle = new Bundle();
                        bundle.putString("NAME", edtInput_name.getText().toString().trim());
                        bundle.putString("EMAIL", edtInputEmail.getText().toString().trim());
                        bundle.putString("MOBILE", edt_phone.getText().toString().trim());
                        bundle.putInt("otp", n);
                        bundle.putString("UHID", "");*/

                        Intent intent_otpchk=new Intent(FragmentSignUp.this,FragmentOTPCheck.class);
                        intent_otpchk.putExtra("NAME", edtInput_name.getText().toString().trim());
                        intent_otpchk.putExtra("EMAIL", edtInputEmail.getText().toString().trim());
                        intent_otpchk.putExtra("MOBILE", edt_phone.getText().toString().trim());
                        intent_otpchk.putExtra("otp", n);
                        intent_otpchk.putExtra("UHID", "");
                        intent_otpchk.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent_otpchk.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_otpchk);
                        finish();
                       /* CureFull.getInstanse().getFlowInstanse()
                                .addWithBottomTopAnimation(new FragmentOTPCheck(), bundle, true);*/
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
        AccountManager am = AccountManager.get(CureFull.getInstanse().getActivityIsntanse());
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
        }

        return email;
    }


    public void jsonUHIDCheck() {
//        JSONObject data = JsonUtilsObject.toLogin("user.doctor1.fortise@hatcheryhub.com", "ashwani");
        JSONObject data = JsonUtilsObject.toUHID(edtInput_name.getText().toString().trim(), edt_phone.getText().toString().trim(), edtInputEmail.getText().toString().trim());
//        Log.e("data", ":- " + data.toString() + ":- " + MyConstants.WebUrls.UHID_SIGN_UP);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.UHID_SIGN_UP, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        btn_signup.setEnabled(true);
                        //CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        progress_bar.setVisibility(View.GONE);
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
                                    uhidItemsChecks = ParseJsonData.getInstance().getUHIDCheck(response.toString());
                                    /*Bundle bundle = new Bundle();
                                    bundle.putString("NAME", edtInput_name.getText().toString().trim());
                                    bundle.putString("EMAIL", edtInputEmail.getText().toString().trim());
                                    bundle.putString("MOBILE", edt_phone.getText().toString().trim());
                                    bundle.putParcelableArrayList("UHID", uhidItemsChecks);
                                    CureFull.getInstanse().getFlowInstanse()
                                            .addWithBottomTopAnimation(new FragmentUHIDSignUp(), bundle, true);
*/
                                    Intent intent_otpchk1=new Intent(FragmentSignUp.this,FragmentUHIDSignUp.class);
                                    intent_otpchk1.putExtra("NAME", edtInput_name.getText().toString().trim());
                                    intent_otpchk1.putExtra("EMAIL", edtInputEmail.getText().toString().trim());
                                    intent_otpchk1.putExtra("MOBILE", edt_phone.getText().toString().trim());
                                    intent_otpchk1.putParcelableArrayListExtra("UHID", uhidItemsChecks);
                                    startActivity(intent_otpchk1);


                                }
                            } else {
                                btn_signup.setEnabled(true);
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
                btn_signup.setEnabled(true);
                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
            }
        }) {


        };
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        Log.e("signup", "signup");
        switch (requestCode) {
            case HandlePermission.MY_PERMISSIONS_REQUEST_READ_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        Cursor c = getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
                        if (c != null) {
                            c.moveToFirst();
                            TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                            edtInput_name.setText("" + c.getString(c.getColumnIndex("display_name")));
                            if (tMgr.getLine1Number() != null) {
                                edt_phone.setText("" + tMgr.getLine1Number().replace("+91", ""));
                            }
                            addAdapterToViews();
//        edtInputEmail.setText("" + getAllAccount());
                            c.close();
                        }
                    } catch (Exception e) {

                    }
                } else {
                    //code for deny


                    break;
                }
        }
    }

}