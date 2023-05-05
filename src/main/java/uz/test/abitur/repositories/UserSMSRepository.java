package uz.test.abitur.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import uz.test.abitur.domains.UserSMS;

public interface UserSMSRepository extends JpaRepository<UserSMS, Integer> {
    @Transactional
    @Modifying
    @Query("update UserSMS u set u.expired = true where u.toTime < now()")
    void updateExpired();

    @Query("select u from UserSMS u where u.expired = false and u.userId = ?1")
    UserSMS findByUserId(String userId);
}