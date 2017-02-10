package com.wildcoder.camera.lib;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class KitkatCameraBinderActivity extends Activity {

	// Camera activity request codes
	View view;
	private Uri fileUri; // file url to store image/video
	SharedPreferences mPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!isDeviceSupportCamera()) {
			Toast.makeText(getApplicationContext(),
					"Sorry! Your device doesn't support camera",
					Toast.LENGTH_LONG).show();
			// will close the app if the device does't have camera
			finish();
		}
		mPreferences = getSharedPreferences(Config.PREFRENCE, MODE_PRIVATE);
		view = new View(this);
		view.postDelayed(new Runnable() {

			@Override
			public void run() {
				captureImage();
			}
		}, 100);

	}

	/**
	 * Checking device has camera hardware or not
	 * */
	private boolean isDeviceSupportCamera() {
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	/**
	 * ------------ Helper Methods ----------------------
	 * */

	/**
	 * Creating file uri to store image/video
	 */
	public Uri getOutputMediaFileUri() {
		return Uri.fromFile(Config.getOutputMediaFile());
	}

	/**
	 * Launching camera app to capture image
	 */
	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri();

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		startActivityForResult(intent, Config.PICK_FROM_CAMERA);
	}

	/**
	 * Receiving activity result method will be called after closing the camera
	 * */
	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		Log.e("", "In Result");
		// super.onActivityResult(requestCode, resultCode, data);

		try {
			mPreferences.edit().putString(Config.PATH, fileUri.toString())
					.commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
		finish();
		// setResult(resultCode, data);

	}

	/**
	 * Here we store the file url as it will be null after returning from camera
	 * app
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// save file url in bundle as it will be null on screen orientation
		// changes
		outState.putParcelable("file_uri", fileUri);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
	}

}
