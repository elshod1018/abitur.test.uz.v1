package uz.test.abitur.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.test.abitur.domains.AuthUser;
import uz.test.abitur.enums.Status;

import java.util.Collection;
import java.util.List;

public class AuthUserDetails implements UserDetails {

    private final AuthUser authUser;

    public AuthUserDetails(AuthUser authUser) {
        this.authUser = authUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + authUser.getRole()));
    }

    @Override
    public String getPassword() {
        return authUser.getPassword();
    }


    public AuthUser getUser() {
        return authUser;
    }

    public String getId() {
        return authUser.getId();
    }

    @Override
    public String getUsername() {
        return authUser.getPhoneNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !Status.BLOCKED.equals(authUser.getStatus());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Status.ACTIVE.equals(authUser.getStatus());
    }
}