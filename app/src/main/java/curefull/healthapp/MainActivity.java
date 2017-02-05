package curefull.healthapp;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import awsgcm.MessageReceivingService;
import fragment.healthapp.FragmentEditGoal;
import fragment.healthapp.FragmentHealthAppNewProgress;
import fragment.healthapp.FragmentHealthNote;
import fragment.healthapp.FragmentLabTestReport;
import fragment.healthapp.FragmentLandingPage;
import fragment.healthapp.FragmentLogin;
import fragment.healthapp.FragmentPrescriptionCheckNew;
import fragment.healthapp.FragmentProfile;
import fragment.healthapp.FragmentReminderDoctorVisit;
import fragment.healthapp.FragmentReminderLabTest;
import fragment.healthapp.FragmentReminderMedicine;
import fragment.healthapp.FragmentSignUp;
import utils.AppPreference;
import utils.CircularImageView;
import utils.HandlePermission;

public class MainActivity extends BaseMainActivity implements TransferListener, View.OnClickListener {

    static {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    //    private Toolbar toolbar;
    private DrawerLayout drawer;
    public static final String TAG = "MainActivity";
    private RelativeLayout relative_logo, relative_action_bar;
    private ActionBarDrawerToggle toggle;
    private ProgressBar progress_bar;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    private TextView txt_doctor_name_edu;
    private ImageView img_drawer, img_share;
    private CircularImageView circularImageView;
    private NavigationView navigationView;
    SharedPreferences preferences;
    private File myPath;
    String encodedImage;
    private View view1;


    private RelativeLayout liner_bottom_view;
    private LinearLayout top_view;
    private LinearLayout txt_bottom_heath_app, txt_bottom_health_note, txt_bottom_home, txt_bottom_prescription, txt_bottom_reports;
    private LinearLayout liner_medincine, liner_doctor_visit, liner_lab_test;
    private TextView txt_med, txt_doctor_visit, txt_lab_test;
    private ImageView img_medicine, img_doctor_visit, img_lab_test, img_health_app, img_health_note, img_health_home, img_health_pre, img_health_report;

    // Since this activity is SingleTop, there can only ever be one instance. This variable corresponds to this instance.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CureFull.getInstanse().initActivity(this);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            Drawable background = this.getResources().getDrawable(R.drawable.login_gradient);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
//            window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(this);


        top_view = (LinearLayout) findViewById(R.id.top_view);
        liner_bottom_view = (RelativeLayout) findViewById(R.id.liner_bottom_view);
        img_health_app = (ImageView) findViewById(R.id.img_health_app);
        img_health_note = (ImageView) findViewById(R.id.img_health_note);
        img_health_home = (ImageView) findViewById(R.id.img_health_home);
        img_health_pre = (ImageView) findViewById(R.id.img_health_pre);
        img_health_report = (ImageView) findViewById(R.id.img_health_report);


        txt_bottom_heath_app = (LinearLayout) findViewById(R.id.txt_bottom_heath_app);
        txt_bottom_health_note = (LinearLayout) findViewById(R.id.txt_bottom_health_note);
        txt_bottom_home = (LinearLayout) findViewById(R.id.txt_bottom_home);
        txt_bottom_prescription = (LinearLayout) findViewById(R.id.txt_bottom_prescription);
        txt_bottom_reports = (LinearLayout) findViewById(R.id.txt_bottom_reports);

        liner_bottom_view = (RelativeLayout) findViewById(R.id.liner_bottom_view);
        txt_med = (TextView) findViewById(R.id.txt_med);
        txt_doctor_visit = (TextView) findViewById(R.id.txt_doctor_visit);
        txt_lab_test = (TextView) findViewById(R.id.txt_lab_test);

        img_medicine = (ImageView) findViewById(R.id.img_medicine);
        img_doctor_visit = (ImageView) findViewById(R.id.img_doctor_visit);
        img_lab_test = (ImageView) findViewById(R.id.img_lab_test);

        liner_medincine = (LinearLayout) findViewById(R.id.liner_medincine);
        liner_doctor_visit = (LinearLayout) findViewById(R.id.liner_doctor_visit);
        liner_lab_test = (LinearLayout) findViewById(R.id.liner_lab_test);
        liner_medincine.setOnClickListener(this);
        liner_doctor_visit.setOnClickListener(this);
        liner_lab_test.setOnClickListener(this);

        txt_bottom_heath_app.setOnClickListener(this);
        txt_bottom_health_note.setOnClickListener(this);
        txt_bottom_home.setOnClickListener(this);
        txt_bottom_prescription.setOnClickListener(this);
        txt_bottom_reports.setOnClickListener(this);


        img_share = (ImageView) findViewById(R.id.img_share);
        img_drawer = (ImageView) findViewById(R.id.img_drawer_open);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
        relative_action_bar = (RelativeLayout) findViewById(R.id.relative_action_bar);
        relative_logo = (RelativeLayout) findViewById(R.id.relative_logo);
//        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        View header = navigationView.getHeaderView(0);
        circularImageView = (CircularImageView) header.findViewById(R.id.placeholder);
        txt_doctor_name_edu = (TextView) header.findViewById(R.id.txt_doctor_name_edu);
        preferences.edit().putBoolean("isDestroy", false).commit();
        getKeyHash();
        showActionBarToggle(false);
        disableDrawer();
        changeTitle("cureFull");

        startService(new Intent(this, MessageReceivingService.class));
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.e("id ", " " + refreshedToken);

//        Intent serviceIntent = new Intent(this, LocationService.class);
//        startService(serviceIntent);

        if (getIntent().getAction() != null) {
            String type = getIntent().getExtras().getString("type");

            if (type.equalsIgnoreCase("LAB_TEST_REMINDER")) {
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentReminderLabTest(), false);
            } else if (type.equalsIgnoreCase("DOCTOR_FOLLOWUP_REMINDER")) {
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentReminderDoctorVisit(), false);
            } else {
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentReminderMedicine(), false);
            }
//            CureFull.getInstanse().getFlowInstanse().clearBackStack();
//            CureFull.getInstanse().getFlowInstanse()
//                    .replace(new FragmentHomeScreenAll(), false);
//            Toast.makeText(this, getIntent().getAction(), Toast.LENGTH_SHORT).show();
        } else {
            if (AppPreference.getInstance().isLogin()) {
                CureFull.getInstanse().getFlowInstanse().clearBackStack();
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentLandingPage(), false);
            } else {
                CureFull.getInstanse().getFlowInstanse().clearBackStack();
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentLogin(), false);
            }
        }

        img_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.closeDrawer(Gravity.LEFT);
                } else {
                    drawer.openDrawer(Gravity.LEFT);
                }

            }
        });

        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconAnim(img_share);
                new LongOperation().execute("");
            }
        });


//        Intent intent = getIntent();
//        if (intent != null) {
//            String action = intent.getAction();
//            Uri data = intent.getData();
//
//            Log.e("action", "" + action);
//            if (data != null) {
//                Log.e("data", "" + data.toString());
//            }
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
        finish();
        preferences.edit().putBoolean("isDestroy", true).commit();
        Log.e("onDestroy", "onDestroy");
    }

    public void selectedNav(int i) {
        navigationView.getMenu().getItem(i).setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;

        } else if (id == android.R.id.home) {
            // Home/Up logic handled by onBackPressed implementation
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setActionDrawerHeading(String name, String id) {
        txt_doctor_name_edu.setText("" + name);
    }

    public void setActionDrawerProfilePic(String name) {
        CureFull.getInstanse().getSmallImageLoader().startLazyLoading(name, circularImageView);
//        Glide.with(this).load(name)
//                .thumbnail(0.5f)
//                .placeholder(R.drawable.profile_avatar)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .into(circularImageView);
    }


//    private CharSequence getMessageText(Intent intent) {
//        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
//        if (remoteInput != null) {
//            return remoteInput.getCharSequence(NotificationUtils.KEY_TEXT_REPLY);
//        }
//        Toast.makeText(this, "Remoteinput is null", Toast.LENGTH_SHORT).show();
//
//        return null;
//    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }


    public void showActionBarToggle(boolean ischeck) {
//        if (ischeck == true) {
//
//            getSupportActionBar().show();
////            toolbar.setVisibility(View.VISIBLE);
//        } else {
//            getSupportActionBar().hide();
////            toolbar.setVisibility(View.GONE);
//        }


    }

    public void showUpButton(boolean show) {
        // To keep states of ActionBar and ActionBarDrawerToggle synchronized,
        // when you enable on one, you disable on the other.
        // And as you may notice, the order for this operation is disable first, then enable - VERY VERY IMPORTANT.
//        if (show) {
//            // Remove hamburger
//            toggle.setDrawerIndicatorEnabled(false);
//            // Show back button
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
//            // clicks are disabled i.e. the UP button will not work.
//            // We need to add a listener, as in below, so DrawerToggle will forward
//            // click events to this listener.
//            if (!mToolBarNavigationListenerIsRegistered) {
//                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onBackPressed();
//                    }
//                });
//
//                mToolBarNavigationListenerIsRegistered = true;
//            }
//
//        } else {
//            // Remove back button
//            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//            // Show hamburger
//            toggle.setDrawerIndicatorEnabled(true);
//            // Remove the/any drawer toggle listener
//            toggle.setToolbarNavigationClickListener(null);
//            mToolBarNavigationListenerIsRegistered = false;
//        }

        // So, one may think "Hmm why not simplify to:
        // .....
        // getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
        // mDrawer.setDrawerIndicatorEnabled(!enable);
        // ......
        // To re-iterate, the order in which you enable and disable views IS important #dontSimplify.
    }

    public void showLogo(boolean b) {
        if (b) {
            relative_logo.setVisibility(View.VISIBLE);
        } else {
            relative_logo.setVisibility(View.GONE);
        }

    }


    public void showRelativeActionBar(boolean b) {
        if (b) {
            relative_action_bar.setVisibility(View.VISIBLE);
        } else {
            relative_action_bar.setVisibility(View.GONE);
        }

    }

    public void showProgressBar(boolean b) {
        if (b) {
            progress_bar.setVisibility(View.VISIBLE);
        } else {
            progress_bar.setVisibility(View.INVISIBLE);
        }

    }


    public void changeColorActionBar(String color) {
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(color));
//        getSupportActionBar().setBackgroundDrawable(colorDrawable);
    }

    public void changeTitle(String tile) {
//        getSupportActionBar().setTitle("CureFull");
    }

    public void getKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//                CureFull.getInstanse().getFlowInstanse().clearBackStack();
//                CureFull.getInstanse().getFlowInstanse()
//                        .replace(new FragmentLogin(), false);
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (!action.equalsIgnoreCase("steps")) {
            String type = intent.getExtras().getString("type");
            if (type.equalsIgnoreCase("LAB_TEST_REMINDER")) {
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentReminderLabTest(), false);
            } else if (type.equalsIgnoreCase("DOCTOR_FOLLOWUP_REMINDER")) {
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentReminderDoctorVisit(), false);
            } else {
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentReminderMedicine(), false);
            }

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {


        switch (requestCode) {
            case HandlePermission.MY_PERMISSIONS_REQUEST_READ_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    List<Fragment> list = CureFull.getInstanse().getActivityIsntanse().getSupportFragmentManager().getFragments();
                    if (list != null) {
                        for (Fragment f : list) {
                            if (f != null && f instanceof FragmentSignUp) {
                                f.onRequestPermissionsResult(requestCode, permissions, grantResults);
                            }
                        }
                    }

                }
                break;


            case HandlePermission.MY_PERMISSIONS_REQUEST_READ_CONTACT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    List<Fragment> list = CureFull.getInstanse().getActivityIsntanse().getSupportFragmentManager().getFragments();
                    if (list != null) {
                        for (Fragment f : list) {
                            if (f != null && f instanceof FragmentLogin) {
                                f.onRequestPermissionsResult(requestCode, permissions, grantResults);
                            }
                        }
                    }

                }
                break;

            case HandlePermission.MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    List<Fragment> list = CureFull.getInstanse().getActivityIsntanse().getSupportFragmentManager().getFragments();
                    if (list != null) {
                        for (Fragment f : list) {
                            if (f != null && f instanceof FragmentPrescriptionCheckNew) {
                                f.onRequestPermissionsResult(requestCode, permissions, grantResults);
                            } else if (f != null && f instanceof FragmentLabTestReport) {
                                f.onRequestPermissionsResult(requestCode, permissions, grantResults);
                            } else if (f != null && f instanceof FragmentProfile) {
                                f.onRequestPermissionsResult(requestCode, permissions, grantResults);
                            }
                        }
                    }

                }
                break;

            case HandlePermission.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    List<Fragment> list = CureFull.getInstanse().getActivityIsntanse().getSupportFragmentManager().getFragments();
                    if (list != null) {
                        for (Fragment f : list) {
                            if (f != null && f instanceof FragmentPrescriptionCheckNew) {
                                f.onRequestPermissionsResult(requestCode, permissions, grantResults);
                            } else if (f != null && f instanceof FragmentLabTestReport) {
                                f.onRequestPermissionsResult(requestCode, permissions, grantResults);
                            } else if (f != null && f instanceof FragmentProfile) {
                                f.onRequestPermissionsResult(requestCode, permissions, grantResults);
                            }
                        }
                    }

                }
                break;

        }

    }

    public void iconAnim(View icon) {
        Animator iconAnim = ObjectAnimator.ofPropertyValuesHolder(icon,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.5f, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.5f, 1f));
        iconAnim.start();
    }


    private void takeScreenShot(View view) {

        int totalHeight = view.getHeight();
        int totalWidth = view.getWidth();

        Bitmap b = getBitmapFromView(view, totalHeight, totalWidth);

        myPath = new File(Environment.getExternalStorageDirectory(), "report.png");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            encodedImage = encodeToBase64(b, Bitmap.CompressFormat.JPEG, 100);
            fos.flush();
            fos.close();
            MediaStore.Images.Media.insertImage(getContentResolver(), b, "Screen", "screen");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {
        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }


    public void clickImage(final View view12) {
        view1 = view12;
    }

    public void shareClick() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri screenshotUri = Uri.fromFile(myPath);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, " " + AppPreference.getInstance().getUserName());
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Name:- " + AppPreference.getInstance().getUserName() + "\n" + "Mobile No:- " + AppPreference.getInstance().getMobileNumber() + "\n" + "Email Id:- " + AppPreference.getInstance().getUserID());
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        startActivity(Intent.createChooser(sharingIntent, "Share image using"));
    }

    @Override
    public void onStateChanged(int id, TransferState state) {
        Log.e("state", " " + state.name());
    }

    @Override
    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
        Log.e("bytesTotal", " " + bytesCurrent);
    }

    @Override
    public void onError(int id, Exception ex) {
        Log.e("error", "" + ex.getMessage());
    }

    @Override
    public void onClick(View view) {
        {

            switch (view.getId()) {
                case R.id.liner_medincine:
                    if (!AppPreference.getInstance().isFragmentMedicine()) {
                        CureFull.getInstanse().getActivityIsntanse().iconAnim(img_medicine);
                        liner_medincine.setBackgroundResource(R.drawable.button_mendinic_click);
                        liner_doctor_visit.setBackgroundResource(R.drawable.button_mendicine_unclick);
                        liner_lab_test.setBackgroundResource(R.drawable.button_mendicine_unclick);
                        img_medicine.setImageResource(R.drawable.medicine_icon_red);
                        txt_med.setTextColor(getResources().getColor(R.color.health_red_drawer));
                        img_doctor_visit.setImageResource(R.drawable.doctor_icon_yellow);
                        txt_doctor_visit.setTextColor(getResources().getColor(R.color.health_yellow));
                        img_lab_test.setImageResource(R.drawable.lab_icon_yellow);
                        txt_lab_test.setTextColor(getResources().getColor(R.color.health_yellow));
                        CureFull.getInstanse().getFlowInstanse()
                                .replace(new FragmentReminderMedicine(), false);
                    }

                    break;
                case R.id.liner_doctor_visit:
                    if (!AppPreference.getInstance().isFragmentDoctorVisit()) {
                        CureFull.getInstanse().getActivityIsntanse().iconAnim(img_doctor_visit);
                        liner_medincine.setBackgroundResource(R.drawable.button_mendicine_unclick);
                        liner_doctor_visit.setBackgroundResource(R.drawable.button_mendinic_click);
                        liner_lab_test.setBackgroundResource(R.drawable.button_mendicine_unclick);
                        img_medicine.setImageResource(R.drawable.medicine_icon_yellow);
                        txt_med.setTextColor(getResources().getColor(R.color.health_yellow));
                        img_doctor_visit.setImageResource(R.drawable.doctor_icon_red);
                        txt_doctor_visit.setTextColor(getResources().getColor(R.color.health_red_drawer));
                        img_lab_test.setImageResource(R.drawable.lab_icon_yellow);
                        txt_lab_test.setTextColor(getResources().getColor(R.color.health_yellow));
                        CureFull.getInstanse().getFlowInstanse()
                                .replace(new FragmentReminderDoctorVisit(), false);
                    }

                    break;
                case R.id.liner_lab_test:
                    if (!AppPreference.getInstance().isFragmentLabTs()) {
                        CureFull.getInstanse().getActivityIsntanse().iconAnim(img_lab_test);
                        liner_medincine.setBackgroundResource(R.drawable.button_mendicine_unclick);
                        liner_doctor_visit.setBackgroundResource(R.drawable.button_mendicine_unclick);
                        liner_lab_test.setBackgroundResource(R.drawable.button_mendinic_click);
                        img_medicine.setImageResource(R.drawable.medicine_icon_yellow);
                        txt_med.setTextColor(getResources().getColor(R.color.health_yellow));
                        img_doctor_visit.setImageResource(R.drawable.doctor_icon_yellow);
                        txt_doctor_visit.setTextColor(getResources().getColor(R.color.health_yellow));
                        img_lab_test.setImageResource(R.drawable.lab_icon_red);
                        txt_lab_test.setTextColor(getResources().getColor(R.color.health_red_drawer));
                        CureFull.getInstanse().getFlowInstanse()
                                .replace(new FragmentReminderLabTest(), false);
                    }

                    break;
                case R.id.txt_bottom_heath_app:
                    if (!AppPreference.getInstance().isFragmentHealthApp()) {
                        CureFull.getInstanse().getActivityIsntanse().iconAnim(img_health_app);
                        if (AppPreference.getInstance().isEditGoal()) {
                            CureFull.getInstanse().getFlowInstanse()
                                    .replace(new FragmentHealthAppNewProgress(), false);
                        } else {
                            CureFull.getInstanse().getFlowInstanse()
                                    .replace(new FragmentEditGoal(), false);

                        }
                    }

                    break;
                case R.id.txt_bottom_health_note:
                    if (!AppPreference.getInstance().isFragmentHealthNote()) {
                        CureFull.getInstanse().getActivityIsntanse().iconAnim(img_health_note);
                        if (AppPreference.getInstance().isEditGoalPage()) {
                            if (AppPreference.getInstance().isEditGoal()) {
                                CureFull.getInstanse().getFlowInstanse()
                                        .replace(new FragmentHealthNote(), false);
                            }
                        } else {
                            CureFull.getInstanse().getFlowInstanse()
                                    .replace(new FragmentHealthNote(), false);
                        }
                    }

                    break;
                case R.id.txt_bottom_home:
                    CureFull.getInstanse().getFlowInstanse()
                            .replace(new FragmentLandingPage(), false);
                    break;
                case R.id.txt_bottom_prescription:
                    if (!AppPreference.getInstance().isFragmentHealtpre()) {
                        CureFull.getInstanse().getActivityIsntanse().iconAnim(img_health_pre);
                        if (AppPreference.getInstance().isEditGoalPage()) {
                            if (AppPreference.getInstance().isEditGoal()) {
                                CureFull.getInstanse().getFlowInstanse()
                                        .replace(new FragmentPrescriptionCheckNew(), false);
                            }
                        } else {
                            CureFull.getInstanse().getFlowInstanse()
                                    .replace(new FragmentPrescriptionCheckNew(), false);
                        }
                    }


                    break;
                case R.id.txt_bottom_reports:
                    if (!AppPreference.getInstance().isFragmentHealtReprts()) {
                        CureFull.getInstanse().getActivityIsntanse().iconAnim(img_health_report);
                        if (AppPreference.getInstance().isEditGoalPage()) {
                            if (AppPreference.getInstance().isEditGoal()) {
                                CureFull.getInstanse().getFlowInstanse()
                                        .replace(new FragmentLabTestReport(), false);
                            }
                        } else {
                            CureFull.getInstanse().getFlowInstanse()
                                    .replace(new FragmentLabTestReport(), false);
                        }
                    }

                    break;

            }
        }

    }

    private class LongOperation extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            takeScreenShot(view1);
            shareClick();

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    public void isTobBarButtonVisible(boolean isback) {
        if (isback) {
            top_view.setVisibility(View.GONE);
        } else {
            liner_medincine.setBackgroundResource(R.drawable.button_mendinic_click);
            liner_doctor_visit.setBackgroundResource(R.drawable.button_mendicine_unclick);
            liner_lab_test.setBackgroundResource(R.drawable.button_mendicine_unclick);
            img_medicine.setImageResource(R.drawable.medicine_icon_red);
            txt_med.setTextColor(getResources().getColor(R.color.health_red_drawer));
            img_doctor_visit.setImageResource(R.drawable.doctor_icon_yellow);
            txt_doctor_visit.setTextColor(getResources().getColor(R.color.health_yellow));
            img_lab_test.setImageResource(R.drawable.lab_icon_yellow);
            txt_lab_test.setTextColor(getResources().getColor(R.color.health_yellow));
            top_view.setVisibility(View.VISIBLE);
        }
    }

    public void isbackButtonVisible(boolean isback, String check) {
        if (isback) {
            liner_bottom_view.setVisibility(View.GONE);
        } else {
            if (check.equalsIgnoreCase("Prescription")) {
                txt_bottom_heath_app.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_health_note.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_prescription.setBackgroundResource(R.drawable.button_mendinic_click);
                txt_bottom_reports.setBackgroundResource(R.drawable.button_mendicine_unclick);
                img_health_app.setImageResource(R.drawable.footer_healthapp_yellow);
                img_health_note.setImageResource(R.drawable.footer_healthnote_yellow);
                img_health_pre.setImageResource(R.drawable.footer_ehr_red);
                img_health_report.setImageResource(R.drawable.footer_report_yellow);
            } else if (check.equalsIgnoreCase("Lab Reports")) {
                txt_bottom_heath_app.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_health_note.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_prescription.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_reports.setBackgroundResource(R.drawable.button_mendinic_click);
                img_health_app.setImageResource(R.drawable.footer_healthapp_yellow);
                img_health_note.setImageResource(R.drawable.footer_healthnote_yellow);
                img_health_pre.setImageResource(R.drawable.footer_ehr_yellow);
                img_health_report.setImageResource(R.drawable.footer_report_red);
            } else if (check.equalsIgnoreCase("Health App")) {
                txt_bottom_heath_app.setBackgroundResource(R.drawable.button_mendinic_click);
                txt_bottom_health_note.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_prescription.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_reports.setBackgroundResource(R.drawable.button_mendicine_unclick);
                img_health_app.setImageResource(R.drawable.footer_healthapp_red);
                img_health_note.setImageResource(R.drawable.footer_healthnote_yellow);
                img_health_pre.setImageResource(R.drawable.footer_ehr_yellow);
                img_health_report.setImageResource(R.drawable.footer_report_yellow);
            } else if (check.equalsIgnoreCase("Note")) {
                txt_bottom_heath_app.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_health_note.setBackgroundResource(R.drawable.button_mendinic_click);
                txt_bottom_prescription.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_reports.setBackgroundResource(R.drawable.button_mendicine_unclick);
                img_health_app.setImageResource(R.drawable.footer_healthapp_yellow);
                img_health_note.setImageResource(R.drawable.footer_healthnote_red);
                img_health_pre.setImageResource(R.drawable.footer_ehr_yellow);
                img_health_report.setImageResource(R.drawable.footer_report_yellow);
            } else {
                txt_bottom_heath_app.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_health_note.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_prescription.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_reports.setBackgroundResource(R.drawable.button_mendicine_unclick);
                img_health_app.setImageResource(R.drawable.footer_healthapp_yellow);
                img_health_note.setImageResource(R.drawable.footer_healthnote_yellow);
                img_health_pre.setImageResource(R.drawable.footer_ehr_yellow);
                img_health_report.setImageResource(R.drawable.footer_report_yellow);
            }
            liner_bottom_view.setVisibility(View.VISIBLE);
        }
    }


    private void manageOnChatScreen(String type) {
        String tag = CureFull.getInstanse().getFlowInstanse().getCurrentFragmentTag();
        // Check if is no fragment on current screen
        if (tag == null || tag.trim().length() == 0) {
            showChatScreen();
            return;
        }
        Fragment f = getSupportFragmentManager().findFragmentByTag(tag);
        // Check the fragment on tag is null
        if (f == null) {
            showChatScreen();
            return;
        }
        // Check that the fragment
        if (f instanceof FragmentReminderMedicine) {
            showChatScreen();
            return;
        }
//        else if (f instanceof FragmentChat) {
//            onBackPressed();
//            showChatScreen(bundle, false);
//            return;
//        } else {
//            showChatScreen(bundle, true);
//        }
    }


    private void showChatScreen() {
        CureFull.getInstanse().getFlowInstanse()
                .replace(new FragmentReminderMedicine(), false);
//        CureFull.getInstanse().getFlowInstanse().replace(new FragmentReminderMedicine(),
//                false);
    }
}
