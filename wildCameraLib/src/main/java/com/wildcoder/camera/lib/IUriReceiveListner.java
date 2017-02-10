package com.wildcoder.camera.lib;

import android.net.Uri;

public interface IUriReceiveListner {
	public void onPickImageComplete(int status, Uri imageUri);
}
