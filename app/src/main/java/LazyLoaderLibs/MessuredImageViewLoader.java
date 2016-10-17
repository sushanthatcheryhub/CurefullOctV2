package LazyLoaderLibs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

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

public class MessuredImageViewLoader {

	private MemoryCache memoryCache = new MemoryCache();
	private FileCache fileCache;

	private Map<ImageViewMessuredSize, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageViewMessuredSize, String>());
	private ExecutorService executorService;
	private Handler handler = new Handler();// handler to display images in UI

	private ILazyLoaderListner _lazy_loader_listner;

	/**
	 * @author Don't Worry
	 * @param onLaziLoadingCompleteListner
	 *            ILazyLoadingCompleteListner
	 */
	public void registerLoadingCompleteListner(
			ILazyLoaderListner onLaziLoadingCompleteListner) {
		this._lazy_loader_listner = onLaziLoadingCompleteListner;
	}

	/**
	 * @author Don't Worry
	 * @param context
	 *            Context
	 * @param defaultResourceId
	 *            Default Resource id
	 */
	public MessuredImageViewLoader(Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	/**
	 * @author Don't Worry
	 * @param url
	 *            String URL
	 * @param imageView
	 *            ImageViewMessuredSize
	 */
	public void startLazyLoading(String url, ImageViewMessuredSize imageView) {
		if (url.length() < 10)
			return;
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			imageView.setCropedImageBitmap(bitmap);
			if (_lazy_loader_listner != null)
				_lazy_loader_listner.onLaziLoadingComplete(true);
		} else {
			queuePhoto(url, imageView);
			if (_lazy_loader_listner != null)
				_lazy_loader_listner.onLaziLoadingStarts();
		}
	}

	/**
	 * @author Don't Worry
	 * @param url
	 *            Url
	 * @param imageView
	 *            ImageViewMessuredSize
	 */
	private void queuePhoto(String url, ImageViewMessuredSize imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	/**
	 * @author Don't Worry
	 * @param url
	 *            String Url
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

	// decodes image and scales it to reduce memory consumption
	/**
	 * @author Wild Coder
	 * @param file
	 *            File to decode
	 * */
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
			bitmap = getSquare(bitmap);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Bitmap getSquare(Bitmap myBitmap) {
		int size = myBitmap.getWidth();
		int offsetX = 0;
		int offsetY = 0;
		int inWidth = myBitmap.getWidth();
		int inHeight = myBitmap.getHeight();
		if (inWidth == inHeight)
			return myBitmap;
		Bitmap croppedBmp = myBitmap;

		if (inWidth > inHeight) {
			offsetX = (inWidth - inHeight) / 2;
			size = myBitmap.getHeight();

		} else {
			offsetY = (inHeight - inWidth) / 2;
			size = myBitmap.getWidth();
		}
		try {
			croppedBmp = Bitmap.createBitmap(myBitmap, offsetX, offsetY, size,
					size);
		} catch (Exception e) {
			return myBitmap;
		}
		return croppedBmp;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageViewMessuredSize imageView;

		/**
		 * @author Don't Worry
		 * @param url_to_proceed
		 *            String url
		 * @param _imageView
		 *            ImageViewMessuredSize
		 */
		public PhotoToLoad(String url_to_proceed,
				ImageViewMessuredSize _imageView) {
			url = url_to_proceed;
			imageView = _imageView;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		/**
		 * @author Don't Worry
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
	 * @author Don't Worry
	 * @param photoToLoad
	 *            PhotoToLoad
	 * @return boolean imageViewReused
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
		 * @author Don't Worry
		 * @param bitmapToCreate
		 *            Bitmap
		 * @param photOLoad
		 *            PhotoToLoad
		 */
		public BitmapDisplayer(Bitmap bitmapToCreate, PhotoToLoad photOLoad) {
			bitmap = bitmapToCreate;
			photoToLoad = photOLoad;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {
				photoToLoad.imageView.setCropedImageBitmap(bitmap);
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
