package item.property;

import utils.MyConstants;

/**
 * Simple container object for contact data
 * <p/>
 * Created by mgod on 9/12/13.
 *
 * @author mgod
 */
public class MedicineReminderItem implements MyConstants.JsonUtils {
    private int id;
    private String doctorName = "";
    private String medicineName = "";
    private String quantityType="";
    private boolean baMealAfter;
    private boolean baMealBefore;
    private String type = "";
    private int interval;
    private boolean isShow;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public boolean isBaMealAfter() {
        return baMealAfter;
    }

    public void setBaMealAfter(boolean baMealAfter) {
        this.baMealAfter = baMealAfter;
    }

    public boolean isBaMealBefore() {
        return baMealBefore;
    }

    public void setBaMealBefore(boolean baMealBefore) {
        this.baMealBefore = baMealBefore;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }
}
