package com.security.jwt.domain.form;

import com.security.jwt.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberForm {
    @NotBlank(message = "아이디를 입력해주세요")
    private String username;                                //아이디

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;                                //비밀번호

    @NotBlank(message = "이름을 입력해주세요")
    private String name;                                    //이름

    public MemberForm(Member member) {
        this.username = member.getUsername();
        this.password = member.getPassword();
        this.name = member.getName();
    }
}
