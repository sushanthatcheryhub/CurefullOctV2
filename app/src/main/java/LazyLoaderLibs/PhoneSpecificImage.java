package LazyLoaderLibs;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.widget.ImageView;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class PhoneSpecificImage {

    private MemoryCache memoryCache = new MemoryCache();
    private FileCache fileCache;

    private Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView, String>());
    private ExecutorService executorService;
    private Handler handler = new Handler();// handler to display images in UI

    private ILazyLoaderListner _lazy_loader_listner;
    private ImageType imageType = ImageType.DEFAULT_IMAGE;

    private int IMAGE_WIDTH = 0, IMAGE_HEIGHT = 0;

    /**
     * @param onLaziLoadingCompleteListner ILazyLoadingCompleteListner
     * @author Don't Worry
     */
    public void registerLoadingCompleteListner(
            ILazyLoaderListner onLaziLoadingCompleteListner) {
        this._lazy_loader_listner = onLaziLoadingCompleteListner;
    }

    public PhoneSpecificImage(FragmentActivity activity, ImageType imageType) {
        fileCache = new FileCache(activity);
        getSizeOfScreen(activity);
        this.imageType = imageType;
        executorService = Executors.newFixedThreadPool(5);
    }

    public void setSizesAccordingtoPhoneDisplay(int IMAGE_WIDTH,
                                                int IMAGE_HEIGHT) {
        this.IMAGE_WIDTH = IMAGE_WIDTH;
        this.IMAGE_HEIGHT = IMAGE_HEIGHT;
    }

    public void getSizeOfScreen(FragmentActivity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        IMAGE_WIDTH = dp2px(activity, size.x);
        IMAGE_HEIGHT = dp2px(activity, size.y);
    }

    private int dp2px(FragmentActivity activity, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                activity.getResources().getDisplayMetrics());
    }

    /**
     * @param url       String URL
     * @param imageView ImageView
     * @author Don't Worry
     */
    public void startLazyLoading(String url, ImageView imageView) {
        if (url == null)
            return;
        if (url.length() < 10 || imageView == null)
            return;

        Log.e("fff","ff");
        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            if (_lazy_loader_listner != null) {
                _lazy_loader_listner.onLaziLoadingComplete(true);
                _lazy_loader_listner.onLaziLoadingComplete(true, bitmap);
            }
        } else {
            queuePhoto(url, imageView);
            if (_lazy_loader_listner != null)
                _lazy_loader_listner.onLaziLoadingStarts();
        }
    }

    /**
     * @param url       Url
     * @param imageView ImageView
     * @author Don't Worry
     */
    private void queuePhoto(String url, ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    /**
     * @param url String Url
     * @author Don't Worry
     */
    private Bitmap getBitmap(String url) {
        File f = fileCache.getFile(url);

        // from SD cache
        Bitmap b = decodeFile(f);
        if (b != null)
            return b;

        // from web
        try {
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl
                    .openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            LoaderUtils.copyStream(is, os);
            os.close();
            conn.disconnect();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Throwable ex) {
            ex.printStackTrace();
            if (ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }

    // decodes image and scales it to reduce memory consumption

    /**
     * @param file File to decode
     * @author Wild Coder
     */
    private Bitmap decodeFile(File file) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(file);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            int scale = 1;

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inJustDecodeBounds = false;
            o2.inSampleSize = scale;

            FileInputStream stream2 = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            bitmap = getScalledBitmapAccordingly(bitmap);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap getScalledBitmapAccordingly(Bitmap myBitmap) {
        Bitmap returnedBitmap = null;
        ScaleImageAccordingly scaleBitmap = new ScaleImageAccordingly(
                IMAGE_WIDTH, IMAGE_HEIGHT);
        switch (imageType) {
            case FULL_SCREEN_IMAGE:
                returnedBitmap = scaleBitmap.scaleBitmapForFullImage(myBitmap);
                break;
            case THUMB_IMAGE:
                returnedBitmap = scaleBitmap.scaleBitmapForThumbImage(myBitmap);
                break;

            default:
                break;
        }

        if (returnedBitmap != null) {
        }

        return returnedBitmap;
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        /**
         * @param photoToLoad PhotoToLoad
         * @author Don't Worry
         */
        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            try {
                if (imageViewReused(photoToLoad))
                    return;
                Bitmap bmp = getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bmp);
                if (imageViewReused(photoToLoad))
                    return;
                BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    /**
     * @param photoToLoad PhotoToLoad
     * @return boolean imageViewReused
     * @author Don't Worry
     */
    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    // Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        /**
         * @param bitmapToCreate Bitmap
         * @param photOLoad      PhotoToLoad
         * @author Don't Worry
         */
        public BitmapDisplayer(Bitmap bitmapToCreate, PhotoToLoad photOLoad) {
            bitmap = bitmapToCreate;
            photoToLoad = photOLoad;
        }

        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null) {
                photoToLoad.imageView.setImageBitmap(bitmap);
                if (_lazy_loader_listner != null) {
                    _lazy_loader_listner.onLaziLoadingComplete(true);
                    _lazy_loader_listner.onLaziLoadingComplete(true, bitmap);
                }
            } else {
                if (_lazy_loader_listner != null)
                    _lazy_loader_listner.onLaziLoadingStarts();
            }
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
}
