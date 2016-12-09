package interfaces;

import java.util.List;

import item.property.PrescriptionImageList;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public interface IOnDoneMoreImage {

    public void optDoneMoreImage(String doctorName, String dieaseName, String prescriptionDate, List<PrescriptionImageList> prescriptionImageListss);
}
