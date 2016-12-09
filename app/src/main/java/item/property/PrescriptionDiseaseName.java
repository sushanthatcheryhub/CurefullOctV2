package item.property;

import utils.MyConstants;

/**
 * Simple container object for contact data
 * <p/>
 * Created by mgod on 9/12/13.
 *
 * @author mgod
 */
public class PrescriptionDiseaseName implements MyConstants.JsonUtils {
    String diseaseName;

    public PrescriptionDiseaseName(String s) {
        setDiseaseName("" + s);
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }
}
