package item.property;

import org.json.JSONObject;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Lab_Test_Reminder_SelfListView {

    private String remMedicineName;
    private String doctorName;
    private int hour;
    private int mintue;

    public Lab_Test_Reminder_SelfListView() {

    }

    public Lab_Test_Reminder_SelfListView(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setDoctorName(jsonObject.getString("doctorName"));
            setRemMedicineName(jsonObject.getString("testName"));
            JSONObject jsonObject2 = new JSONObject(jsonObject.getString("labTestTime"));
            setHour(jsonObject2.getInt("hour"));
            setMintue(jsonObject2.getInt("minute"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getRemMedicineName() {
        return remMedicineName;
    }

    public void setRemMedicineName(String remMedicineName) {
        this.remMedicineName = remMedicineName;
    }


    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMintue() {
        return mintue;
    }

    public void setMintue(int mintue) {
        this.mintue = mintue;
    }
}
