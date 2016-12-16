package curefull.healthapp;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.RemoteInput;
import android.content.DialogInterface;
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
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fragment.healthapp.FragmentHomeScreenAll;
import fragment.healthapp.FragmentLandingPage;
import fragment.healthapp.FragmentLogin;
import utils.AppPreference;
import utils.CircularImageView;
import utils.MyConstants;
import utils.NotificationUtils;

public class MainActivity extends BaseMainActivity {

    static {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    //    private Toolbar toolbar;
    private DrawerLayout drawer;
    public static final String TAG = "MainActivity";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private int location_Permission, phone_Permission,
            storageRead_Permission, storageWrite_Permission, read_contact, camera, sms;
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 940;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private GoogleApiClient mClient = null;
    private RelativeLayout relative_logo, relative_action_bar;
    private ActionBarDrawerToggle toggle;
    private ProgressBar progress_bar;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    private TextView txt_doctor_name_edu;
    private ImageView img_drawer, img_share;
    private CircularImageView circularImageView;
    private LinearLayout liner_logout;
    private NavigationView navigationView;
    SharedPreferences preferences;
    private File myPath;
    String encodedImage;
    private View view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CureFull.getInstanse().initActivity(this);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        liner_logout = (LinearLayout) findViewById(R.id.liner_logout);
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
        if (checkAndRequestPermissions()) {
        }
        changeTitle("cureFull");
//        Intent serviceIntent = new Intent(this, LocationService.class);
//        startService(serviceIntent);

        if (getIntent().getAction() != null) {
            Toast.makeText(this, getIntent().getAction(), Toast.LENGTH_SHORT).show();
        } else {
            if (AppPreference.getInstance().isLogin()) {
                CureFull.getInstanse().getFlowInstanse().clearBackStack();
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentHomeScreenAll(), false);
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


        liner_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppPreference.getInstance().clearAllData();
                AppPreference.getInstance().setIsLogin(false);
                CureFull.getInstanse().getFlowInstanseAll().clearBackStack();
                CureFull.getInstanse().getFlowInstanse().clearBackStack();
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentLogin(), false);
            }
        });

        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenShot(view1);
                shareClick();
            }
        });

//        Toast.makeText(this, getMessageText(getIntent()), Toast.LENGTH_SHORT).show();

//        if (checkAndRequestPermissions()) {
//            CureFull.getInstanse().getFlowInstanse().clearBackStack();
//            CureFull.getInstanse().getFlowInstanse()
//                    .replace(new FragmentLogin(), false);
//        }else{
//            CureFull.getInstanse().getFlowInstanse().clearBackStack();
//            CureFull.getInstanse().getFlowInstanse()
//                    .replace(new FragmentLogin(), false);
//        }


        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            Uri data = intent.getData();

            Log.e("action", "" + action);
            if (data != null) {

                Log.e("data", "" + data.toString());
            }
        }


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
        CureFull.getInstanse().getSmallImageLoader().clearCache();
        CureFull.getInstanse().getSmallImageLoader().startLazyLoading(MyConstants.WebUrls.HOST_IP + "/CurefullWeb-0.0.1/resources/images/profileImage/" + name, circularImageView);
    }


    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(NotificationUtils.KEY_TEXT_REPLY);
        }
        Toast.makeText(this, "Remoteinput is null", Toast.LENGTH_SHORT).show();

        return null;
    }

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
        Log.e("bundle", "push event :- " + action);
    }


    private boolean checkAndRequestPermissions() {
        location_Permission = ContextCompat.checkSelfPermission(CureFull.getInstanse().getActivityIsntanse(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        phone_Permission = ContextCompat.checkSelfPermission(CureFull.getInstanse().getActivityIsntanse(),
                android.Manifest.permission.READ_PHONE_STATE);
        storageRead_Permission = ContextCompat.checkSelfPermission(CureFull.getInstanse().getActivityIsntanse(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE);
        storageWrite_Permission = ContextCompat.checkSelfPermission(CureFull.getInstanse().getActivityIsntanse(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        read_contact = ContextCompat.checkSelfPermission(CureFull.getInstanse().getActivityIsntanse(),
                Manifest.permission.READ_CONTACTS);
        camera = ContextCompat.checkSelfPermission(CureFull.getInstanse().getActivityIsntanse(),
                Manifest.permission.CAMERA);
        sms = ContextCompat.checkSelfPermission(CureFull.getInstanse().getActivityIsntanse(),
                Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<String>();

        if (location_Permission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (phone_Permission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (storageRead_Permission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (storageWrite_Permission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (read_contact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(android.Manifest.permission.READ_CONTACTS);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.CAMERA);
        }
        if (sms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.READ_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(CureFull.getInstanse().getActivityIsntanse(), listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            // checksume = 1;
            return false;
        }
        // checksume = 2;
        return true;
    }

    private void showDialogOK(String message,
                              DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(CureFull.getInstanse().getActivityIsntanse()).setMessage(message).setCancelable(false)
                .setPositiveButton("OK", okListener).show();
    }

    public void startFitService() {
        Intent intent = new Intent(this, FitGoogleService.class);
        startService(intent);
    }


    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.e(TAG, "Displaying permission rationale to provide additional context.");
        } else {
            Log.e(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.READ_CONTACTS,
                        PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION,
                        PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_PHONE_STATE,
                        PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.CAMERA,
                        PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_SMS,
                        PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && perms.get(android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                CureFull.getInstanse().getActivityIsntanse(), android.Manifest.permission.CAMERA)
                                || ActivityCompat
                                .shouldShowRequestPermissionRationale(
                                        CureFull.getInstanse().getActivityIsntanse(),
                                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                                || ActivityCompat
                                .shouldShowRequestPermissionRationale(
                                        CureFull.getInstanse().getActivityIsntanse(),
                                        android.Manifest.permission.READ_PHONE_STATE)
                                || ActivityCompat
                                .shouldShowRequestPermissionRationale(
                                        CureFull.getInstanse().getActivityIsntanse(),
                                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                || ActivityCompat
                                .shouldShowRequestPermissionRationale(
                                        CureFull.getInstanse().getActivityIsntanse(),
                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat
                                .shouldShowRequestPermissionRationale(
                                        CureFull.getInstanse().getActivityIsntanse(),
                                        android.Manifest.permission.CAMERA) || ActivityCompat
                                .shouldShowRequestPermissionRationale(
                                        CureFull.getInstanse().getActivityIsntanse(),
                                        android.Manifest.permission.READ_SMS)) {
                            showDialogOK("Permission Required For This App",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:

                                                    CureFull.getInstanse().getActivityIsntanse().runOnUiThread(new Runnable() {

                                                        @Override
                                                        public void run() {
                                                            checkAndRequestPermissions();
                                                        }
                                                    });

                                                    break;

                                            }
                                        }
                                    });
                        }
                    }
                }
            }

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
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Name:- " + AppPreference.getInstance().getUserName() + "\n" + "Mobile No:- 9654052212" + "\n" + "Email Id:- sushant@gmail.com");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        startActivity(Intent.createChooser(sharingIntent, "Share image using"));
    }
}
