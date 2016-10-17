package LazyLoaderLibs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadImageFromUrl {

    private ExecutorService executorService;
    private Handler handler = new Handler();// handler to display images in UI
    private String AppFolderName = ".AvonCaches";
    private ILazyDownloaderListner _lazy_download_listner;
    private String fileName = Long.toString(System.currentTimeMillis());

    public void registerLoadingCompleteListner(
            ILazyDownloaderListner _lazy_download_listner) {
        this._lazy_download_listner = _lazy_download_listner;
    }

    public DownloadImageFromUrl(Context context) {
        executorService = Executors.newFixedThreadPool(5);
    }

    public void startLazyDownloading(final String url, File file) {
        try {
            if (file == null)
                return;
            if (!file.exists()) {
                Log.e("file.exists()", "file.exists()");
                fileName = file.getName();
                loadPhoto(url);
                if (_lazy_download_listner != null)
                    _lazy_download_listner.onLaziDownloadingStarts();
            } else {
                Log.e("file.exists() not", "file.exists() not");
                if (_lazy_download_listner != null)
                    _lazy_download_listner
                            .onLaziDownloadingComplete(true, file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param url       String URL
     * @param imageView ImageView
     * @author Don't Worry
     */

    private void loadPhoto(String url) {
        Log.e("load photo", "load photo");
        PhotoToLoad p = new PhotoToLoad(url);
        executorService.submit(new PhotosLoader(p));
    }

    /**
     * @param url String Url
     * @author Don't Worry
     */
    private File getFile(String url) {

        // if (!url.startsWith("http")) {
        // byte[] decodedString = Base64.decode(url, Base64.DEFAULT);
        // return BitmapFactory.decodeByteArray(decodedString, 0,
        // decodedString.length);
        //
        // }

        // from web
        try {

            Log.e("getFile", "getFile");
            File storagePath = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + AppFolderName + "/");
            storagePath.mkdirs();

            final File myImage = new File(storagePath, fileName);
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl
                    .openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(myImage);
            LoaderUtils.copyStream(is, os);
            os.close();
            conn.disconnect();
            return myImage;
        } catch (Throwable ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // decodes image and scales it to reduce memory consumption

    /**
     * @param file File to decode
     * @author Don't Worry
     */
    public Bitmap decodeFile(File file) {
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

            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Task for the queue
    private class PhotoToLoad {
        public String url;

        public PhotoToLoad(String url_to_proceed) {
            url = url_to_proceed;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        /**
         * @param photoToLoad PhotoToLoad
         * @author Don't Worry
         */
        PhotosLoader(PhotoToLoad photoToLoad) {
            Log.e("photoToLoad", "photoToLoad");
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            try {
                Log.e("run", "run");
                File file = getFile(photoToLoad.url);
                if (file == null)
                    return;

                Log.e("file not null", "file not null " + file);
                BitmapDisplayer bd = new BitmapDisplayer(file, photoToLoad);
                handler.post(bd);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    // Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        File file;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(File fil, PhotoToLoad photOLoad) {
            file = fil;
            photoToLoad = photOLoad;
            Log.e("BitmapDisplayer", "BitmapDisplayer");
        }

        public void run() {
            if (file != null) {
                Log.e("BitmapDisplayer file not null",
                        "BitmapDisplayer file not null");
                if (_lazy_download_listner != null)
                    _lazy_download_listner
                            .onLaziDownloadingComplete(true, file);
            } else {
                Log.e("BitmapDisplayer file null", "BitmapDisplayer file null");
                if (_lazy_download_listner != null)
                    _lazy_download_listner.onLaziDownloadingComplete(false,
                            null);
            }
        }
    }

}
