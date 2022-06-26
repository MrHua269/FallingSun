package org.novau233.fallingsun;

import java.util.*;

public class Util {
    public static String getRandomString(int minLength,int maxLength) {
        String str = "abcdefghijklmno!':|<>?,./pqrstuvwxyzA@#$%^&*()_+-=[]{};BCDEFGHIJKLMNOPQRSTUVWXYZ0123456789~`";
        Random random = new Random();
        int length=random.nextInt(maxLength) % (maxLength-minLength+1)+minLength;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(str.length()-1);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
