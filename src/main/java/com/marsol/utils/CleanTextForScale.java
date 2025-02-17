package com.marsol.utils;

import org.apache.commons.lang3.StringUtils;

public class CleanTextForScale {

    public static String cleanText(String text){
        return StringUtils.stripAccents(text)
                .replace("Ñ","N")
                .replace("¦","|")
                .replace("¨","*")
                .replace("\t","")
                .replace("°","")
                .replace("\u00BA","");
    }
}
