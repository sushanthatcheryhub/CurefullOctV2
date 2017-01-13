package curefull.healthapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */

public class SplashScreen extends Activity {
    private Handler mHandler;
    private LinearLayout loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        mHandler = new Handler();
        mHandler.postDelayed(mUpdate, 3000);
    }

    /**
     * @category Loading Home
     */
    private void loadHome() {
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
        destroyHandler();
        finish();
    }

    /**
     * @category Loader Visiblity
     *
     */


    /**
     * @category Destroy Handler
     */
    private void destroyHandler() {
        if (mHandler != null && mUpdate != null) {
            mHandler.removeCallbacks(mUpdate);
        }
    }

    // runnable
    Runnable mUpdate = new Runnable() {
        @Override
        public void run() {
            loadHome();

        }
    };

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}