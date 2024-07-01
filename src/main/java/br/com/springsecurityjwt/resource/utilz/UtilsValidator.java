package br.com.springsecurityjwt.resource.utilz;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilsValidator {

    public static boolean validateTagsUpdate(String values) {
        String regex = "^([a-zA-Z0-9]+)(,(\\s*[a-zA-Z0-9]+))*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(values);
        return matcher.matches();
    }
}
