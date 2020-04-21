package com.webshoprest.api.v1.util;

public class EmailContentUtil {

    private static final String BASE_PATH = "/api/v1/sec";

    // only for testing purposes
    public static String buildVerificationLink(String serverName, Integer serverPort, String token){
        return "http://" + serverName + ":" + serverPort + BASE_PATH + "/confirm-email?tk=" + token;
    }

    public static String buildEmailText(String name, String verificationLink, EmailType emailType){
        return "Dear " + name + " please visit this link to " + emailType.getText() + "\n" + verificationLink;
    }


}
