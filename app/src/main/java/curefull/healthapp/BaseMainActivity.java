package curefull.healthapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import awsgcm.AlarmReceiver;
import fragment.healthapp.FragmentContact;
import fragment.healthapp.FragmentLandingPage;
import fragment.healthapp.FragmentLogin;
import fragment.healthapp.FragmentProfile;
import fragment.healthapp.FragmentReminderMedicine;
import fragment.healthapp.FragmentTermCondition;
import fragment.healthapp.FragmentUHID;
import utils.AppPreference;
import utils.MyConstants;

public class BaseMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int _backBtnCount = 0;
    SharedPreferences preferences;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            CureFull.getInstanse().getActivityIsntanse().setshareVisibilty(true);
            CureFull.getInstanse().cancel();
            CureFull.getInstanse().getFlowInstanse().clearBackStack();
            CureFull.getInstanse().getFlowInstanse()
                    .replace(new FragmentLandingPage(), false);
        } else if (id == R.id.nav_profile) {
            CureFull.getInstanse().getActivityIsntanse().setshareVisibilty(true);
            CureFull.getInstanse().cancel();
            CureFull.getInstanse().getFlowInstanse().clearBackStack();
            CureFull.getInstanse().getFlowInstanse()
                    .replace(new FragmentProfile(), false);
        } else if (id == R.id.nav_uhid) {
            CureFull.getInstanse().getActivityIsntanse().setshareVisibilty(true);
            CureFull.getInstanse().cancel();
            CureFull.getInstanse().getFlowInstanse().clearBackStack();
            CureFull.getInstanse().getFlowInstanse()
                    .replace(new FragmentUHID(), false);
        } else if (id == R.id.nav_logout) {  //yaha se band krna h usko
            CureFull.getInstanse().cancel();
            // ek ye simple method hota hai.
            //
            //stopService(new Intent(BaseMainActivity.this, MessengerService.class));
            FragmentLandingPage fragmentLandingPage = new FragmentLandingPage();
            fragmentLandingPage.stopStepService();
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);

            jsonLogout();
        } else if (id == R.id.nav_policy) {
            CureFull.getInstanse().getActivityIsntanse().setshareVisibilty(true);
            CureFull.getInstanse().cancel();
            CureFull.getInstanse().getFlowInstanse().clearBackStack();
            CureFull.getInstanse().getFlowInstanse()
                    .replace(new FragmentTermCondition(), false);
        } else if (id == R.id.nav_contact) {
            CureFull.getInstanse().cancel();
            CureFull.getInstanse().getFlowInstanse().clearBackStack();
            CureFull.getInstanse().getFlowInstanse()
                    .replace(new FragmentContact(), false);
        } else if (id == R.id.nav_share) {

            getShareLink();
        }

//        else if (id == R.id.nav_setting) {
//
//            CureFull.getInstanse().cancel();
//            CureFull.getInstanse().getFlowInstanse().clearBackStack();
//            CureFull.getInstanse().getFlowInstanse()
//                    .replace(new FragmentSettingPage(), true);
//        }

        else if (id == R.id.nav_reminder) {
            CureFull.getInstanse().getActivityIsntanse().setshareVisibilty(true);
            CureFull.getInstanse().cancel();
            CureFull.getInstanse().getFlowInstanse().clearBackStack();
            CureFull.getInstanse().getFlowInstanse()
                    .replace(new FragmentReminderMedicine(), false);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void disableDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void activateDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            boolean isToWorkOnBack = true;
            if (CureFull.getInstanse().getFlowInstanse() != null) {
                List<Fragment> list = getSupportFragmentManager().getFragments();
                if (list != null) {
                    for (Fragment f : list) {
                        if (f != null && f instanceof BaseBackHandlerFragment) {
                            isToWorkOnBack = ((BaseBackHandlerFragment) f)
                                    .onBackPressed();
                        }
                    }
                }
            }

            if (!isToWorkOnBack)
                return;


            if (!CureFull.getInstanse().getFlowInstanse().hasNoMoreBack())
                super.onBackPressed();
            else {
                _backBtnCount++;
                if (_backBtnCount == 2) {
                    preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    preferences.edit().putBoolean("destroy", true).commit();
                    System.exit(0);
                    finish();
                    return;
                } else if (_backBtnCount == 1) {
                    String first = "Press back twice to exit";
                    String meassgeTxt = first;
                    Toast.makeText(CureFull.getInstanse().getActivityIsntanse(), meassgeTxt, Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            _backBtnCount = 0;
                        }
                    }, 500);

                }
            }
        }
    }


    public void showSnackbar(View view, String text) {
        Snackbar snackbar = Snackbar
                .make(view, text, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        TextView tv = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(getResources().getColor(R.color.red));
        tv.setTextSize(16);
        snackBarView.setBackgroundColor(getResources().getColor(R.color.white));
        snackbar.show();

    }


    public static String getTodayDate() {
        String formattedDate = null;
        try {
            SimpleDateFormat initialformatter = new SimpleDateFormat(
                    "yyyy-MM-dd", Locale.getDefault());
            java.util.Date today = Calendar.getInstance().getTime();
            formattedDate = initialformatter.format(today);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }


    @Override
    protected void onDestroy() {
        CureFull.getInstanse().onDestroy();
        super.onDestroy();
    }

    public void hideVirtualKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) CureFull.getInstanse().getActivityIsntanse()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View focused = CureFull.getInstanse().getActivityIsntanse().getCurrentFocus();
        if (focused != null && focused.getWindowToken() != null) {
            inputManager.hideSoftInputFromWindow(focused.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public String updateTime(int hours, int mins) {


        int selctHour = hours;

        String timeSet = "";
        if (selctHour > 12) {
            selctHour -= 12;
            timeSet = "pm";
        } else if (selctHour == 00) {
            selctHour += 12;
            timeSet = "am";
        } else if (selctHour == 12) {
            timeSet = "pm";
        } else {
            timeSet = "am";
        }

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

//        // Append in a StringBuilder
//        String aTime = new StringBuilder().append(selctHour).append(':')
//                .append(minutes).append(" ").append(timeSet).toString();
        String aTime = new StringBuilder().append((selctHour < 10 ? "0" + selctHour : selctHour)).append(':').append(minutes).append(timeSet).toString();
        return aTime;
    }

    public String updateTimeSpace(int hours, int mins) {


        int selctHour = hours;

        String timeSet = "";
        if (selctHour > 12) {
            selctHour -= 12;
            timeSet = "pm";
        } else if (selctHour == 00) {
            selctHour += 12;
            timeSet = "am";
        } else if (selctHour == 12) {
            timeSet = "pm";
        } else {
            timeSet = "am";
        }

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

//        // Append in a StringBuilder
//        String aTime = new StringBuilder().append(selctHour).append(':')
//                .append(minutes).append(" ").append(timeSet).toString();
        String aTime = new StringBuilder().append((selctHour < 10 ? "0" + selctHour : selctHour)).append(':').append(minutes).append(' ').append(timeSet).toString();
        return aTime;
    }


    public void jsonLogout() {
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.LOGOUT, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            preferences.edit().putBoolean("logout", true).commit();
                            AppPreference.getInstance().setIsFirstTimeSteps(false);
                            preferences.edit().putBoolean("resetStepFirstTime", true).commit();
                           /* CureFull.getInstanse().getFlowInstanse().clearBackStack();
                            CureFull.getInstanse().getActivityIsntanse().startActivity(new Intent(BaseMainActivity.this,FragmentLogin.class));*/
                            CureFull.getInstanse().getFlowInstanse().clearBackStack();
                           Intent intent=new Intent(BaseMainActivity.this,FragmentLogin.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            /*CureFull.getInstanse().getFlowInstanse()
                                    .replace(new FragmentLogin(), false);*/
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("a_t", AppPreference.getInstance().getAt());
                headers.put("r_t", AppPreference.getInstance().getRt());
                headers.put("user_name", AppPreference.getInstance().getUserName());
                headers.put("email_id", AppPreference.getInstance().getUserID());
                headers.put("cf_uuhid", AppPreference.getInstance().getcf_uuhid());
                return headers;
            }
        };
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }

    public void getShareLink() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
//            share.setType("image/jpeg");
            i.putExtra(Intent.EXTRA_SUBJECT, "CureFull");
            String sAux = "Curefull has made 24*7 personalized mobile health monitoring a reality\nWant to track your Daily physical activity? Keep record of Daily health issues ? Manage Digital\n\nJust a click away! ";
            sAux = sAux + "https://play.google.com/store/apps/details?id=com.curefull \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }
}
