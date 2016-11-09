package fragment.healthapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import item.property.EduationDetails;
import item.property.UserInfo;
import sticky.header.UnderlineTextView;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.CustomTypefaceSpan;
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
    CallbackManager callbackManager;
    TextView login_button, btn_create_new, btn_click_forgot, sign_out_button_facebook, txt_term_conditions;
    private boolean showPwd = false;
    private EditText edtInputEmail, edtInputPassword;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_login,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
        CureFull.getInstanse().getActivityIsntanse().disableDrawer();
        CureFull.getInstanse().getActivityIsntanse().showLogo(true);
        CureFull.getInstanse().getActivityIsntanse().showRelativeActionBar(false);
        txt_term_conditions = (TextView) rootView.findViewById(R.id.txt_term_conditions);
        edtInputEmail = (EditText) rootView.findViewById(R.id.input_email);
        edtInputPassword = (EditText) rootView.findViewById(R.id.input_password);
        sign_out_button_facebook = (TextView) rootView.findViewById(R.id.sign_out_button_facebook);
        btn_create_new = (TextView) rootView.findViewById(R.id.btn_create_new);
        btn_click_forgot = (TextView) rootView.findViewById(R.id.btn_click_forgot);

        login_button = (TextView) rootView.findViewById(R.id.btn_login);
        sign_out_button_facebook.setOnClickListener(this);
        login_button.setOnClickListener(this);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.e("loginResult", ":- " + loginResult.getAccessToken());
//                Profile profile = Profile.getCurrentProfile();
//                Log.e("S", "+" +profile);

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("LoginActivity", ":-" + response.toString());
                                ArrayList<EduationDetails> eduationDetailses = new ArrayList<EduationDetails>();
                                EduationDetails eduationDetails = null;
                                try {
                                    AppPreference.getInstance().setFacebookUserName(object.getString("name"));
                                    JSONObject friendsJsonObject = object.getJSONObject("picture");
                                    JSONObject friendsJsonObject1 = new JSONObject(friendsJsonObject.getString("data"));
                                    AppPreference.getInstance().setFacebookProfileImage(friendsJsonObject1.getString("url"));
                                    JSONArray friendsArray = new JSONArray(object.getString("education"));
                                    for (int i = 0; i < friendsArray.length(); i++) {
                                        JSONObject jsonObject = friendsArray.getJSONObject(i);
                                        JSONObject friends = jsonObject.getJSONObject("school");
                                        Log.e("name", ":- " + friends.getString("name"));
                                        Log.e("type", ":- " + jsonObject.getString("type"));
                                        eduationDetails = new EduationDetails();
                                        eduationDetails.setInstituteName(friends.getString("name"));
                                        eduationDetails.setInstituteType(jsonObject.getString("type"));
                                        eduationDetailses.add(eduationDetails);
                                    }
                                    jsonFacebookLogin(object.getString("id"), object.getString("name"), object.getString("email"), object.getString("birthday"), object.getString("gender"), object.getString("relationship_status"), friendsJsonObject1.getString("url"), "Android", eduationDetailses);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // Application code

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,picture.width(150).height(150),gender,birthday,devices,location,relationship_status,education");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

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


        String you = "You agree to";
        String termCondtiions = " Term and Conditions ";
        String submit = "of Curefull. After clicking submit";

        String meassgeNew = you + termCondtiions + submit;

        Spannable sb1 = new SpannableString(meassgeNew);
        sb1.setSpan(new ForegroundColorSpan(getActivity().getResources()
                        .getColor(R.color.white)), meassgeNew.indexOf(you),
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
                        .getColor(R.color.white)), meassgeNew.indexOf(submit),
                meassgeNew.indexOf(submit) + submit.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_term_conditions.setText(sb1);


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
                                edtInputPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon, 0, R.drawable.password_visible, 0);
                            } else {
                                showPwd = false;
                                edtInputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                edtInputPassword.setSelection(edtInputPassword.getText().length());
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


        return rootView;
    }

    private boolean validateEmail() {
        String email = edtInputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            edtInputEmail.setError("Email Id cannot be left blank.");
            requestFocus(edtInputEmail);
            return false;
        } else {
            edtInputEmail.setError(null);
        }
        return true;
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

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            CureFull.getInstanse().getActivityIsntanse().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
                LoginManager.getInstance().logInWithReadPermissions(FragmentLogin.this, Arrays.asList("public_profile", "user_friends", "user_birthday", "email", "user_location", "user_about_me", "user_status", "user_relationships", "user_posts", "user_education_history"));

                break;
            case R.id.btn_create_new:
                CureFull.getInstanse().getFlowInstanse()
                        .addWithBottomTopAnimation(new FragmentSignUp(), null, true);
                break;
            case R.id.btn_click_forgot:
                CureFull.getInstanse().getFlowInstanse()
                        .addWithBottomTopAnimation(new FragmentResetPassword(), null, true);
                break;
            case R.id.btn_login:
//                Intent login = PopupActivity.getStartIntent(getActivity(), PopupActivity.MORPH_TYPE_BUTTON);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation
//                        (getActivity(), rootView, getString(R.string.transition_morph_view));
//                startActivity(login, options.toBundle());
//                NotificationUtils notificationUtils = new NotificationUtils(CureFull.getInstanse().getActivityIsntanse());
//                notificationUtils.notificationWaterInTake();
//                notificationUtils.notificationWithImage();
//
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
            return;
        }

        if (!validatePassword()) {
            return;
        }
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            jsonLoginCheck();
        } else {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);

        }


    }


    public void jsonLoginCheck() {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
//        JSONObject data = JsonUtilsObject.toLogin("user.doctor1.fortise@hatcheryhub.com", "ashwani");
        JSONObject data = JsonUtilsObject.toLogin(edtInputEmail.getText().toString().trim(), edtInputPassword.getText().toString().trim());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.LOGIN, data,
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
                            UserInfo userInfo = ParseJsonData.getInstance().getLoginData(response.toString());
                            if (ParseJsonData.getInstance().getHttp_code().equalsIgnoreCase(MyConstants.JsonUtils.OK)) {
                                AppPreference.getInstance().setUserName(userInfo.getUser_name());
                                AppPreference.getInstance().setUserID(userInfo.getUser_id());
                                CureFull.getInstanse().getActivityIsntanse().setActionDrawerHeading(userInfo.getUser_name(), userInfo.getUser_id());
                                AppPreference.getInstance().setAt(userInfo.getA_t());
                                AppPreference.getInstance().setRt(userInfo.getR_t());
                                CureFull.getInstanse().getFlowInstanse().clearBackStack();
                                CureFull.getInstanse().getFlowInstanse()
                                        .replace(new FragmentHomeScreenAll(), false);
                            }
                        } else {
                            Toast.makeText(CureFull.getInstanse().getActivityIsntanse(), "Invalid Details", Toast.LENGTH_SHORT).show();
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


    public void jsonFacebookLogin(String facebookId, String name, String emailId, String dateOfBirth, String gender, String relationshipStatus, String profileImageUrl, String devices, ArrayList<EduationDetails> eduationDeatilsResults) {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
        JSONObject data = JsonUtilsObject.toSignUpFacebook(facebookId, name, emailId, dateOfBirth, gender, relationshipStatus, profileImageUrl, devices, eduationDeatilsResults);

        Log.e("facbook", ":- " + data.toString());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.FACEBOOK_SIGNUP, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("FragmentFb, URL 3.", response.toString());
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == 100) {
                            UserInfo userInfo = ParseJsonData.getInstance().getLoginData(response.toString());
                            if (ParseJsonData.getInstance().getHttp_code().equalsIgnoreCase(MyConstants.JsonUtils.OK)) {
                                AppPreference.getInstance().setUserName(userInfo.getUser_name());
                                AppPreference.getInstance().setUserID(userInfo.getUser_id());
                                CureFull.getInstanse().getActivityIsntanse().setActionDrawerHeading(userInfo.getUser_name(), userInfo.getUser_id());
                                AppPreference.getInstance().setAt(userInfo.getA_t());
                                AppPreference.getInstance().setRt(userInfo.getR_t());
                                CureFull.getInstanse().getFlowInstanse().clearBackStack();
                                CureFull.getInstanse().getFlowInstanse()
                                        .replace(new FragmentHomeScreenAll(), false);
                            }
                        } else {
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