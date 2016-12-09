package curefull.healthapp;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import fragment.healthapp.FragmentLandingPage;
import fragment.healthapp.FragmentLogin;
import fragment.healthapp.FragmentProfile;
import fragment.healthapp.FragmentSettingPage;
import fragment.healthapp.FragmentTermCondition;
import fragment.healthapp.FragmentUHID;
import utils.AppPreference;

public class BaseMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int _backBtnCount = 0;
    SharedPreferences preferences;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            CureFull.getInstanse().getFlowInstanseAll().clearBackStack();
            CureFull.getInstanse().getFlowInstanseAll()
                    .replace(new FragmentLandingPage(), false);
        } else if (id == R.id.nav_profile) {
            CureFull.getInstanse().getFlowInstanseAll().clearBackStack();
            CureFull.getInstanse().getFlowInstanseAll()
                    .replace(new FragmentProfile(), false);
        } else if (id == R.id.nav_uhid) {
            CureFull.getInstanse().getFlowInstanseAll().clearBackStack();
            CureFull.getInstanse().getFlowInstanseAll()
                    .replace(new FragmentUHID(), false);
        } else if (id == R.id.nav_logout) {
            AppPreference.getInstance().clearAllData();
            AppPreference.getInstance().setIsLogin(false);
            CureFull.getInstanse().getFlowInstanseAll().clearBackStack();
            CureFull.getInstanse().getFlowInstanse().clearBackStack();
            CureFull.getInstanse().getFlowInstanse()
                    .replace(new FragmentLogin(), false);
        } else if (id == R.id.nav_policy) {
            CureFull.getInstanse().getFlowInstanseAll().clearBackStack();
            CureFull.getInstanse().getFlowInstanseAll()
                    .replace(new FragmentTermCondition(), false);
        } else if (id == R.id.nav_setting) {
            CureFull.getInstanse().getFlowInstanseAll().clearBackStack();
            CureFull.getInstanse().getFlowInstanseAll()
                    .replace(new FragmentSettingPage(), false);
        } else if (id == R.id.nav_share) {
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
//                    CureFull.getInstanse().getActivityIsntanse().startFitService();
                    System.exit(0);
                    finish();
                    return;
                } else {
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


    public String formatMonth(String month) throws ParseException {

        try {
            SimpleDateFormat monthParse = new SimpleDateFormat("MM");
            SimpleDateFormat monthDisplay = new SimpleDateFormat("MMM");
            return monthDisplay.format(monthParse.parse(month));
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public String updateTime(int hours, int mins) {


        int selctHour = hours;

        String timeSet = "";
        if (selctHour > 12) {
            selctHour -= 12;
            timeSet = "pm";
        } else if (selctHour == 0) {
            selctHour += 12;
            timeSet = "pm";
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
        String aTime = new StringBuilder().append(selctHour).append(timeSet).toString();
        return aTime;
    }

}
