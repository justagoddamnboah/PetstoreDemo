package petstore.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import petstore.models.enums.UserRoles;
import petstore.repositories.UserRepository;
import petstore.services.AppUserDetailsService;

@Slf4j
@Configuration
public class AppSecurityConfig {

    private final UserRepository userRepository;

    public AppSecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
        log.info("AppSecurityConfiguration инициализирована");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityContextRepository securityContextRepository) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Публичные эндпоинты (доступны всем)
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/favicon.ico", "/error").permitAll()
                .requestMatchers("/", "/catalogue/**", "/catalogue/pet-card/**").permitAll()  // Каталог доступен всем
                .requestMatchers("/users/profile", "/users/profile/**", "/users/login", "/users/register", "/users/login-error", "/users/logout").permitAll()
                .requestMatchers("/actuator/**").permitAll()

                // Эндпоинты для покупателей (CUSTOMER роль)
                .requestMatchers("/cart/**", "/order/**").hasRole(UserRoles.CUSTOMER.name())

                // Эндпоинты для модераторов (MODERATOR роль)
                .requestMatchers("/pet-add", "/catalogue/pet-card/remove-pet/**").hasRole(UserRoles.MODERATOR.name())
            )
            .formLogin(form -> form
                .loginPage("/users/login")
                .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                .defaultSuccessUrl("/", true)
                .failureForwardUrl("/users/login-error")
                .permitAll()
            )
            .rememberMe(remember -> remember
                .key("uniqueAndSecret") // В production использовать секрет из конфигурации
                .tokenValiditySeconds(86400 * 7) // 7 дней
                .userDetailsService(userDetailsService())
                .rememberMeParameter("remember-me")
            )
            .logout(logout -> logout
                .logoutUrl("/users/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .permitAll()
            )
            .securityContext(securityContext -> securityContext
                .securityContextRepository(securityContextRepository)
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/actuator/**") // Для демо
            );

        log.info("SecurityFilterChain настроен");
        return http.build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
            new RequestAttributeSecurityContextRepository(),
            new HttpSessionSecurityContextRepository()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new AppUserDetailsService(userRepository);
    }
}