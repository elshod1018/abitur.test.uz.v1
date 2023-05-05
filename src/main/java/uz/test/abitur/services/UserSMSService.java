package uz.test.abitur.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.test.abitur.domains.AuthUser;
import uz.test.abitur.domains.UserSMS;
import uz.test.abitur.repositories.UserSMSRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserSMSService {
    private final UserSMSRepository userSMSRepository;

    public UserSMS sendSMSCode(AuthUser user) {
        String userId = user.getId();
        UserSMS userSMS = this.findByUserId(userId);
        if (userSMS != null) {
            if (userSMS.getToTime().isAfter(LocalDateTime.now())) {
                return userSMS;
            } else {
                userSMS.setExpired(true);
                this.update(userSMS);
            }
        }
        String smsCode ="666666"; /*baseUtils.generateCode()*/;
        userSMS = this.save(userId, smsCode);
       /* smsCode=smsCode.substring(0,3)+" "+smsCode.substring(3);
        twilioService.sendSMS(user.getPhoneNumber(), smsCode);*/
        return userSMS;
    }

    private UserSMS save(String userId, String smsCode) {
        UserSMS userSMS = UserSMS.builder()
                .userId(userId)
                .code(smsCode)
                .build();
        return userSMSRepository.save(userSMS);
    }

    public UserSMS findByUserId(String userId) {
        return userSMSRepository.findByUserId(userId);
    }

    public UserSMS update(UserSMS userSMS) {
        return userSMSRepository.save(userSMS);
    }
}
