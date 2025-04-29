package com.hmsapp.service;

import com.hmsapp.config.TwilioConfig;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    private final TwilioConfig twilioConfig;

    @Autowired
    public TwilioService(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    public String sendSms(String to, String messageBody) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(to), // To number
                    new PhoneNumber(twilioConfig.getPhoneNumber()), // From number
                    messageBody // Message body
            ).create();

            return "Message sent successfully with SID: " + message.getSid();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send message: " + e.getMessage();
        }
    }
}
