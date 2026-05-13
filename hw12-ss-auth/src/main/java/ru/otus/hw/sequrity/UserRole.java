package ru.otus.hw.sequrity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static ru.otus.hw.sequrity.AppAuthorities.VIEW_LIBRARY_INDEX;
import static ru.otus.hw.sequrity.AppAuthorities.VIEW_BOOK_DETAILS;
import static ru.otus.hw.sequrity.AppAuthorities.WRITE_EDIT_COMMENTS;
import static ru.otus.hw.sequrity.AppAuthorities.MODERATE_ALL_COMMENTS;
import static ru.otus.hw.sequrity.AppAuthorities.EDIT_BOOKS;

/**
 * Содержит набор Authorities и встраивается в модель пользователя
 */
public enum UserRole {
    PUBLIC(new String[]{VIEW_LIBRARY_INDEX}),
    READER(new String[]{VIEW_LIBRARY_INDEX, VIEW_BOOK_DETAILS}),
    COMMENTER(new String[]{VIEW_LIBRARY_INDEX, VIEW_BOOK_DETAILS, WRITE_EDIT_COMMENTS}),
    EDITOR(new String[]{VIEW_LIBRARY_INDEX, VIEW_BOOK_DETAILS, WRITE_EDIT_COMMENTS, EDIT_BOOKS}),
    MODERATOR(new String[]{
            VIEW_LIBRARY_INDEX, VIEW_BOOK_DETAILS, WRITE_EDIT_COMMENTS, EDIT_BOOKS, MODERATE_ALL_COMMENTS});

    private final String[] authorities;

    UserRole(String[] authorities) {
        this.authorities = authorities;
    }

    public Collection<String> getAuthorities() {
        return List.of(authorities);
    }

    public Collection<? extends GrantedAuthority> getGrantedAuthorities() {
        return Arrays.stream(authorities).map(SimpleGrantedAuthority::new).toList();
    }

    public boolean hasAuthority(String authority) {
        for (String auth : authorities) {
            if (auth.equals(authority)) {
                return true;
            }
        }
        return false;
    }
}
