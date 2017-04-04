package fragment.healthapp;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.ParseError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import ElasticVIews.ElasticAction;
import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import awsgcm.MessageReceivingService;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogTCFullView;
import item.property.UserInfo;
import operations.DbOperations;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.CustomTypefaceSpan;
import utils.HandlePermission;
import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentLogin extends Fragment implements
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private ProgressDialog mProgressDialog;
    private View rootView;
    TextView login_button, btn_create_new, btn_click_forgot, sign_out_button_facebook, txt_term_conditions;
    private boolean showPwd = false;
    private AutoCompleteTextView edtInputEmail;
    private EditText edtInputPassword;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
    private SharedPreferences sharedPreferencesUserLogin;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_login,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().isbackButtonVisible(true, "");
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
        CureFull.getInstanse().getActivityIsntanse().isTobBarButtonVisible(true, "");
        CureFull.getInstanse().getActivityIsntanse().disableDrawer();
        CureFull.getInstanse().getActivityIsntanse().showLogo(true);
        CureFull.getInstanse().getActivityIsntanse().showRelativeActionBar(false);
        preferences = PreferenceManager.getDefaultSharedPreferences(CureFull.getInstanse().getActivityIsntanse());
        sharedPreferencesUserLogin = CureFull.getInstanse().getActivityIsntanse()
                .getSharedPreferences("Login", 0);
        txt_term_conditions = (TextView) rootView.findViewById(R.id.txt_term_conditions);
        edtInputEmail = (AutoCompleteTextView) rootView.findViewById(R.id.input_email);
        edtInputPassword = (EditText) rootView.findViewById(R.id.input_password);
        sign_out_button_facebook = (TextView) rootView.findViewById(R.id.sign_out_button_facebook);
        btn_create_new = (TextView) rootView.findViewById(R.id.btn_create_new);
        btn_click_forgot = (TextView) rootView.findViewById(R.id.btn_click_forgot);
        login_button = (TextView) rootView.findViewById(R.id.btn_login);
        sign_out_button_facebook.setOnClickListener(this);
        login_button.setOnClickListener(this);

        btn_create_new.setOnClickListener(this);
        btn_click_forgot.setOnClickListener(this);

        btn_click_forgot.setPaintFlags(btn_click_forgot.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        String comma = "Log in via ";
        String gameName = "facebook";

        String meassgeTxt = comma + gameName;

        Spannable sb = new SpannableString(meassgeTxt);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "facebook-letter-faces.ttf");
        sb.setSpan(new ForegroundColorSpan(getActivity().getResources()
                        .getColor(R.color.health_login_text)), meassgeTxt.indexOf(comma),
                meassgeTxt.indexOf(comma) + comma.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new CustomTypefaceSpan("", font), 11, 19, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(getActivity().getResources()
                        .getColor(R.color.blue_interpid)), meassgeTxt.indexOf(gameName),
                meassgeTxt.indexOf(gameName) + gameName.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new RelativeSizeSpan(1.1f), 11, 19, 0);
        sign_out_button_facebook.setText(sb);


        String you = "You agree to ";
        String termCondtiions = "Term and Conditions";
        String submit = " of Curefull. After clicking submit";

        String meassgeNew = you + termCondtiions + submit;

        Spannable sb1 = new SpannableString(meassgeNew);
        sb1.setSpan(new ForegroundColorSpan(getActivity().getResources()
                        .getColor(R.color.health_light_gray)), meassgeNew.indexOf(you),
                meassgeNew.indexOf(you) + you.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb1.setSpan(new ForegroundColorSpan(getActivity().getResources()
                        .getColor(R.color.health_yellow)), meassgeNew.indexOf(termCondtiions),
                meassgeNew.indexOf(termCondtiions) + termCondtiions.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb1.setSpan(new UnderlineSpan(), meassgeNew.indexOf(termCondtiions),
                meassgeNew.indexOf(termCondtiions) + termCondtiions.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb1.setSpan(new ForegroundColorSpan(getActivity().getResources()
                        .getColor(R.color.health_light_gray)), meassgeNew.indexOf(submit),
                meassgeNew.indexOf(submit) + submit.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_term_conditions.setText(sb1);

        txt_term_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTCFullView dialogProfileFullView = new DialogTCFullView(CureFull.getInstanse().getActivityIsntanse());
                dialogProfileFullView.show();
            }
        });
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
                                edtInputPassword.setTextSize(14f);
                                edtInputPassword.setSelection(edtInputPassword.getText().length());
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

        edtInputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login_button.setEnabled(false);
                    CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
                    submitForm();
                }
                return false;
            }
        });


        edtInputEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edtInputPassword.requestFocus();
                }
                return false;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (HandlePermission.checkPermissionReadContact(CureFull.getInstanse().getActivityIsntanse())) {
                addAdapterToViews();
            }
        }

//        AppPreference.getInstance().clearAllData();
        AppPreference.getInstance().setIsLogin(false);
        return rootView;
    }

    private boolean validateEmail() {
        String email = edtInputEmail.getText().toString().trim();

        if (email.isEmpty()) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Email Id / Mobile number cannot be left blank.");
            return false;
        }
        return true;
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validatePassword() {
        if (edtInputPassword.getText().toString().trim().isEmpty()) {
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


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_out_button_facebook:

                break;
            case R.id.btn_create_new:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                CureFull.getInstanse().getFlowInstanse()
                        .addWithBottomTopAnimation(new FragmentSignUp(), null, true);
                break;
            case R.id.btn_click_forgot:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                CureFull.getInstanse().getFlowInstanse()
                        .addWithBottomTopAnimation(new FragmentResetPassword(), null, true);
                break;
            case R.id.btn_login:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
//                Intent login = PopupActivity.getStartIntent(getActivity(), PopupActivity.MORPH_TYPE_BUTTON);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation
//                        (getActivity(), rootView, getString(R.string.transition_morph_view));
//                startActivity(login, options.toBundle());
//                NotificationUtils notificationUtils = new NotificationUtils(CureFull.getInstanse().getActivityIsntanse());
//                notificationUtils.notificationWaterInTake();
//                notificationUtils.notificationWithImage();
//
                CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
                submitForm();


                break;

//            case R.id.img_visible_pass:
//
//
//                break;
        }
    }


    private void submitForm() {


        if (!validateEmail()) {
            login_button.setEnabled(true);
            return;
        }

        if (!validatePassword()) {
            login_button.setEnabled(true);
            return;
        }
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            jsonLoginCheck();
        } else {
            login_button.setEnabled(true);
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);

        }


    }

    private boolean validateMobileNo() {
        String email = edtInputEmail.getText().toString().trim();

        if (email.isEmpty() || email.length() != 10) {
            if (email.length() < 10 && email.length() > 1) {
                edtInputEmail.setError("Mobile Number cannot be less than 10 numbers.");
            } else {
                edtInputEmail.setError("Mobile Number cannot be left blank.");
            }
            requestFocus(edtInputEmail);
            return false;

        } else {
            edtInputEmail.setError(null);
        }
        return true;
    }

    public void jsonLoginCheck() {
//        JSONObject data = JsonUtilsObject.toLogin("user.doctor1.fortise@hatcheryhub.com", "ashwani");
        JSONObject data = JsonUtilsObject.toLogin(edtInputEmail.getText().toString().trim(), edtInputPassword.getText().toString().trim());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.LOGIN, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.e("res", "" + response.toString());
                        login_button.setEnabled(true);
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            ParseJsonData.getInstance().getLoginData(response.toString());
                            UserInfo userInfo = DbOperations.getLoginList(CureFull.getInstanse().getActivityIsntanse());
//                            UserInfo userInfo = ParseJsonData.getInstance().getLoginData(response.toString());
                            if (ParseJsonData.getInstance().getHttp_code().equalsIgnoreCase(MyConstants.JsonUtils.OK)) {

                                if (userInfo != null) {
                                    if (sharedPreferencesUserLogin.getString("tokenid",
                                            "123").equalsIgnoreCase("123")) {
                                        sharedPreferencesUserLogin.edit().putBoolean(getString(R.string.first_launch), true).commit();
                                        CureFull.getInstanse().getActivityIsntanse().startService(new Intent(CureFull.getInstanse().getActivityIsntanse(), MessageReceivingService.class));
                                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
                                        return;
                                    }

                                    if (AppPreference.getInstance().getUserName().equalsIgnoreCase(userInfo.getUser_id())) {
                                        AppPreference.getInstance().setIsLoginFirst(false);
                                    } else {
                                        AppPreference.getInstance().setIsLoginFirst(true);
                                    }
                                    AppPreference.getInstance().setPassword("" + edtInputPassword.getText().toString().trim());
                                    AppPreference.getInstance().setHintScreen(userInfo.getHintScreen());
                                    AppPreference.getInstance().setUserName(userInfo.getUser_name());
                                    AppPreference.getInstance().setUserIDProfile(userInfo.getUser_id_profile());
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("email_id", userInfo.getUser_id());
                                    DbOperations.insertEmailList(CureFull.getInstanse().getActivityIsntanse(), contentValues, userInfo.getUser_id());
                                    AppPreference.getInstance().setUserID(userInfo.getUser_id());
                                    AppPreference.getInstance().setcf_uuhid(userInfo.getCf_uuhid());
                                    AppPreference.getInstance().setcf_uuhidNeew(userInfo.getCf_uuhid());
                                    AppPreference.getInstance().setMobileNumber(userInfo.getMobile_number());
                                    AppPreference.getInstance().setProfileImage(userInfo.getProfileImageUrl());
                                    CureFull.getInstanse().getActivityIsntanse().setActionDrawerProfilePic(userInfo.getProfileImageUrl());
                                    CureFull.getInstanse().getActivityIsntanse().setActionDrawerHeading(userInfo.getUser_name(), userInfo.getUser_id());
                                    if (userInfo.getA_t().equalsIgnoreCase("") || userInfo.getA_t().equalsIgnoreCase("null")) {
                                        return;
                                    }
                                    preferences.edit().putString("a_t", userInfo.getA_t()).commit();
                                    preferences.edit().putString("r_t", userInfo.getR_t()).commit();
                                    preferences.edit().putString("user_name", userInfo.getUser_name()).commit();
                                    preferences.edit().putString("email_id", userInfo.getUser_id()).commit();
                                    preferences.edit().putString("cf_uuhid", userInfo.getCf_uuhid()).commit();
                                    preferences.edit().putString("user_id", userInfo.getUser_id_profile()).commit();

                                    AppPreference.getInstance().setAt(userInfo.getA_t());
                                    AppPreference.getInstance().setRt(userInfo.getR_t());
//                                    if (sharedPreferencesUserLogin.getString("tokenid",
//                                            "123").equalsIgnoreCase("123")) {
//                                        sharedPreferencesUserLogin.edit().putBoolean(getString(R.string.first_launch), true).commit();
//                                        CureFull.getInstanse().getActivityIsntanse().startService(new Intent(CureFull.getInstanse().getActivityIsntanse(), MessageReceivingService.class));
//                                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);
//
//                                    } else {
                                    String token_Id = sharedPreferencesUserLogin.getString("tokenid",
                                            "123");
                                    String device_Id = sharedPreferencesUserLogin.getString("android_id",
                                            "123");

                                    if (AppPreference.getInstance().isFirstTimeStepsNotifictaion()) {
//                                        Log.e("aaya","kya");
                                        AppPreference.getInstance().setIsFirstTimeNotifictaion(false);
                                        jsonSaveNotification(token_Id, device_Id);
                                    }
                                    CureFull.getInstanse().getFlowInstanse().clearBackStack();
                                    CureFull.getInstanse().getFlowInstanse()
                                            .replace(new FragmentLandingPage(), false);
//                                    }

                                } else {
                                    login_button.setEnabled(true);
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);

                                }

                            }
                        } else {
                            login_button.setEnabled(true);
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
//                Log.e("error", "" + error.getMessage());
                login_button.setEnabled(true);
                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
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
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }


    private void addAdapterToViews() {
        if (!(DbOperations.getEmailList(CureFull.getInstanse().getActivityIsntanse()).size() > 0)) {
            Account[] accounts = AccountManager.get(CureFull.getInstanse().getActivityIsntanse()).getAccounts();
            for (Account account : accounts) {
                if (EMAIL_PATTERN.matcher(account.name).matches()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("email_id", account.name);
                    DbOperations.insertEmailList(CureFull.getInstanse().getActivityIsntanse(), contentValues, account.name);
                }
            }
            List<String> emailSet = DbOperations.getEmailList(CureFull.getInstanse().getActivityIsntanse());
            edtInputEmail.setAdapter(new ArrayAdapter<String>(CureFull.getInstanse().getActivityIsntanse(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>(emailSet)));

        } else {
            List<String> emailSet = DbOperations.getEmailList(CureFull.getInstanse().getActivityIsntanse());
            edtInputEmail.setAdapter(new ArrayAdapter<String>(CureFull.getInstanse().getActivityIsntanse(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>(emailSet)));

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case HandlePermission.MY_PERMISSIONS_REQUEST_READ_CONTACT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addAdapterToViews();
                }
                break;
        }
    }

    public void jsonSaveNotification(String registrationToken, String deviceId) {

//        if (registrationToken.contains(":")) {
//            String[] token = registrationToken.split(":");
//            registrationToken = token[1];
//        }

        JSONObject data = JsonUtilsObject.toRegisterUserForNotification(registrationToken, deviceId);
//        Log.e("token", " " + registrationToken);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.URL_NOTIFICATION, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");

                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {

                            } else {
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
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
//                VolleyLog.e("FragmentLogin, URL 3.", "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("a_t", AppPreference.getInstance().getAt());
                headers.put("r_t", AppPreference.getInstance().getRt());
                headers.put("user_name", AppPreference.getInstance().getUserName());
                headers.put("email_id", AppPreference.getInstance().getUserID());
                headers.put("cf_uuhid", AppPreference.getInstance().getcf_uuhidNeew());
                headers.put("user_id", AppPreference.getInstance().getUserIDProfile());
                return headers;
            }
        };
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }
}