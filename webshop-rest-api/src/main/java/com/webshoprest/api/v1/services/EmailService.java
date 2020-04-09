package com.webshoprest.api.v1.services;

public interface EmailService {

    void sendEmail(String to, String subject, String text);

}
