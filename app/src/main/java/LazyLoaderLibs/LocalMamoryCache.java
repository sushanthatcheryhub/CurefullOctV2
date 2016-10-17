package LazyLoaderLibs;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LocalMamoryCache {

	private Map<Integer, Bitmap> cache = Collections
			.synchronizedMap(new LinkedHashMap<Integer, Bitmap>(10, 1.5f, true));// Last
																					// ordering
	private long size = 0;// current allocated size
	private long limit = 1000000;// max memory in bytes

	public LocalMamoryCache() {
		// use 25% of available heap size
		setLimit(Runtime.getRuntime().maxMemory() / 4);
	}

	/**
	 * @author Wild Coder
	 * @param new_limit
	 *            long New Limit
	 */
	public void setLimit(long new_limit) {
		limit = new_limit;
		// Log.i(TAG, "MemoryCache will use up to "+limit/1024./1024.+"MB");
	}

	/**
	 * @author Wild Coder
	 * @param id
	 *            String id
	 * @return Bitmap
	 */
	public Bitmap get(int id) {
		try {
			if (!cache.containsKey(id))
				return null;
			// NullPointerException sometimes happen here
			// http://code.google.com/p/osmdroid/issues/detail?id=78
			return cache.get(id);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * @author Wild Coder
	 * @param id
	 *            String id
	 * @param bitmap
	 *            Bitmap
	 */
	public void put(int id, Bitmap bitmap) {
		try {
			if (cache.containsKey(id))
				size -= getSizeInBytes(cache.get(id));
			cache.put(id, bitmap);
			size += getSizeInBytes(bitmap);
			checkSize();
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

	private void checkSize() {
		if (size > limit) {
			Iterator<Entry<Integer, Bitmap>> iter = cache.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<Integer, Bitmap> entry = iter.next();
				size -= getSizeInBytes(entry.getValue());
				iter.remove();
				if (size <= limit)
					break;
			}
		}
	}

	public void clear() {
		if (cache == null)
			return;
		try {
			cache.clear();
			size = 0;
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @author Wild Coder
	 * @param bitmap
	 *            Bitmap
	 */
	long getSizeInBytes(Bitmap bitmap) {
		if (bitmap == null)
			return 0;
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
}