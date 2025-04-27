package org.zerock.club.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.zerock.club.security.dto.ClubAuthMemberDTO;
import org.zerock.club.security.util.JWTUtil;

import java.io.IOException;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    private JWTUtil jwtUtil;



    public ApiLoginFilter(String defaultFilterProcessesUrl, JWTUtil jwtUtil) {
        super(defaultFilterProcessesUrl);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        log.info("attemptAuthentication...............");

        String email = request.getParameter("email");
        String pw = request.getParameter("pw");

        log.info("[DEG]email" + email);
        log.info("[DEG]pw" + pw);


        if(email == null) {
            throw new BadCredentialsException("email cannot be null");
        }

        UsernamePasswordAuthenticationToken authToken
                = new UsernamePasswordAuthenticationToken(email, pw);

        //AnonymousAuthenticationToken

        log.info("[DEG]:authToken:" + authToken);
        return getAuthenticationManager().authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        log.info("-------------------ApiLoginFilter ------------------ ");
        log.info("successfulAuthentication: " + authResult);

        log.info(authResult.getPrincipal());

        String email=((ClubAuthMemberDTO)authResult.getPrincipal()).getUsername();

        String token="";

        token=jwtUtil.generateToken(email);

        response.setContentType("text/plain");
        response.getOutputStream().write(token.getBytes());

        log.info(token);
    }
}
