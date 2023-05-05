package uz.test.abitur.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.test.abitur.domains.AuthUser;
import uz.test.abitur.repositories.AuthUserRepository;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {
    private final AuthUserRepository authUserRepository;

    @Override
    public AuthUserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        AuthUser authUser = authUserRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("Bad Credentials"));
        return new AuthUserDetails(authUser);
    }
}
