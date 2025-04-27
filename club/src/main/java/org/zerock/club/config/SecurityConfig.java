package org.zerock.club.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zerock.club.security.filter.ApiCheckFilter;
import org.zerock.club.security.filter.ApiLoginFilter;
import org.zerock.club.security.handler.ApiLoginFailHandler;
import org.zerock.club.security.handler.ClubLoginSuccessHandler;
import org.zerock.club.security.util.JWTUtil;

@Configuration
@EnableWebSecurity
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)//접근제한
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();//길이 60글자 , 매번실행시 값이 바뀐다.
    }

//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        UserDetails user = User.builder()
//                .username("user1")
//                .password(passwordEncoder().encode("1111"))
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }

    @Bean
    //antMatchers 은 depracated 되어 requestMatchers 사용
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/user**","/mypage/**").authenticated()//로그인된 사용자 접근허용
                .requestMatchers("/admin/**").hasRole("ADMIN")//로그인된 사용자 그리고 권한이 ROLE_ADMIN 만 접근허용
                .anyRequest().permitAll()//위 설정을 제외한 모든 페이지에 인증을 거치지 않는다.
        );
       //Spring boot 3.1이후
        http.formLogin(form -> form
                .loginPage("/login") // 사용자 정의 로그인 페이지
                .usernameParameter("email") // 사용자명 필드 변경
                //.passwordParameter("password") // 사용자명 필드 변경
                .failureUrl("/login?error=true") // 로그인 실패 시 이동할 URL
                .permitAll()
        );//인가/인증시 로그인 화면

        //http.csrf().disable();
        http.logout(logout -> logout
                .logoutUrl("/logout")//로그아웃 페이지
                .logoutSuccessUrl("/login?logout=true") // 로그아웃 성공 후 이동할 URL
                .invalidateHttpSession(true)//세션 정보 삭제(초기화)
                .deleteCookies("JSESSIONID") // 쿠키 삭제
        );

        //http.oauth2Login().successHandler(clubLoginSuccessHandler());
        http.oauth2Login( oauth2 -> oauth2
                .loginPage("/login")
                .successHandler(clubLoginSuccessHandler()));// 사용자 정의 로그인 페이지지 사용시 sns인증을 넣으면 /login 사용자페이지가 안나옴

        http.rememberMe(rememberMe -> rememberMe
                .tokenValiditySeconds(60 * 60 * 24 * 7)//7일간 로그인 정보를 보존
                .key("uniqueAndSecret")//토큰 암호화 키
                .userDetailsService(userDetailsService)//사용자 정보 서비스 설정
        );
        //.userDetailsService(userDetailsService)

        //UsernamePasswordAuthenticationFilter 전에 apiCheckFilter 추가
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        //authenticationManagerBuilder.userDetailsService(apiUserDetailsService).passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.authenticationManager(authenticationManager);//반드시 필요




        http.addFilterBefore(apiLoginFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    public ApiLoginFilter apiLoginFilter(AuthenticationManager authenticationManager) throws Exception {
        //APILoginFilter
        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login", new JWTUtil());
        apiLoginFilter.setAuthenticationManager(authenticationManager);
        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());//인증에 실패했을때

        //http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);
        return apiLoginFilter;
    }

    @Bean
    public ClubLoginSuccessHandler clubLoginSuccessHandler() {
        return new ClubLoginSuccessHandler(passwordEncoder());//passwordEncoder()

    }

    @Bean
    public ApiCheckFilter apiCheckFilter() {
        return new ApiCheckFilter("/notes/**/*",jwtUtil());
    }
    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }

}
