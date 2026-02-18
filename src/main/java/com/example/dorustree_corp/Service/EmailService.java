package com.example.dorustree_corp.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async("taskExecutor")
    public void sendOrderConfirmation(String toEmail, String orderId) {

        log.info("Async method started. Thread: {}", Thread.currentThread().getName());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Order Confirmation - " + orderId);
            message.setText("Your order with ID " + orderId + " has been successfully placed.");


            mailSender.send(message);

            log.info("Email sent successfully for Order Confirmation");
        } catch (Exception e) {
            log.error("Error while Sending Email, ",e);
        }
    }
    public void sendOrderCancellation(String toEmail, String orderId){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Order Cancelled - " + orderId);
            message.setText("Your order with ID " + orderId + " is Cancelled");


            mailSender.send(message);
            log.info("Email sent successfully for Order Cancellation");

        } catch (Exception e) {
            log.error("Error while Sending Email, ",e);
        }
    }
}
