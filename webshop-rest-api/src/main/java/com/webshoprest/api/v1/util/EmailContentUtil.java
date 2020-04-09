package com.webshoprest.api.v1.util;

public class EmailContentUtil {

    private static final String BASE_PATH = "/api/v1/sec/";

    public static String buildVerificationLink(String serverName, Integer serverPort, Long userId){
        return "http://" + serverName + ":" + serverPort + BASE_PATH + userId + "/confirm-email";
    }

    public static String buildEmailText(String name, String verificationLink){
        return "Dear " + name + " please visit this link to confirm your email " + verificationLink;
    }
}
