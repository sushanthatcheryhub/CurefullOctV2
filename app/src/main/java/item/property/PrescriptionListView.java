package item.property;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import curefull.healthapp.CureFull;
import operations.DatabaseHelper;
import operations.DbOperations;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class PrescriptionListView implements MyConstants.JsonUtils {

    private String cfUuhid;
    private String prescriptionId;
    private String prescriptionDate;
    private String doctorName;
    private String diseaseName;
    private String countOfFiles;
    private String uploadedBy;
    private String dateOfUpload;
    private String common_id;
    private String isUploaded;

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {

        this.isUploaded = isUploaded;
    }

    private ArrayList<PrescriptionImageFollowUpListView> prescriptionImageFollowUpListViews;

    /*private String prescriptonImageFollowupId;

    private String imageNumber;

    private String prescriptionImage;
    private String prescriptionImagePartId;*/

   /* public void setImageNumber(String imageNumber) {
        this.imageNumber = imageNumber;
    }

    public void setPrescriptionImagePartId(String prescriptionImagePartId) {
        this.prescriptionImagePartId = prescriptionImagePartId;
    }

    public String getImageNumber() {
        return imageNumber;
    }

    public String getPrescriptionImagePartId() {
        return prescriptionImagePartId;
    }



    public String getPrescriptionImage() {
        return prescriptionImage;
    }

    public String getPrescriptonImageFollowupId() {
        return prescriptonImageFollowupId;
    }

    public void setPrescriptionImage(String prescriptionImage) {
        this.prescriptionImage = prescriptionImage;
    }

    public void setPrescriptonImageFollowupId(String prescriptonImageFollowupId) {
        this.prescriptonImageFollowupId = prescriptonImageFollowupId;
    }*/

    public PrescriptionListView() {
    }

    public PrescriptionListView(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setCfUuhid(jsonObject.getString(MyConstants.JsonUtils.CFUUHIDs));
            setPrescriptionId(jsonObject.getString(MyConstants.JsonUtils.PRESCRIPTION_ID));
            setPrescriptionDate(jsonObject.getString(MyConstants.JsonUtils.PRESCRIPTION_DATE));
            setDoctorName(jsonObject.getString(MyConstants.JsonUtils.DOCTOR_NAME));
//            setDiseaseName(jsonObject.getString(MyConstants.JsonUtils.DISEASE_NAME));
            setCountOfFiles(jsonObject.getString(MyConstants.JsonUtils.COUNT_OF_FILES));
            setUploadedBy(jsonObject.getString(MyConstants.JsonUtils.UPLOAD_BY));

//            setDateOfUpload(jsonObject.getString(MyConstants.JsonUtils.DATE_OF_UPLOAD));
            setPrescriptionImageFollowUpListViews(jsonObject.getJSONArray("prescriptionFollowupList"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PrescriptionListView(Cursor cur) {
        if (cur == null)
            return;
        try {
            setCfUuhid(cur.getString(cur.getColumnIndex(CFUUHIDs)));
            setPrescriptionId(cur.getString(cur.getColumnIndex(PRESCRIPTION_ID)));
            setPrescriptionDate(cur.getString(cur.getColumnIndex(PRESCRIPTION_DATE)));
            setDoctorName(cur.getString(cur.getColumnIndex(DOCTOR_NAME)));
            setCountOfFiles(cur.getString(cur.getColumnIndex(COUNT_OF_FILES)));
            setUploadedBy(cur.getString(cur.getColumnIndex(UPLOAD_BY)));
            setCommonID(cur.getString(cur.getColumnIndex(COMMON_ID)));
            setIsUploaded(cur.getString(cur.getColumnIndex("isUploaded")));
            ArrayList<PrescriptionImageFollowUpListView> prescriptionImageFollowUpListViews=DbOperations.setPrescriptionImageFollowUpListViewsLocal(CureFull.getInstanse().getActivityIsntanse(),cur.getString(cur.getColumnIndex(COMMON_ID)));
            setPrescriptionImageFollowUpListViews(prescriptionImageFollowUpListViews);
            //setPrescriptionImageFollowUpListViewsLocal(prescriptionImageFollowUpListViews);
            /*setPrescriptonImageFollowupId(cur.getString(cur.getColumnIndex(PRESCRIPTION_IMAGEFOLLOWUP_ID)));

            setPrescriptionImage(cur.getString(cur.getColumnIndex(PRESCRIPTION_IMAGE)));
            setImageNumber(cur.getString(cur.getColumnIndex(IMAGE_NUMBER)));
            setPrescriptionImagePartId(cur.getString(cur.getColumnIndex(PRESCRIPTION_IMAGEPARTID)));*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void uploadFilelocal(String prescriptionDate, String doctorName, String dieaseName, String cfUuhidId, List<PrescriptionImageList> imageFile,int countOfFiles,String uploadedBy) {

        String countoffilis=String.valueOf(countOfFiles);


            try {
                String commonid=String.valueOf(System.currentTimeMillis());
              /*  File fileUpload = new File(compressImage(imageFile.get(i).getPrescriptionImage()));
                String[] spiltName = new File(imageFile.get(i).getPrescriptionImage()).getName().split("\\.");
                String getName = spiltName[1];
                String name = cfUuhidId + "-" + imageFile.get(i).getImageNumber() + "." + getName;
*/
                ContentValues values = new ContentValues();

                values.put(PRESCRIPTION_DATE, prescriptionDate);

                values.put(DOCTOR_NAME, doctorName);
                values.put(CFUUHIDs, AppPreference.getInstance().getcf_uuhidNeew());
                values.put(COUNT_OF_FILES,countoffilis);
                values.put(UPLOAD_BY, "self");
                values.put("isUploaded", "1");
                values.put(COMMON_ID,commonid);
                values.put(PRESCRIPTION_ID,commonid);
                DbOperations.insertPrescriptionImage(CureFull.getInstanse().getActivityIsntanse(), values,"1",commonid);

                ArrayList<PrescriptionImageFollowUpListView> followup=new ArrayList<>();

                PrescriptionImageFollowUpListView imagelist=new PrescriptionImageFollowUpListView();
                imagelist.setCountOfFiles(countoffilis);
                imagelist.setPrescriptionDate(prescriptionDate);
                imagelist.setPrescriptonImageFollowupId(commonid);
                imagelist.setPrescriptionImageListViews(imageFile);

                //imagelist.setInsertingValue(countoffilis,prescriptionDate,commonid,commonid,imageFile);

                followup.add(imagelist);

                imagelist.setInsertingValue(followup);

            } catch (Exception e) {
                e.printStackTrace();
            }



    }


    // insert through online
    public void getInsertingValue(JSONObject json) throws JSONException {
        try {
            ContentValues values = new ContentValues();
            values.put(CFUUHIDs, json.getString(CFUUHIDs));
            values.put(PRESCRIPTION_ID, json.getString(PRESCRIPTION_ID));
            values.put(PRESCRIPTION_DATE, json.getString(PRESCRIPTION_DATE));
            values.put(DOCTOR_NAME, json.getString(DOCTOR_NAME));
            values.put(COUNT_OF_FILES, json.getString(COUNT_OF_FILES));
            values.put(UPLOAD_BY, json.getString(UPLOAD_BY));
            setCommonID(json.getString(PRESCRIPTION_ID));
            values.put(COMMON_ID, getCommonID());
            values.put("isUploaded", "0");

            DbOperations.insertPrescriptionList(CureFull.getInstanse().getActivityIsntanse(), values, AppPreference.getInstance().getcf_uuhidNeew(),getCommonID());

            setPrescriptionImageFollowUpListViewsLocal(json.getJSONArray(PRESCRIPTION_FOLLOWUPLIST),getCommonID());

            } catch (Exception e) {
            e.printStackTrace();

        }

    }


    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename ="";
        if(CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            filename = getFilename();
        }else{
            filename = getFilenameLocal();
        }
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 98, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }
    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "CureFull/Prescription");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public String getFilenameLocal() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "CureFull/PrescriptionLocal");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }
    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = CureFull.getInstanse().getActivityIsntanse().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
    public String getCfUuhid() {
        return cfUuhid;
    }

    public void setCfUuhid(String cfUuhid) {
        this.cfUuhid = cfUuhid;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(String prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }
    public void setCommonID(String common_id) {
        this.common_id = common_id;
    }
    public String getCommonID() {
        return common_id;
    }
    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getCountOfFiles() {
        return countOfFiles;
    }

    public void setCountOfFiles(String countOfFiles) {
        this.countOfFiles = countOfFiles;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getDateOfUpload() {
        return dateOfUpload;
    }

    public void setDateOfUpload(String dateOfUpload) {
        this.dateOfUpload = dateOfUpload;
    }

    public ArrayList<PrescriptionImageFollowUpListView> getPrescriptionImageFollowUpListViews() {
        return prescriptionImageFollowUpListViews;
    }

    public void setPrescriptionImageFollowUpListViews(ArrayList<PrescriptionImageFollowUpListView> prescriptionImageListViews) {
        this.prescriptionImageFollowUpListViews = prescriptionImageListViews;
    }

    public void setPrescriptionImageFollowUpListViews(JSONArray symptomslistArray) {
        if (symptomslistArray == null)
            return;
        PrescriptionImageFollowUpListView card = null;
        this.prescriptionImageFollowUpListViews = new ArrayList<PrescriptionImageFollowUpListView>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                card = new PrescriptionImageFollowUpListView(symptomslistArray.getJSONObject(i));
                this.prescriptionImageFollowUpListViews.add(card);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setPrescriptionImageFollowUpListViewsLocal(JSONArray symptomslistArray, String commonID) {
        if (symptomslistArray == null)
            return;
        PrescriptionImageFollowUpListView card = null;
        this.prescriptionImageFollowUpListViews = new ArrayList<PrescriptionImageFollowUpListView>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                card = new PrescriptionImageFollowUpListView(symptomslistArray.getJSONObject(i));
                this.prescriptionImageFollowUpListViews.add(card);

                card.getInsertingValue(symptomslistArray.getJSONObject(i),commonID);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
