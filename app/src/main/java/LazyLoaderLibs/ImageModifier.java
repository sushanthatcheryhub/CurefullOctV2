package LazyLoaderLibs;

import android.graphics.Bitmap;


public class ImageModifier {

	public static Bitmap getImage(Bitmap mBitmap, ImageModifierType imageType) {
		if (mBitmap == null)
			return mBitmap;
		if (mBitmap.getWidth() == mBitmap.getHeight())
			return mBitmap;
		return getCroped(mBitmap, imageType);
	}

	private static Bitmap getCroped(Bitmap mBitmap, ImageModifierType imageType) {
		int size = mBitmap.getWidth();
		int offsetX = 0;
		int offsetY = 0;
		int inWidth = mBitmap.getWidth();
		int inHeight = mBitmap.getHeight();
		try {
			Bitmap croppedBmp = mBitmap;
			if (inWidth > inHeight) {
				offsetX = (inWidth - inHeight) / 2;
				size = mBitmap.getHeight();
			} else {
				offsetY = (inHeight - inWidth) / 2;
				size = mBitmap.getWidth();
			}
			try {
				switch (imageType) {
				case POSSIBLE_XY_OFFSET:
					croppedBmp = Bitmap.createBitmap(mBitmap, offsetX, offsetY,
							size, size);
					break;
				case DONT_SET_OFFSET:
					croppedBmp = Bitmap.createBitmap(mBitmap, 0, 0, size, size);
					break;
				case ENABLE_OFFSET_X_DISABLE_OFFSET_Y:
					croppedBmp = Bitmap.createBitmap(mBitmap, offsetX, 0, size,
							size);
					break;
				case DISABLE_OFFSET_X_DISABLE_ENABLE_Y:
					croppedBmp = Bitmap.createBitmap(mBitmap, 0, offsetY, size,
							size);
					break;
				default:
					break;
				}
				return croppedBmp;
			} catch (Exception e) {
				return mBitmap;
			}
		} catch (Exception e) {
			return mBitmap;
		}
	}
}
