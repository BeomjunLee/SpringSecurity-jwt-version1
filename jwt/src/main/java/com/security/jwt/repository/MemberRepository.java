package com.security.jwt.repository;

import com.security.jwt.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    //아이디 중복검색
    int countByUsername(String username);

    //refreshToken 검색 최적화
    @Query("select m.refreshToken from Member m where m.username = :username")
    Optional<String> findRefreshToken(@Param("username") String username);
}
