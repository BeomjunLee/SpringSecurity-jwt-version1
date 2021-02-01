package com.security.jwt.domain;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    
    @Column(unique = true)
    private String username;                                //아이디

    private String password;                                //비밀번호

    private String name;                                    //이름

    @Enumerated(value = EnumType.STRING)
    private Role role;                                      //권한

    private String refreshToken;                            //refreshToken

    //refreshToken 갱신
    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    //비밀번호 암호화위해 setter
    public void encodingPassword(String password) {
        this.password = password;
    }

    //==비지니스 로직
    public void changeRole(Role role) { //권한 변경
        this.role = role;
    }
}
