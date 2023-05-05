package uz.test.abitur.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import uz.test.abitur.domains.AuthUser;

@Component
public class UserSession {
    public AuthUser getUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Object authUserDetails = authentication.getPrincipal();
        if (authUserDetails instanceof AuthUserDetails userDetails)
            return userDetails.getUser();
        throw new UsernameNotFoundException("unauthorized");
    }

    public String getId() {
        AuthUser user = getUser();
        if (user != null)
            return user.getId();
        return "-1";
    }
}
