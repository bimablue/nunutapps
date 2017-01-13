package com.blackbeltcoder.nunut.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class StringUtil {

    private static final char[] symbols;

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch)
            tmp.append(ch);
        for (char ch = 'a'; ch <= 'z'; ++ch)
            tmp.append(ch);
        for (char ch = 'A'; ch <= 'Z'; ++ch)
            tmp.append(ch);
        symbols = tmp.toString().toCharArray();
    }

    //private final Random random = new Random();

    //private char[] buf;

    //Email Pattern
    /*
	private static Pattern pattern;
    private static Matcher matcher;
    private static final String EMAIL_PATTERN = 
            "^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$";
    */

    /*public static boolean validate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
 
    }*/

    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0 ? true: false;
    }

    public static String dateToString(Date date, String format) {
        SimpleDateFormat newDateFormat = new SimpleDateFormat(format);
        newDateFormat.applyPattern(format);

        if(date != null)
            return newDateFormat.format(date);
        else
            return "-";
    }

    /*public void randomString(int length) {
        if (length < 1)
            throw new IllegalArgumentException("length < 1: " + length);
        buf = new char[length];
    }

    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }*/

    public static String generateRandomCode(int length){
        Random random = new Random();
        char[] buf = new char[length];
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    public static String priceFormat(Long price, int mode) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");

        if(mode == 0)
            return formatter.format(price).replace(",", ".");
        else if(mode == 1)
            return "Rp " + formatter.format(price).replace(",", ".");
        else if(mode == 2){
            if(price > 1l){
                return formatter.format(price).replace(",", ".") + " items";
            } else {
                return formatter.format(price).replace(",", ".") + " item";
            }
        }

        return "";
    }
}
