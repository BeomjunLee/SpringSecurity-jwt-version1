package com.security.jwt.controller;
import com.security.jwt.domain.Member;
import com.security.jwt.domain.Role;
import com.security.jwt.domain.dto.MemberDto;
import com.security.jwt.domain.form.MemberForm;
import com.security.jwt.domain.response.Response;
import com.security.jwt.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/members", produces = MediaTypes.HAL_JSON_VALUE)
public class ApiMemberController {

    private final MemberService memberService;

    /**
     * [회원] 가입
     */
    @PostMapping("/new")
    public ResponseEntity signUpMember(@Valid @RequestBody MemberForm memberForm) {
        Member member = Member.builder()
                .username(memberForm.getUsername())
                .password(memberForm.getPassword())
                .name(memberForm.getName())
                .build();
        memberService.createMember(member);
        Response response = Response.builder()
                .result("success")
                .status(201)
                .message("회원가입 성공")
                .build();
        URI createUri = linkTo(ApiMemberController.class).slash("new").toUri();
        return ResponseEntity.created(createUri).body(response);
    }


    /**
     * [회원] 정보보기
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/me")
    public ResponseEntity myInfo(Principal principal) {
        Member member = memberService.findByUsername(principal.getName());
            MemberDto dto = MemberDto.builder()
                    .username(member.getUsername())
                    .name(member.getName())
                    .build();
            return ResponseEntity.ok(dto);
        }



}
