package utils;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class MyConstants {

    public interface WebUrls {
        String HOST_IP = "http://127.0.0.1:8080";
        String HOST_LOCATION = "/CurefullRestService-0.0.1/api/v1/";
        String URL = HOST_IP + HOST_LOCATION;
        String API = "&project-app-key=rj8u5a826ilhk9rg1ghlxym6";
        String LOGIN = URL + "signup/endUser";
        String SIGN_UP = URL + "signup/endUser";
        String FACEBOOK_SIGNUP = URL + "signup/endUserThroughFb";
        String ADD_SYMPTOMS_BY_DOCTOR = URL + "prescription/getDoctorSymptomsListTerm?doctorId=" + AppPreference.getInstance().getDoctorId();
        String ADD_SIGNS_BY_DOCTOR = URL + "prescription/getDoctorSignListTerm?doctorId=" + AppPreference.getInstance().getDoctorId();
        String ADD_INVESTIGATION_BY_DOCTOR = URL + "prescription/getDoctorDiagonsisListTerm?doctorId=" + AppPreference.getInstance().getDoctorId();
        String ADD_ALLERGY_BY_DOCTOR = URL + "prescription/getDoctorAllergyTerm?doctorId=" + AppPreference.getInstance().getDoctorId();
        String ADD_ALL_AUTOCOMPLETE_TEXTVIEW = URL + "prescription/getPrescriptionTearm";
        String ADD_ALL_PATIENT_DASHBORAD = URL + "doctorInfoService/getPatientUnderDoctorOfOneClinic";
        String ADD_NEW_SYMPTOMS = URL + "prescription/addNewSymptomsTerm";
        String ADD_NEW_SIGNS = URL + "prescription/addNewSignTerm";
        String ADD_NEW_DIAGONIS = URL + "prescription/addNewDiagonsisTerm";
        String REMOVE_NEW_SYMSPTOMS = URL + "prescription/removeDoctorSymptomsTerm";
        String REMOVE_NEW_SIGN = URL + "prescription/removeDoctorSignTerm";
        String REMOVE_NEW_DIAGNOSIS = URL + "prescription/removeDoctorDiagnosisTerm";
        String ADD_MEDICINE_NAME = URL + "medicineService/getMedinceBySearch?medicineName=";
        String GET_MEDICINE_TYPE = URL + "medicineService/getMedinceTypeList";
        //patientDoctorId
        String PATIENT_DETAILS_BY_DOCTOR = URL + "patientInfoService/getPatientPhysicalMeasurmentDetailsForPrescription?doctorPatientId=";
        String FORGOT_PASSWORD = URL + "forgotPassword/resend";
        String PENDING_PYHISCAL_DATA = URL + "patientInfoService/createAndUpdatePendingPhysicalData";
        String SAVE_PATIENT_DATA = URL + "prescription/savePatientPrescriptionAndDoctorSearchKeyword";
        String PRESCRIPTION_HISTORY = URL + "prescription/getPrescriptionHistoryThumbnail?patientDoctorId=";
        String DOCTOR_COMMON_DETAILS = URL + "doctorInfoService/getDoctorCommonDetails";
        String RECENT_PATIENT_IMAGE = URL + "prescription/getRecentPrescriptionImage?patientDoctorId=";
        String E_PRESCRIPTION_FOLLOW_UP = URL + "prescription/getEPrescriptionDetails?ePrescriptionId=";
        String DOCTOR_BY_HOSPITAL_NAME = URL + "refer/getDoctorDetailsByHealthcareName?healthcareName=";
        String DOCTOR_BY_DOCTOR_NAME = URL + "doctorInfoService/searchDoctorName?doctorName=";
        String DOCTOR_BY_Specilization_NAME = URL + "doctorInfoService/searchSpecilization?specilization=";
        String REFER_TO_OTHER_DOCTOR = URL + "refer/referPatient";
        String PRESCRIPTION_HISTORY_FULL_IMAGE = URL + "prescription/getPrescriptionImage";
        String PRESCRIPTION_HISTORY_BY_DATE = URL + "prescription/getPrescriptionThumbnailByDate";
        String FILE_UPLOAD = URL + "refer/uploadPrescription";

    }

    public interface CustomMessages {
        String No_INTERNET_USAGE = "No Internet Connection";
        String ISSUE_WITH_DATA = "Issue with data";
        String No_DATA = "No data";
        String ISSUES_WITH_SERVER = "Issue with sever error";
    }


    public interface RequestType {
        int REGISTER = 1;
        int LOGIN = 2;
    }

    public interface PrefrenceKeys {
        String PREF_SUBJECTS_TIME_STAMP = "subject_timestamp";
        String PREF_CLASS_ID = "class_id";
        String PREF_CLASS = "class";
        String PREF_IS_LOGIN = "isAlreadyLogin";
        String LOGIN_FIRST = "login_first";
        String PROFILE_PIC = "pics";
        String PROFILE_ID = "id";
        String USER_NAME = "userName";
        String PASSWORD = "password";
        String NAME = "name";
        String EMAIL = "emailId";
        String HEADERS = "headers";
        String USER_ID = "userId";
        String USER_ROLE = "userRole";
        String RESEND_ID = "resendPasswordId";
        String RESEND_TYPE = "resendPasswordType";
    }


    public interface JsonUtils {
        String HTTP_CODE = "httpStatusCode";

        //Patient List
        String FNAME = "fname";
        String A_T = "a_t";
        String R_T = "r_t";
        String HEADERS = "headers";
        String USER_ID = "user_id";
        String USERNAME = "user_name";
        String INSTITUTE_NAME="instituteName";
        String INSTITUTE_TYPE="instituteType";

        String PATIENT_PROFILE_ID = "patientProfileId";
        String FIRST_NAME = "firstName";
        String GENDER = "gender";
        String BIRTH_DATE = "dateOfBirth";
        String PATIENT_ID = "patient_id";
        String EMAIL_ID = "email_id";
        String STREET = "street";
        String CITY = "city";
        String PINCODE = "pincode";
        String COUNTRY = "country";
        String MOBILE_NO = "mobileNo";
        String CFUUHIDID = "cfUuhidId";
        String CFUUHID = "cfuuhid";
        String ADDRESS = "address";
        String STATE = "state";
        String YEARS = "years";
        String YEAR = "year";
        String MONTH = "month";
        String AGE = "age";
        String PRESCRIPTION_EXIST = "prescriptionExist";
        String PROFILE_CREATE_DATE = "profileCreatedDate";
        String APPOINTMENT_TIME = "appointmentTime";

        //Doctors
        String DOCTOR_ID = "doctorId";
        String DOCTOR_PROVIDER_ID = "doctorServiceProviderId";
        String DOCTORS_NAME = "doctorName";
        String DOB = "dob";

        //Add Patient
        String PATIENT_NAME = "patientName";

        String PATIENT_HEIGHT = "height";
        String PATIENT_INCH = "heightInch";
        String PATIENT_FEET = "heightFeet";
        String PATIENT_WEIGHT_KG = "weightKg";
        String PATIENT_WEIGHT_GM = "weightGm";
        String PATIENT_WEIGHT = "weight";
        String PATIENT_BLOOD_PRESURE = "bloodPresure";
        String PATIENT_BODY_TEMP = "bodyTemp";
        String PATIENT_APPOINTMENT_TIME = "appointmentTime";
        String PATIENT_DOCTOR_ID = "patientDoctorId";
        String PATIENT_DOCTOR_APP_ID = "patientDoctorAppointmentId";
        String PATIENT_AGE = "patientAge";
        String PATIENT_NO_OF_DOCTOR = "noOfDoctor";
        String PATIENT_SAVE_FOR_LATER = "saveForLater";
        String PATIENTID = "patientId";
        //Response Webservices Keys

        String JSON_KEY_PAYLOAD = "payload";
        String JSON_KEY_RESPONSE = "response";
        String JSON_KEY_CODE = "code";
        String JSON_KEY_MESSAGE = "message";
        String JSON_ARRAY_PATIENT_LIST_DOCTOR_LIST = "listOfDoctorDetils";
        String JSON_ARRAY_SYMPTOMS = "symptomsListAddByDoctor";
        String JSON_ARRAY_SINGS = "signsListAddByDoctor";
        String JSON_ARRAY_INVESTIGATION = "diagnosisListAddByDoctor";
        String JSON_ARRAY_ALLERGY = "allergyListResponse";
        String JSON_ARRAY_PATIENT_UNDER_OF_CLINIC_ST = "patientList";
        String JSON_ARRAY_PATIENT_UNDER_CLINIC_ST = "patientUnderDoctorclinicList";
        String OK = "OK";

        String CATEGORIES = "categories";
        String EPRESCRIPTION_IMAGE = "ePrescriptionImage";
        String E_PRESCRIPTION_CREATE_DATE = "ePrescriptionCreatedDate";

    }

    public interface IResponseCode {
        int RESPONSE_SUCCESS = 200;
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

    }

    public interface IArrayData {
        String[] listPopUp = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String[] listPopUpWeightKgs = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100", "101", "102", "103", "104", "105", "106", "107", "108", "109"};
        String[] listPopUpWeightGms = {"100", "200", "300", "400", "500", "600", "700", "800", "900"};
        String[] listPopUpBloodGrp = {"0+", "0-", "A+", "A-", "B+", "B-", "AB+", "AB-"};
        String[] listPopUpDate = {"Older", "Latest"};
    }
}
