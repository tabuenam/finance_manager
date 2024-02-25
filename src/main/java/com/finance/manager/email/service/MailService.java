package com.finance.manager.email.service;

public interface MailService {
    void sendMail(String mailAddress, String title, String mailMessage);
}
