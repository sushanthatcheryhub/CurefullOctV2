package curefull.healthapp;


import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.io.IOException;

import LazyLoaderLibs.ImageType;
import LazyLoaderLibs.PhoneSpecificImage;
import operations.DatabaseHelper;
import utils.FlowOrganizer;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */

public class CureFull extends Application {

    private FlowOrganizer _flow;
    private FlowOrganizer _flowAll;
    private MainActivity _activity;
    private static CureFull _application;
    private RequestQueue requestQueue;
    public final String TAG = CureFull.class.getName();
    //    private DatabaseHelper _db_configuration;
    private Typeface opensansTypefaceRegular;
    private Typeface opensansTypefaceLine;
    private Typeface opensansTypefaceBold;
    private Typeface opensansTypefaceSemiBold;

    public boolean isEditext() {
        return isEditext;
    }

    private PhoneSpecificImage fullImageLoader;

    public void setEditext(boolean editext) {
        isEditext = editext;
    }

    private boolean isEditext;

    @Override
    public void onCreate() {
        super.onCreate();
        _application = this;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        _application = this;
//        requestQueue = Volley.newRequestQueue(getApplicationContext());
//        _db_configuration = new DatabaseHelper.Builder(getApplicationContext())
//                .setName("EPrescriptionMobileApp.sqlite").build();
//        try {
//            _db_configuration.createDataBase();
//        } catch (IOException e) {
//        }
//    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    public void onDestroy() {
        _application = null;
    }


    public void initActivity(MainActivity _activity) {
        this._activity = _activity;
    }

    public MainActivity getActivityIsntanse() {
        return _activity;
    }


    public FlowOrganizer getFlowInstanse() {
        if (_flow == null) {
            _flow = new FlowOrganizer(_activity, R.id.framge_parent);
        }
        return _flow;
    }

    //
    public FlowOrganizer getFlowInstanseAll() {
        if (_flowAll == null) {
            _flowAll = new FlowOrganizer(_activity, R.id.framge_child);

        }
        return _flowAll;
    }

    public PhoneSpecificImage getFullImageLoader() {
        if (fullImageLoader == null) {
            fullImageLoader = new PhoneSpecificImage(_activity,
                    ImageType.THUMB_IMAGE);
        }
        return fullImageLoader;
    }


    public static synchronized CureFull getInstanse() {
        if (_application == null) {
            _application = new CureFull();
        }
        return _application;
    }


    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancel() {
        requestQueue.cancelAll(TAG);
    }

//    public DatabaseHelper getDatabaseHelperInstance(Context context) {
//        if (_db_configuration == null)
//            _db_configuration = new DatabaseHelper.Builder(context).setName(
//                    "EPrescriptionMobileApp.sqlite").build();
//        try {
//            _db_configuration.createDataBase();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return _db_configuration;
//    }


    public Typeface getOpenSansRegular(Context context) {
        if (opensansTypefaceRegular == null)
            opensansTypefaceRegular = Typeface.createFromAsset(context.getAssets(),
                    "Montserrat-Regular.ttf");
        return opensansTypefaceRegular;
    }

    public Typeface getMontserratHairLine(Context context) {
        if (opensansTypefaceLine == null)
            opensansTypefaceLine = Typeface.createFromAsset(context.getAssets(),
                    "montserrat-hairline.ttf");
        return opensansTypefaceLine;
    }

    public Typeface getOpenSansBold(Context context) {
        if (opensansTypefaceBold == null)
            opensansTypefaceBold = Typeface.createFromAsset(context.getAssets(),
                    "Montserrat-Bold.ttf");
        return opensansTypefaceBold;
    }

    public Typeface getOpenSansSemiBold(Context context) {
        if (opensansTypefaceSemiBold == null)
            opensansTypefaceSemiBold = Typeface.createFromAsset(context.getAssets(),
                    "OpenSans-Semibold.ttf");
        return opensansTypefaceSemiBold;
    }
}
