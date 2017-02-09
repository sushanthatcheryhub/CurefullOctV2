package utils;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class MyConstants {

    public interface WebUrls {
        String HOST_IP = "http://lp.hatcheryhub.com";
//                        String HOST_IP = "http://192.168.0.26:8080";
        //                        String HOST_IP = "http://" + AppPreference.getInstance().get_ip() + ":8080";
        String HOST_LOCATION = "/api/v1/";
//                        String HOST_LOCATION = "/CurefullRestService/api/v1/";
        String URL = HOST_IP + HOST_LOCATION;
        String API = "&project-app-key=rj8u5a826ilhk9rg1ghlxym6";
        String PRECRIPTION_IMAGE_PATH = "http://hatcheryhub.com/images/prescription/";
        String REPORT_IMAGE_PATH = "http://hatcheryhub.com/images/labReport/";
        String PROFILE_IMAGE_PATH = "http://hatcheryhub.com/images/profileImage/";
        String LOGIN = URL + "login/endUser";
        String FORGOT_NEW = URL + "forgotPassword/endUser/updatePassword";
        String FORGOT_SEND = URL + "forgotPassword/endUser/resetPassword";
        String SIGN_UP = URL + "signup/endUser";
        String FACEBOOK_SIGNUP = URL + "signup/endUserThroughFb";
        String HEALTH_NOTE_ADD = URL + "endUser/healthNote/addhealthNote";
        String OTP_WEB_SERVICE = "https://admagister.net/api/mt/SendSMS?user=curefull2016&password=123456&senderid=CURFUL&channel=trans&DCS=0&flashsms=0&number=";
        String OTP_MESSAGE = "&text=";
        String OTP_LAST = "&route=2";
        String HEALTH_LIST_NOTE = URL + "endUser/healthNote/listOfhealthNotes?";
        String HEALTH_LIST_DELETE = URL + "endUser/healthNote/deleteHealthNote?id=";
        String SET_GOALS = URL + "endUser/healthapp/updateRecommendedTarget";
        String SET_GOALS_DEATILS = URL + "endUser/healthapp/setHealthappProfileDetailsAndgetRecommendedTarget";
        String GET_SET_GOALS_DEATILS = URL + "endUser/healthapp/getgoal";
        String SAVE_HELTHAPP_DETALS = URL + "endUser/healthapp/savehealthappDailyDetails";
        String UPLOAD_PRESCRIPTION = URL + "endUser/ehr/uploadPrescription";
        String GET_PRESCRIPTION_LIST = URL + "endUser/ehr/getListOfPrescription";
        String DELETE_PRESCRIPTION = URL + "endUser/ehr/deletePrescriptionById?prescriptionId=";
        String DELETE_SUB_PRESCRIPTION = URL + "endUser/ehr/deletePrescriptionImagePartById?prescriptionId=";
        String UPLOAD_LAB_TEST_REPORT = URL + "endUser/ehr/uploadLabReport";
        String GET_LAB_TEST_REPORT_list = URL + "endUser/ehr/getListOfLabReport";
        String Delete_Report = URL + "endUser/ehr/deleteLabReportById?reportId=";
        String DELETE_SUB_LAB_REPORT = URL + "endUser/ehr/deleteLabReportImagePartById?reportId=";
        String SELECT_GLASS = URL + "endUser/healthapp/setGlassSizeForWaterIntake?glassSize=";
        String CfUuhidList = URL + "uuhid/getCfuuhidListOfEndUser";
        String CfUuhidUpload = URL + "uuhid/getCfuuhidFromNameAndMobileNumber";
        String GET_GRAPH = URL + "endUser/healthapp/gethealthappDetailsForGraphs";
        String CHECK_MOBILE_VALID = URL + "forgotPassword/endUser/getUserBasedOnMobileNumber?mobileNumber=";
        String CHECK_EMAIL_VALID = URL + "forgotPassword/endUser/sendPasswordResetLink?emailId=";
        String SELECTED_USER_LIST = URL + "uuhid/updateSelectedCfUuhid?cfuhid=";
        String DELETE_SELECTE_USER = URL + "uuhid/deleteCfUuhidFromMappingList?cfuhid=";
        String UPDATE_PROFILE = URL + "endUser/profile/updateProfileImage";
        String GET_HEALTH_DAILY_APP = URL + "endUser/healthapp/gethealthappDetails?date=";
        String UHID_SIGN_UP = URL + "uuhid/getCfuuhidListForSignup";
        String LOGOUT = URL + "logout/user";
        String RECOMMENDED_TARGETS_STEPS = URL + "endUser/healthapp/getRecommendedDailyExercise?gender=";
        String PROFILE_UPDATE = URL + "endUser/profile/updateProfileDetails";
        String PRESCRIPTION_FILTER_DATA = URL + "endUser/ehr/getPrescriptionFilterDataList?";
        String REPORTS_FILTER_DATA = URL + "endUser/ehr/getLabReportFilterDataList?";
        String INCRESE_WATER_INTAKE = URL + "endUser/healthapp/addWaterIntake?isWaterIntakeAdd=";
        String ADD_LIST_OF_HEALTH_NOTE = URL + "healthNote/addListOfHealthNote";
        String ADD_MEDICINE_REM = URL + "enduser/medicineService/addMedicineReminder";
        String EDIT_MEDICINE_REM = URL + "enduser/medicineService/editMedicineReminder";
        String GET_LIST_OF_MED = URL + "enduser/medicineService/getListOfMedicineDosageByDate?";
        String ADD_LAB_TEST_REM = URL + "endUser/labTestReminderService/addLabTestReminder";
        String GET_LIST_OF_LAB_TEST = URL + "endUser/labTestReminderService/getListOfLabTestReminder?";
        String ADD_DOCTOR_VISIT_REM = URL + "enduser/dotorFollowupService/addOrEditDoctorFollowupReminder";
        String GET_LIST_DOCTOR_VISIT_REM = URL + "enduser/dotorFollowupService/getDoctorFollowupReminder?";
        String GET_LIST_DOCTOR_NAME_MEDICINE = URL + "enduser/medicineService/getListOfDoctorNameForReminder?cfuuhId=";
        String GET_LIST_DOCTOR_NAME_DOCTOR_VISIT = URL + "enduser/dotorFollowupService/getListOfDoctorNameForFollowup?cfuuhId=";
        String GET_LIST_DOCTOR_NAME_LAB_TEST = URL + "endUser/labTestReminderService/getListOfDoctorNameForLabTest?cfuuhId=";

        String DOCTOR_VIsit_DELETE_ = URL + "enduser/dotorFollowupService/deleteDoctorFollowupReminder?doctorFollowupReminderId=";
        String LAB_TEST_DELETE_ = URL + "endUser/labTestReminderService/deleteLabTestReminder?labTestReminderId=";
        String MEDICINCE_DELETE_ = URL + "enduser/medicineService/deleteMedicineReminder?medicineReminderId=";

        String MEDICINCE_HISTORY_API = URL + "enduser/medicineService/historyOfMedicineReminder?cfuuhId=";

        //Upload AWS Image Prescription
        String SAVE_UPLOAD_PRESCRIPTION_METADATA = URL + "endUser/ehr/saveUploadPrescriptionMetadata";
        String TEMPORY_CREDENTIALS = URL + "aws/getTemporaryCredentials";
        String UPLOADED_PRESCRETION_DATA = URL + "endUser/ehr/saveUploadedPrescriptionData";

        //Upload AWS Image LabReport
        String SAVE_UPLOAD_LAB_REPORTS_METADATA = URL + "endUser/ehr/saveUploadLabReportMetadata";
        String UPLOADED_LAB_REPORTS_DATA = URL + "endUser/ehr/saveUploadedLabReportData";

        //Upload AWS Image Profile
        String UPLOADED_PROFILE = URL + "endUser/profile/updateProfileImage?profileImage=";

        //Notification
        String URL_NOTIFICATION = URL + "aws/registerUserForNotification";

        //Notification Coming
        String GET_NOTIFICATION_MEDICINE = URL + "enduser/medicineService/updateMedicineStatus";

        String GET_NOTIFICATION_DOCTOR = URL + "enduser/dotorFollowupService/updateDoctorFollowupReminderStatus";
        String GET_NOTIFICATION_LAB_TEST = URL + "endUser/labTestReminderService/updateLabTestReminderStatus";


        //Contact
        String GET_CONTACT = URL + "endUser/contact/sendMail";
    }

    public interface AWSType {
        String BUCKET_NAME = "cure.ehr.lp";
        String BUCKET_PROFILE_NAME = "cure.user.profile.lp";
        String FOLDER_PRECREPTION_NAME = "/prescription";
        String FOLDER_LAB_REPORT_NAME = "/labReport";
        String FOLDER_PROFILE_NAME = "/profileImages";

    }

    public interface CustomMessages {
        String No_INTERNET_USAGE = "No Internet Connection";
        String ISSUE_WITH_DATA = "Issue with data";
        String No_DATA = "No data";
        String ISSUES_WITH_SERVER = "Issue with sever error";
        String OFFLINE_MODE = "Your in Offline Mode";
    }


    public interface RequestType {
        int REGISTER = 1;
        int LOGIN = 2;
    }


    public interface JsonUtils {

        String PROFILE_IMAGE_URL = "profileImageUrl";
        String PREF_SUBJECTS_TIME_STAMP = "subject_timestamp";
        String PREF_CLASS_ID = "class_id";
        String PREF_CLASS = "class";
        String PREF_IS_LOGIN = "isAlreadyLogin";
        String LOGIN_FIRST = "login_first";
        String PROFILE_PIC = "pics";
        String PROFILE_ID = "id";
        String USER_NAME = "userName";
        String PASSWORD = "password";
        String AT = "a_t";
        String RT = "r_t";
        String MOBILE_NO = "mobileNumber";

        String EMAIL = "emailId";
        String USER_ROLE = "userRole";
        String RESEND_ID = "resendPasswordId";
        String RESEND_TYPE = "resendPasswordType";
        String HTTP_CODE = "httpStatusCode";
        String NAME = "name";
        String CFUUHIDs = "cfUuhid";
        String PRESCRIPTION_ID = "prescriptionId";
        String PRESCRIPTION_DATE = "prescriptionDate";
        String DOCTOR_NAME = "doctorName";
        String PRESCRIPTION_RESPONSE_LIST = "prescriptionResponseList";
        String LAB_REPORT_LIST = "reportImageList";
        String IMAGE_NUMBER = "imageNumber";
        String PRESCRIPTION_IMAGE = "prescriptionImage";
        String REPORT_IMAGE = "reportImage";
        String DISEASE_NAME = "diseaseName";
        String COUNT_OF_FILES = "countOfFiles";
        String UPLOAD_BY = "uploadedBy";
        String DATE_OF_UPLOAD = "dateOfUpload";

        String REPORT_ID = "reportId";
        String REPORT_DATE = "reportDate";
        String TEST_NAME = "testName";
        //Patient List
        String FNAME = "fname";
        String ID = "healthNoteId";
        String NOTE_DATE = "dateOfNote";
        String NOTE_TIME = "fromTime";
        String NOTE_TIME_TO = "toTime";
        String NOTE_HEADING = "subject";
        String NOTE_DEATILS = "details";
        String A_T = "a_t";
        String R_T = "r_t";
        String CF_UUHID = "cf_uuhid";
        String HEADERS = "headers";
        String USER_ID = "user_id";
        String USERNAME = "user_name";
        String INSTITUTE_NAME = "instituteName";
        String INSTITUTE_TYPE = "instituteType";
        String YEAR = "year";
        String JSON_KEY_PAYLOAD = "payload";
        String OK = "OK";
    }

    public interface IResponseCode {
        int RESPONSE_SUCCESS = 100;
        int RESPONSE_ALREADY_THERE = 202;
        int RESPONSE_ERROR = 201;
        int RESPONSE_SESSION = 301;
        int RESPONSE_DATABASE_NOT_FOUND = 100;
        int RESPONSE_JSON_ERROR = 401;
        int RESPONSE_JSON_ERROR_NEXT = 403;
    }

    public interface IDataBaseTableNames {
        String TABLE_USER_INFO = "user_info";
        String TABLE_DOCTOR = "doctor_list";
        String TABLE_PATIENT_DETAILS = "patient_list";

    }

    public interface IDataBaseTableKeys {
        String TABLE_NOTE = "note_master";
        String TABLE_LOGIN = "tbl_user";
        String TABLE_EMAIL = "tbl_emailId";
        String TABLE_EDIT_GOAL = "tbl_edit_goal";
        String TABLE_GRAPH = "tbl_graph";
        String TABLE_OFFLINE_NOTE = "offline_note_master";
        String TABLE_STEPS = "tbl_steps";
    }


    public interface IArrayData {
        String[] listPopUpDogase = {"1", "2", "3", "4", "5", "6"};
        String[] listPopUp = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] listPopUpWeightKgs = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100", "101", "102", "103", "104", "105", "106", "107", "108", "109"};
        String[] listPopUpWeightGms = {"100", "200", "300", "400", "500", "600", "700", "800", "900"};
        String[] listPopUpBloodGrp = {"0+", "0-", "A+", "A-", "B+", "B-", "AB+", "AB-"};
        String[] listPopUpHeight = {"Ft & In", "Cm"};
        String[] listPopUpWeight = {"Kgs", "Pounds"};
        String[] listUploadBy = {"Self", "CureFull"};
        String[] listStepsName = {"Steps", "Running", "Cycling"};
        String[] listDays = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        String[] mMonths = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String[] listDaily = {"011", "111", "222", "332", "344", "345", "634", "7343", "3438", "3439", "1340", "3411", "3412", "1343", "1344", "1345", "3416", "3417", "1348", "1349", "2340", "2341", "2234", "3423", "3424", "3425", "2643", "2734", "2834", "29343", "3340"};
        String[] listWeekly = {"340", "341", "342", "343", "434", "534", "634", "347"};
        String[] listMonths = {"100", "200", "300", "400", "500", "600", "700", "800", "900", "545", "123", "000"};

        int[] YEAR_2016 = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    }

    public static int getMonthName(String month) {
        switch (month) {
            case "Jan":
                return 1;

            case "Feb":
                return 2;

            case "Mar":
                return 3;

            case "Apr":
                return 4;

            case "May":
                return 5;

            case "Jun":
                return 6;

            case "Jul":
                return 7;

            case "Aug":
                return 8;

            case "Sep":
                return 9;

            case "Oct":
                return 10;

            case "Nov":
                return 11;

            case "Dec":
                return 12;
        }

        return 0;
    }
}
