package item.property;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

import curefull.healthapp.CureFull;
import operations.DbOperations;
import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class PrescriptionImageFollowUpListView implements Parcelable, MyConstants.JsonUtils {

    private String prescriptionImage;
    private String prescriptonImageFollowupId;
    private String prescriptionDate;
    private String countOfFiles;
    private ArrayList<PrescriptionImageListView> prescriptionImageListViews;
    private List<PrescriptionImageList> imageFile;
    public PrescriptionImageFollowUpListView() {

    }

    public PrescriptionImageFollowUpListView(Cursor cur,String common_id) {

        if (cur == null)
            return;
        try {
            setPrescriptonImageFollowupId(cur.getString(cur.getColumnIndex(PRESCRIPTION_IMAGEFOLLOWUP_ID)));
            ArrayList<PrescriptionImageListView> prescriptionImageListViews=DbOperations.setPrescriptionResponseListViewsLocal(CureFull.getInstanse().getActivityIsntanse(),common_id);
            setPrescriptionImageListViews(prescriptionImageListViews);

        }catch (Exception e){
            e.getMessage();
        }
    }

    public PrescriptionImageFollowUpListView(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setCountOfFiles(jsonObject.getString("countOfFiles"));
            setPrescriptionDate(jsonObject.getString("prescriptionDate"));
//            setPrescriptionImage(jsonObject.getString(MyConstants.JsonUtils.PRESCRIPTION_IMAGE));
            setPrescriptonImageFollowupId(jsonObject.getString("prescriptonImageFollowupId"));
            setPrescriptionImageListViews(jsonObject.getJSONArray(MyConstants.JsonUtils.PRESCRIPTION_RESPONSE_LIST));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getPrescriptionImage() {
        return prescriptionImage;
    }

    public void setPrescriptionImage(String prescriptionImage) {
        this.prescriptionImage = prescriptionImage;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.prescriptionDate);
        dest.writeString(this.prescriptionImage);
        dest.writeString(this.prescriptonImageFollowupId);
        dest.writeString(this.countOfFiles);
    }

    public PrescriptionImageFollowUpListView(Parcel in) {
        this.prescriptionDate = in.readString();
        this.prescriptionImage = in.readString();
        this.prescriptonImageFollowupId = in.readString();
        this.countOfFiles = in.readString();
    }

    public static final Creator<PrescriptionImageFollowUpListView> CREATOR = new Creator<PrescriptionImageFollowUpListView>() {
        public PrescriptionImageFollowUpListView createFromParcel(Parcel in) {
            return new PrescriptionImageFollowUpListView(in);
        }

        public PrescriptionImageFollowUpListView[] newArray(int size) {
            return new PrescriptionImageFollowUpListView[size];
        }
    };

    public String getPrescriptonImageFollowupId() {
        return prescriptonImageFollowupId;
    }

    public void setPrescriptonImageFollowupId(String prescriptonImageFollowupId) {
        this.prescriptonImageFollowupId = prescriptonImageFollowupId;
    }

    public String getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(String prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public String getCountOfFiles() {
        return countOfFiles;
    }

    public void setCountOfFiles(String countOfFiles) {
        this.countOfFiles = countOfFiles;
    }

    public ArrayList<PrescriptionImageListView> getPrescriptionImageListViews() {
        return prescriptionImageListViews;
    }

    public void setPrescriptionImageListViews(ArrayList<PrescriptionImageListView> prescriptionImageListViews) {
        this.prescriptionImageListViews = prescriptionImageListViews;
    }

    public void setPrescriptionImageListViews(JSONArray symptomslistArray) {
        if (symptomslistArray == null)
            return;
        PrescriptionImageListView card = null;
        this.prescriptionImageListViews = new ArrayList<PrescriptionImageListView>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                card = new PrescriptionImageListView(symptomslistArray.getJSONObject(i));
                this.prescriptionImageListViews.add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setPrescriptionImageListViewsLocal(JSONArray symptomslistArray,String commonID) {
        if (symptomslistArray == null)
            return;
        PrescriptionImageListView card = null;
        this.prescriptionImageListViews = new ArrayList<PrescriptionImageListView>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                card = new PrescriptionImageListView(symptomslistArray.getJSONObject(i));
                this.prescriptionImageListViews.add(card);

                card.getInsertingValue(symptomslistArray.getJSONObject(i),commonID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getInsertingValue(JSONObject jsonobject,String commonID) {
        try {
            ContentValues values1 = new ContentValues();

            values1.put(COUNT_OF_FILES, jsonobject.getString(COUNT_OF_FILES));
            values1.put(PRESCRIPTION_DATE, jsonobject.getString(PRESCRIPTION_DATE));
            values1.put(PRESCRIPTION_IMAGEFOLLOWUP_ID, jsonobject.getString(PRESCRIPTION_IMAGEFOLLOWUP_ID));
            values1.put(COMMON_ID, commonID);
            DbOperations.insertPrescriptionFollowUPList(CureFull.getInstanse().getActivityIsntanse(), values1, commonID, jsonobject.getString(PRESCRIPTION_IMAGEFOLLOWUP_ID));

            setPrescriptionImageListViewsLocal(jsonobject.getJSONArray(PRESCRIPTION_RESPONSE_LIST),commonID);
        }catch (Exception e){

        }

    }

    //generate from local
    /*public void setInsertingValue(String countoffilis,String prescriptionDate,String prescriptonImageFollowupId,String commonid,List<PrescriptionImageList> imageFile) {
        try {



                ContentValues values2 = new ContentValues();
                values2.put(COUNT_OF_FILES, countoffilis);
                values2.put(PRESCRIPTION_DATE, prescriptionDate);
                values2.put(PRESCRIPTION_IMAGEFOLLOWUP_ID, prescriptonImageFollowupId);
                values2.put(COMMON_ID, commonid);


                DbOperations.insertPrescriptionFollowUPListLocal(CureFull.getInstanse().getActivityIsntanse(), values2, commonid);

                PrescriptionImageListView imagelist=new PrescriptionImageListView();
                imagelist.setInsertingValue(imageFile,commonid);


        } catch (Exception e) {

        }
    }
*/

    public void setPrescriptionImageListViews(List<PrescriptionImageList> imageFile) {
        this.imageFile=imageFile;
    }

    public List<PrescriptionImageList>  getPrescriptionImageList()
    {
        return imageFile;
    }
    public void setInsertingValue(ArrayList<PrescriptionImageFollowUpListView> followup) {

        ContentValues values2 = new ContentValues();
        values2.put(COUNT_OF_FILES, followup.get(0).getCountOfFiles());
        values2.put(PRESCRIPTION_DATE,followup.get(0).getPrescriptionDate());
        values2.put(PRESCRIPTION_IMAGEFOLLOWUP_ID, followup.get(0).getPrescriptonImageFollowupId());
        values2.put(COMMON_ID, followup.get(0).getPrescriptonImageFollowupId());


        DbOperations.insertPrescriptionFollowUPListLocal(CureFull.getInstanse().getActivityIsntanse(), values2, followup.get(0).getPrescriptonImageFollowupId());

        PrescriptionImageListView imagelist=new PrescriptionImageListView();
        imagelist.setInsertingValue(imageFile,followup.get(0).getPrescriptonImageFollowupId());

    }
}
