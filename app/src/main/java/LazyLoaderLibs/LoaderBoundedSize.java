package LazyLoaderLibs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;
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

public class LoaderBoundedSize {

	private MemoryCache memoryCache = new MemoryCache();
	private FileCache fileCache;
	private Context mContext;

	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	private ExecutorService executorService;
	private Handler handler = new Handler();// handler to display images in UI
											// thread

	/**
	 * @author Wild Coder
	 * @param context
	 *            Context
	 */
	public LoaderBoundedSize(Context context) {
		fileCache = new FileCache(context);
		mContext = context;
		executorService = Executors.newFixedThreadPool(5);
	}

	private ILazyLoaderListner _lazy_loader_listner;

	/**
	 * @author Wild Coder
	 * @param onLaziLoadingCompleteListner
	 *            ILazyLoaderListner
	 */
	public void registerLazyLoaderListner(ILazyLoaderListner lazyLaoderListner) {
		this._lazy_loader_listner = lazyLaoderListner;
	}

	/**
	 * @author Wild Coder
	 * @param url
	 *            String url of image
	 * @param imageView
	 *            ImageView to show
	 */
	public void startLazyLoading(String url, ImageView imageView) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			if (_lazy_loader_listner != null)
				_lazy_loader_listner.onLaziLoadingComplete(true);
		} else {
			queuePhoto(url, imageView);
			if (_lazy_loader_listner != null)
				_lazy_loader_listner.onLaziLoadingStarts();
		}

	}

	/**
	 * @author Wild Coder
	 * @param url
	 *            String url to put in queue
	 * @param imageView
	 *            ImageView
	 */

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	/**
	 * @author Wild Coder
	 * @param url
	 *            String url to put in queue
	 * @return Bitmap from url
	 * 
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
	@SuppressWarnings("deprecation")
	/**
	 * @author Wild Coder
	 * @param _file
	 *            File
	 *            @return Bitmap
	 * 
	 */
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			WindowManager wm = (WindowManager) mContext
					.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			float height = display.getHeight();
			int scale = 1;
			int IMAGE_MAX_SIZE = (int) (height / 2.7f);
			scale = (int) Math.pow(
					2,
					(int) Math.round(Math.log(IMAGE_MAX_SIZE
							/ (double) Math.max(o.outHeight, o.outWidth))
							/ Math.log(0.5)));

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inJustDecodeBounds = false;
			o2.inSampleSize = scale;

			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);

			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		/**
		 * @author Wild Coder
		 * @param photoToLoad
		 *            PhotoToLoad
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
	 * @author Wild Coder
	 * @param photoToLoad
	 *            PhotoToLoad
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
		 * @author Wild Coder
		 * @param _bitmap
		 *            Bitmap
		 * @param _photoToLoad
		 *            PhotoToLoad
		 * 
		 */
		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {
				photoToLoad.imageView.setImageBitmap(bitmap);
				if (_lazy_loader_listner != null)
					_lazy_loader_listner.onLaziLoadingComplete(true);
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
