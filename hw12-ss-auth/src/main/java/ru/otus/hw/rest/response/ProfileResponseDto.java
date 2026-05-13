package ru.otus.hw.rest.response;

import lombok.Getter;
import ru.otus.hw.models.entity.ReaderEntity;

import java.util.Collection;

@Getter
public class ProfileResponseDto {

    private final String username;

    private final String firstName;

    private final String lastName;

    private final String fullName;

    private final Collection<String> authorities;

    public ProfileResponseDto(ReaderEntity reader) {
        this.username = reader.getUsername();
        this.firstName = reader.getFirstName();
        this.lastName = reader.getLastName();
        this.fullName = reader.getFullName();
        this.authorities = reader.getRole().getAuthorities();
    }
}
