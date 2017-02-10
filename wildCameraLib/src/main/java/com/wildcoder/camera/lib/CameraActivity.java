package com.wildcoder.camera.lib;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class CameraActivity extends Activity {
	public static final int PICK_FROM_CAMERA = 1;
	public static final int PICK_FROM_FILE = 2;
	protected final int PICK_FROM_CROP = 3;

	private AlertDialog dialog;
	private File file;
	private File dir;
	private ICameraListnerCropListner cameraListner;

	private int width, height;

	private String fileName = "my_image.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		height = displaymetrics.heightPixels;
		width = displaymetrics.widthPixels;
		loadDialog();
	}

	protected void startCamera() {
		if (dir == null)
			dir = new File(
					Environment.getExternalStorageDirectory().toString(), "/."
							+ getPackageName());
		if (!dir.exists())
			dir.mkdir();
		if (file == null)
			file = new File(dir, fileName);
		dialog.show();
	}

	protected void loadDialog() {
		final String[] items = new String[] { "Take from camera",
				"Select from gallery" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Select Image");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) { // pick from
																	// camera
				if (item == 0) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					try {
						intent.putExtra("return-data", true);
						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
				} else { // pick from file
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(Intent.createChooser(intent,
							"Complete action using"), PICK_FROM_FILE);
				}
			}
		});
		dialog = builder.create();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// System.out.println("result " + resultCode + "dfds" + requestCode);
		if (resultCode != RESULT_OK)
			return;
		Uri selectedImageUri;
		switch (requestCode) {
		case PICK_FROM_CAMERA:
			selectedImageUri = data.getData();
			if (cameraListner != null)
				cameraListner.onPickImageComplete(PICK_FROM_CAMERA,
						selectedImageUri);
			performCropImage(selectedImageUri);
			break;
		case PICK_FROM_FILE:
			selectedImageUri = data.getData();
			if (cameraListner != null)
				cameraListner.onPickImageComplete(PICK_FROM_FILE,
						selectedImageUri);
			performCropImage(selectedImageUri);
			break;
		case PICK_FROM_CROP:
			if (cameraListner != null) {
				cameraListner.onCropComplete(file.exists(), file.getPath());
			}
			break;

		}
	}

	private boolean performCropImage(Uri mFinalImageUri) {
		try {
			if (mFinalImageUri != null) {
				// call the standard crop action intent (the user device may not
				// support it)
				Intent cropIntent = new Intent("com.android.camera.action.CROP");
				// indicate image type and Uri
				cropIntent.setDataAndType(mFinalImageUri, "image/*");
				// set crop properties
				cropIntent.putExtra("crop", "true");
				// indicate aspect of desired crop
				cropIntent.putExtra("aspectX", 1);
				cropIntent.putExtra("aspectY", 1.6);
				cropIntent.putExtra("scale", true);
				// indicate output X and Y
				cropIntent.putExtra("outputX", width);
				cropIntent.putExtra("outputY", height);
				// retrieve data on return
				cropIntent.putExtra("return-data", false);
				File f = createNewFile();
				Uri mCropImagedUri = Uri.fromFile(f);
				cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImagedUri);
				// start the activity - we handle returning in onActivityResult
				startActivityForResult(cropIntent, PICK_FROM_CROP);
				return true;
			}
		} catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
			return false;
		}
		return false;
	}

	private File createNewFile() {
		if (file.exists()) {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * @author Don't Worry
	 * @param filename
	 *            String file name with extention
	 */
	protected void setFileName(String filename) {
		fileName = filename;
	}

	/**
	 * @author Don't Worry
	 * @param filename
	 *            String file name with extention
	 */
	protected void setFileDirectory(File filename) {
		dir = filename;
	}

	/**
	 * @author Don't Worry
	 * @param onICameraListner
	 *            ICameraListner
	 */
	protected void registerCameraListner(
			ICameraListnerCropListner onICameraListner) {
		cameraListner = onICameraListner;
	}

	/**
	 * @author Don't Worry
	 * @param width
	 *            int Width of image in pixel
	 * @param height
	 *            int Height of image in pixel
	 */
	protected void setImageSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
}
