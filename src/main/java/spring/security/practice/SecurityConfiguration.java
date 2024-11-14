package spring.security.practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import spring.security.practice.model.MyUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private MyUserDetailService userDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Menonaktifkan CSRF untuk simplifikasi
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/home", "/register/**").permitAll(); // Halaman home & register terbuka untuk semua
                    registry.requestMatchers("/admin/**").hasRole("ADMIN"); // Hanya admin bisa akses URL /admin/**
                    registry.requestMatchers("/user/**").hasRole("USER"); // Hanya user bisa akses URL /user/**
                    registry.anyRequest().authenticated(); // URL lainnya butuh login
                })
                .formLogin(httpSecurityFormLoginConfigurer -> {
                    httpSecurityFormLoginConfigurer
                            .loginPage("/login") // Mengarahkan ke halaman login custom
                            .successHandler(new AuthenticationSuccessHandler()) // Menggunakan handler sukses login
                            .permitAll(); // Halaman login terbuka untuk semua
                })
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Menyediakan UserDetailsService kustom untuk mengambil data user
        return userDetailService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Menyediakan provider autentikasi yang mengandalkan UserDetailsService dan encoder password
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Menggunakan BCrypt untuk enkripsi password
        return new BCryptPasswordEncoder();
    }
}
