package com.marsol.domain.model;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

public class Note {

    private int LFCode;
    private String Value;

    //Constructor
    public Note() {
    }

    @Override
    public String toString() {
        return LFCode + "\t" + Value;
    }

    //Getters
    public int getLFCode() {return LFCode;}
    public String getValue() {
        return Value;
    }
    //Setters
    public void setLFCode(int LFCode) {
        if(LFCode < 0 || LFCode > 999999) {
            throw new IllegalArgumentException("LFCode es obligatorio y debe estar entre 0 y 999999.");
        }
        this.LFCode = LFCode;
    }
    public void setValue(String value) {
        String value_sin_acentos;
        String value_sin_n;
        value_sin_acentos = StringUtils.stripAccents(value);
        value_sin_n = value_sin_acentos.replace("ñ","n")
                .replace("Ñ","N")
                .replace("¦","|")
                .replace("¨","*")
                .replace("\t","");
        if (value.length() >= 1000) {
            Value = value_sin_n.replace("°","").replace("\u00BA","").substring(0,1000);//convertirUTF8aISO(value.substring(0,1000)); //convertidorTexto(value.substring(0,950));
        } else {
            Value = value_sin_n.replace("°","").replace("\u00BA","");//convertirUTF8aISO(value);//convertidorTexto(value);}

        }
    }



    public String convertirUTF8aISO(String texto){
        String texto_1 = StringUtils.stripAccents(texto);
        String texto_2 = texto_1.replace("ñ","n").replace("Ñ","N");
        byte[] bytes = texto_2.getBytes(StandardCharsets.UTF_8);
        String textoISO = new String(bytes, StandardCharsets.ISO_8859_1);
        textoISO = textoISO.replace("°"," ");
        return textoISO;
    }

    public String convertidorTexto(String str){
        byte[] isoBytes = str.getBytes(StandardCharsets.ISO_8859_1);
        String textoUtf8 = new String(isoBytes, StandardCharsets.UTF_8);

        //Filtrar carecteres no validos UTF-8
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < textoUtf8.length(); i++) {
            char c = textoUtf8.charAt(i);
            if (Character.isDefined(c) && !Character.isISOControl(c)) {
                sb.append(c);
            }
        }
        return sb.toString().replaceAll("\uFFFD", "");
    }
}