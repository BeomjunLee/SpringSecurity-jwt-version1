package com.security.jwt.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.security.jwt.domain.Role;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //기본 생성자 protected
@Builder
public class MemberDto {
    @JsonIgnore
    private Long member_id;
    private String username;
    private String name;
    private String phoneNum;
    private String email;
    private int point;

    @JsonIgnore
    private Role role;

}