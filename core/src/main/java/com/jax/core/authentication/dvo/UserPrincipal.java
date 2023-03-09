package com.jax.core.authentication.dvo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserPrincipal implements UserDetails {
    private String id;
    private String username;
    private String email;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal() {
    }

    ;

    public UserPrincipal(String id, String username, String email, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.authorities = authorities;
    }

    public static UserPrincipal create(String id, String username, String email, List<String> scopes) {
        List<GrantedAuthority> authorities = scopes
                .stream()
                .map(scope -> new SimpleGrantedAuthority(scope))
                .collect(Collectors.toList());

        return new UserPrincipal(
                id,
                username,
                email,
                authorities
        );
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
