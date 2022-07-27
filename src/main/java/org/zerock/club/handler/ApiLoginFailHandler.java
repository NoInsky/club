package org.zerock.club.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;

@Log4j2
public class ApiLoginFailHandler implements AuthenticationFailureHandler {
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        
                log.info("login fail fandler................");
                log.info(exception.getMessage());

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = exception.getMessage();
                json.put("code", "401");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
    }
}
