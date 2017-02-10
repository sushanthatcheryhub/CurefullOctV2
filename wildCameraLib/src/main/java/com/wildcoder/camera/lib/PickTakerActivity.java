package com.wildcoder.camera.lib;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;

public class PickTakerActivity extends Activity {
	public static final int PICK_FROM_CAMERA = 0;
	public static final int PICK_FROM_FILE = 1;

	private AlertDialog dialog;
	private File file;
	private File dir;
	private IUriReceiveListner uriListner;

	private String fileName = "my_image.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
				Intent intent;
				switch (item) {
				case PICK_FROM_CAMERA:
					intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					try {
						intent.putExtra("return-data", true);
						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
					break;
				case PICK_FROM_FILE:

					try {
						intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(Intent.createChooser(intent,
								"Complete action using"), PICK_FROM_FILE);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}
		});
		dialog = builder.create();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;
		Uri selectedImageUri;
		switch (requestCode) {
		case PICK_FROM_CAMERA:
			selectedImageUri = data.getData();
			if (uriListner != null)
				uriListner.onPickImageComplete(PICK_FROM_CAMERA,
						selectedImageUri);
			break;
		case PICK_FROM_FILE:
			selectedImageUri = data.getData();
			if (uriListner != null)
				uriListner
						.onPickImageComplete(PICK_FROM_FILE, selectedImageUri);
			break;
		}
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
	protected void registerUriReceiveListner(IUriReceiveListner onICameraListner) {
		uriListner = onICameraListner;
	}

}
