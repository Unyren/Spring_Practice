package org.zerock.club.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.club.security.util.JWTUtil;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Base64;


public class JWTTests {

    private JWTUtil jwtUtil;
    @BeforeEach
    public void testBefore(){
        System.out.println("testBefore");
        jwtUtil = new JWTUtil();
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256비트 = 32바이트
        random.nextBytes(keyBytes);
        String secretKey = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println(secretKey);
    }


    public void testValidate() throws Exception {
        String email="user95@zerock.org";
        String str=jwtUtil.generateToken(email);
        Thread.sleep(5000);
        String resultEmail=jwtUtil.validateAndExtract(str);
        System.out.println(resultEmail);


    }

    @Test
    public void testEncode() {
        String email = "user95@zerock.org";

        String str = null;
        try {
            str = jwtUtil.generateToken(email);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        System.out.println(str);
    }
}
