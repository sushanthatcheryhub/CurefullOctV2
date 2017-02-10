/*package com.wildcoder.camera.lib;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import src.info.androidhive.camerafileupload.Config;
import src.info.androidhive.camerafileupload.MainActivity;
import src.info.androidhive.camerafileupload.UploadActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class NewCameraFragment extends Fragment {
	// LogCat tag
	private static final String TAG = Fragment.class.getSimpleName();

	// Camera activity request codes
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	private Uri fileUri; // file url to store image/video

	private Button btnCapturePicture, btnRecordVideo;

	*//**
	 * Checking device has camera hardware or not
	 * *//*
	private boolean isDeviceSupportCamera() {
		if (getActivity().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	*//**
	 * Launching camera app to capture image
	 *//*
	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	
	*//**
	 * Here we store the file url as it will be null after returning from
	 * camera app
	 *//*
	
	 * @Override protected void onSaveInstanceState(Bundle outState) {
	 * super.onSaveInstanceState(outState);
	 * 
	 * // save file url in bundle as it will be null on screen orientation //
	 * changes outState.putParcelable("file_uri", fileUri); }
	 * 
	 * @Override protected void onRestoreInstanceState(Bundle
	 * savedInstanceState) { super.onRestoreInstanceState(savedInstanceState);
	 * // get the file url fileUri =
	 * savedInstanceState.getParcelable("file_uri"); }
	 
	*//**
	 * Receiving activity result method will be called after closing the camera
	 * *//*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				// successfully captured the image
				// launching upload activity
				launchUploadActivity(true);

			} else if (resultCode == Activity.RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(getActivity(), "User cancelled image capture",
						Toast.LENGTH_SHORT).show();
			} else {
				// failed to capture image
				Toast.makeText(getActivity(), "Sorry! Failed to capture image",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	*//**
	 * ------------ Helper Methods ----------------------
	 * *//*

	*//**
	 * Creating file uri to store image/video
	 *//*
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	*//**
	 * returning image / video
	 *//*
	private static File getOutputMediaFile() {

		// External sdcard location
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				Config.IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(TAG, "Oops! Failed create " + Config.IMAGE_DIRECTORY_NAME
						+ " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");

		return mediaFile;
	}
}
*/