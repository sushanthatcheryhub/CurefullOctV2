package com.wildcoder.camera.lib;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Environment;
import android.util.Log;

public class Config {
	public static final int PICK_FROM_CAMERA = 201;
	public static final int PICK_FROM_FILE = 202;
	public static final int PICK_FROM_CROP = 203;
	public static String DIRECTORY_NAME = "App Pics";
	public static String PREFRENCE = "CAMERA_PREFRENCE";
	public static String PATH = "path";

	/**
	 * returning image / video
	 */
	public static File getOutputMediaFile() {
		// External sdcard location
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("Camera Config", "Oops! Failed create " + DIRECTORY_NAME
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
