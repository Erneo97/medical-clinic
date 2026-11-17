package com.github.erneo97.medical_clinic.utils;

import java.util.Base64;

public class JwtPeek {
    public static void main(String[] args) {
        String[] parts = args[0].split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        System.out.println("header: " + new String(decoder.decode(parts[0])));
        System.out.println("payload: " + new String(decoder.decode(parts[1])));
        System.out.println("signature: " + parts[2].length() + " Base64URL characters");
    }
}