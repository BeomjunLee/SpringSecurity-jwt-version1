package com.security.jwt.security.tokens;

import com.security.jwt.security.MemberContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 인증후
 */
public class PostAuthorizationToken extends UsernamePasswordAuthenticationToken {

    private PostAuthorizationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static PostAuthorizationToken setPostAuthorizationTokenFromMemberContext(MemberContext context) {
        return new PostAuthorizationToken(context.getUsername(), context.getPassword(), context.getAuthorities());
    }
}
