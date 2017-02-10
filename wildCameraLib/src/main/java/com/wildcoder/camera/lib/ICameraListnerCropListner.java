package com.wildcoder.camera.lib;

import android.net.Uri;

public interface ICameraListnerCropListner {
	public void onCropComplete(boolean isSucces, String path);

	public void onPickImageComplete(int status, Uri imageUri);

}
