package ru.otus.hw.sequrity.userdetails;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import ru.otus.hw.models.entity.ReaderEntity;

import java.util.Collection;

@RequiredArgsConstructor
@Validated
public class AppUserDetails implements UserDetails {

    @NotNull
    private final ReaderEntity reader;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return reader.getRole().getAuthorities().stream().map(it -> (GrantedAuthority) () -> it).toList();
    }

    @Override
    public String getPassword() {
        return reader.getPwdHash();
    }

    @Override
    public String getUsername() {
        return reader.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
