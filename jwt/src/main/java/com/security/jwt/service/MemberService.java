package com.security.jwt.service;
import com.security.jwt.domain.Member;
import com.security.jwt.domain.Role;
import com.security.jwt.exception.DuplicateUsernameException;
import com.security.jwt.exception.NotFoundRefreshTokenException;
import com.security.jwt.exception.NotMatchedRefreshTokenException;
import com.security.jwt.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) //조회최적화
@RequiredArgsConstructor    //스프링 주입
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인체크
     */
    public Member loginCheck(String username, String password) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + "에 해당되는 유저를 찾을수 없습니다"));
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치 하지않습니다");
        } else return member;
    }

    /**
     * 로그인 성공시 refreshToken 저장 및 갱신
     */
    @Transactional
    public void updateRefreshToken(String username, String refreshToken) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + "에 해당되는 유저를 찾을수 없습니다"));
        member.changeRefreshToken(refreshToken);
    }

    /**
     * DB에 저장된 refreshToken 값
     */
    public boolean checkRefreshToken(String username, String refreshToken) {
        String findRefreshToken = memberRepository.findRefreshToken(username).orElseThrow(() -> new NotFoundRefreshTokenException("회원의 refresh_token을 찾을 수 없습니다"));
        if (refreshToken.equals(findRefreshToken)) return true;
        else throw new NotMatchedRefreshTokenException("요청된 refresh_token이 회원의 refresh_token과 일치하지 않습니다");
    }


    /**
     * 비밀번호 변경
     */
    @Transactional
    public void changePassword(String username, String currentPassword, String newPassword) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + "에 해당되는 유저를 찾을수 없습니다"));
        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치 하지않습니다");
        }
        member.encodingPassword(passwordEncoder.encode(newPassword));
    }


    /**
     * [회원] 회원가입
     */
    @Transactional //조회가 아니므로 Transactional
    public Member createMember(Member member) {
        validateDuplicateMember(member.getUsername()); //중복회원검증
        member.changeRole(Role.USER);   //권한부여
        //비밀번호 encoding
        member.encodingPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }


    /**
     * 중복 회원 검증
     */
    public void validateDuplicateMember(String username) {
        int findMembers = memberRepository.countByUsername(username);
        if (findMembers > 0) {
            throw new DuplicateUsernameException("아이디가 중복되었습니다");
        }
        //두 유저가 동시에 가입할 경우를 대비해서 DB 에도 유니크 제약조건을 걸어줘야함
    }


    /**
     * 회원 정보 보기
     */
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("해당되는 유저를 찾을수 없습니다"));
    }

}
