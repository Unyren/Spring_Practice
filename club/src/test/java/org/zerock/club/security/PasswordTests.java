package org.zerock.club.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testEncoder() {
        String password = "1111";
        String enPw = passwordEncoder.encode(password);
        System.out.println("enPw"+enPw);
        //암호화 된 비밀번호 /할때마다 다르게 나옴
        //$2a$10$8.h1s8aokf7nixz1Dyxa6unEIXFG0ChS42Vjzi25yFw7BhSDS1aTS
        //$2a$10$cvt0Xqo0aYHf4n/1bytdEeP0NRx2h7xJROBCytM8vXVwCgew3TK1y
        //$2a$10$ynZOpLUdOiF7p926G61gL.k5//bztI4ouvuWFWYqjCVT.mszgsote
        boolean matchResult = passwordEncoder.matches(password, enPw);
        System.out.println("matchResult"+matchResult);
    }
}
