package item.property;

import android.database.Cursor;

import org.json.JSONObject;

import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class EduationDetails {

    private String instituteName;
    private String instituteType;


    public EduationDetails() {

    }


    public EduationDetails(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setInstituteName(jsonObject.getString(MyConstants.JsonUtils.INSTITUTE_NAME));
            setInstituteType(jsonObject.getString(MyConstants.JsonUtils.INSTITUTE_TYPE));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getInstituteType() {
        return instituteType;
    }

    public void setInstituteType(String instituteType) {
        this.instituteType = instituteType;
    }
}
