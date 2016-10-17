package LazyLoaderLibs;

import android.widget.ImageView;

public class PhotoToLoad {
	public String url;
	public ImageView imageView;

	/**
	 * @author Wild Coder
	 * @param url
	 *            String
	 * @param view
	 *            ImageView
	 */
	public PhotoToLoad(String url, ImageView view) {
		this.url = url;
		this.imageView = view;
	}
}
