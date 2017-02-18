package com.wildcoder.camera.lib;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class CameraFragment extends Fragment implements
        OnSharedPreferenceChangeListener {

    private AlertDialog dialog;
    private File file;
    private File dir;
    private ICameraListnerCropListner cameraListner;

    private int width = 300;

    private boolean isFixedHeightWidth = false;
    private String dialogTitle = "Select Image";
    private String dialogMessage = "Chose any option";

    private String fileName = "my_image.jpg";
    Uri mImageCaptureUri;
    private SharedPreferences mPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = getActivity().getSharedPreferences(Config.PREFRENCE,
                Activity.MODE_PRIVATE);
        mPreferences.registerOnSharedPreferenceChangeListener(this);
        intitaliseCamera();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri;
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("dd", "RESULT_CANCELED -");
            return;
        }
        switch (requestCode) {
            case Config.PICK_FROM_CAMERA:
                if (data != null)
                    selectedImageUri = data.getData();
                else {
                    Log.e("dsd", "Data is Null");
                    selectedImageUri = mImageCaptureUri;
                }
                Log.e("sds", "image uri-" + selectedImageUri);
                if (cameraListner != null)
                    cameraListner.onPickImageComplete(Config.PICK_FROM_CAMERA,
                            selectedImageUri);
                performCropImage(selectedImageUri);
                break;
            case Config.PICK_FROM_FILE:
                selectedImageUri = data.getData();
                if (cameraListner != null)
                    cameraListner.onPickImageComplete(Config.PICK_FROM_FILE,
                            selectedImageUri);
                performCropImage(selectedImageUri);
                break;
            case Config.PICK_FROM_CROP:
                if (cameraListner != null) {
                    cameraListner.onCropComplete(file.exists(), file.getPath());
                }
                break;
        }
    }

    protected void startCamera() {
        intitaliseCamera();
        dialog.show();
    }

    protected void loadDialog() {
        final String[] items = new String[]{"Take Snapshots",
                "Choose From Photos", "Cancel"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.select_dialog_item, items);
        Builder builder = new Builder(getActivity());
        builder.setTitle(dialogTitle);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        startCapture();
                        break;
                    case 1:
                        pickFromGallery();
                        break;
                    default:
                        if (dialog != null)
                            dialog.dismiss();
                        break;
                }
            }
        });
        dialog = builder.create();
    }

    /*
     * public void startCapture() {
     *
     * if (android.os.Build.VERSION.SDK_INT < 19) { Intent
     * intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
     * startActivityForResult(intent, Config.PICK_FROM_CAMERA); } else {
     * startCameraForKitkat(); } }
     */
    public void startCapture() {
        try {
            Intent intent = new Intent(getActivity(),
                    KitkatCameraBinderActivity.class);
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    /**
     * @Void Picking image from gallery request Code 1
     */
    public void pickFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        if (android.os.Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(intent, "Complete action using"),
                    Config.PICK_FROM_FILE);
        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(
                    Intent.createChooser(intent, "Complete action using"),
                    Config.PICK_FROM_FILE);
        }
    }

    private void intitaliseCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("return-data", true);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;
        loadDialog();
        dir = new File(Environment.getExternalStorageDirectory(), "/AppPics/");
        if (!dir.exists())
            dir.mkdir();
        if (file == null)
            file = new File(dir, fileName);
        File toSameMainImage = Config.getOutputMediaFile();
        mImageCaptureUri = Uri.fromFile(toSameMainImage);
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

                if (isFixedHeightWidth) {
                    cropIntent.putExtra("aspectX", 1);
                    cropIntent.putExtra("aspectY", 1);
                    cropIntent.putExtra("outputX", width);
                    cropIntent.putExtra("outputY", width);
                    cropIntent.putExtra("scale", true);
                } else {
                    cropIntent.putExtra("scale", false);
                }

                // retrieve data on return
                cropIntent.putExtra("return-data", false);
                File f = createNewFile();


                Uri mCropImagedUri = Uri.fromFile(f);

                cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImagedUri);
                // start the activity - we handle returning in onActivityResult
                startActivityForResult(cropIntent, Config.PICK_FROM_CROP);
                return true;
            }
        } catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage,
                    Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return false;
    }

    private File createNewFile() {
        dir = new File(Environment.getExternalStorageDirectory(), "/AppPics/");
        if (!dir.exists())
            dir.mkdir();
        if (file == null)
            file = new File(dir, fileName);
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

    /**
     * @param filename String file name with extention
     * @author Don't Worry
     */
    protected void setFileName(String filename) {
        fileName = filename;
    }

    /**
     * @param filename String file name with extention
     * @author Don't Worry
     */
    protected void setFileDirectory(File filename) {
        dir = filename;
    }

    /**
     * @param onICameraListner ICameraListner
     * @author Don't Worry
     */
    public void registerCameraListner(ICameraListnerCropListner onICameraListner) {
        cameraListner = onICameraListner;
    }

    /**
     * @param width  int Width of image in pixel
     * @param height int Height of image in pixel
     * @author Don't Worry
     */
    protected void setImageSize(int width, int height) {
        this.width = width;
    }

    /**
     * @return the dialogTitle
     */
    public String getDialogTitle() {
        return dialogTitle;
    }

    /**
     * @param dialogTitle the dialogTitle to set
     */
    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    /**
     * @return the dialogMessage
     */
    public String getDialogMessage() {
        return dialogMessage;
    }

    /**
     * @param dialogMessage the dialogMessage to set
     */
    public void setDialogMessage(String dialogMessage) {
        this.dialogMessage = dialogMessage;
    }

    /**
     * @return the isFixedHeightWidth
     */
    public boolean isFixedHeightWidth() {
        return isFixedHeightWidth;
    }

    /**
     * @param isFixedHeightWidth the isFixedHeightWidth to set
     * @param value              Integer
     */
    public void setFixedHeightWidth(boolean isFixedHeightWidth, int value) {
        this.isFixedHeightWidth = isFixedHeightWidth;
        this.width = value;
    }

    /**
     * @param isFixedHeightWidth the isFixedHeightWidth to set
     * @param value              Integer
     */
    public void setFixedHeightWidth(boolean isFixedHeightWidth) {
        this.isFixedHeightWidth = isFixedHeightWidth;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
        String path = arg0.getString(Config.PATH, "");
        Log.e("", "Path-" + path);
        if (path == null || path.length() == 0)
            return;
        Uri uri = Uri.parse(path);
        mImageCaptureUri = uri;
        if (cameraListner != null)
            cameraListner.onPickImageComplete(Config.PICK_FROM_CAMERA, uri);
        performCropImage(uri);
    }
}
