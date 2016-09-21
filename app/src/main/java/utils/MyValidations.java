package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class MyValidations {
    // Email validity check
    public static boolean isValidEmail(String email) {
        if(!isValidString(email))
            return false;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //String validator
    public static boolean isValidString(String data) {
        if (data == null || data.trim().length() == 0 || data.equalsIgnoreCase("null"))
            return false;
        else
            return true;
    }

}
