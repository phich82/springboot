package com.example.demo.services;

import java.text.NumberFormat;
import java.text.ParsePosition;

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
}
