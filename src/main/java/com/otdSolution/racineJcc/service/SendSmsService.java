package com.otdSolution.racineJcc.service;

import com.otdSolution.racineJcc.sendSmsConfiguration.SmsRequest;
import com.otdSolution.racineJcc.sendSmsConfiguration.SmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@org.springframework.stereotype.Service
public class SendSmsService {

    private final SmsSender smsSender;

    @Autowired
    public SendSmsService(@Qualifier("twilio") TwilioSmsSender smsSender) {
        this.smsSender = smsSender;
    }

    public void sendSms(SmsRequest smsRequest) {
        smsSender.sendSms(smsRequest);
    }
}
