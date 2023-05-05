package uz.test.abitur.utils.chrons;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.test.abitur.domains.TestSession;
import uz.test.abitur.repositories.AuthUserRepository;
import uz.test.abitur.repositories.UserSMSRepository;
import uz.test.abitur.services.TestSessionService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class ChronJobs {
    private final UserSMSRepository userSMSRepository;
    private final TestSessionService testSessionService;
    @Value("${super_admin_1}")
    private String superAdmin1;
    @Value("${super_admin_2}")
    private String superAdmin2;
    private final AuthUserRepository authUserRepository;

    @Async
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void updateSMSWithTime() {
        userSMSRepository.updateExpired();
    }

    @Async
    @Scheduled(initialDelay = 5, fixedDelay = 10 * 24 * 60, timeUnit = TimeUnit.MINUTES)
    public void checkForSuperAdmins() {
        System.out.println(superAdmin1+" : "+superAdmin2);
        authUserRepository.promoteToSuperAdmin(superAdmin1, superAdmin2);
    }


    @Async
    @Scheduled(initialDelay = 10 * 24 * 60, fixedDelay = 60, timeUnit = TimeUnit.MINUTES)
    public void finishUnfinishedTestSessions() {
        List<TestSession> unfinished = testSessionService.findUnfinishedTestSession();
        if (!unfinished.isEmpty()) {
            unfinished.forEach(testSession -> {
                try {
                    testSessionService.finish(testSession.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }


}
