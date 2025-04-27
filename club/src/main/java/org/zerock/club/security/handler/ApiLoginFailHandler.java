package org.zerock.club.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiLoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
       log.info("login fail handler....");
       log.info(exception.getMessage());

       response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
       //json 리턴 및 한글깨짐 수정
       response.setContentType("application/json;charset=utf-8");
       JSONObject json = new JSONObject();// {}
       json.put("code", "401");// {"code": "401"}
       json.put("message", exception.getMessage());// {"code": "401", "message": "FAIL CHECK API TOKEN"}
       PrintWriter out = response.getWriter();
       out.print(json);

    }
}
