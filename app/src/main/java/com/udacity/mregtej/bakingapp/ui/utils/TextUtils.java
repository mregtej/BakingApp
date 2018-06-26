package com.udacity.mregtej.bakingapp.ui.utils;

public class TextUtils {

    public static boolean isEmpty(String str) {
        if (str != null && !str.isEmpty() && !str.equals("null")) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isNegative(Double d) {
        return (d < 0) ? true : false;
    }

}
