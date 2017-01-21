package item.property;

import java.util.ArrayList;
import java.util.List;

import utils.MyConstants;

/**
 * Simple container object for contact data
 * <p/>
 * Created by mgod on 9/12/13.
 *
 * @author mgod
 */
public class LabTestReminderDoctorName implements MyConstants.JsonUtils {
    String doctorName;
    private ArrayList<Lab_Test_Reminder_DoctorListView> reminder_doctorListViews;

    public LabTestReminderDoctorName(String s, Object value) {
        setDoctorName("" + s);
        setReminder_doctorListViews((List<Object>) value);
    }


    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public ArrayList<Lab_Test_Reminder_DoctorListView> getReminder_doctorListViews() {
        return reminder_doctorListViews;
    }

    public void setReminder_doctorListViews(ArrayList<Lab_Test_Reminder_DoctorListView> reminder_doctorListViews) {
        this.reminder_doctorListViews = reminder_doctorListViews;
    }

    public void setReminder_doctorListViews(List<Object> value) {
        Lab_Test_Reminder_DoctorListView card = null;
        this.reminder_doctorListViews = new ArrayList<Lab_Test_Reminder_DoctorListView>();
        for (int i = 0; i < value.size(); i++) {
            try {
                card = new Lab_Test_Reminder_DoctorListView(value.get(i));
                this.reminder_doctorListViews.add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
