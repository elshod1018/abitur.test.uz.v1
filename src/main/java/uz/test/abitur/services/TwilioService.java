package uz.test.abitur.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class TwilioService {
    @Value("${twilio.sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.token}")
    private String AUTH_TOKEN;

    @Value("${twilio.from.number}")
    private String FROM_NUMBER;

    @Async
    public void sendSMS(String toNumber, String sms) {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String messageBody = "Your verification code is:  " + sms;

        Message message = Message.creator(
                        new PhoneNumber(toNumber),
                        new PhoneNumber(FROM_NUMBER),
                        messageBody)
                .create();

        String sid = message.getSid();
        System.out.println(sid);
    }
}
