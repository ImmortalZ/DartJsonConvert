package me.immortalz.dartjsonconvert.util;

import java.util.regex.Pattern;

public class DartTypeUtil {
    public static boolean isBasicVariableTyp(String targetType) {
        String[] simpleTypeArray = {"int", "double", "String", "bool", "dynamic"};
        for (String type: simpleTypeArray) {
            if (type.equals(targetType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isListType(String targetType) {
        return Pattern.matches("List<.*>", targetType);
    }

    public static boolean isObjectType(String targetType) {
        return Pattern.matches("^[a-zA-Z_]+[a-zA-Z0-9_]*",targetType);
    }

    public static String getListDynamicType(String ListType) {
        return RegexUtil.regexMatch(ListType, "<(.*)>");
    }


}
