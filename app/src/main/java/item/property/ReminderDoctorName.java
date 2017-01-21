package item.property;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utils.MyConstants;

/**
 * Simple container object for contact data
 * <p/>
 * Created by mgod on 9/12/13.
 *
 * @author mgod
 */
public class ReminderDoctorName implements MyConstants.JsonUtils {
    String doctorName;
    private ArrayList<Reminder_DoctorListView> reminder_doctorListViews;

    public ReminderDoctorName(String s, Object value) {
        setDoctorName("" + s);
        setReminder_doctorListViews((List<Object>) value);
    }


    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public ArrayList<Reminder_DoctorListView> getReminder_doctorListViews() {
        return reminder_doctorListViews;
    }

    public void setReminder_doctorListViews(ArrayList<Reminder_DoctorListView> reminder_doctorListViews) {
        this.reminder_doctorListViews = reminder_doctorListViews;
    }

    public void setReminder_doctorListViews(List<Object> value) {
        Reminder_DoctorListView card = null;
        this.reminder_doctorListViews = new ArrayList<Reminder_DoctorListView>();
        for (int i = 0; i < value.size(); i++) {
            try {
                card = new Reminder_DoctorListView(value.get(i));
                this.reminder_doctorListViews.add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
