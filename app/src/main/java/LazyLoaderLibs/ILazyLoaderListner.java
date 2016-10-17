package LazyLoaderLibs;

import android.graphics.Bitmap;

public interface ILazyLoaderListner {

    /**
     * @param isSuccess boolean return loading result
     * @author Wild Coder
     */
    public void onLaziLoadingComplete(boolean isSuccess);

    public void onLaziLoadingComplete(boolean isSuccess, Bitmap bitmap);

    /**
     * @author Wild Coder
     */
    public void onLaziLoadingStarts();
}
