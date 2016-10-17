package LazyLoaderLibs;

import android.content.Context;

import java.io.File;

public class FileCache {

	private File cacheDir;

	/**
	 * @author Wild Coder
	 * @param context
	 *            Context
	 */
	public FileCache(Context context) {

		if (context == null)
			return;
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"Android/" + context.getPackageResourcePath());
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	/**
	 * @author Wild Coder
	 * @param url
	 *            String url to load
	 */
	public File getFile(String url) {
		// I identify images by hashcode. Not a perfect solution, good for the
		// demo.
		String filename = String.valueOf(url.hashCode());
		// Another possible solution (thanks to grantland)
		// String filename = URLEncoder.encode(url);
		File f = new File(cacheDir, filename);
		return f;

	}

	/**
	 * @Void remove cache
	 * */
	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}

}