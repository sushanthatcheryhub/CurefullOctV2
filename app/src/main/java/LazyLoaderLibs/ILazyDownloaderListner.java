package LazyLoaderLibs;

import java.io.File;

public interface ILazyDownloaderListner {

	/**
	 * @author Don't Worry
	 * @param isSuccess
	 *            boolean return loading result
	 */
	public void onLaziDownloadingComplete(boolean isSuccess, File file);

	/**
	 * @author Don't Worry
	 */
	public void onLaziDownloadingStarts();
}
