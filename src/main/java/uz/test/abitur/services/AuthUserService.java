package uz.test.abitur.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.test.abitur.config.security.UserSession;
import uz.test.abitur.domains.AuthUser;
import uz.test.abitur.domains.Document;
import uz.test.abitur.dtos.user.UserPasswordUpdateDTO;
import uz.test.abitur.dtos.user.UserProfileUpdateDTO;
import uz.test.abitur.dtos.user.UserRegisterDTO;
import uz.test.abitur.dtos.user.UserUpdateDTO;
import uz.test.abitur.enums.Role;
import uz.test.abitur.enums.Status;
import uz.test.abitur.ex_handler.exceptions.NotFoundException;
import uz.test.abitur.ex_handler.exceptions.PasswordException;
import uz.test.abitur.repositories.AuthUserRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSession userSession;
    private final DocumentService documentService;



    public AuthUser save(UserRegisterDTO dto) {
        AuthUser authUser = AuthUser.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phoneNumber(dto.getPhoneNumber())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
        return authUserRepository.save(authUser);
    }

    /*@Cacheable(value = "userCache", key = "#userId")*/
    public AuthUser findById(String userId) {
        return authUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public AuthUser findByPhoneNumber(String phoneNumber) {
        return authUserRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("Bad Credentials"));
    }
   /* @CachePut(value = "userCache", key = "#user.id")*/
    public AuthUser update(AuthUser user) {
        user.setUpdatedAt(LocalDateTime.now());
        return authUserRepository.save(user);
    }

    /*@CachePut(value = "userCache", key = "#dto.id")*/
    public AuthUser update(UserUpdateDTO dto) {
        AuthUser user = findById(dto.getId());
        user.setStatus(dto.getStatus());
        if (Status.DELETED.equals(dto.getStatus())) {
            user.setDeleted(true);
        }
        return authUserRepository.save(user);
    }


    public List<AuthUser> getAllByStatus(Status status) {
        return authUserRepository.findAllByStatus(status, userSession.getId());
    }

    public List<AuthUser> getAll() {
        return authUserRepository.findAll(userSession.getId());
    }

    public AuthUser getByPhoneAndDeletedFalse(String phoneNumber) {
        return authUserRepository.findByPhoneAndDeletedFalse(phoneNumber);
    }

    /*@CacheEvict(value = "userCache", allEntries = true)*/
    public AuthUser updateProfile(UserProfileUpdateDTO dto) throws IOException {
        AuthUser user = userSession.getUser();

        MultipartFile file = dto.getProfilePicture();
        Document document = documentService.saveDocument(file);
        user.setProfilePhotoGeneratedName(document.getGeneratedName());

        user.setFirstName(Objects.requireNonNullElse(dto.getFirstName(), user.getFirstName()));
        user.setMiddleName(dto.getMiddleName());
        user.setLastName(Objects.requireNonNullElse(dto.getLastName(), user.getLastName()));
        user.setUpdatedAt(LocalDateTime.now());
        return authUserRepository.save(user);
    }

    /*@CacheEvict(value = "userCache", allEntries = true)*/
    public AuthUser updatePassword(UserPasswordUpdateDTO dto) {
        String newConfirmPassword = dto.getNewConfirmPassword();
        String newPassword = dto.getNewPassword();

        checkPassword(newPassword);
        checkPassword(newConfirmPassword);
        if (!newPassword.equals(newConfirmPassword))
            throw new PasswordException("New password must be the same as confirm password");

        AuthUser user = userSession.getUser();
        if (passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setUpdatedAt(LocalDateTime.now());
            return authUserRepository.save(user);
        }
        throw new PasswordException("Old password is incorrect");
    }

    private void checkPassword(String password) {
        if (password.length() < 8 || password.length() > 16) {
            throw new PasswordException("Password must be at least %s characters and at most %s characters"
                    .formatted(8, 16));
        }
    }

    public List<AuthUser> getAllByRole(Role role) {
        return authUserRepository.findByRole(role);
    }
}
