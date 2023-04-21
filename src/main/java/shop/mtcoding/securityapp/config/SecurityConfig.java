package shop.mtcoding.securityapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    //패스워드 암호화 알고리즘 BCrypt-> 60Byte로 단방향 해시 암호화 +솔트

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. CSRF 해제
        http.csrf().disable(); // postman 접근해야 함!! - CSR 할때!!

        // 2. Form 로그인 설정 - 필터가 아니다.
        // 활성화시 UsernamePasswordAuthenticationFilter 작동, HttpBasic 작동 안함

        http.formLogin()
                .loginPage("/loginForm")
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/login") // POST + X-WWW-Form-urlEncoded
                //: 로그인할 주소 설정
                //.defaultSuccessUrl("/")

                //2-1. 로그인 성공시 타는 핸들러
                .successHandler((eq, resp, authentication) -> {
//                    System.out.println("디버그 : 로그인이 완료되었습니다");
                    log.debug("디버그 : 로그인이 완료되었습니다");
                    resp.sendRedirect("/");
                })
                //2-2. 로그인 실패시 타는 핸들러
                .failureHandler((req, resp, ex) -> {
//                    System.out.println("디버그 : 로그인 실패 -> " + ex.getMessage());
                    log.debug("디버그 : 로그인 실패 -> " + ex.getMessage());
                });

        // 3. 인증, 권한 필터 설정
        http.authorizeRequests(
                authorize -> authorize.antMatchers("/users/**").authenticated()
                        .antMatchers("/manager/**")
                        .access("hasRole('ADMIN') or hasRole('MANAGER')")
                        //UserDetails에는 문법으로 ROLE_을 반드시 추가해야한다.
                        //: 자동으로 'ROLE_'을 추가한다. - defaultRolePrefix
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
