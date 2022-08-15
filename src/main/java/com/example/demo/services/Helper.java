package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static boolean isNumeric(Object value) {
        String v = (String) value;

        if (v.length() < 1) {
            return false;
        }

        // return v.matches("[+-]?\\d*(\\.\\d+)?");

        // ParsePosition pos = new ParsePosition(0);
        // NumberFormat.getInstance().parse(v, pos);
        // return v.length() == pos.getIndex();

        for (char c: v.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static String implode(Object[] arr, String separator) {
        if (arr.length == 0) {
            //empty array return empty string
            return "";
        }

        if (arr.length < 2) {
            //only 1 item
            return String.valueOf(arr[0]);
        }

        StringBuffer stringbuffer = new StringBuffer();
        for (int i=0; i < arr.length; i++) {
            if (i != 0) {
                stringbuffer.append(separator);
            }
            stringbuffer.append(String.valueOf(arr[i]));
        }

        return stringbuffer.toString();
    }

    public static String implode(List<?> arr, String separator) {
        if (arr.size() == 0) {
            //empty array return empty string
            return "";
        }

        if (arr.size() < 2) {
            //only 1 item
            return String.valueOf(arr.get(0));
        }

        StringBuffer stringbuffer = new StringBuffer();
        for (int i=0; i < arr.size(); i++) {
            if (i != 0) {
                stringbuffer.append(separator);
            }
            stringbuffer.append(String.valueOf(arr.get(i)));
        }

        return stringbuffer.toString();
    }

    public List<Object> merge(List<Object> list1, List<Object> list2) {
        List<Object> newList = new ArrayList<Object>(list1);
        newList.addAll(list2);
        return newList;
    }
}
