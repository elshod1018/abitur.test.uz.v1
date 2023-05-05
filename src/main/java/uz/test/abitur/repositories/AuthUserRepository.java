package uz.test.abitur.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import uz.test.abitur.domains.AuthUser;
import uz.test.abitur.enums.Role;
import uz.test.abitur.enums.Status;

import java.util.List;
import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, String> {
    @Query("""
            select a from AuthUser a
            where a.role = ?1 and
            a.status = uz.test.abitur.enums.Status.ACTIVE and
            a.deleted = false""")
    List<AuthUser> findByRole(Role role);
    @Query("select a from AuthUser a where a.phoneNumber = ?1 and a.deleted = false")
    Optional<AuthUser> findByPhoneNumber(String phoneNumber);

    @Query("select a from AuthUser a where a.phoneNumber = ?1 and a.deleted = false")
    AuthUser findByPhoneAndDeletedFalse(String phoneNumber);

    @Query("select a from AuthUser a where a.status = ?1 and a.id <> ?2 order by a.createdAt desc")
    List<AuthUser> findAllByStatus(Status status, String userId);

    @Query("select a from AuthUser a where a.id <> ?1 and a.status <> uz.test.abitur.enums.Role.SUPER_ADMIN order by a.createdAt desc")
    @NonNull List<AuthUser> findAll(String userId);

    @Transactional
    @Modifying
    @Query("""
            update AuthUser a set a.role = uz.test.abitur.enums.Role.SUPER_ADMIN
            where a.deleted = false and a.status <> uz.test.abitur.enums.Status.DELETED  and a.phoneNumber in (?1, ?2)""")
    void promoteToSuperAdmin(String phone1,String phone2);
}