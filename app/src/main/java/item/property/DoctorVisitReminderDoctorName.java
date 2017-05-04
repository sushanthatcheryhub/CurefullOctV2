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
public class DoctorVisitReminderDoctorName implements MyConstants.JsonUtils {
    String doctorName;
    private ArrayList<Doctor_Visit_Reminder_DoctorListView> reminder_doctorListViews;

    public DoctorVisitReminderDoctorName(String s, Object value) {
        setDoctorName("" + s);
        setReminder_doctorListViews((List<Object>) value);
    }


    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public ArrayList<Doctor_Visit_Reminder_DoctorListView> getReminder_doctorListViews() {
        return reminder_doctorListViews;
    }

    public void setReminder_doctorListViews(ArrayList<Doctor_Visit_Reminder_DoctorListView> reminder_doctorListViews) {
        this.reminder_doctorListViews = reminder_doctorListViews;
    }

    public void setReminder_doctorListViews(List<Object> value) {
        Doctor_Visit_Reminder_DoctorListView card = null;
        this.reminder_doctorListViews = new ArrayList<Doctor_Visit_Reminder_DoctorListView>();
        for (int i = 0; i < value.size(); i++) {
            try {
                card = new Doctor_Visit_Reminder_DoctorListView(value.get(i));
                this.reminder_doctorListViews.add(card);
                card.getInsertingValue(value.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
