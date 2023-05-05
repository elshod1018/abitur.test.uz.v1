package uz.test.abitur.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true

)
@RequiredArgsConstructor
public class SecurityConfigurer {
    private final AuthUserDetailsService authUserDetailsService;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    public static final String[] WHITE_LIST = {
            "/css/**",
            "/js/**",
            "/home",
            "/auth/**",
            "/upload",
            "/"
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers(WHITE_LIST)
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .formLogin(httpSecurityFormLoginConfigurer ->
                        httpSecurityFormLoginConfigurer
                                .loginPage("/auth/login")
                                .loginProcessingUrl("/auth/login")
                                .usernameParameter("phoneNumber")
                                .passwordParameter("password")
                                .successForwardUrl("/home")
                                .defaultSuccessUrl("/home", false)
                                .failureHandler(authenticationFailureHandler)
                )
                .logout(httpSecurityLogoutConfigurer ->
                        httpSecurityLogoutConfigurer
                                .logoutUrl("/auth/logout")
                                .clearAuthentication(true)
                                .deleteCookies("JSESSIONID", "rememberMe")
                                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))
                )
                .userDetailsService(authUserDetailsService)
                .rememberMe(httpSecurityRememberMeConfigurer ->
                        httpSecurityRememberMeConfigurer
                                .rememberMeParameter("rememberMe")
                                .key("EWT$@WEFYG%H$ETGE@R!T#$HJYYT$QGRWHNJU%$TJRUYRHFRYFJRYUYRHD")
                                .tokenValiditySeconds(10 * 24 * 60 * 60)// default is 30 minutes
                                .rememberMeCookieName("rememberMe")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }


}
