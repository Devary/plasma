/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.parsing;

import java.util.Arrays;
import java.util.List;

public class PlasmaUtils {
    public static String convertToUnderscoredName(String name) {
        name = name.substring(0,1).replace(name.substring(0,1),name.substring(0,1).toLowerCase()).concat(name.substring(1));
        List<String> splits = Arrays.asList(name.split(""));
        String result = "";
        int i = 0;
        for (String split : splits) {
            boolean isLast = i==name.length()-1;
            char currentChar = split.charAt(0);
            char nextChar = isLast? 0:splits.get(i+1).charAt(0);
            char prevChar = i-1<0? 0:splits.get(i-1).charAt(0);
            int lastIndexUnderscore = result.lastIndexOf("_");
            if ((!Character.isUpperCase(prevChar) && Character.isUpperCase(currentChar) ) || (Character.isUpperCase(nextChar) && Character.isUpperCase(currentChar) && lastIndexUnderscore != -1 && lastIndexUnderscore == i - 2)) {
                //if (Character.isUpperCase(split.charAt(0)) && !isLast && !Character.isUpperCase(splits.get(i+1).charAt(0))) {
                String mod = "_" + split.toLowerCase();
                result = result.concat(mod);
            } else {
                result = result.concat(split.toLowerCase());
            }
            i++;
        }
        return result;
    }
}
