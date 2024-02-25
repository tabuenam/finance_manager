package com.finance.manager.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final Environment environment;

    @Override
    public void sendMail(final String mailAddress, final String title, final String mailMessage) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(environment.getProperty("spring.mail.username"));
        simpleMailMessage.setTo(mailAddress);
        simpleMailMessage.setSubject(title);
        simpleMailMessage.setText(mailMessage);

        mailSender.send(simpleMailMessage);
    }
}
