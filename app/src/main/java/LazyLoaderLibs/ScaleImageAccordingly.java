package LazyLoaderLibs;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ScaleImageAccordingly {

	private int IMAGE_WIDTH = 0, IMAGE_HEIGHT = 0;
	private int ThumbImageScale = 4;

	public ScaleImageAccordingly() {
	}

	public ScaleImageAccordingly(int IMAGE_WIDTH, int IMAGE_HEIGHT) {
		this.IMAGE_WIDTH = IMAGE_WIDTH;
		this.IMAGE_HEIGHT = IMAGE_HEIGHT;
	}

	public boolean checkForImageDimensions(Bitmap image_bitmap) {

		boolean isConvertBitmap = false;

		int width = image_bitmap.getWidth();
		int height = image_bitmap.getHeight();

		if (width <= IMAGE_WIDTH) {
			IMAGE_WIDTH = width;
			if (height <= IMAGE_HEIGHT) {
			} else {
				isConvertBitmap = true;
			}
		} else {
			isConvertBitmap = true;
		}

		if (height <= IMAGE_HEIGHT) {
			IMAGE_HEIGHT = height;
			if (width <= IMAGE_WIDTH) {
			} else {
				isConvertBitmap = true;
			}
		} else {
			isConvertBitmap = true;
		}
		return isConvertBitmap;
	}

	public Bitmap scaleBitmapForFullImage(Bitmap image_bitmap) {
		Bitmap croppedBitmap = null;
		if (!checkForImageDimensions(image_bitmap)) {
			return image_bitmap;
		} else {
			Matrix matrix = new Matrix();
			// resize the bit map
			matrix.postScale(this.IMAGE_WIDTH, this.IMAGE_HEIGHT);
			croppedBitmap = Bitmap.createScaledBitmap(image_bitmap,
					this.IMAGE_WIDTH, this.IMAGE_HEIGHT, false);
			return croppedBitmap;
		}
	}

	private boolean checkForImageThumb(Bitmap image_bitmap) {
		boolean isToScaleImage = false;

		int width = image_bitmap.getWidth();
		int height = image_bitmap.getHeight();

		if (width > (IMAGE_WIDTH / 2) || height > (IMAGE_HEIGHT / 2))
			isToScaleImage = true;

		return isToScaleImage;
	}

	public Bitmap scaleBitmapForThumbImage(Bitmap image_bitmap) {
		if (!checkForImageThumb(image_bitmap))
			return image_bitmap;
		int thumbHeight = image_bitmap.getHeight() / ThumbImageScale;
		int thumbWidth = image_bitmap.getWidth() / ThumbImageScale;
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(thumbHeight, thumbWidth);
		Bitmap croppedBitmap = Bitmap.createScaledBitmap(image_bitmap,
				thumbWidth, thumbHeight, false);
		return croppedBitmap;
	}

}
