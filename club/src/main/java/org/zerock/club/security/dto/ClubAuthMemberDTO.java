package org.zerock.club.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Log4j2
@Getter
@Setter
@ToString
public class ClubAuthMemberDTO extends User implements OAuth2User {

    private  Map<String, Object> attr;
    private String email;//아이디
    private String password;
    private String name;//닉네임
    private boolean fromSocial;//소셜 로그인으로 회원가입 여부

    public ClubAuthMemberDTO(String username, String password, boolean fromSocial, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.email = username;
        this.fromSocial = fromSocial;
        this.password = password;
    }

    public ClubAuthMemberDTO(String username, String password, boolean fromSocial, Collection<? extends GrantedAuthority> authorities,
                             Map<String, Object> attr) {
        this(username, password,fromSocial,authorities);

        this.attr =attr;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }
}
