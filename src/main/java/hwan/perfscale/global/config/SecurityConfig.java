package hwan.perfscale.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 실제 인증(JWT)이 붙기 전까지의 자리표시자.
 * spring-boot-starter-security-oauth2-client가 클래스패스에 있으면 스프링 시큐리티가
 * 기본적으로 모든 요청을 Basic 인증으로 막기 때문에, 그 동안은 전체 허용해둔다.
 * 실제 인증을 붙일 때 이 클래스를 교체한다.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
