package item.property;

import android.graphics.Bitmap;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class LabReportImageList {

    private int imageNumber;
    private Bitmap reportImage;
    private boolean isChecked;

    public LabReportImageList() {

    }

    public int getImageNumber() {
        return imageNumber;
    }

    public void setImageNumber(int imageNumber) {
        this.imageNumber = imageNumber;
    }

    public Bitmap getReportImage() {
        return reportImage;
    }

    public void setReportImage(Bitmap report) {
        this.reportImage = reportImage;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
