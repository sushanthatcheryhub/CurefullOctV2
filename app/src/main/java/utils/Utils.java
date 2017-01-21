package utils;

import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import curefull.healthapp.CureFull;

public class Utils {

    /**
     * Returns a proportion (n out of a total) as a percentage, in a float.
     */
    public static float getPercentage(int n, int total) {
        float proportion = ((float) n) / ((float) total);
        return proportion * 100;
    }

    public static float getPercentageValue(float percent, int total) {
        float proportion = percent / 100;
        return proportion * total;
    }

    public static double getCaloriesBurnt(double pounds, int steps) {
        double cal = (pounds * 0.30 * (0.0005 * steps));
        return cal;
    }

    public static float getIdealWeightMen(double inch) {
        float cal = (float) (50 + (2.3 * inch));
        return cal;
    }

    public static double getMlToLiter(int ml) {
        double cal = (ml * 0.001);
        return cal;
    }

    public static double getLiterToMl(int liter) {
        double cal = (liter * 1000);
        return cal;
    }

    public static double getLiterToMl(double liter) {
        double cal = (liter * 1000);
        return cal;
    }

    public static double getIdealWeightWomen(double inch) {
        double cal = (45.5 + (2.3 * inch));
        return cal;
    }

    public static double getConvertingPoundsIntoKilograms(double pounds) {
        double kilograms = pounds * 0.453592;
        return kilograms;
    }

    public static double getConvertingKilogramsIntoPounds(double kg) {
        double pounds = kg * 2.20462;
        return pounds;
    }


    public static String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();
        if (ageS.equalsIgnoreCase("-1"))
            ageS = "0";

        return ageS;
    }


    public static double convertFeetandInchesToCentimeter(String feet, String inches) {
        Log.e("convert", "feet " + feet + "inches " + inches);
        double heightInFeet = 0;
        double heightInInches = 0;
        try {
            if (feet != null && feet.trim().length() != 0) {
                heightInFeet = Double.parseDouble(feet);
            }
            if (inches != null && inches.trim().length() != 0) {
                heightInInches = Double.parseDouble(inches);
            }
        } catch (NumberFormatException nfe) {

        }

        return (heightInFeet * 30.48) + (heightInInches * 2.54);
    }


    public static String formatMonth(String month) throws ParseException {

        try {
            SimpleDateFormat monthParse = new SimpleDateFormat("MM");
            SimpleDateFormat monthDisplay = new SimpleDateFormat("MMM");
            return monthDisplay.format(monthParse.parse(month));
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static String getTodayDate() {
        String formattedDate = null;
        try {
            SimpleDateFormat initialformatter = new SimpleDateFormat(
                    "yyyy-MM-dd", Locale.getDefault());
            java.util.Date today = Calendar.getInstance().getTime();
            formattedDate = initialformatter.format(today);
            Log.e("", "formattedDate" + formattedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }


    public static String updateTime(int hours, int mins) {


        int selctHour = hours;

        String timeSet = "";
        if (selctHour > 12) {
            selctHour -= 12;
            timeSet = "pm";
        } else if (selctHour == 0) {
            selctHour += 12;
            timeSet = "am";
        } else if (selctHour == 12) {
            timeSet = "pm";
        } else {
            timeSet = "am";
        }

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(selctHour).append(" ").append(timeSet).toString();

        return aTime;
    }


    public static String updateTimeAMPM(int hours, int mins) {


        int selctHour = hours;

        String timeSet = "";
        if (selctHour > 12) {
            selctHour -= 12;
            timeSet = "pm";
        } else if (selctHour == 0) {
            selctHour += 12;
            timeSet = "am";
        } else if (selctHour == 12) {
            timeSet = "pm";
        } else {
            timeSet = "am";
        }

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(selctHour).append(":").append(minutes).append(" ").append(timeSet).toString();

        return aTime;
    }

    public static String getTodayTime() {
        String formattedDate = null;
        try {
            SimpleDateFormat initialformatter = new SimpleDateFormat(
                    "HH:mm", Locale.getDefault());
            java.util.Date today = Calendar.getInstance().getTime();
            formattedDate = initialformatter.format(today);
            Log.e("", "formattedDate" + formattedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }


    public static String uploadFile(String bucketName, String folderName, String fileName, File imageFile) {

        String imageUploadUrl = null;
    /*
    String yourAccessKeyID = "AKIAJN5QJUNXG75URSMA";
	String yourSecretAccessKey = "4C59wSvi9p3nGdHy31OVWdvdToJGz9xOJMPLZy/m";

	AWSCredentials credentials = new BasicAWSCredentials(yourAccessKeyID,  yourSecretAccessKey);*/
        AWSCredentials credentials = new BasicAWSCredentials("AKIAJN5QJUNXG75URSMA", "4C59wSvi9p3nGdHy31OVWdvdToJGz9xOJMPLZy/m");
        AmazonS3 s3client = new AmazonS3Client(credentials);
//        s3client.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
        try {

            if (!(s3client.doesBucketExist(bucketName))) {
                // Note that CreateBucketRequest does not specify region. So bucket is
                // created in the region specified in the client.
                s3client.createBucket(new CreateBucketRequest(
                        bucketName));
            }


        } catch (Exception e) {

        }
        TransferUtility transferUtility = new TransferUtility(s3client, CureFull.getInstanse().getActivityIsntanse());
        // Request server-side encryption.
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
        Log.e("imageFile", " " + imageFile.getAbsolutePath() + " " + imageFile.getName());
        TransferObserver observer = transferUtility.upload(
                "curefull.storage.test",
                imageFile.getName(),
                imageFile
        );
        observer.setTransferListener(CureFull.getInstanse().getActivityIsntanse());
//        TransferObserver observer = transferUtility.download(
//                "curefull.storage.test/cure.ehr",
//                "",
//                imageFile
//        );

//        for (Bucket bucket : s3client.listBuckets()) {
//            Log.e("Bucket list - ", bucket.getName());
//        }
        observer.refresh();
        return "";
    }
}