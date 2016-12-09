package item.property;

import org.json.JSONObject;

import utils.MyConstants;

/**
 * Simple container object for contact data
 * <p/>
 * Created by mgod on 9/12/13.
 *
 * @author mgod
 */
public class PrescriptionDoctorName implements MyConstants.JsonUtils {
    String doctorName;

    public PrescriptionDoctorName(String s) {
        setDoctorName("" + s);
    }


    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
