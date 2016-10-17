/**
 *@author Wild Coder
 */
package LazyLoaderLibs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;


/**
 * @author Wild Coder
 * 
 */
public class ImageViewMessuredSize extends ImageView {

	private float myWidh = 0;
	private float myHeight = 0;

	/**
	 * @param context
	 */
	public ImageViewMessuredSize(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ImageViewMessuredSize(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ImageViewMessuredSize(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	private void setImage(final Bitmap bitmap) {
		ViewTreeObserver vto = getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
				setMyWidh(getMeasuredWidth());
				setMyHeight(getMeasuredHeight());
				if (onUIMessureListner != null)
					onUIMessureListner.onMessureds(getMeasuredWidth(),
							getMeasuredHeight());
				if (bitmap != null)
					setCropedImage(bitmap);
			}
		});
	}

	public void setCropImageResource(int resourceId) {
		Bitmap bitmap = BitmapFactory
				.decodeResource(getResources(), resourceId);
		setCropedImageBitmap(bitmap);
	}

	public void setCropedImageBitmap(Bitmap bitmap) {
		if (getMyWidh() == 0 && getMyHeight() == 0) {
			setImage(bitmap);
		} else {
			setCropedImage(bitmap);
		}
	}

	private void setCropedImage(Bitmap bitmap) {
		setScaleType(ScaleType.CENTER_CROP);
		setImageBitmap(bitmap);
	}

	/**
	 * @author Wild Coder
	 * @return the myWidh
	 */
	public float getMyWidh() {
		return myWidh;
	}

	/**
	 * @author Wild Coder
	 * @param myWidh
	 *            the myWidh to set
	 */
	public void setMyWidh(float myWidh) {
		Log.e("myWidh", myWidh + "");
		this.myWidh = myWidh;
	}

	/**
	 * @author Wild Coder
	 * @return the myHeight
	 */
	public float getMyHeight() {
		return myHeight;
	}

	/**
	 * @author Wild Coder
	 * @param myHeight
	 *            the myHeight to set
	 */
	public void setMyHeight(float myHeight) {
		Log.e("myHeight:", myHeight + "");
		this.myHeight = myHeight;
	}

	/**
	 * @author Wild Coder
	 * @return the onUIMessureListner
	 */
	public IOnUIMessureListner getOnUIMessureListner() {
		return onUIMessureListner;
	}

	/**
	 * @author Wild Coder
	 * @param onUIMessureListner
	 *            the onUIMessureListner to set
	 */
	public void setOnUIMessureListner(IOnUIMessureListner onUIMessureListner) {
		this.onUIMessureListner = onUIMessureListner;
		setImage(null);
	}

	private IOnUIMessureListner onUIMessureListner;

}
