package LazyLoaderLibs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class LocalImageLoader {
	private Context context;
	private LocalMamoryCache memoryCache = new LocalMamoryCache();

	public LocalImageLoader(Context context) {
		this.context = context;
	}

	public void loadImage(int imageResId, ImageView imageView) {
		Bitmap bitmap = getBitmapFromMemCache(imageResId);
		if (bitmap == null) {
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					imageResId);
			addBitmapToMemoryCache(imageResId, bitmap);
		}
		imageView.setImageBitmap(bitmap);
	}

	private void addBitmapToMemoryCache(int key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			memoryCache.put(key, bitmap);
		}
	}

	private Bitmap getBitmapFromMemCache(int key) {
		return memoryCache.get(key);
	}

	public void clear() {
		if (memoryCache != null)
			memoryCache.clear();
	}

}
