package org.zerock.club.dto;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NoteDTO {

    private Long num;

    private String title;
    private String content;
    private String writerEmail;

    private LocalDateTime regDate, modDate;

}
