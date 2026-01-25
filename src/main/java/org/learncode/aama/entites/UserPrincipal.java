package org.learncode.aama.entites;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private Users users;
    public UserPrincipal(Users user) {
        this.users = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (users.getRole() == null || users.getRole().isEmpty()) {
            return List.of();
        }
        else
            return List.of(new SimpleGrantedAuthority("ROLE_"+users.getRole()));
    }

    public Users getUser() {
        return users;
    }

    @Override
    public @Nullable String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getUserID().toString();
    }
}
